package com.itheima.googleplay_31.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * 类    名:  BaseFragmentCommon
 * 创 建 者:  伍碧林
 * 创建时间:  2016/10/15 09:59
 * 描    述： 所有Fragment的最基本的基类
 */

public abstract class BaseFragmentCommon extends Fragment {
    //属性1
    //属性2
    //方法1
    //方法2
    /*
    抽取基类(BaseFragmentCommon)的作用/好处?
        1.从java语言-->可以放置共有的方法和共有的属性-->提供复用性-->减少代码量
        2.Framgent具体的子类不用覆写相应的生命周期方法,只需要覆写我们定义的方法即可
        3.而且我们还可以决定哪些方法是必须实现,哪些方法是选择性实现
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initListener();
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * @des 完成一些初始化操作, 比如接收传递过来的参数之类
     * @des 在BaseFragment中不知道具体如何初始化,交给子类
     * @des 子类是选择性实现,所以保持默认即可
     */
    public void init() {

    }

    /**
     * @des 决定Framgent中的View是啥
     * @des 在BaseFramgent中不知道具体如何初始化view,交给子类
     * @des 子类是必须实现,定义为抽象方法即可
     */
    public abstract View initView();

    /**
     * @des 初始化相关的Listener
     */
    public void initListener() {

    }

    /**
     * @des 完成相应数据的加载
     */
    public void initData() {

    }

}
