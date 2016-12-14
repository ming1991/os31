package com.itheima.googleplay_31.protocol;

import android.support.annotation.NonNull;

import com.itheima.googleplay_31.base.BaseProtocol;

import java.util.List;

/**
 * 类    名:  HotProtocol
 * 创 建 者:  伍碧林
 * 创建时间:  2016/10/20 10:50
 * 描    述： ${TODO}
 */
public class HotProtocol extends BaseProtocol<List<String>>{
    @NonNull
    @Override
    public String getInterfaceKey() {
        return "hot";
    }

   /* @Override
    public List<String> parseData(Gson gson, String jsonString) {
        return gson.fromJson(jsonString,new TypeToken<List<String>>(){}.getType());
    }*/
}
