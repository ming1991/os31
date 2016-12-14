package com.itheima.googleplay_31.fragment;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.googleplay_31.base.BaseFragment;
import com.itheima.googleplay_31.base.LoadingPager;
import com.itheima.googleplay_31.protocol.HotProtocol;
import com.itheima.googleplay_31.utils.UIUtils;
import com.itheima.googleplay_31.views.FlowLayout;

import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * 类    名:  HomeFragment
 * 创 建 者:  伍碧林
 * 创建时间:  2016/10/15 09:09
 * 描    述： ${TODO}
 */
public class HotFragment extends BaseFragment {

    private List<String> mDatas;

    /**
     * @return
     * @des 在子线程中真正的加载具体的数据
     */
    @Override
    public LoadingPager.LoadedResultEnum initData() {
        HotProtocol protocol = new HotProtocol();
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
        ScrollView scrollView = new ScrollView(UIUtils.getContext());
        FlowLayout flowLayout = new FlowLayout(UIUtils.getContext());
        //为FlowLayout添加孩子
        for (int i = 0; i < mDatas.size(); i++) {
            //view
            TextView tv = new TextView(UIUtils.getContext());
            //data
            final String data = mDatas.get(i);
            //data+view
            tv.setText(data);

            //修改textview的样式
            int padding = UIUtils.dip2px(4);
            tv.setPadding(padding, padding, padding, padding);
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(Color.WHITE);
//            tv.setBackgroundResource(R.drawable.shape_hot_tv);


            GradientDrawable normalBg = new GradientDrawable();
            //设置填充颜色
            Random random = new Random();
            int alpha = 255;
            int red = random.nextInt(171) + 50;//50-220
            int green = random.nextInt(171) + 50;//50-220
            int blue = random.nextInt(171) + 50;//50-220
            int args = Color.argb(alpha, red, green, blue);
            normalBg.setColor(args);
            //设置圆角半径
            normalBg.setCornerRadius(UIUtils.dip2px(6));

            GradientDrawable pressedBg = new GradientDrawable();
            pressedBg.setCornerRadius(UIUtils.dip2px(6));
            pressedBg.setColor(Color.DKGRAY);


            StateListDrawable selectorBg = new StateListDrawable();

            //默认时候图片
            selectorBg.addState(new int[]{-android.R.attr.state_pressed}, normalBg);

            //按下去时候的图片
            selectorBg.addState(new int[]{android.R.attr.state_pressed}, pressedBg);


            tv.setBackgroundDrawable(selectorBg);

            tv.setClickable(true);

            flowLayout.addView(tv);

            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(UIUtils.getContext(), data, Toast.LENGTH_SHORT).show();
                }
            });
        }

        scrollView.addView(flowLayout);
        return scrollView;
    }
}
