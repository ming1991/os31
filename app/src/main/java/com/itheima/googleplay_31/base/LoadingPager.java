package com.itheima.googleplay_31.base;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import com.itheima.googleplay_31.R;
import com.itheima.googleplay_31.factory.ThreadPoolProxyFactory;
import com.itheima.googleplay_31.utils.UIUtils;

/**
 * 类    名:  LoadingController
 * 创 建 者:  伍碧林
 * 创建时间:  2016/10/15 10:45
 * 描    述： 1.提供视图(4种(加载中,错误,空,成功)视图类型中的一种),可以把自身提供出去
 * 描    述： 2.加载数据
 * 描    述： 3.数据和视图的绑定
 */
public abstract class LoadingPager extends FrameLayout {
    public static final int STATE_LOADING = 0;//加载中
    public static final int STATE_ERROR   = 1;//错误
    public static final int STATE_EMTPY   = 2;//空
    public static final int STATE_SUCCESS = 3;//成功
    public              int mCurState     = STATE_LOADING;//默认是展示加载中视图
    private View         mLoadignView;
    private View         mErrorView;
    private View         mEmptyView;
    private View         mSuccessView;
    private LoadDataTsak mLoadDataTsak;

    public LoadingPager(Context context) {
        super(context);
        //完成3个静态视图的创建
        initCommonView();
    }

    /**
     * @des 创建3个静态视图
     * @called LoadingPager一旦创建的时候被调用
     */
    private void initCommonView() {
        //加载中视图
        mLoadignView = View.inflate(UIUtils.getContext(), R.layout.pager_loading, null);
        addView(mLoadignView);

        //错误视图
        mErrorView = View.inflate(UIUtils.getContext(), R.layout.pager_error, null);
        addView(mErrorView);
        mErrorView.findViewById(R.id.error_btn_retry).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //希望重新触发加载数据
                triggerLoadData();
            }
        });

        //空视图
        mEmptyView = View.inflate(UIUtils.getContext(), R.layout.pager_empty, null);
        addView(mEmptyView);

        //根据状态显示对应的视图
        refreshUIByState();
    }

    /**
     * @des 根据状态显示对应的视图
     * @called 1.一旦创建loadingPager的时候会被调用
     * @called 2.数据加载之前-->显示加载中的视图
     * @called 3.数据加载完成之后
     */
    private void refreshUIByState() {
        //隐藏所有的视图
        mLoadignView.setVisibility(View.GONE);
        mErrorView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.GONE);

        if (mSuccessView != null) {//之前没有创建过成功视图
            mSuccessView.setVisibility(View.GONE);
        }
        switch (mCurState) {
            case STATE_LOADING:
                mLoadignView.setVisibility(View.VISIBLE);
                break;
            case STATE_ERROR:
                mErrorView.setVisibility(View.VISIBLE);
                break;
            case STATE_EMTPY:
                mEmptyView.setVisibility(View.VISIBLE);
                break;
            case STATE_SUCCESS:
                if (mSuccessView == null) {
                    mSuccessView = initSuccessView();
                    addView(mSuccessView);
                }
                //显示成功视图
                mSuccessView.setVisibility(View.VISIBLE);


                break;

            default:
                break;
        }
    }


    /**
     * @des 触发加载数据
     * @called 外界需要去加载数据的时候调用
     */
    public void triggerLoadData() {
        if (mCurState != STATE_SUCCESS && mLoadDataTsak == null) {
            //重置当前的状态为加载中
            mCurState = STATE_LOADING;
            refreshUIByState();

            //异步加载数据
            mLoadDataTsak = new LoadDataTsak();

//            new Thread(mLoadDataTsak).sart();
//            使用线程池-->找代理,创建代理-->找对应的代理的工厂
            ThreadPoolProxyFactory.createNormalPoolProxy().submit(mLoadDataTsak);
        }
    }

    class LoadDataTsak implements Runnable {
        @Override
        public void run() {
            //在子线程中真正的加载数据的数据了吧
            LoadedResultEnum loadedResultEnum = initData();
            int state = loadedResultEnum.state;

            //得到数据,处理数据
            mCurState = state;

            MyApplication.getHandler().post(new Runnable() {
                @Override
                public void run() {
                    //刷新ui->需要重新赋值mCurState
                    refreshUIByState();
                }
            });

            //run方法体的最后代表任务执行完成
            mLoadDataTsak = null;
        }
    }

    /**
     * @return
     * @des 在子线程中真正的加载数据的数据
     * @des 当前的LoadingPager不知道如何具体加载, 只能交给子类
     * @des 子类必须实现, 定义成为抽象方法即可
     */
    public abstract LoadedResultEnum initData();

    /**
     * @return
     * @des 创建具体的成功视图
     * @des 在LoadingPager不知道具体如何创建成功视图, 交给子类
     * @des 子类是必须实现, 定义成为抽象方法即可
     */
    public abstract View initSuccessView();

    public enum LoadedResultEnum {
        EMPTY(STATE_EMTPY), SUCCESS(STATE_SUCCESS), ERROR(STATE_ERROR);

        public int state;

        LoadedResultEnum(int state) {
            this.state = state;
        }
    }
}
