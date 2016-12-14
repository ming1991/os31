package com.itheima.googleplay_31.base;

import android.view.View;

import butterknife.ButterKnife;

/**
 * 类    名:  BaseHolder
 * 创 建 者:  伍碧林
 * 创建时间:  2016/10/15 17:23
 * 描    述： 所有的Holder的基类
 */
public abstract class BaseHolder<HOLDERBEANTYPE> {
    public View   mHolderView;//持有的视图
    public HOLDERBEANTYPE mData;

    public BaseHolder() {
        //  决定HolderView长啥样子
        mHolderView = initHolderView();

        //初始化孩子对象
        ButterKnife.bind(this, mHolderView);

        //视图"找"一个"满足条件的类的对象"绑定在自己身上
        mHolderView.setTag(this);
    }

    /**
     * @return
     * @des 初始化持有的视图(决定持有的视图长什么样子)
     * @des 交给子类,子类是必须实现
     */
    public abstract View initHolderView() ;

    /**
     * @param data
     * @des 接收数据
     * @des 数据和视图的绑定
     */
    public void setDataAndRefreshHolderView(HOLDERBEANTYPE data) {
        //对成员变量里面的数据赋值
        mData = data;

        //刷新HolderView
        refresHolderView(data);
    }

    /**
     * @des 数据和视图的绑定
     * @des 交给子类,必须实现,定义成为抽象方法
     * @param data
     */
    public abstract void refresHolderView(HOLDERBEANTYPE data);
}
