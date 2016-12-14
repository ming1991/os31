package com.itheima.googleplay_31.base;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.itheima.googleplay_31.factory.ThreadPoolProxyFactory;
import com.itheima.googleplay_31.holder.LoadMoreHolder;
import com.itheima.googleplay_31.utils.LogUtils;

import java.util.List;

/**
 * 类    名:  SuperBaseAdapter
 * 创 建 者:  伍碧林
 * 创建时间:  2016/10/15 17:30
 * 描    述： 基于MyBaseAdapter做封装,针对的是getView方法
 */
public abstract class SuperBaseAdapter<ITEMBEANTYPE> extends MyBaseAdapter<ITEMBEANTYPE> implements AdapterView.OnItemClickListener {

    public static final int VIEWTYPE_LOADMORE = 0;
    public static final int VIEWTYPE_NORMAL   = 1;
    private ListView       mListView;
    private LoadMoreHolder mLoadMoreHolder;
    private LoadMoreTask   mLoadMoreTask;
    private int            mCurState;

    public SuperBaseAdapter(List<ITEMBEANTYPE> datas, ListView listView) {
        super(datas);
        mListView = listView;
        mListView.setOnItemClickListener(this);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        int curItemViewType = getItemViewType(position);
            /*--------------- 决定根视图长什么样子 ---------------*/
        BaseHolder holder;
        if (convertView == null) {
            if (curItemViewType == VIEWTYPE_LOADMORE) {//加载更多
                holder = getLoadMoreHolder();
            } else {//普通条目类型
                holder = getSpecialBaseHolder(position);
            }

        } else {
            holder = (BaseHolder) convertView.getTag();
        }

            /*--------------- 得到ItemBean,然后进行数据和视图的绑定 ---------------*/
        if (curItemViewType == VIEWTYPE_LOADMORE) {//加载更多

            if (hasLoadMore()) {
                mLoadMoreHolder.setDataAndRefreshHolderView(LoadMoreHolder.LOADMORE_LOADING);
                //TODO触发加载更多的数据
                triggerLoadMoreData();
            } else {
                mLoadMoreHolder.setDataAndRefreshHolderView(LoadMoreHolder.LOADMORE_NONE);
            }


        } else {//普通条目类型
            //data
            ITEMBEANTYPE data = mDatas.get(position);

            //传递数据给holder,让holder进行数据和视图的绑定
            holder.setDataAndRefreshHolderView(data);
        }


        return holder.mHolderView;//就是希望得到一个经过了数据和视图绑定的view
    }


    /**
     * @param position
     * @return
     * @des 返回具体的BaseHolder的子类对象
     * @des 交给子类, 子类是必须实现, 定义成为抽象方法
     */
    @NonNull
    public abstract BaseHolder getSpecialBaseHolder(int position);

    /*--------------- listView中显示几种ViewType begin ---------------*/
    /*
    getViewTypeCount();-->  得到ViewType的总数
     */
    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount() + 1;//1(普通条目)+1(加载更多)=2
    }

    /*
     getItemViewType  (int position)--> 得到指定position所对应条目的ViewType
     0 to getViewTypeCount - 1-->现在返回值应该就是0或者是1
     */
    @Override
    public int getItemViewType(int position) {
        if (position == getCount() - 1) {
            return VIEWTYPE_LOADMORE;//加载更多
        } else {
            return getNormalItemViewType(position);
        }
    }

    /**
     * @param position
     * @return
     * @des 得到普通条目的ViewType类型
     * @des 子类可以覆写该方法, 返回更多普通条目的类型
     */
    public int getNormalItemViewType(int position) {
        return VIEWTYPE_NORMAL;//默认是1
    }

    @Override
    public int getCount() {
        return super.getCount() + 1;
    }

    /**
     * 得到加载更多的holder
     *
     * @return
     */
    private LoadMoreHolder getLoadMoreHolder() {
        if (mLoadMoreHolder == null) {
            mLoadMoreHolder = new LoadMoreHolder();
        }
        return mLoadMoreHolder;
    }

    /**
     * @return
     * @des 是否有加载更多
     * @des 子类可以覆写该方法, 最终决定是否有加载更多
     */
    public boolean hasLoadMore() {
        return false;//默认没有加载更多
    }

    /**
     * 触发加载更多的数据
     */
    private void triggerLoadMoreData() {
        if (mLoadMoreTask == null) {
            LogUtils.s("###触发加载更多的数据");
            mCurState = LoadMoreHolder.LOADMORE_LOADING;
            mLoadMoreHolder.setDataAndRefreshHolderView(mCurState);


            //异步加载
            mLoadMoreTask = new LoadMoreTask();
            ThreadPoolProxyFactory.createNormalPoolProxy().execute(mLoadMoreTask);
        }
    }

    class LoadMoreTask implements Runnable {
        private static final int PAGERSIZE = 20;

        @Override
        public void run() {
            /*--------------- 定义数据 ---------------*/
            mCurState = 0;
            List<ITEMBEANTYPE> loadMoreList = null;

            /*--------------- 真正的在子线中加载更多的数据,得到数据,处理数据 ---------------*/
            try {
                loadMoreList = onLoadMoreData();
                if (loadMoreList == null) {
                    mCurState = LoadMoreHolder.LOADMORE_NONE;//没有加载更多
                } else {
                    if (loadMoreList.size() < PAGERSIZE) {
                        mCurState = LoadMoreHolder.LOADMORE_NONE;//没有加载更多
                    } else {
                        mCurState = LoadMoreHolder.LOADMORE_LOADING;//有加载更多
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                mCurState = LoadMoreHolder.LOADMORE_ERROR;//加载更多失败,点击重试
            }
            /*--------------- 创建2个临时变量 ---------------*/
            final List<ITEMBEANTYPE> finalLoadMoreList = loadMoreList;
            final int finalCurState = mCurState;

            /*--------------- 刷新2个ui ---------------*/
            MyApplication.getHandler().post(new Runnable() {
                @Override
                public void run() {
                    //刷新UI-->listview-->更新数据集,然后使用notifyDataSetChanged()-->mDatas.addAll(loadMoreList)
                    if (finalLoadMoreList != null && finalLoadMoreList.size() > 0) {
                        //更新数据集
                        mDatas.addAll(finalLoadMoreList);
                        //刷新listView
                        notifyDataSetChanged();

                    }
                    //刷新UI-->mLoadmoreHolder-->得到最新的状态,调用setDataAndRefreshHolderView()-->mCurState
                    mLoadMoreHolder.setDataAndRefreshHolderView(finalCurState);

                }
            });
            //置空任务
            mLoadMoreTask = null;
        }
    }

    /**
     * @return
     * @des 在子线程中真正的加载更多的数据
     * @des 在SuperBaseAdapter中不知道如何加载更多的数据, 交给子类
     * @des 子类是
     */
    public List<ITEMBEANTYPE> onLoadMoreData() throws Exception {
        return null;
    }
    /*--------------- listView中显示几种ViewType end ---------------*/

    /*--------------- 处理item的点击事件 ---------------*/

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        position = position - mListView.getHeaderViewsCount();

        int curItemViewType = getItemViewType(position);
        if (curItemViewType == VIEWTYPE_LOADMORE) {
            if (mCurState == LoadMoreHolder.LOADMORE_ERROR) {
                triggerLoadMoreData();
            }
        } else {
            onNormalItemClick(parent, view, position, id);
        }
    }

    /**
     * 普通条目的点击事件
     * 子类覆写方法,处理普通条目的点击事件
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    public void onNormalItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

}
