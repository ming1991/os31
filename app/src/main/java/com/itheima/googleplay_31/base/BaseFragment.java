package com.itheima.googleplay_31.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.itheima.googleplay_31.utils.UIUtils;

import java.util.List;
import java.util.Map;

/**
 * 类    名:  BaseFragment
 * 创 建 者:  伍碧林
 * 创建时间:  2016/10/15 10:28
 * 描    述： 这个基类是谷歌市场里面针对所有的Framgent的一个抽取
 * 描    述： 所有的页面都需要视图展示和数据加载,但是相应的代码没有写到BaseFragment,而是把相应代码写到LoadingPager
 */
public abstract class BaseFragment extends Fragment {

    public LoadingPager mLoadingPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mLoadingPager == null) {
            mLoadingPager = new LoadingPager(UIUtils.getContext()) {
                /**
                 * @return
                 * @des 在子线程中真正的加载数据的数据
                 */
                @Override
                public LoadedResultEnum initData() {
                    return BaseFragment.this.initData();
                }

                /**
                 * @return
                 * @des 创建具体的成功视图
                 */
                @Override
                public View initSuccessView() {
                    return BaseFragment.this.initSuccessView();
                }
            };
        } else {
            //处理在2.3上面的兼容问题
            ViewParent parent = mLoadingPager.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(mLoadingPager);
            }
        }
        return mLoadingPager;//(①错误视图,②加载中的视图,③空视图,④成功视图4种视图类型中的一种)-->都是经过了数据绑定的视图
    }

    /**
     * @return
     * @des 在子线程中真正的加载数据的数据
     * @des 在BaseFragment中, 不知道如何具体加载数据, 只能交给子类
     * @des BaseFramgent子类必须实现, 定义成为抽象方法
     */
    public abstract LoadingPager.LoadedResultEnum initData();

    /**
     * @return
     * @des 创建具体的成功视图
     * @des 在BaseFragment中, 不知道如何创建成功视图, 只能交给子类
     * @des BaseFramgent子类必须实现, 定义成为抽象方法
     */
    public abstract View initSuccessView();


    /*
    所有Framgent具体展示的那个View分析一下
        视图类型有几种情况?
            ①错误视图
            ②加载中的视图
            ③空视图
            ④成功视图
        那4种视图类型的特点
             ①错误视图,  ②加载中的视图, ③空视图 属于静态视图
             ④成功视图是一个可变视图
         4种视图的展示规律
            1.某一个时刻只看展示其中的一种视图
            2.默认时候展示的应该加载中的视图
     */
    /*
    所有Framgent还需要加载对应的数据
        基本的加载流程是怎样的?
            0.触发加载                -->②加载中的视图
                进入页面开始加载
                点击按钮重试
                下拉刷新
                上拉加载更多
            1.异步请求-->开线程
            2.得到数据,处理数据
            3.刷新ui-->要主线程刷新ui
                    数据加载失败  -->①错误视图
                    数据加载成功
                        数据为空  -->③空视图
                        数据不为空  -->④成功视图
     */

    public LoadingPager.LoadedResultEnum checkResData(Object resObj) {
        if (resObj == null) {
            return LoadingPager.LoadedResultEnum.EMPTY;
        }

        if (resObj instanceof List) {
            if (((List) resObj).size() == 0) {
                return LoadingPager.LoadedResultEnum.EMPTY;
            }
        }
        if (resObj instanceof Map) {
            if (((Map) resObj).size() == 0) {
                return LoadingPager.LoadedResultEnum.EMPTY;
            }
        }

        return LoadingPager.LoadedResultEnum.SUCCESS;
    }
}
