package com.itheima.googleplay_31.fragment;

import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.itheima.googleplay_31.R;
import com.itheima.googleplay_31.base.BaseFragment;
import com.itheima.googleplay_31.base.LoadingPager;
import com.itheima.googleplay_31.base.MyBaseAdapter;
import com.itheima.googleplay_31.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 类    名:  HomeFragment
 * 创 建 者:  伍碧林
 * 创建时间:  2016/10/15 09:09
 * 描    述： ${TODO}
 */
public class HomeFragmentBackUp1 extends BaseFragment {

    private List<String> mDatas;

    /**
     * @return
     * @des 在子线程中真正的加载具体的数据
     */
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

    /**
     * @return
     * @des 创建具体的成功视图, 进行相应的数据绑定
     */
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
            /*--------------- 决定根视图长什么样子 ---------------*/
            ViewHolder holder = null;
            if (convertView == null) {
                //创建holder对象
                holder = new ViewHolder();
                //决定ItemView长啥样子
                convertView = View.inflate(UIUtils.getContext(), R.layout.item_temp, null);
                //初始化holder持有的对象
                holder.mTemp1 = (TextView) convertView.findViewById(R.id.tmp_tv_1);
                holder.mTemp2 = (TextView) convertView.findViewById(R.id.tmp_tv_2);
                //难理解-->视图"找"一个"满足条件的类的对象"绑定在自己身上
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            /*--------------- 得到ItemBean,然后进行数据和视图的绑定 ---------------*/
            //data
            String data = mDatas.get(position);

            //data+view
            holder.mTemp1.setText("我是头-" + data);
            holder.mTemp2.setText("我是尾巴-" + data);

            return convertView;//就是希望得到一个经过了数据和视图绑定的view
        }

        /*
            ViewHolder的条件
                需要持有根视图对应的孩子对象-->严格条件-->最终条件-->直接告知有哪些孩子对象
                持有根视图-->宽松条件-->可以自行找出对应的孩子对象-->最终也可以确定有哪些孩子对象
         */
        class ViewHolder {
            TextView mTemp1;
            TextView mTemp2;
        }
    }
}
