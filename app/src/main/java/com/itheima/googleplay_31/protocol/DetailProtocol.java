package com.itheima.googleplay_31.protocol;

import android.support.annotation.NonNull;

import com.itheima.googleplay_31.base.BaseProtocol;
import com.itheima.googleplay_31.bean.ItemBean;

import java.util.Map;

/**
 * 类    名:  DetailProtocol
 * 创 建 者:  伍碧林
 * 创建时间:  2016/10/20 14:32
 * 描    述： ${TODO}
 */
public class DetailProtocol extends BaseProtocol<ItemBean> {
    private String packageName;
    //http://localhost:8080/GooglePlayServer/detail?packageName=com.itheima.www
    //http://localhost:8080/GooglePlayServer/detail?index=xx

    public DetailProtocol(String packageName) {
        this.packageName = packageName;
    }

    @NonNull
    @Override
    public String getInterfaceKey() {
        return "detail";
    }

/*    @Override
    public ItemBean parseData(Gson gson, String jsonString) {
        return gson.fromJson(jsonString, ItemBean.class);
    }*/

    @Override
    public void getParmasMap(Map<String, Object> paramsMap) {
        paramsMap.put("packageName", packageName);
    }

    @NonNull
    @Override
    public String generateKey(int index) {
        return getInterfaceKey() + "." + packageName;
    }
}
