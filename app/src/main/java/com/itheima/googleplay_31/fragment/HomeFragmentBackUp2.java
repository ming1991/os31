package com.itheima.googleplay_31.fragment;

/**
 * 类    名:  HomeFragment
 * 创 建 者:  伍碧林
 * 创建时间:  2016/10/15 09:09
 * 描    述： ${TODO}
 */
public class HomeFragmentBackUp2 {

    /*private List<String> mDatas;

    *//**
     * @return
     * @des 在子线程中真正的加载具体的数据
     *//*
    @Override
    public LoadingPager.LoadedResultEnum initData() {
        //模拟网络请求耗时操作
        SystemClock.sleep(2000);

        //模拟网络请求回来的数据集
        mDatas = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            mDatas.add(i + "");
        }

        return LoadingPager.LoadedResultEnum.SUCCESS;
    }

    *//**
     * @return
     * @des 创建具体的成功视图, 进行相应的数据绑定
     *//*
    @Override
    public View initSuccessView() {
        //view
        ListView listView = new ListView(UIUtils.getContext());
        //dataSets-->成员变量里面

        //data+view
        listView.setAdapter(new MyAdapter(mDatas));

        return listView;
    }

    class MyAdapter extends MyBaseAdapter<String> {

        public MyAdapter(List datas) {
            super(datas);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            *//*--------------- 决定根视图长什么样子 ---------------*//*
            ItemHolder homeHolder;
            if(convertView==null){
                homeHolder = new ItemHolder();
            }else{
                homeHolder = (ItemHolder) convertView.getTag();
            }

            *//*--------------- 得到ItemBean,然后进行数据和视图的绑定 ---------------*//*
            //data
            String data = mDatas.get(position);

            //传递数据给holder,让holder进行数据和视图的绑定
            homeHolder.setDataAndRefreshHolderView(data);

            return homeHolder.mHolderView;//就是希望得到一个经过了数据和视图绑定的view
        }
    }*/
}
