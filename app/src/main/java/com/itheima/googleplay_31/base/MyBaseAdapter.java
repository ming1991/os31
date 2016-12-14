package com.itheima.googleplay_31.base;

import android.widget.BaseAdapter;

import java.util.List;

/**
 * 类    名:  MyBaseAdapter
 * 创 建 者:  伍碧林
 * 创建时间:  2016/10/15 16:46
 * 描    述： 针对BaseAdapter中的3个常规方法进行封装
 */
public abstract class MyBaseAdapter<ITEMBEANTYPE> extends BaseAdapter {
    public List<ITEMBEANTYPE> mDatas;

    public MyBaseAdapter(List<ITEMBEANTYPE> datas) {
        mDatas = datas;
    }

    @Override
    public int getCount() {
        if (mDatas != null) {
            return mDatas.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if (mDatas != null) {
            return mDatas.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
}
