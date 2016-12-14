package com.itheima.googleplay_31.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.itheima.googleplay_31.activity.DetailActivity;
import com.itheima.googleplay_31.base.BaseHolder;
import com.itheima.googleplay_31.base.SuperBaseAdapter;
import com.itheima.googleplay_31.bean.ItemBean;
import com.itheima.googleplay_31.holder.ItemHolder;
import com.itheima.googleplay_31.manager.DownLoadManager;
import com.itheima.googleplay_31.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 类    名:  ItemAdapter
 * 创 建 者:  伍碧林
 * 创建时间:  2016/10/20 11:16
 * 描    述： ${TODO}
 */
public class ItemAdapter extends SuperBaseAdapter<ItemBean> {
    //在adapter中定义一个集合保存所有的观察者
    public List<ItemHolder> mItemHolders = new ArrayList<>();

    public ItemAdapter(List<ItemBean> datas, ListView listView) {
        super(datas, listView);
    }

    /**
     * @param position
     * @return
     * @des 返回具体的BaseHolder的子类对象
     */
    @NonNull
    @Override
    public BaseHolder getSpecialBaseHolder(int position) {
        ItemHolder itemHolder = new ItemHolder();
        //添加观察者到我们自己定义的集合中
        mItemHolders.add(itemHolder);

        //添加观察者到观察者集合中
        DownLoadManager.getInstance().addObserver(itemHolder);

        return itemHolder;
    }

    /**
     * 覆写hasLoadMore,决定有加载更多
     *
     * @return
     */
    @Override
    public boolean hasLoadMore() {
        return true;
    }

    @Override
    public void onNormalItemClick(AdapterView<?> parent, View view, int position, long id) {
        //data
        ItemBean itemBean = mDatas.get(position);
//        Toast.makeText(UIUtils.getContext(), itemBean.name, Toast.LENGTH_SHORT).show();
        //TODO 调用详情页面
        Intent intent = new Intent(UIUtils.getContext(), DetailActivity.class);
        intent.putExtra("packageName", itemBean.packageName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        UIUtils.getContext().startActivity(intent);
    }
}
