package com.itheima.googleplay_31.utils;

import java.util.Map;

/**
 * 类    名:  HttpUtils
 * 创 建 者:  伍碧林
 * 创建时间:  2016/10/17 11:28
 * 描    述： ${TODO}
 */
public class HttpUtils {
    public static String getUrlParamsByMap(Map<String, Object> map) {
        if (map == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }
}
