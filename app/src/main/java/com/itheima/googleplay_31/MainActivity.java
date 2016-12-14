package com.itheima.googleplay_31;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStripExtends;
import com.itheima.googleplay_31.base.BaseFragment;
import com.itheima.googleplay_31.factory.FragmentFactory;
import com.itheima.googleplay_31.utils.LogUtils;
import com.itheima.googleplay_31.utils.UIUtils;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
Activity:3.0最后的可以直接管理Fragment(sdk里面的Fragment)
FragmentActivity:可以管理Framgent,管理的是v4包中的Fragment
AppCompatActivity:使用v7中的actionBar,还可以管理v4包中Fragment
 */
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.fl_leftMenu)
    FrameLayout                 mFlLeftMenu;
    @BindView(R.id.drwerlayout)
    DrawerLayout                mDrwerlayout;
    @BindView(R.id.tabs)
    PagerSlidingTabStripExtends mTabs;
    @BindView(R.id.viewPager)
    ViewPager                   mViewPager;
    private ActionBarDrawerToggle mToggle;
    private String[]              mTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initActionBar();
        initData();//创建adapter,设置adapter-->getItem-->至少创建两个Framgent(HomeFragment,AppFramgent)-->放到容器中
        initListener();//设置listener,手动选中第一页-->取出HomeFramgent-->找到LoadingPager调用TriggerLoadDate方法
    }

    private void initListener() {
        final MyPageChangeListener listener = new MyPageChangeListener();

        //监听viewpager的滚动切换
        mTabs.setOnPageChangeListener(listener);

        mViewPager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //手动选中第一页-->早了一些-->应该晚一些调用-->找一个触发调用的时机
                listener.onPageSelected(0);

                //移除监听
                mViewPager.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

    }

    class MyPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //position-->BaseFramgent-->loadingPager-->触发加载
            Map<Integer, BaseFragment> mCacheFramgents = FragmentFactory.mCacheFramgents;

            if (mCacheFramgents.containsKey(position)) {//创建了fragment,并且已经加入到容器中
                BaseFragment baseFragment = mCacheFramgents.get(position);
                baseFragment.mLoadingPager.triggerLoadData();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private void initActionBar() {
        //得到actionbar对象
        ActionBar actionBar = getSupportActionBar();

        //设置标题
        actionBar.setTitle("GooglePlay");
//        actionBar.setSubtitle("我是副标题");

        //设置图标
        actionBar.setIcon(R.mipmap.ic_launcher);
        actionBar.setLogo(R.mipmap.ic_action_edit);

        //显示图标-->默认是隐藏的,默认是icon优先
        actionBar.setDisplayShowHomeEnabled(false);
//        设置logo图标优先于icon图标-->默认是不用logo
        actionBar.setDisplayUseLogoEnabled(true);

        //显示返回按钮/显示回退部分-->默认是隐藏
        actionBar.setDisplayHomeAsUpEnabled(true);

        initActionBarDrawerToggle();
    }

    private void initActionBarDrawerToggle() {
        //1.创建ActionBarDrawerToggle对象
        mToggle = new ActionBarDrawerToggle(this, mDrwerlayout, R.string.open, R.string.close);

        //2.调用ActionBarDrawerToggle同步方法-->回退按钮ui发生了改变
        mToggle.syncState();

        //3.让ActionBarDrawerToggle按钮可以跟随DrawerLayout拖动而发生变化
        mDrwerlayout.addDrawerListener(mToggle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                Toast.makeText(getApplicationContext(), "我被点击了", Toast.LENGTH_SHORT).show();
                //4.点击ActionBarDrawerToggle可以控制DrawerLayout打开和关闭
                mToggle.onOptionsItemSelected(item);
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initData() {
        //DataSets
        mTitles = UIUtils.getStrings(R.array.titles);
        //为viewpager设置adapter
//        MyViewPagerAdapter adapter = new MyViewPagerAdapter();
        MyFragmentStatePagerAdapter adapter = new MyFragmentStatePagerAdapter(getSupportFragmentManager());
//        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);

        //tabs和viewpager进行绑定操作
        mTabs.setViewPager(mViewPager);
    }

    /*
        PagerAdapter-->View
        FragmentStatePagerAdapter-->Fragment
        FragmentPagerAdapter-->Fragment
     */
    class MyViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            if (mTitles != null) {
                return mTitles.length;
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //模拟一个textview-->View
            TextView tv = new TextView(UIUtils.getContext());
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(Color.BLUE);

            //data
            String data = mTitles[position];

            //data+view
            tv.setText(data);

            //加入容器
            container.addView(tv);

            return tv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }
    }

    class MyFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

        public MyFragmentStatePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            LogUtils.sf("创建-->" + mTitles[position]);
            Fragment framgent = FragmentFactory.createFragment(position);
            return framgent;
        }

        @Override
        public int getCount() {
            if (mTitles != null) {
                return mTitles.length;
            }
            return 0;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }
    }

    class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            LogUtils.sf("创建-->" + mTitles[position]);
            Fragment framgent = FragmentFactory.createFragment(position);
            return framgent;
        }

        @Override
        public int getCount() {
            if (mTitles != null) {
                return mTitles.length;
            }
            return 0;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }
    }
}
