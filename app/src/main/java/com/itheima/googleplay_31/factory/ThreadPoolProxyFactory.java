package com.itheima.googleplay_31.factory;

import com.itheima.googleplay_31.proxy.ThreadPoolProxy;

/**
 * 类    名:  ThreadPoolProxyFactory
 * 创 建 者:  伍碧林
 * 创建时间:  2016/10/17 10:16
 * 描    述： 谷歌市场需要2类线程池,就需要2类的线程池代理,对2类线程池代理创建进行简单的封装
 */
public class ThreadPoolProxyFactory {
    static ThreadPoolProxy mNormalProxy;
    static ThreadPoolProxy mDownLoadProxy;

    /**
     * 创建普通的线程池代理
     */
    public static ThreadPoolProxy createNormalPoolProxy() {
        if (mNormalProxy == null) {
            synchronized (ThreadPoolProxyFactory.class) {
                if (mNormalProxy == null) {
                    mNormalProxy = new ThreadPoolProxy(5);
                }
            }
        }
        return mNormalProxy;
    }

    /**
     * 创建下载的线程池代理
     */
    public static ThreadPoolProxy createDownLoadPoolProxy() {
        if (mDownLoadProxy == null) {
            synchronized (ThreadPoolProxyFactory.class) {
                if (mDownLoadProxy == null) {
                    mDownLoadProxy = new ThreadPoolProxy(3);
                }
            }
        }
        return mDownLoadProxy;
    }
}
