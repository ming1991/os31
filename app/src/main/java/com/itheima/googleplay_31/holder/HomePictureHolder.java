package com.itheima.googleplay_31.holder;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.itheima.googleplay_31.R;
import com.itheima.googleplay_31.base.BaseHolder;
import com.itheima.googleplay_31.base.MyApplication;
import com.itheima.googleplay_31.conf.Constants;
import com.itheima.googleplay_31.utils.LogUtils;
import com.itheima.googleplay_31.utils.UIUtils;
import com.itheima.googleplay_31.views.ChildViewPager;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;

/**
 * 类    名:  HomePictureHolder
 * 创 建 者:  伍碧林
 * 创建时间:  2016/10/18 14:59
 * 描    述： ${TODO}
 */
public class HomePictureHolder extends BaseHolder<List<String>> {
    @BindView(R.id.item_home_picture_pager)
    ChildViewPager mItemHomePicturePager;
    @BindView(R.id.item_home_picture_container_indicator)
    LinearLayout   mItemHomePictureContainerIndicator;
    private List<String> mPictureList;

    @Override
    public View initHolderView() {
        return View.inflate(UIUtils.getContext(), R.layout.item_home_picture, null);
    }

    @Override
    public void refresHolderView(List<String> pictureList) {
        //保存数据到成员变量
        mPictureList = pictureList;

        //绑定mItemHomePicturePager对应的数据
        mItemHomePicturePager.setAdapter(new MyPagerAdapter());

        //绑定mItemHomePictureContainerIndicator对应的数据

        for (int i = 0; i < mPictureList.size(); i++) {
            ImageView ivIndicator = new ImageView(UIUtils.getContext());
            ivIndicator.setImageResource(R.drawable.indicator_normal);


            int width = UIUtils.dip2px(6);//6dp
            int height = UIUtils.dip2px(6);//6dp
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
            params.bottomMargin = UIUtils.dip2px(6);//6dp
            params.leftMargin = UIUtils.dip2px(6);//6dp
            mItemHomePictureContainerIndicator.addView(ivIndicator, params);

            if (i == 0) {//默认第一个是选中的
                ivIndicator.setImageResource(R.drawable.indicator_selected);
            }
        }
        //设置indicator的切换效果
        mItemHomePicturePager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //处理position
                position = position % mPictureList.size();

                for (int i = 0; i < mPictureList.size(); i++) {
                    //还原成默认的
                    ImageView ivIndicator = (ImageView) mItemHomePictureContainerIndicator.getChildAt(i);
                    ivIndicator.setImageResource(R.drawable.indicator_normal);
                    //选中应该选中的
                    if (i == position) {
                        ivIndicator.setImageResource(R.drawable.indicator_selected);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //左右无限轮播
        mItemHomePicturePager.setCurrentItem(5000);

        //自动轮播
        final AutoScrollTask autoScrollTask = new AutoScrollTask();
        autoScrollTask.start();

        //按下去的时候停止轮播
        mItemHomePicturePager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        LogUtils.sf("ACTION_DOWN");
                        autoScrollTask.stop();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        LogUtils.sf("ACTION_MOVE");

                        break;
                    case MotionEvent.ACTION_UP:
                        LogUtils.sf("ACTION_UP");
                        autoScrollTask.start();
                        break;

                    case MotionEvent.ACTION_CANCEL:
//                        autoScrollTask.start();
                        LogUtils.sf("ACTION_CANCEL");

                        break;


                    default:
                        break;
                }
                return false;
            }
        });
    }

    class AutoScrollTask implements Runnable {
        public void start() {
            MyApplication.getHandler().postDelayed(this, 3000);
        }

        public void stop() {
            MyApplication.getHandler().removeCallbacks(this);
        }

        @Override
        public void run() {
            //得到当前的position
            int curPosition = mItemHomePicturePager.getCurrentItem();
            curPosition++;

            mItemHomePicturePager.setCurrentItem(curPosition);

            start();
        }
    }

    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            if (mPictureList != null) {
//                return mPictureList.size();
                return 10000;
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //处理position
            position = position % mPictureList.size();

            //view-->ImageView
            ImageView iv = new ImageView(UIUtils.getContext());
            iv.setScaleType(ImageView.ScaleType.FIT_XY);

            //data
            String url = mPictureList.get(position);

            //data+view
            Picasso.with(UIUtils.getContext()).load(Constants.URLS.IMGBASEURL + url).into(iv);

            //加入容器
            container.addView(iv);

            return iv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
