package com.itheima.googleplay_31.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.itheima.googleplay_31.base.LoadingPager;

import java.util.List;
import java.util.Map;

/**
 * 类    名:  BaseActivity
 * 创 建 者:  伍碧林
 * 创建时间:  2016/10/20 15:10
 * 描    述： ${TODO}
 */
public class BaseActivity extends AppCompatActivity {
    private LoadingPager mLoadingPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initView();
        setContentView(mLoadingPager);
        initData();
        initListener();
        initActionBar();
    }

    public void initActionBar() {

    }

    public void init() {

    }

    private void initView() {
        mLoadingPager = new LoadingPager(this) {
            @Override
            public LoadedResultEnum initData() {
                return onInitData();
            }

            @Override
            public View initSuccessView() {
                return onInitSuccessView();
            }
        };
    }

    private void initData() {
        //触发加载详情页面对应的数据
        mLoadingPager.triggerLoadData();
    }


    /**
     * @return
     * @des 在子线程中正在的加载具体的数据
     */
    public LoadingPager.LoadedResultEnum onInitData() {
        return null;
    }

    /**
     * @return
     * @des 初始化对应的成功视图
     */
    public View onInitSuccessView() {
        return null;
    }


    public void initListener() {

    }

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
