package com.itheima.googleplay_31.holder;

import android.view.View;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.itheima.googleplay_31.R;
import com.itheima.googleplay_31.base.BaseHolder;
import com.itheima.googleplay_31.bean.ItemBean;
import com.itheima.googleplay_31.utils.UIUtils;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;

import butterknife.BindView;

/**
 * 类    名:  DetailInfoHolder
 * 创 建 者:  伍碧林
 * 创建时间:  2016/10/20 15:06
 * 描    述： ${TODO}
 */
public class DetailDesHolder extends BaseHolder<ItemBean> implements View.OnClickListener {


    @BindView(R.id.app_detail_des_tv_des)
    TextView mAppDetailDesTvDes;
    @BindView(R.id.app_detail_des_tv_author)
    TextView mAppDetailDesTvAuthor;

    @BindView(R.id.app_detail_des_iv_arrow)
    ImageView mAppDetailDesIvArrow;
    private boolean isOpen = true;//默认是展开
    private ItemBean mData;
    private int      mMAppDetailDesTvDesMeasuredHeight;

    @Override
    public View initHolderView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_detail_des, null);
        view.setOnClickListener(this);
        return view;
    }

    @Override
    public void refresHolderView(ItemBean data) {
        //保存数据为成员变量
        mData = data;

        mAppDetailDesTvAuthor.setText(data.author);
        mAppDetailDesTvDes.setText(data.des);

        mAppDetailDesTvDes.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //默认折叠
                changeDesTvHeight(false);
                mAppDetailDesTvDes.getViewTreeObserver().removeGlobalOnLayoutListener(this);

            }
        });
    }

    @Override
    public void onClick(View v) {
        changeDesTvHeight(true);

    }

    private void changeDesTvHeight(boolean isAnimation) {
        //折叠,展开的动画
        if (isOpen) {//折叠动画
//            折叠动画 mAppDetailDesTvDes 高度变化 从 应有的高度-->7行的高度

            if (mMAppDetailDesTvDesMeasuredHeight == 0) {
                mMAppDetailDesTvDesMeasuredHeight = mAppDetailDesTvDes.getMeasuredHeight();
            }

            int start = mMAppDetailDesTvDesMeasuredHeight;
            int end = getShortTextHeight(7, mData.des);
            if (isAnimation) {
                doAnimation(start, end);
            } else {
                mAppDetailDesTvDes.setHeight(end);
            }


        } else {//展开动画

            int start = getShortTextHeight(7, mData.des);
            int end = mMAppDetailDesTvDesMeasuredHeight;

            if (isAnimation) {
                doAnimation(start, end);
            } else {
                mAppDetailDesTvDes.setHeight(end);
            }
        }
        isOpen = !isOpen;
    }

    private void doAnimation(int start, int end) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(mAppDetailDesTvDes, "height", start, end);
        objectAnimator.start();

        if (isOpen) {
            ObjectAnimator.ofFloat(mAppDetailDesIvArrow, "rotation", 180, 0).start();
        } else {
            ObjectAnimator.ofFloat(mAppDetailDesIvArrow, "rotation", 0, 180).start();
        }
        //监听动画执行完成
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                //找到外层的Scrollview完成滚动
                ViewParent parent = mAppDetailDesTvDes.getParent();
                while (true) {
                    parent = parent.getParent();
                    if (parent instanceof ScrollView) {
                        ((ScrollView) parent).fullScroll(View.FOCUS_DOWN);
                        break;
                    }
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    /**
     * 取到指定行高的指定内容的TextView的高度
     *
     * @param lineHeight
     * @param content
     * @return
     */
    private int getShortTextHeight(int lineHeight, String content) {
        TextView tempTextView = new TextView(UIUtils.getContext());
        tempTextView.setText(content);

        tempTextView.setLines(lineHeight);

        tempTextView.measure(0, 0);
        int height = tempTextView.getMeasuredHeight();
        return height;
    }
}
