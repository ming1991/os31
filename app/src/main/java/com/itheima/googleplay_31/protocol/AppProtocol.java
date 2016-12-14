package com.itheima.googleplay_31.protocol;

import android.support.annotation.NonNull;

import com.itheima.googleplay_31.base.BaseProtocol;
import com.itheima.googleplay_31.bean.ItemBean;

import java.util.List;

/**
 * 类    名:  AppProtocol
 * 创 建 者:  伍碧林
 * 创建时间:  2016/10/18 14:33
 * 描    述： ${TODO}
 */
public class AppProtocol extends BaseProtocol<List<ItemBean>> {
    @NonNull
    @Override
    public String getInterfaceKey() {
        return "app";
    }

 /*   @Override
    public List<ItemBean> parseData(Gson gson, String jsonString) {
        //泛型解析 jsonString-->List<T>
        return gson.fromJson(jsonString, new TypeToken<List<ItemBean>>() {
        }.getType());
    }*/
}
