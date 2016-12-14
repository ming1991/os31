package com.itheima.googleplay_31.bean;

import java.util.List;


public class ItemBean {
    public String des;
    public long   size;
    public String name;
    public String downloadUrl;
    public int    id;
    public String packageName;
    public String iconUrl;
    public float  stars;

    /*--------------- 额外字段(详情页面里面的) ---------------*/
    public String             date;//2015-06-10
    public String             downloadNum;//40万+
    public List<ItemSafeBean> safe;//Array
    public List<String>       screen;//Array
    public String             author;//	黑马程序员
    public String             version;//

    public class ItemSafeBean {
        public String safeDes;//已通过安智市场安全检测，请放心使用
        public int    safeDesColor;//0
        public String safeDesUrl;//app/com.itheima.www/safeDesUrl0.jpg
        public String safeUrl;//app/com.itheima.www/safeIcon0.jpg
    }
}