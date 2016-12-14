package com.itheima.googleplay_31.protocol;

import android.support.annotation.NonNull;

import com.itheima.googleplay_31.base.BaseProtocol;
import com.itheima.googleplay_31.bean.ItemBean;

import java.util.List;

/**
 * 类    名:  GameProtocol
 * 创 建 者:  伍碧林
 * 创建时间:  2016/10/18 14:45
 * 描    述： ${TODO}
 */
public class GameProtocol extends BaseProtocol<List<ItemBean>> {
    @NonNull
    @Override
    public String getInterfaceKey() {
        return "game";
    }

 /*   @Override
    public List<ItemBean> parseData(Gson gson, String jsonString) {
        return gson.fromJson(jsonString, new TypeToken<List<ItemBean>>() {
        }.getType());
    }*/
}
