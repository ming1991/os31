package com.itheima.googleplay_31.factory;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.ListView;

import com.itheima.googleplay_31.utils.UIUtils;

/**
 * 类    名:  ListViewFactory
 * 创 建 者:  伍碧林
 * 创建时间:  2016/10/18 14:37
 * 描    述： ${TODO}
 */
public class ListViewFactory {
    public static ListView createListView() {
        ListView listView = new ListView(UIUtils.getContext());
        //统一的属性设置
        listView.setFastScrollEnabled(true);
        listView.setSelector(new ColorDrawable(Color.TRANSPARENT));

        //兼容性设置
        listView.setDividerHeight(0);
        listView.setCacheColorHint(Color.TRANSPARENT);

        return listView;
    }
}
