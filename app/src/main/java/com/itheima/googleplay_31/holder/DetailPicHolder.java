package com.itheima.googleplay_31.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.itheima.googleplay_31.R;
import com.itheima.googleplay_31.base.BaseHolder;
import com.itheima.googleplay_31.bean.ItemBean;
import com.itheima.googleplay_31.conf.Constants;
import com.itheima.googleplay_31.utils.UIUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;

/**
 * 类    名:  DetailInfoHolder
 * 创 建 者:  伍碧林
 * 创建时间:  2016/10/20 15:06
 * 描    述： ${TODO}
 */
public class DetailPicHolder extends BaseHolder<ItemBean> {


    @BindView(R.id.app_detail_pic_iv_container)
    LinearLayout mAppDetailPicIvContainer;

    @Override
    public View initHolderView() {
        return View.inflate(UIUtils.getContext(), R.layout.item_detail_pic, null);
    }

    @Override
    public void refresHolderView(ItemBean data) {
        List<String> screenList = data.screen;
        for (int i = 0; i < screenList.size(); i++) {
            ImageView iv = new ImageView(UIUtils.getContext());
            //data
            String url = screenList.get(i);
            Picasso.with(UIUtils.getContext()).load(Constants.URLS.IMGBASEURL + url).into(iv);


            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
            if (i != 0) {
                params.leftMargin = UIUtils.dip2px(4);
            }
            mAppDetailPicIvContainer.addView(iv, params);
        }
    }
}
