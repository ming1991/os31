package com.itheima.googleplay_31.conf;

import com.itheima.googleplay_31.utils.LogUtils;

/**
 * 类    名:  Constants
 * 创 建 者:  伍碧林
 * 创建时间:  2016/10/14 15:04
 * 描    述： ${TODO}
 */
public class Constants {
    /*
    LEVEL_OFF:关闭日志
    LEVEL_ALL:打开日志
     */
    public static final int DEBUGLEVEL = LogUtils.LEVEL_ALL;

    public static final class URLS {
        public static final String BASEURL    = "http://10.0.3.2:8080/GooglePlayServer/";
        public static final String IMGBASEURL = BASEURL + "image?name=";

    }

    public static final class REQ {

    }

    public static final class RES {

    }

    public static final class PAYTYPE {
        public static final int PAYTYPE_WEIXIN = 0;//微信
        public static final int PAYTYPE_ALIPAY = 1;//支付宝
        public static final int PAYTYPE_UUPAY  = 2;//银联
    }
}
