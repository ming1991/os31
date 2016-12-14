package com.itheima.googleplay_31.fragment;

import android.os.SystemClock;
import android.view.View;
import android.widget.ListView;

import com.itheima.googleplay_31.adapter.ItemAdapter;
import com.itheima.googleplay_31.base.BaseFragment;
import com.itheima.googleplay_31.base.LoadingPager;
import com.itheima.googleplay_31.bean.DownLoadInfo;
import com.itheima.googleplay_31.bean.ItemBean;
import com.itheima.googleplay_31.factory.ListViewFactory;
import com.itheima.googleplay_31.holder.ItemHolder;
import com.itheima.googleplay_31.manager.DownLoadManager;
import com.itheima.googleplay_31.protocol.GameProtocol;

import java.io.IOException;
import java.util.List;

/**
 * 类    名:  HomeFragment
 * 创 建 者:  伍碧林
 * 创建时间:  2016/10/15 09:09
 * 描    述： ${TODO}
 */
public class GameFragment extends BaseFragment {

    private GameProtocol   mProtocol;
    private List<ItemBean> mDatas;
    private GameAdapter mAdapter;

    /**
     * @return
     * @des 在子线程中真正的加载具体的数据
     */
    @Override
    public LoadingPager.LoadedResultEnum initData() {
        mProtocol = new GameProtocol();
        try {
            mDatas = mProtocol.loadData(0);
            return checkResData(mDatas);
        } catch (IOException e) {
            e.printStackTrace();
            return LoadingPager.LoadedResultEnum.ERROR;
        }
    }

    /**
     * @return
     * @des 创建具体的成功视图, 进行相应的数据绑定
     */
    @Override
    public View initSuccessView() {
        ListView listView = ListViewFactory.createListView();
        mAdapter = new GameAdapter(mDatas, listView);
        listView.setAdapter(mAdapter);
        return listView;
    }

    class GameAdapter extends ItemAdapter {

        public GameAdapter(List<ItemBean> datas, ListView listView) {
            super(datas, listView);
        }

        @Override
        public List<ItemBean> onLoadMoreData() throws Exception {
            SystemClock.sleep(2000);
            return mProtocol.loadData(mDatas.size());
        }
    }

    @Override
    public void onResume() {
        //动态的添加观察者
        if (mAdapter != null) {
            List<ItemHolder> itemHolders = mAdapter.mItemHolders;
            for (ItemHolder itemHolder : itemHolders) {
                DownLoadManager.getInstance().addObserver(itemHolder);
                //手动发布最新的状态
                DownLoadInfo downLoadInfo = DownLoadManager.getInstance().createDownLoadInfo(itemHolder.mData);
                DownLoadManager.getInstance().notifyObservers(downLoadInfo);
            }
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        //动态的移除观察者
        if (mAdapter != null) {
            List<ItemHolder> itemHolders = mAdapter.mItemHolders;
            for (ItemHolder itemHolder : itemHolders) {
                DownLoadManager.getInstance().deleteObserver(itemHolder);
            }
        }
        super.onPause();
    }
}
