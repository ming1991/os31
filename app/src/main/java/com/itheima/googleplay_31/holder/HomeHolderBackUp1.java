package com.itheima.googleplay_31.holder;

import android.view.View;
import android.widget.TextView;

import com.itheima.googleplay_31.R;
import com.itheima.googleplay_31.utils.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 类    名:  ItemHolder
 * 创 建 者:  伍碧林
 * 创建时间:  2016/10/15 17:08
 * 描    述： 1.提供视图
 * 描    述： 2.接收数据
 * 描    述： 3.数据和视图的绑定
 */
public class HomeHolderBackUp1 {
    public View   mHolderView;//持有的视图
    public String mData;

    @BindView(R.id.tmp_tv_1)
    TextView mTmpTv1;
    @BindView(R.id.tmp_tv_2)
    TextView mTmpTv2;


    public HomeHolderBackUp1() {
        //  决定HolderView长啥样子
        mHolderView = initHolderView();

        //初始化孩子对象
        ButterKnife.bind(this, mHolderView);

        //视图"找"一个"满足条件的类的对象"绑定在自己身上
        mHolderView.setTag(this);
    }

    /**
     * @return
     * @des 初始化持有的视图(决定持有的视图长什么样子)
     */
    private View initHolderView() {
        return View.inflate(UIUtils.getContext(), R.layout.item_temp, null);
    }

    /**
     * @param data
     * @des 接收数据
     * @des 数据和视图的绑定
     */
    public void setDataAndRefreshHolderView(String data) {
        //对成员变量里面的数据赋值
        mData = data;

        //刷新HolderView
        refresHolderView(data);
    }

    /**
     * 数据和视图的绑定
     *
     * @param data
     */
    private void refresHolderView(String data) {
        //data-->成员变量有,局部变量也有
        //view-->成员变量里面
        //data+view
        mTmpTv1.setText("我是头-" + data);
        mTmpTv2.setText("我是尾巴-" + data);
    }
}
