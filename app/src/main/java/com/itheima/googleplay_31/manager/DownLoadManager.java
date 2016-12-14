package com.itheima.googleplay_31.manager;

import com.itheima.googleplay_31.bean.DownLoadInfo;
import com.itheima.googleplay_31.bean.ItemBean;
import com.itheima.googleplay_31.conf.Constants;
import com.itheima.googleplay_31.factory.ThreadPoolProxyFactory;
import com.itheima.googleplay_31.utils.CommonUtils;
import com.itheima.googleplay_31.utils.FileUtils;
import com.itheima.googleplay_31.utils.HttpUtils;
import com.itheima.googleplay_31.utils.IOUtils;
import com.itheima.googleplay_31.utils.UIUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 类    名:  DownLoadManager
 * 创 建 者:  伍碧林
 * 创建时间:  2016/10/22 09:42
 * 描    述： 下载管理器,负责和下载相关的逻辑
 * 描    述： 只需要一个下载管理器即可,单例
 * 描    述： 1.定义常量,而且需要`时刻记录`当前的状态
 * 描    述： 2.会根据ItemBean返回与之对应的DownLoadInfo-->难
 * 描    述： 被观察者-->发布消息
 */
public class DownLoadManager {
    public static final int STATE_UNDOWNLOAD      = 0;//未下载
    public static final int STATE_DOWNLOADING     = 1;//下载中
    public static final int STATE_PAUSEDOWNLOAD   = 2;//暂停下载
    public static final int STATE_WAITINGDOWNLOAD = 3;//等待下载
    public static final int STATE_DOWNLOADFAILED  = 4;//下载失败
    public static final int STATE_DOWNLOADED      = 5;//下载完成
    public static final int STATE_INSTALLED       = 6;//已安装

    //保存用户点击了下载按钮之后产生的downloadInfo
    public Map<String, DownLoadInfo> mCacheDownLoadInfoMap = new HashMap<>();
    private static DownLoadManager instance;

    private DownLoadManager() {

    }

    public static DownLoadManager getInstance() {
        if (instance == null) {
            synchronized (DownLoadManager.class) {
                if (instance == null) {
                    instance = new DownLoadManager();
                }
            }
        }
        return instance;
    }

    /**
     * @param downloadInfo
     * @des 触发开始下载
     * @called 用户点击了下载按钮的时候
     * @des downLoadInfo可能有的状态(等待中, 下载中, 下载失败, 下载完成)
     */
    public void downLoad(DownLoadInfo downloadInfo) {
        mCacheDownLoadInfoMap.put(downloadInfo.packageName, downloadInfo);

        /*############### 当前状态: 未下载 ###############*/
        downloadInfo.curState = STATE_UNDOWNLOAD;
        notifyObservers(downloadInfo);
        /*#######################################*/

        /*############### 当前状态: 等待中 ###############*/
        downloadInfo.curState = STATE_WAITINGDOWNLOAD;
        notifyObservers(downloadInfo);
        /*#######################################*/
        /*
        预先把状态设置为等待中
            因为任务提交给线程池有2种情况
                1.进入工作线程-->直接走run方法体-->切换状态为下载中
                2.进入任务队列-->状态不会切换-->继续保留等待中的状态(正合我意)
         */

        //异步下载,得到数据,处理数据
        DownLoadTask task = new DownLoadTask(downloadInfo);

        //对DownLoadInfo里面的Task赋值
        downloadInfo.task = task;

        ThreadPoolProxyFactory.createDownLoadPoolProxy().submit(task);
        //刷新ui
    }


    class DownLoadTask implements Runnable {
        private DownLoadInfo downloadInfo;

        public DownLoadTask(DownLoadInfo downloadInfo) {
            this.downloadInfo = downloadInfo;
        }

        @Override
        public void run() {
            /*############### 当前状态: 下载中 ###############*/
            downloadInfo.curState = STATE_DOWNLOADING;
            notifyObservers(downloadInfo);
           /*#######################################*/


            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {

                File apkFile = new File(downloadInfo.savePath);

                long initRange = 0;
                if (apkFile.exists()) {
                    //初始的range-->应该已有数据的长度(saveFile)
                    initRange = apkFile.length();

                    //②进度条的初始化进度
                    downloadInfo.progress = initRange;
                }


                //真正的在子线程中下载具体的apk
                OkHttpClient okHttpClient = new OkHttpClient();
                //http://localhost:8080/GooglePlayServer/download?name=app/com.itheima.www/com.itheima.www.apk&range=0

                String url = Constants.URLS.BASEURL + "download";

                Map<String, Object> parmasMap = new HashMap<>();
                parmasMap.put("name", downloadInfo.downloadUrl);

                parmasMap.put("range", initRange);//①,发起请求的时候,range应该已有数据的长度

                String urlParamsByMap = HttpUtils.getUrlParamsByMap(parmasMap);

                //拼接最后的参数
                url = url + "?" + urlParamsByMap;

                Request request = new Request.Builder().get().url(url).build();

                Response response = okHttpClient.newCall(request).execute();

                if (response.isSuccessful()) {
                    inputStream = response.body().byteStream();
                    //inputStream->apk
                    /*String dir = FileUtils.getDir("apk");//外置sdcard应用程序的缓存目录(sdcard/Android/data/包目录/apk)
                    String fileName = packageName + ".apk";*/
                    //③已追加的方式去写文件
                    outputStream = new BufferedOutputStream(new FileOutputStream(apkFile, true));

                    int len = -1;
                    byte[] buffer = new byte[1024];
                    boolean isPause = false;
                    while ((len = inputStream.read(buffer)) != -1) {//buffer里面被写入了数据
                        if (downloadInfo.curState == STATE_PAUSEDOWNLOAD) {
                            isPause = true;
                            break;
                        }

                        downloadInfo.progress += len;


                        outputStream.write(buffer, 0, len);

                          /*############### 当前状态: 下载中 ###############*/
                        downloadInfo.curState = STATE_DOWNLOADING;
                        notifyObservers(downloadInfo);
                          /*#######################################*/

                        //提前跳出while循环
                        if (downloadInfo.progress == downloadInfo.max) {
                            break;
                        }
                    }
                    if (isPause) {//暂停来到这个地方

                    } else {
                        //写完了-->下载完成

                    /*############### 当前状态: 完成 ###############*/
                        downloadInfo.curState = STATE_DOWNLOADED;
                        notifyObservers(downloadInfo);
                    /*#######################################*/
                    }

                } else {
                      /*############### 当前状态: 下载失败 ###############*/
                    downloadInfo.curState = STATE_DOWNLOADFAILED;
                    notifyObservers(downloadInfo);
                          /*#######################################*/
                }
            } catch (IOException e) {
                e.printStackTrace();
                 /*############### 当前状态: 下载失败 ###############*/
                downloadInfo.curState = STATE_DOWNLOADFAILED;
                notifyObservers(downloadInfo);
                /*#######################################*/

            } finally {
                IOUtils.close(outputStream);
                IOUtils.close(inputStream);
            }
        }
    }

    /**
     * 根据ItemBean,得到与之对应的DownloadInfo
     *
     * @param itemBean
     * @return
     */
    public DownLoadInfo createDownLoadInfo(ItemBean itemBean) {
        DownLoadInfo downLoadInfo = new DownLoadInfo();
        String dir = FileUtils.getDir("apk");
        String fileName = itemBean.packageName + ".apk";
        File saveFile = new File(dir, fileName);

        //相应的赋值-->常规的赋值
        downLoadInfo.downloadUrl = itemBean.downloadUrl;
        downLoadInfo.savePath = saveFile.getAbsolutePath();
        downLoadInfo.packageName = itemBean.packageName;
        downLoadInfo.max = itemBean.size;
        downLoadInfo.progress = 0;

        //相应的赋值-->最重要的赋值,最难的赋值
        /*
        未下载
        下载中
        暂停下载
        等待下载
        下载失败
        下载完成
        已安装
         */
        //已安装
        if (CommonUtils.isInstalled(UIUtils.getContext(), itemBean.packageName)) {
            downLoadInfo.curState = STATE_INSTALLED;
            return downLoadInfo;
        }
        //下载完成
        if (saveFile.exists() && saveFile.length() == itemBean.size) {
            downLoadInfo.curState = STATE_DOWNLOADED;
            return downLoadInfo;
        }
        //下载中,下载失败,等待下载,暂停下载-->难
        if (mCacheDownLoadInfoMap.containsKey(itemBean.packageName)) {
            downLoadInfo = mCacheDownLoadInfoMap.get(itemBean.packageName);
            //此时的downLoadInfo就已经包含了状态
            return downLoadInfo;
        }
        //未下载
        downLoadInfo.curState = STATE_UNDOWNLOAD;
        return downLoadInfo;
    }

    /*=============== 自己实现观察者设计模式,通知最新的下载进度给外界 ===============*/
    //1.定义接口以及接口方法
    public interface DownLoadInfoObserver {
        void onDownLoadInfoChanged(DownLoadInfo downLoadInfo);
    }

    //2.定义集合保存接口对象
    public List<DownLoadInfoObserver> observers = new ArrayList<>();

    //3. 常见方法-->添加观察者到观察者集合中
    public synchronized void addObserver(DownLoadInfoObserver o) {
        if (o == null)
            throw new NullPointerException();
        if (!observers.contains(o)) {
            observers.add(o);
        }
    }

    //3. 常见方法-->从观察者集合中移除观察者
    public synchronized void deleteObserver(DownLoadInfoObserver o) {
        observers.remove(o);
    }

    //3. 常见方法-->通知所有的观察者消息已经发生改变
    public void notifyObservers(DownLoadInfo downLoadInfo) {
        for (DownLoadInfoObserver o : observers) {
            o.onDownLoadInfoChanged(downLoadInfo);
        }
    }

    /**
     * @param downLoadInfo
     * @des 暂停下载
     * @called 正在下载的时候点击了下载按钮
     */
    public void pause(DownLoadInfo downLoadInfo) {
        /*############### 当前的状态:暂停 ###############*/
        downLoadInfo.curState = STATE_PAUSEDOWNLOAD;
        notifyObservers(downLoadInfo);
        /*#######################################*/
    }

    /**
     * 取消下载
     *
     * @param downLoadInfo
     */
    public void cancel(DownLoadInfo downLoadInfo) {
      /*############### 当前的状态:未下载 ###############*/
        downLoadInfo.curState = STATE_UNDOWNLOAD;
        notifyObservers(downLoadInfo);
        /*#######################################*/
        ThreadPoolProxyFactory.createDownLoadPoolProxy().remove(downLoadInfo.task);
    }
}
