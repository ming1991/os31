package com.itheima.googleplay_31.holder;

import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.itheima.googleplay_31.R;
import com.itheima.googleplay_31.base.BaseHolder;
import com.itheima.googleplay_31.bean.ItemBean;
import com.itheima.googleplay_31.conf.Constants;
import com.itheima.googleplay_31.utils.StringUtils;
import com.itheima.googleplay_31.utils.UIUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;

/**
 * 类    名:  DetailInfoHolder
 * 创 建 者:  伍碧林
 * 创建时间:  2016/10/20 15:06
 * 描    述： ${TODO}
 */
public class DetailInfoHolder extends BaseHolder<ItemBean> {


    @BindView(R.id.app_detail_info_iv_icon)
    ImageView mAppDetailInfoIvIcon;
    @BindView(R.id.app_detail_info_tv_name)
    TextView  mAppDetailInfoTvName;
    @BindView(R.id.app_detail_info_rb_star)
    RatingBar mAppDetailInfoRbStar;
    @BindView(R.id.app_detail_info_tv_downloadnum)
    TextView  mAppDetailInfoTvDownloadnum;
    @BindView(R.id.app_detail_info_tv_version)
    TextView  mAppDetailInfoTvVersion;
    @BindView(R.id.app_detail_info_tv_time)
    TextView  mAppDetailInfoTvTime;
    @BindView(R.id.app_detail_info_tv_size)
    TextView  mAppDetailInfoTvSize;

    @Override
    public View initHolderView() {
        View holderView = View.inflate(UIUtils.getContext(), R.layout.item_detail_info, null);

        ViewCompat.animate(holderView).rotationX(360)
                .setInterpolator(new OvershootInterpolator(4))
                .setDuration(1000)
                .start();

        return holderView;
    }

    @Override
    public void refresHolderView(ItemBean data) {
        mAppDetailInfoTvName.setText(data.name);
        String downLoadNum = UIUtils.getResources().getString(R.string.detailDownloadNum, data.downloadNum);
        String size = UIUtils.getResources().getString(R.string.detailSize, StringUtils.formatFileSize(data.size));
        String date = UIUtils.getResources().getString(R.string.detailDate, data.date);
        String version = UIUtils.getResources().getString(R.string.detailVersion, data.version);

        mAppDetailInfoTvVersion.setText(version);
        mAppDetailInfoTvSize.setText(size);
        mAppDetailInfoTvTime.setText(date);
        mAppDetailInfoTvDownloadnum.setText(downLoadNum);


        mAppDetailInfoRbStar.setRating(data.stars);

        Picasso.with(UIUtils.getContext()).load(Constants.URLS.IMGBASEURL + data.iconUrl).into(mAppDetailInfoIvIcon);
    }
}
