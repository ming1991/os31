package com.itheima.googleplay_31.fragment;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ListView;

import com.itheima.googleplay_31.base.BaseFragment;
import com.itheima.googleplay_31.base.BaseHolder;
import com.itheima.googleplay_31.base.LoadingPager;
import com.itheima.googleplay_31.base.SuperBaseAdapter;
import com.itheima.googleplay_31.bean.SubjectBean;
import com.itheima.googleplay_31.factory.ListViewFactory;
import com.itheima.googleplay_31.holder.SubjectHolder;
import com.itheima.googleplay_31.protocol.SubjectProtocol;

import java.io.IOException;
import java.util.List;

/**
 * 类    名:  HomeFragment
 * 创 建 者:  伍碧林
 * 创建时间:  2016/10/15 09:09
 * 描    述： ${TODO}
 */
public class SubjectFragment extends BaseFragment {

    private SubjectProtocol   mProtocol;
    private List<SubjectBean> mDatas;

    /**
     * @return
     * @des 在子线程中真正的加载具体的数据
     */
    @Override
    public LoadingPager.LoadedResultEnum initData() {
        mProtocol = new SubjectProtocol();
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
        listView.setAdapter(new SubjectAdapter(mDatas, listView));
        return listView;
    }

    class SubjectAdapter extends SuperBaseAdapter<SubjectBean> {

        public SubjectAdapter(List<SubjectBean> datas, ListView listView) {
            super(datas, listView);
        }

        @NonNull
        @Override
        public BaseHolder getSpecialBaseHolder(int position) {
            return new SubjectHolder();
        }
    }
}