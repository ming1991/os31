package com.itheima.googleplay_31.holder;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.googleplay_31.R;
import com.itheima.googleplay_31.base.BaseHolder;
import com.itheima.googleplay_31.bean.CategoryBean;
import com.itheima.googleplay_31.conf.Constants;
import com.itheima.googleplay_31.utils.UIUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;

/**
 * 类    名:  CategoryTitleHolder
 * 创 建 者:  伍碧林
 * 创建时间:  2016/10/20 09:11
 * 描    述： ${TODO}
 */
public class CategoryNormalHolder extends BaseHolder<CategoryBean> {


    @BindView(R.id.item_category_icon_1)
    ImageView    mItemCategoryIcon1;
    @BindView(R.id.item_category_name_1)
    TextView     mItemCategoryName1;
    @BindView(R.id.item_category_item_1)
    LinearLayout mItemCategoryItem1;
    @BindView(R.id.item_category_icon_2)
    ImageView    mItemCategoryIcon2;
    @BindView(R.id.item_category_name_2)
    TextView     mItemCategoryName2;
    @BindView(R.id.item_category_item_2)
    LinearLayout mItemCategoryItem2;
    @BindView(R.id.item_category_icon_3)
    ImageView    mItemCategoryIcon3;
    @BindView(R.id.item_category_name_3)
    TextView     mItemCategoryName3;
    @BindView(R.id.item_category_item_3)
    LinearLayout mItemCategoryItem3;

    @Override
    public View initHolderView() {
        return View.inflate(UIUtils.getContext(), R.layout.item_category_normal, null);
    }

    @Override
    public void refresHolderView(CategoryBean data) {
        /*mItemCategoryName1.setText(data.name1);
        mItemCategoryName2.setText(data.name2);
        mItemCategoryName3.setText(data.name3);

        Picasso.with(UIUtils.getContext()).load(Constants.URLS.IMGBASEURL + data.url1).into(mItemCategoryIcon1);
        Picasso.with(UIUtils.getContext()).load(Constants.URLS.IMGBASEURL + data.url2).into(mItemCategoryIcon2);
        Picasso.with(UIUtils.getContext()).load(Constants.URLS.IMGBASEURL + data.url3).into(mItemCategoryIcon3);*/

        bindData(data.name1, data.url1, mItemCategoryName1, mItemCategoryIcon1);
        bindData(data.name2, data.url2, mItemCategoryName2, mItemCategoryIcon2);
        bindData(data.name3, data.url3, mItemCategoryName3, mItemCategoryIcon3);

    }

    public void bindData(final String name, String url, TextView tvName, ImageView ivIcon) {
        ViewParent parent = tvName.getParent();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(url)) {
            ((ViewGroup) parent).setVisibility(View.INVISIBLE);
        } else {
            ((ViewGroup) parent).setVisibility(View.VISIBLE);
            Picasso.with(UIUtils.getContext()).load(Constants.URLS.IMGBASEURL + url).into(ivIcon);
            tvName.setText(name);
            //点击事件
            ((ViewGroup) parent).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(UIUtils.getContext(), name, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
