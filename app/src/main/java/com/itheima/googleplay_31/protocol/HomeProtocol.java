package com.itheima.googleplay_31.protocol;

import android.support.annotation.NonNull;

import com.itheima.googleplay_31.base.BaseProtocol;
import com.itheima.googleplay_31.bean.HomeBean;

/**
 * 类    名:  HomeProtocol
 * 创 建 者:  伍碧林
 * 创建时间:  2016/10/17 14:55
 * 描    述： ${TODO}
 */
public class HomeProtocol extends BaseProtocol<HomeBean> {
    /**
     * @return
     * @des 传递协议的关键字
     */
    @NonNull
    @Override
    public String getInterfaceKey() {
        return "home";
    }

    /**
     * @param gson
     * @param jsonString @return
     * @des 解析网络请求回来的数据
     */
 /*   @Override
    public HomeBean parseData(Gson gson, String jsonString) {
        return gson.fromJson(jsonString, HomeBean.class);
    }*/
}
