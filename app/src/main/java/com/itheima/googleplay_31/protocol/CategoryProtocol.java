package com.itheima.googleplay_31.protocol;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.itheima.googleplay_31.base.BaseProtocol;
import com.itheima.googleplay_31.bean.CategoryBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 类    名:  CategoryProtocol
 * 创 建 者:  伍碧林
 * 创建时间:  2016/10/20 08:44
 * 描    述： ${TODO}
 */
public class CategoryProtocol extends BaseProtocol<List<CategoryBean>> {
    @NonNull
    @Override
    public String getInterfaceKey() {
        return "category";
    }

    @Override
    public List<CategoryBean> parseData(Gson gson, String jsonString) {
        //bean解析,泛型解析-->gson可以做到的
        //gson解析的原则:jsonString对应的结构和所对应的Bean的结构必须保持一致
        //结点解析
        List<CategoryBean> result = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            //遍历jsonArray
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject itemJsonObj = jsonArray.getJSONObject(i);
                String title = itemJsonObj.getString("title");
                CategoryBean titleCategoryBean = new CategoryBean();
                titleCategoryBean.title = title;
                titleCategoryBean.isTitle = true;

                //保存到集合
                result.add(titleCategoryBean);

                JSONArray infosJsonArr = itemJsonObj.getJSONArray("infos");
                //遍历infosJsonArr
                for (int j = 0; j < infosJsonArr.length(); j++) {

                    JSONObject infoJsonObj = infosJsonArr.getJSONObject(j);
                    String name1 = infoJsonObj.getString("name1");
                    String name2 = infoJsonObj.getString("name2");
                    String name3 = infoJsonObj.getString("name3");
                    String url1 = infoJsonObj.getString("url1");
                    String url2 = infoJsonObj.getString("url2");
                    String url3 = infoJsonObj.getString("url3");


                    CategoryBean infoCategoryBean = new CategoryBean();
                    infoCategoryBean.name1 = name1;
                    infoCategoryBean.name2 = name2;
                    infoCategoryBean.name3 = name3;
                    infoCategoryBean.url1 = url1;
                    infoCategoryBean.url2 = url2;
                    infoCategoryBean.url3 = url3;
                    //加入到集合中
                    result.add(infoCategoryBean);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}
