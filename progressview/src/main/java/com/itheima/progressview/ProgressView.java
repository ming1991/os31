package com.itheima.progressview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 类    名:  ProgressView
 * 创 建 者:  伍碧林
 * 创建时间:  2016/10/22 09:01
 * 描    述： ${TODO}
 *
 * Viewgroup想走ondrawf方法 必须设置背景
 */
public class ProgressView extends LinearLayout {

    private ImageView mIvIcon;
    private TextView  mTvNote;
    private RectF     mOval;
    private Paint     mPaint;
    private boolean isProgressEnable = true;
    private long    mMax             = 100;
    private long mProgress;

    public void setProgressEnable(boolean progressEnable) {
        isProgressEnable = progressEnable;
    }

    public void setMax(long max) {
        mMax = max;
    }

    public void setProgress(long progress) {
        mProgress = progress;
        //重新绘制ui
        invalidate();
    }

    /**
     * 设置图标
     *
     * @param resId
     */
    public void setIcon(int resId) {
        mIvIcon.setImageResource(resId);
    }

    /**
     * 设置对应的说明文字
     *
     * @param content
     */
    public void setNote(String content) {
        mTvNote.setText(content);
    }

    public ProgressView(Context context) {
        this(context, null);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //挂载对应的布局
        View rootView = View.inflate(context, R.layout.infalte_progressview, this);
        //找孩子
        mIvIcon = (ImageView) rootView.findViewById(R.id.ivIcon);
        mTvNote = (TextView) rootView.findViewById(R.id.tvNote);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);//绘制背景-->透明背景
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);//绘制图标和位置

        if (isProgressEnable) {
            int left = mIvIcon.getLeft();
            int top = mIvIcon.getTop();
            int right = mIvIcon.getRight();
            int bottom = mIvIcon.getBottom();
            //确定弧形的范围
            if (mOval == null) {
                mOval = new RectF(left, top, right, bottom);
            }
            float startAngle = -90;
            float sweepAngle = mProgress * 1.0f / mMax * 360;//动态计算
            boolean useCenter = false;
            if (mPaint == null) {
                mPaint = new Paint();
                //属性设置
                mPaint.setColor(Color.BLUE);
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeWidth(3);
                mPaint.setAntiAlias(true);
            }

            canvas.drawArc(mOval, startAngle, sweepAngle, useCenter, mPaint);
        }
    }
}
