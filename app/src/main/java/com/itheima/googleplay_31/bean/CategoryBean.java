package com.itheima.googleplay_31.bean;

/**
 * 类    名:  CategoryBean
 * 创 建 者:  伍碧林
 * 创建时间:  2016/10/20 08:45
 * 描    述： ${TODO}
 */
public class CategoryBean {
    public String title;
    public String name1;//休闲
    public String name2;//棋牌
    public String name3;//益智
    public String url1;//image/category_game_0.jpg
    public String url2;//image/category_game_1.jpg
    public String url3;//image/category_game_2.jpg

    //额外的加一个属性
    public boolean isTitle;//是否是title

    @Override
    public String toString() {
        return "CategoryBean{" +
                "title='" + title + '\'' +
                ", name1='" + name1 + '\'' +
                ", name2='" + name2 + '\'' +
                ", name3='" + name3 + '\'' +
                ", url1='" + url1 + '\'' +
                ", url2='" + url2 + '\'' +
                ", url3='" + url3 + '\'' +
                ", isTitle=" + isTitle +
                '}';
    }
}
