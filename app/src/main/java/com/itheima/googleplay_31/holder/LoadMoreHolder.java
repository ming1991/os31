package com.itheima.googleplay_31.holder;

import android.view.View;
import android.widget.LinearLayout;

import com.itheima.googleplay_31.R;
import com.itheima.googleplay_31.base.BaseHolder;
import com.itheima.googleplay_31.utils.UIUtils;

import butterknife.BindView;

/**
 * 类    名:  LoadMoreHolder
 * 创 建 者:  伍碧林
 * 创建时间:  2016/10/18 10:49
 * 描    述： ${TODO}
 */
public class LoadMoreHolder extends BaseHolder<Integer> {
    @BindView(R.id.item_loadmore_container_loading)
    LinearLayout mItemLoadmoreContainerLoading;
    @BindView(R.id.item_loadmore_container_retry)
    LinearLayout mItemLoadmoreContainerRetry;

    public static final int LOADMORE_LOADING = 0;//正在加载更多
    public static final int LOADMORE_ERROR   = 1;//加载更多失败,点击重试
    public static final int LOADMORE_NONE    = 2;//没有加载更多

    @Override
    public View initHolderView() {
        return View.inflate(UIUtils.getContext(), R.layout.item_loadmore, null);
    }

    @Override
    public void refresHolderView(Integer state) {
        //隐藏所有的
        mItemLoadmoreContainerLoading.setVisibility(View.GONE);
        mItemLoadmoreContainerRetry.setVisibility(View.GONE);

        switch (state) {
            case LOADMORE_LOADING:
                mItemLoadmoreContainerLoading.setVisibility(View.VISIBLE);
                break;
            case LOADMORE_ERROR:
                mItemLoadmoreContainerRetry.setVisibility(View.VISIBLE);
                break;
            case LOADMORE_NONE:

                break;

            default:
                break;
        }
    }
}
