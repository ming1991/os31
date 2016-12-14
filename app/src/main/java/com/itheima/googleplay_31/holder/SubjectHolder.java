package com.itheima.googleplay_31.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.itheima.googleplay_31.R;
import com.itheima.googleplay_31.base.BaseHolder;
import com.itheima.googleplay_31.bean.SubjectBean;
import com.itheima.googleplay_31.conf.Constants;
import com.itheima.googleplay_31.utils.UIUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;

/**
 * 类    名:  SubjectHolder
 * 创 建 者:  伍碧林
 * 创建时间:  2016/10/18 17:05
 * 描    述： ${TODO}
 */
public class SubjectHolder extends BaseHolder<SubjectBean> {
    @BindView(R.id.item_subject_iv_icon)
    ImageView mItemSubjectIvIcon;
    @BindView(R.id.item_subject_tv_title)
    TextView  mItemSubjectTvTitle;

    @Override
    public View initHolderView() {
        return View.inflate(UIUtils.getContext(), R.layout.item_subject, null);
    }

    @Override
    public void refresHolderView(SubjectBean data) {
        mItemSubjectTvTitle.setText(data.des);
        Picasso.with(UIUtils.getContext()).load(Constants.URLS.IMGBASEURL + data.url).into(mItemSubjectIvIcon);
    }
}
