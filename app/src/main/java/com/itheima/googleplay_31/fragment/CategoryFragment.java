package com.itheima.googleplay_31.fragment;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ListView;

import com.itheima.googleplay_31.base.BaseFragment;
import com.itheima.googleplay_31.base.BaseHolder;
import com.itheima.googleplay_31.base.LoadingPager;
import com.itheima.googleplay_31.base.SuperBaseAdapter;
import com.itheima.googleplay_31.bean.CategoryBean;
import com.itheima.googleplay_31.factory.ListViewFactory;
import com.itheima.googleplay_31.holder.CategoryNormalHolder;
import com.itheima.googleplay_31.holder.CategoryTitleHolder;
import com.itheima.googleplay_31.protocol.CategoryProtocol;
import com.itheima.googleplay_31.utils.LogUtils;

import java.io.IOException;
import java.util.List;

/**
 * 类    名:  HomeFragment
 * 创 建 者:  伍碧林
 * 创建时间:  2016/10/15 09:09
 * 描    述： ${TODO}
 */
public class CategoryFragment extends BaseFragment {

    private List<CategoryBean> mDatas;

    /**
     * @return
     * @des 在子线程中真正的加载具体的数据
     */
    @Override
    public LoadingPager.LoadedResultEnum initData() {
        CategoryProtocol protocol = new CategoryProtocol();
        try {
            mDatas = protocol.loadData(0);
            //打印数据
            LogUtils.printList(mDatas);
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
        listView.setAdapter(new CategoryAdapter(mDatas, listView));
        return listView;
    }

    class CategoryAdapter extends SuperBaseAdapter<CategoryBean> {

        public CategoryAdapter(List<CategoryBean> datas, ListView listView) {
            super(datas, listView);
        }

        @NonNull
        @Override
        public BaseHolder getSpecialBaseHolder(int curPosition) {
            CategoryBean categoryBean = mDatas.get(curPosition);

            if (categoryBean.isTitle) {
                return new CategoryTitleHolder();
            } else {
                return new CategoryNormalHolder();
            }

        }

        @Override
        public int getViewTypeCount() {
            return super.getViewTypeCount() + 1;
        }

        /*@Override
        public int getItemViewType(int position) {//0 1 2
            if (position == getCount() - 1) {
                return 0;
            } else {
                CategoryBean categoryBean = mDatas.get(position);
                if (categoryBean.isTitle) {
                    return 1;
                } else {
                    return 2;
                }
            }
        }*/

        @Override
        public int getNormalItemViewType(int position) {
            CategoryBean categoryBean = mDatas.get(position);
            if (categoryBean.isTitle) {
                return 1;
            } else {
                return 2;
            }
        }

    }
}
