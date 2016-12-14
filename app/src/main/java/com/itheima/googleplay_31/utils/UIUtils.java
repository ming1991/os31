package com.itheima.googleplay_31.utils;

import android.content.Context;
import android.content.res.Resources;

import com.itheima.googleplay_31.base.MyApplication;

/**
 * 类    名:  UIUtils
 * 创 建 者:  伍碧林
 * 创建时间:  2016/10/14 14:32
 * 描    述： 和ui操作相关的工具方法
 */
public class UIUtils {
    /**
     * 得到上下文
     */
    public static Context getContext() {
        return MyApplication.getContext();
    }

    /**
     * 得到Resource对象
     */
    public static Resources getResources() {
        return getContext().getResources();
    }

    /**
     * 得到String.xml中的字符串
     */
    public static String getString(int resId) {
        return getResources().getString(resId);
    }

    /**
     * 得到String.xml中的字符串数组
     */
    public static String[] getStrings(int resId) {
        return getResources().getStringArray(resId);
    }

    /**
     * 得到Color.xml中的颜色信息
     */
    public static int getColor(int resId) {
        return getResources().getColor(resId);
    }

    /**
     * 得到应用程序的包名
     *
     * @return
     */
    public static String getPackageName() {
        return getContext().getPackageName();
    }

    /**
     * dip-->px
     *
     * @param dip
     * @return
     */
    public static int dip2px(int dip) {
        //知道px和dp转换关系
        /*
            1.px/(ppi/160)=dp
            2.px/dp = density
         */

        float density = getResources().getDisplayMetrics().density;
        int densityDpi = getResources().getDisplayMetrics().densityDpi;
        LogUtils.s("density:" + density);
        LogUtils.s("densityDpi:" + densityDpi);
        //px=dp*density
        int px = (int) (dip * density + .5f);
        return px;
    }

    /**
     * px-->dip
     *
     * @param px
     * @return
     */
    public static int px2dp(int px) {
        float density = getResources().getDisplayMetrics().density;
        int densityDpi = getResources().getDisplayMetrics().densityDpi;
        LogUtils.s("density:" + density);
        LogUtils.s("densityDpi:" + densityDpi);
        //px=dp*density
        int dip = (int) (px / density + .5f);
        return dip;
    }
}
