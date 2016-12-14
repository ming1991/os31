package com.itheima.googleplay_31.fragment;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.itheima.googleplay_31.base.BaseFragment;
import com.itheima.googleplay_31.base.LoadingPager;
import com.itheima.googleplay_31.protocol.RecommendProtocol;
import com.itheima.googleplay_31.utils.UIUtils;
import com.itheima.googleplay_31.views.flyinflyout.StellarMap;

import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * 类    名:  HomeFragment
 * 创 建 者:  伍碧林
 * 创建时间:  2016/10/15 09:09
 * 描    述： ${TODO}
 */
public class RecommendFragment extends BaseFragment {

    private List<String> mDatas;

    /**
     * @return
     * @des 在子线程中真正的加载具体的数据
     */
    @Override
    public LoadingPager.LoadedResultEnum initData() {
        RecommendProtocol protocol = new RecommendProtocol();
        try {
            mDatas = protocol.loadData(0);
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
        StellarMap stellarMap = new StellarMap(UIUtils.getContext());
        //绑定数据
        stellarMap.setAdapter(new RecommendAdapter());

        //处理两个小问题
        stellarMap.setGroup(0, true);
        stellarMap.setRegularity(15, 20);

        return stellarMap;
    }

    class RecommendAdapter implements StellarMap.Adapter {
        public static final int PAGESIZE = 15;//规定每页15个

        @Override
        public int getGroupCount() {//返回有多少组 32  2
            if (mDatas.size() % PAGESIZE == 0) {
                return mDatas.size() / PAGESIZE;
            } else {
                return mDatas.size() / PAGESIZE + 1;
            }
        }

        @Override
        public int getCount(int group) {//每组多少个对象
            if (mDatas.size() % PAGESIZE == 0) {
                return PAGESIZE;
            } else {
                if (group == getGroupCount() - 1) {//最后一组
                    return mDatas.size() % PAGESIZE;
                } else {
                    return PAGESIZE;
                }
            }
        }

        @Override
        public View getView(int group, int position, View convertView) {//返回指定组里面指定位置的具体的View
            //view-->textview
            TextView tv = new TextView(UIUtils.getContext());
            //data
            int index = group * PAGESIZE + position;
            String data = mDatas.get(index);
            //data+view
            tv.setText(data);

            //控制textivew的样式
            //随机大小(12-16)
            Random random = new Random();
//            tv.setTextSize(random.nextInt(大小差值+1) + 最小值);
            tv.setTextSize(random.nextInt(5) + 12);

            //随机颜色
            int alpha = 255;
            int red = random.nextInt(101)+100;//100-200
            int green = random.nextInt(101)+100;//100-200
            int blue = random.nextInt(101)+100;//100-200
            int color = Color.argb(alpha, red, green, blue);
            tv.setTextColor(color);
            return tv;
        }

        @Override
        public int getNextGroupOnPan(int group, float degree) {
            return 0;
        }

        @Override
        public int getNextGroupOnZoom(int group, boolean isZoomIn) {
            return 0;
        }
    }
}
