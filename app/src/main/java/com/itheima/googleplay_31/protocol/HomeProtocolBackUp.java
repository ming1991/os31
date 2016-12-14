package com.itheima.googleplay_31.protocol;

import com.google.gson.Gson;
import com.itheima.googleplay_31.bean.HomeBean;
import com.itheima.googleplay_31.conf.Constants;
import com.itheima.googleplay_31.utils.HttpUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 类    名:  HomeProtocol
 * 创 建 者:  伍碧林
 * 创建时间:  2016/10/17 14:55
 * 描    述： ${TODO}
 */
public class HomeProtocolBackUp {
    /*
    方法里面的异常,到底是抛出去还是自己try catch
    1.如果抛出去抛到了什么地方?
        谁调用谁处理-->抛到方法的调用处
    2.什么时候选择抛出去?
        方法的调用处是否需要异常
     */

    public HomeBean loadData() throws IOException {
        //模拟网络请求耗时操作
        //1.创建OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.创建请求对象
        //?index=0
        String url = Constants.URLS.BASEURL + "home";

        //定义一个map装参数
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("index", "0");
        //paramsMap-->"index=0"
        String urlParamsByMap = HttpUtils.getUrlParamsByMap(paramsMap);

        url = url + "?" + urlParamsByMap;

        Request request = new Request.Builder().get().url(url).build();
        //3.发起请求
        Response response = okHttpClient.newCall(request).execute();

        if (response.isSuccessful()) {//服务器有响应
            String jsonString = response.body().string();
            //解析jsonString
            Gson gson = new Gson();
            HomeBean homeBean = gson.fromJson(jsonString, HomeBean.class);
            return homeBean;
        }
        return null;
    }
}
