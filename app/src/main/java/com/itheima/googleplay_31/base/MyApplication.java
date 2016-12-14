package com.itheima.googleplay_31.base;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import java.util.HashMap;
import java.util.Map;

/**
 * 类    名:  MyApplication
 * 创 建 者:  伍碧林
 * 创建时间:  2016/10/14 11:20
 * 描    述： 全局的,单例
 * 描    述： 需要在清单文件中配置
 */
public class MyApplication extends Application {

    private static Context mContext;
    private static Handler mHandler;
    //定义存储结构
    private Map<String, String> mCacheMap = new HashMap<>();

    public Map<String, String> getCacheMap() {
        return mCacheMap;
    }

    /**
     * 得到上下文
     *
     * @return
     */
    public static Context getContext() {
        return mContext;
    }

    /**
     * 得到主线程的Handler
     *
     * @return
     */
    public static Handler getHandler() {
        return mHandler;
    }

    @Override
    public void onCreate() {//程序的入口方法
        super.onCreate();

        //创建一些整个app常用的一些对象
        //上下文
        mContext = getApplicationContext();

        //主线程的Handler
        mHandler = new Handler();
    }
}
