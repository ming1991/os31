package com.itheima.googleplay_31.factory;

import com.itheima.googleplay_31.base.BaseFragment;
import com.itheima.googleplay_31.fragment.AppFragment;
import com.itheima.googleplay_31.fragment.CategoryFragment;
import com.itheima.googleplay_31.fragment.GameFragment;
import com.itheima.googleplay_31.fragment.HomeFragment;
import com.itheima.googleplay_31.fragment.HotFragment;
import com.itheima.googleplay_31.fragment.RecommendFragment;
import com.itheima.googleplay_31.fragment.SubjectFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * 类    名:  FragmentFactory
 * 创 建 者:  伍碧林
 * 创建时间:  2016/10/15 09:05
 * 描    述： 负责完成Framgent的创建过程
 */
public class FragmentFactory {
    public static final int                        FRAGMENT_HOME      = 0;//首页
    public static final int                        FRAGMENT_APP       = 1;//应用
    public static final int                        FRAGMENT_GAME      = 2;//游戏
    public static final int                        FRAGMENT_SUBJECT   = 3;//专题
    public static final int                        FRAGMENT_RECOMMEND = 4;//推荐
    public static final int                        FRAGMENT_CATEGORY  = 5;//分类
    public static final int                        FRAGMENT_HOT       = 6;//排行
    public static       Map<Integer, BaseFragment> mCacheFramgents    = new HashMap<>();

    /**
     * 根据position创建对应的Framgent
     *
     * @param position
     * @return
     */
    public static BaseFragment createFragment(int position) {
        BaseFragment fragment = null;
        //优先从集合中返回
        if (mCacheFramgents.containsKey(position)) {
            return mCacheFramgents.get(position);
        }

        switch (position) {
            case FRAGMENT_HOME:
                fragment = new HomeFragment();
                break;
            case FRAGMENT_APP:
                fragment = new AppFragment();
                break;
            case FRAGMENT_GAME:
                fragment = new GameFragment();
                break;
            case FRAGMENT_SUBJECT:
                fragment = new SubjectFragment();
                break;
            case FRAGMENT_RECOMMEND:
                fragment = new RecommendFragment();
                break;
            case FRAGMENT_CATEGORY:
                fragment = new CategoryFragment();
                break;
            case FRAGMENT_HOT:
                fragment = new HotFragment();
                break;

            default:
                break;
        }
        //统一保存到集合中
        mCacheFramgents.put(position, fragment);

        return fragment;
    }
}
