package com.itheima.googleplay_31.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.itheima.googleplay_31.R;
import com.itheima.googleplay_31.utils.LogUtils;
import com.itheima.googleplay_31.utils.UIUtils;

/**
 * 类    名:  RatioFrameLayout
 * 创 建 者:  伍碧林
 * 创建时间:  2016/10/18 17:26
 * 描    述： 已知宽度,动态计算高度
 * 描    述： 已知高度,动态计算宽度
 * 描    述： 公式 selfWidth/selfHeight = mPicRatio;
 */
public class RatioFrameLayout extends FrameLayout {
    public static final int   RELATIVE_WIDTH  = 0;//相对于宽度,已知宽度,动态计算高度
    public static final int   RELATIVE_HEIGHT = 1;//相对于高度,已知高度,动态计算宽度
    public              int   mCurRelative    = RELATIVE_WIDTH;
    public              float mPicRatio       = 1f;

    public RatioFrameLayout(Context context) {
        this(context, null);
    }

    public RatioFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        //取出自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatioFrameLayout);
        mPicRatio = typedArray.getFloat(R.styleable.RatioFrameLayout_picRatio, mPicRatio);
        mCurRelative = typedArray.getInt(R.styleable.RatioFrameLayout_relative, mCurRelative);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //得到宽度的模式
        /*
        AT_MOST  至多
        UNSPECIFIED 不确定的  xml中的体现的话-->wrap_content
        EXACTLY 确定的 xml中的体现的话-->fill_parent math_parent 100dp 100px
         */
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY && mCurRelative == RELATIVE_WIDTH) {//已知宽度,动态计算高度
            //如何取到selfWidth
            int selfWidth = MeasureSpec.getSize(widthMeasureSpec);

            //计算应有的高度
            //selfWidth/selfHeight = mPicRatio;
            int selfHeight = (int) (selfWidth / mPicRatio + .5f);
            LogUtils.s("应有的高度->" + UIUtils.px2dp(selfHeight));

            //保存测绘结果
            setMeasuredDimension(selfWidth, selfHeight);

            //计算孩子的宽度和高度

            int childWidth = selfWidth - getPaddingLeft() - getPaddingRight();
            int childHeight = selfHeight - getPaddingBottom() - getPaddingTop();

            //让孩子测量自身
            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
            measureChildren(childWidthMeasureSpec, childHeightMeasureSpec);


        } else if (heightMode == MeasureSpec.EXACTLY && mCurRelative == RELATIVE_HEIGHT) {//已知高度,动态计算宽度
            //取出自身的高度
            int selfHeight = MeasureSpec.getSize(heightMeasureSpec);

            //动态计算宽度
            //selfWidth/selfHeight = mPicRatio;
            int selfWidth = (int) (selfHeight * mPicRatio + .5f);
            LogUtils.s("应有的高度->" + UIUtils.px2dp(selfHeight));

            //保存测绘结果
            setMeasuredDimension(selfWidth, selfHeight);

            //计算孩子的宽度和高度

            int childWidth = selfWidth - getPaddingLeft() - getPaddingRight();
            int childHeight = selfHeight - getPaddingBottom() - getPaddingTop();

            //让孩子测量自身
            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
            measureChildren(childWidthMeasureSpec, childHeightMeasureSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }


    }
}
