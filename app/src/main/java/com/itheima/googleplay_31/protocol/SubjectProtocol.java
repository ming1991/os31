package com.itheima.googleplay_31.protocol;

import android.support.annotation.NonNull;

import com.itheima.googleplay_31.base.BaseProtocol;
import com.itheima.googleplay_31.bean.SubjectBean;

import java.util.List;

/**
 * 类    名:  SubjectProtocol
 * 创 建 者:  伍碧林
 * 创建时间:  2016/10/18 17:02
 * 描    述： ${TODO}
 */
public class SubjectProtocol extends BaseProtocol<List<SubjectBean>> {
    @NonNull
    @Override
    public String getInterfaceKey() {
        return "subject";
    }

  /*  @Override
    public List<SubjectBean> parseData(Gson gson, String jsonString) {
        return gson.fromJson(jsonString, new TypeToken<List<SubjectBean>>() {
        }.getType());
    }*/
}
