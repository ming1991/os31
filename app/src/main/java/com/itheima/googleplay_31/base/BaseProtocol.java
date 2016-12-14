package com.itheima.googleplay_31.base;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.itheima.googleplay_31.conf.Constants;
import com.itheima.googleplay_31.utils.FileUtils;
import com.itheima.googleplay_31.utils.HttpUtils;
import com.itheima.googleplay_31.utils.IOUtils;
import com.itheima.googleplay_31.utils.LogUtils;
import com.itheima.googleplay_31.utils.UIUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 类    名:  BaseProtocol
 * 创 建 者:  伍碧林
 * 创建时间:  2016/10/17 15:02
 * 描    述： ${TODO}
 */
public abstract class BaseProtocol<RESTYPE> {

    public static final int PROTOCOLTIMEOUT = 5 * 60 * 1000;//5分钟
    private int mIndex;

    /**
     * 1.先内存-->返回
     * 2.在本地-->返回,存内存
     * 3.在网络-->,返回,存本地,存内存
     *
     * @param index
     * @return
     * @throws IOException
     */
    public RESTYPE loadData(int index) throws IOException {
        RESTYPE result = null;
//        1.先内存-->返回
        result = loadDataFromMem(index);
        if (result != null) {
            LogUtils.s("从内存加载缓存-->" + generateKey(index));
            return result;
        }

        //2.在本地-->返回,存内存
        result = loadDataFromLocal(index);
        if (result != null) {
            LogUtils.s("从本地加载缓存-->" + getCacheFile(index).getAbsolutePath());
            return result;
        }


        //3.在网络-->,返回,存本地,存内存
        result = loadDataFromNet(index);
        return result;
    }

    /**
     * 从内存加载数据
     *
     * @param index
     * @return
     */
    private RESTYPE loadDataFromMem(int index) {
        //找到存储结构
        MyApplication app = (MyApplication) UIUtils.getContext();//getApplicationContext();
        Map<String, String> cacheMap = app.getCacheMap();
        String key = generateKey(index);
        if (cacheMap.containsKey(key)) {
            String memJsonString = cacheMap.get(key);
            return parseData(new Gson(), memJsonString);
        }
        return null;
    }

    /**
     * @param index
     * @return
     * @des 得到缓存的唯一索引
     * @des 子类可以覆写, 返回不同的缓存的key
     */
    @NonNull
    public String generateKey(int index) {
        return getInterfaceKey() + "." + index;//默认的规则
    }

    /**
     * 从本地加载数据
     *
     * @param index
     * @return
     */
    private RESTYPE loadDataFromLocal(int index) {
        //1.找到缓存文件
        BufferedReader reader = null;
        try {
            File cacheFile = getCacheFile(index);
            if (cacheFile.exists()) {//有缓存
                //读取插入时间,判断缓存是否有效
                reader = new BufferedReader(new FileReader(cacheFile));
                String firstLine = reader.readLine();
                long insertTime = Long.parseLong(firstLine);

                if ((System.currentTimeMillis() - insertTime) < PROTOCOLTIMEOUT) {//有效的缓存
                    //读取缓存内容
                    String cacheJsonString = reader.readLine();
                    /*--------------- 保存数据到内存 ---------------*/
                    write2Mem(index, cacheJsonString);
                    //解析返回
                    return parseData(new Gson(), cacheJsonString);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(reader);
        }


        return null;
    }

    private void write2Mem(int index, String cacheJsonString) {
        LogUtils.s("保存数据到内存-->" + generateKey(index));
        MyApplication app = (MyApplication) UIUtils.getContext();
        Map<String, String> cacheMap = app.getCacheMap();
        cacheMap.put(generateKey(index), cacheJsonString);
    }

    /**
     * 得到缓存文件
     *
     * @param index
     * @return
     */
    @NonNull
    private File getCacheFile(int index) {
        String dir = FileUtils.getDir("json");//sdcard/Android/data/包目录/json
        String fileName = generateKey(index);//唯一命中
        return new File(dir, fileName);
    }

    /**
     * 从网络加载数据
     *
     * @param index
     * @return
     * @throws IOException
     */
    private RESTYPE loadDataFromNet(int index) throws IOException {
        mIndex = index;

        //模拟网络请求耗时操作
        //1.创建OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.创建请求对象
        //?index=0
        String url = Constants.URLS.BASEURL + getInterfaceKey();

        //定义一个map装参数
        Map<String, Object> paramsMap = new HashMap<>();
        getParmasMap(paramsMap);
        //paramsMap-->"index=0"
        String urlParamsByMap = HttpUtils.getUrlParamsByMap(paramsMap);

        url = url + "?" + urlParamsByMap;

        Request request = new Request.Builder().get().url(url).build();
        //3.发起请求
        Response response = okHttpClient.newCall(request).execute();

        if (response.isSuccessful()) {//服务器有响应
            String jsonString = response.body().string();
            /*--------------- 存本地 ---------------*/
            write2Local(index, jsonString);

            /*--------------- 存内存 ---------------*/
            write2Mem(index, jsonString);
            //解析jsonString
            Gson gson = new Gson();
            RESTYPE result = parseData(gson, jsonString);
            return result;
        }
        return null;
    }

    /**
     * @param mIndex
     * @param paramsMap
     * @des 对参数多对应的ParamsMap赋值
     * @des 子类可以覆写该方法, 返回具体的参数
     */
    public void getParmasMap(Map<String, Object> paramsMap) {
        paramsMap.put("index", mIndex + "");//默认的
    }

    /**
     * 缓存协议内容到本地
     *
     * @param index
     * @param jsonString
     */
    private void write2Local(int index, String jsonString) {
        BufferedWriter writer = null;
        try {
            File cacheFile = getCacheFile(index);
            LogUtils.s("缓存数据到本地-->" + cacheFile.getAbsolutePath());
            writer = new BufferedWriter(new FileWriter(cacheFile));
            //写入缓存的生成时间
            writer.write(System.currentTimeMillis() + "");
            //换行
            writer.newLine();
            //写入缓存内容
            writer.write(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(writer);
        }
    }


    /**
     * @return
     * @des 得到协议对应的关键字
     * @des 在BaseProtocl中不知道具体的协议关键字是啥, 交给子类
     * @des 子类是必须实现, 定义成为抽象方法即可
     */
    @NonNull
    public abstract String getInterfaceKey();

    /**
     * @param gson
     * @param jsonString
     * @return
     * @des 解析请求回来数据
     * @des 在BaseProtocl中不知道如何具体解析, 交给子类
     * @des 子类是必须实现, 定义成为抽象方法即可
     */
    public RESTYPE parseData(Gson gson, String jsonString) {
        Type type = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return gson.fromJson(jsonString, type);
    }
}
