package com.itheima.googleplay_31.holder;

import android.view.View;
import android.widget.TextView;

import com.itheima.googleplay_31.base.BaseHolder;
import com.itheima.googleplay_31.bean.CategoryBean;
import com.itheima.googleplay_31.utils.UIUtils;

/**
 * 类    名:  CategoryTitleHolder
 * 创 建 者:  伍碧林
 * 创建时间:  2016/10/20 09:11
 * 描    述： ${TODO}
 */
public class CategoryTitleHolder extends BaseHolder<CategoryBean> {

    private TextView mTvTitle;

    @Override
    public View initHolderView() {
        mTvTitle = new TextView(UIUtils.getContext());
        int padding = UIUtils.dip2px(4);
        mTvTitle.setPadding(padding, padding, padding, padding);
        return mTvTitle;
    }

    @Override
    public void refresHolderView(CategoryBean data) {
        mTvTitle.setText(data.title);
    }
}
