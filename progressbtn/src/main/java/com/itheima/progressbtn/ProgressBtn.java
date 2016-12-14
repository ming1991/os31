package com.itheima.progressbtn;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * 类    名:  ProgressBtn
 * 创 建 者:  伍碧林
 * 创建时间:  2016/10/22 08:47
 * 描    述： ${TODO}
 */
public class ProgressBtn extends Button {
    private long mMax = 100;
    private long mProgress;
    private boolean isProgressEnable = true;

    /**
     * 设置进度的最大值
     *
     * @param max
     */
    public void setMax(long max) {
        mMax = max;
    }

    /**
     * 设置进度的当前值
     *
     * @param progress
     */
    public void setProgress(long progress) {
        mProgress = progress;
        //重新绘制进度
        invalidate();
    }

    /**
     * 设置是否允许进度
     *
     * @param progressEnable
     */
    public void setProgressEnable(boolean progressEnable) {
        isProgressEnable = progressEnable;
    }

    public ProgressBtn(Context context) {
        super(context);
    }

    public ProgressBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isProgressEnable) {
//        canvas.drawText("haha",20,20,getPaint());
            Drawable blueBg = new ColorDrawable(Color.BLUE);
            //指定Drawable绘制范围
            int left = 0;
            int top = 0;
            int right = (int) (mProgress * 1.0f / mMax * getMeasuredWidth() + .5f);
            int bottom = getBottom();
            blueBg.setBounds(left, top, right, bottom);
            blueBg.draw(canvas);
        }
        //画背景
        super.onDraw(canvas);
    }
}
