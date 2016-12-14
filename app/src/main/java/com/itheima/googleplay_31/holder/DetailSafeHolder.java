package com.itheima.googleplay_31.holder;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itheima.googleplay_31.R;
import com.itheima.googleplay_31.base.BaseHolder;
import com.itheima.googleplay_31.bean.ItemBean;
import com.itheima.googleplay_31.conf.Constants;
import com.itheima.googleplay_31.utils.UIUtils;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;

/**
 * 类    名:  DetailInfoHolder
 * 创 建 者:  伍碧林
 * 创建时间:  2016/10/20 15:06
 * 描    述： ${TODO}
 */
public class DetailSafeHolder extends BaseHolder<ItemBean> implements View.OnClickListener {


    @BindView(R.id.app_detail_safe_iv_arrow)
    ImageView mAppDetailSafeIvArrow;

    @BindView(R.id.app_detail_safe_pic_container)
    LinearLayout mAppDetailSafePicContainer;

    @BindView(R.id.app_detail_safe_des_container)
    LinearLayout mAppDetailSafeDesContainer;
    private boolean isOpen = true;//默认是展开的

    @Override
    public View initHolderView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_detail_safe, null);
        view.setOnClickListener(this);
        return view;
    }

    @Override
    public void refresHolderView(ItemBean data) {
        List<ItemBean.ItemSafeBean> itemSafeBeanList = data.safe;
        for (ItemBean.ItemSafeBean itemSafeBean : itemSafeBeanList) {


            //为mAppDetailSafePicContainer填充内容
            String safeUrl = itemSafeBean.safeUrl;
            ImageView ivIcon = new ImageView(UIUtils.getContext());
            Picasso.with(UIUtils.getContext()).load(Constants.URLS.IMGBASEURL + safeUrl).into(ivIcon);
            mAppDetailSafePicContainer.addView(ivIcon);

            //为mAppDetailSafeDesContainer填充内容
            String safeDes = itemSafeBean.safeDes;
            int safeDesColor = itemSafeBean.safeDesColor;
            String safeDesUrl = itemSafeBean.safeDesUrl;

            //定义一行
            LinearLayout line = new LinearLayout(UIUtils.getContext());
            int padding = UIUtils.dip2px(2);
            line.setPadding(padding, padding, padding, padding);
            //内容垂直居中
            line.setGravity(Gravity.CENTER_VERTICAL);

            //定义一行里面的内容
            ImageView ivDesIcon = new ImageView(UIUtils.getContext());
            TextView tvDes = new TextView(UIUtils.getContext());

            //对每一行里面的对象赋值
            tvDes.setSingleLine(true);
            tvDes.setText(safeDes);
            if (safeDesColor == 0) {
                tvDes.setTextColor(UIUtils.getColor(R.color.app_detail_safe_normal));
            } else {
                tvDes.setTextColor(UIUtils.getColor(R.color.app_detail_safe_warning));
            }
            Picasso.with(UIUtils.getContext()).load(Constants.URLS.IMGBASEURL + safeDesUrl).into(ivDesIcon);

            //加入到每一行
            line.addView(ivDesIcon);
            line.addView(tvDes);

            //把每一行加到到容器中
            mAppDetailSafeDesContainer.addView(line);
        }
        //默认执行
        changeDetailDesContainerHeight(false);
    }

    @Override
    public void onClick(View v) {
        changeDetailDesContainerHeight(true);
    }

    private void changeDetailDesContainerHeight(boolean isAnimation) {
        if (isOpen) {//折叠
            //折叠 mAppDetailSafeDesContainer 高度变化 从 应有的高度-->0
            //mAppDetailSafeDesContainer应有的高度

//            Toast.makeText(UIUtils.getContext(), start + "", Toast.LENGTH_SHORT).show();

            int start = mAppDetailSafeDesContainer.getMeasuredHeight();
            int end = 0;
            if (isAnimation) {
                doAnimation(start, end);
            } else {//直接修改高度
                //取出LayoutParams
                ViewGroup.LayoutParams layoutParams = mAppDetailSafeDesContainer.getLayoutParams();
                layoutParams.height = end;

                //重新设置layoutParams
                mAppDetailSafeDesContainer.setLayoutParams(layoutParams);
            }


        } else {//展开
            //折叠 mAppDetailSafeDesContainer 高度变化 从 0-->应有的高度


            //测量

//            int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//            int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//            mAppDetailSafeDesContainer.measure(widthMeasureSpec, heightMeasureSpec);

            mAppDetailSafeDesContainer.measure(0, 0);
            int start = 0;
            int end = mAppDetailSafeDesContainer.getMeasuredHeight();

            if (isAnimation) {
                doAnimation(start, end);
            } else {//直接修改高度
                //取出LayoutParams
                ViewGroup.LayoutParams layoutParams = mAppDetailSafeDesContainer.getLayoutParams();
                layoutParams.height = end;

                //重新设置layoutParams
                mAppDetailSafeDesContainer.setLayoutParams(layoutParams);
            }

        }
        //isOpen取反
        isOpen = !isOpen;
    }

    private void doAnimation(int start, int end) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(start, end);
        valueAnimator.start();

        //监听动画执行过程中渐变值
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int tempHeight = (int) valueAnimator.getAnimatedValue();

                //取出LayoutParams
                ViewGroup.LayoutParams layoutParams = mAppDetailSafeDesContainer.getLayoutParams();
                layoutParams.height = tempHeight;
                //重新设置layoutParams
                mAppDetailSafeDesContainer.setLayoutParams(layoutParams);
            }
        });
        //箭头跟着旋转
        if (isOpen) {
            ObjectAnimator.ofFloat(mAppDetailSafeIvArrow, "rotation", 180, 0).start();
        } else {
            ObjectAnimator.ofFloat(mAppDetailSafeIvArrow, "rotation", 0, 180).start();
        }
    }
}
