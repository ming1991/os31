package com.itheima.googleplay_31.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 类    名:  ChildViewPager
 * 创 建 者:  伍碧林
 * 创建时间:  2016/10/18 16:13
 * 描    述： 左右滑动的时候,请求父容器不要拦截事件
 */
public class ChildViewPager extends ViewPager {

    private float mDownX;
    private float mDownY;

    public ChildViewPager(Context context) {
        super(context);
    }

    public ChildViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /*
       1.是否分发?
        return true:都不会来到自己的onInterceptTouchEvent(比较特别)
        return false:都不会来到自己的onInterceptTouchEvent
        return super:走到自己的onInterceptTouchEvent
        概念:一般dispatchTouchEvent不做处理
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    /*
     2.是否拦截
        return true:来到自己的onTouchEvent
        return false:事件就会传递给-->孩子(有待孩子处理)
        return super:默认,该怎样就怎样
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    /*
       3.是否消费
            return true:事件消费,事件传递就结束
            return false:事件未消费,事件就会传递-->父容器(有待父容器消费)
            return super:默认,该怎样就怎样
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getRawX();
                mDownY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = ev.getRawX();
                float moveY = ev.getRawY();
                int diffX = (int) (moveX - mDownX + .5f);
                int diffY = (int) (moveY - mDownY + .5f);
                if (Math.abs(diffX) > Math.abs(diffY)) {//左右滑动
                    //请求父容器不要拦截事件
                    getParent().requestDisallowInterceptTouchEvent(true);
                } else {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }

                break;
            case MotionEvent.ACTION_UP:

                break;

            default:
                break;
        }
        return super.onTouchEvent(ev);
    }
}
