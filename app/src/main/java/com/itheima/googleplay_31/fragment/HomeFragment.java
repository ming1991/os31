package com.itheima.googleplay_31.fragment;

import android.os.SystemClock;
import android.view.View;
import android.widget.ListView;

import com.itheima.googleplay_31.adapter.ItemAdapter;
import com.itheima.googleplay_31.base.BaseFragment;
import com.itheima.googleplay_31.base.LoadingPager;
import com.itheima.googleplay_31.bean.DownLoadInfo;
import com.itheima.googleplay_31.bean.HomeBean;
import com.itheima.googleplay_31.bean.ItemBean;
import com.itheima.googleplay_31.factory.ListViewFactory;
import com.itheima.googleplay_31.holder.HomePictureHolder;
import com.itheima.googleplay_31.holder.ItemHolder;
import com.itheima.googleplay_31.manager.DownLoadManager;
import com.itheima.googleplay_31.protocol.HomeProtocol;

import java.io.IOException;
import java.util.List;

/**
 * 类    名:  HomeFragment
 * 创 建 者:  伍碧林
 * 创建时间:  2016/10/15 09:09
 * 描    述： 动态的添加和移除观察者
 */
public class HomeFragment extends BaseFragment {


    private List<ItemBean> mDatas;
    private List<String>   mPictureList;
    private HomeProtocol   mProtocol;
    private MyAdapter      mAdapter;

    /**
     * @return
     * @des 在子线程中真正的加载具体的数据
     */
    @Override
    public LoadingPager.LoadedResultEnum initData() {
        /*try {
            //模拟网络请求耗时操作
            //1.创建OkHttpClient对象
            OkHttpClient okHttpClient = new OkHttpClient();
            //2.创建请求对象
            //?index=0
            String url = Constants.URLS.BASEURL + "home";

            //定义一个map装参数
            Map<String, Object> paramsMap = new HashMap<>();
            paramsMap.put("index", "0");
            //paramsMap-->"index=0"
            String urlParamsByMap = HttpUtils.getUrlParamsByMap(paramsMap);

            url = url + "?" + urlParamsByMap;

            Request request = new Request.Builder().get().url(url).build();
            //3.发起请求
            Response response = okHttpClient.newCall(request).execute();

            if (response.isSuccessful()) {//服务器有响应
                String jsonString = response.body().string();
                //解析jsonString
                Gson gson = new Gson();
                HomeBean homeBean = gson.fromJson(jsonString, HomeBean.class);

                //根据返回回来的数据,进行判断,然后返回与之对应的状态
                LoadingPager.LoadedResultEnum state = checkResData(homeBean);
                if (state != LoadingPager.LoadedResultEnum.SUCCESS) {//说明homeBean有问题
                    return state;//empty
                }
                state = checkResData(homeBean.list);
                if (state != LoadingPager.LoadedResultEnum.SUCCESS) {//说明homeBean.list有问题
                    return state;
                }

                //走到这里来的时候,homeBean里面的list是有数据
                mDatas = homeBean.list;
                mPictureList = homeBean.picture;

                return state;//success
            } else {
                return LoadingPager.LoadedResultEnum.ERROR;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return LoadingPager.LoadedResultEnum.ERROR;
        }*/
        /*--------------- 协议简单的封装以后 ---------------*/
        mProtocol = new HomeProtocol();
        try {
            HomeBean homeBean =  mProtocol.loadData(0);

            //根据返回回来的数据,进行判断,然后返回与之对应的状态
            LoadingPager.LoadedResultEnum state = checkResData(homeBean);
            if (state != LoadingPager.LoadedResultEnum.SUCCESS) {//说明homeBean有问题
                return state;//empty
            }
            state = checkResData(homeBean.list);
            if (state != LoadingPager.LoadedResultEnum.SUCCESS) {//说明homeBean.list有问题
                return state;
            }

            //走到这里来的时候,homeBean里面的list是有数据
            mDatas = homeBean.list;
            mPictureList = homeBean.picture;

            return state;//success
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
        //view
        ListView listView = ListViewFactory.createListView();
        //dataSets-->成员变量里面

        //添加一个轮播图
        HomePictureHolder pictureHolder = new HomePictureHolder();
        listView.addHeaderView(pictureHolder.mHolderView);//pictureView一个经过了数据绑定的view
        pictureHolder.setDataAndRefreshHolderView(mPictureList);

        //data+view
        mAdapter = new MyAdapter(mDatas, listView);
        listView.setAdapter(mAdapter);

        return listView;
    }

    class MyAdapter extends ItemAdapter {
        public MyAdapter(List<ItemBean> datas, ListView listView) {
            super(datas, listView);
        }

        /**
         * 覆写onLoadMoreData,在子线程完成具体的加载更多
         *
         * @return
         * @throws Exception
         */
        @Override
        public List<ItemBean> onLoadMoreData() throws Exception {
            SystemClock.sleep(4000);
            HomeBean homeBean = mProtocol.loadData(mDatas.size()); //请求加载数据的url 关键字 随着集合的长度发生改变 0 --20  ---40--
            if (homeBean != null) {
                return homeBean.list;
            }
            return super.onLoadMoreData();  //父类默认是null
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
