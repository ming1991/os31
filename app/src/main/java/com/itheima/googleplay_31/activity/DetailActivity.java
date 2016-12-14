package com.itheima.googleplay_31.activity;

import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.itheima.googleplay_31.R;
import com.itheima.googleplay_31.base.LoadingPager;
import com.itheima.googleplay_31.bean.DownLoadInfo;
import com.itheima.googleplay_31.bean.ItemBean;
import com.itheima.googleplay_31.holder.DetailDesHolder;
import com.itheima.googleplay_31.holder.DetailDownloadHolder;
import com.itheima.googleplay_31.holder.DetailInfoHolder;
import com.itheima.googleplay_31.holder.DetailPicHolder;
import com.itheima.googleplay_31.holder.DetailSafeHolder;
import com.itheima.googleplay_31.manager.DownLoadManager;
import com.itheima.googleplay_31.protocol.DetailProtocol;
import com.itheima.googleplay_31.utils.UIUtils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 类    名:  DetailActivity
 * 创 建 者:  伍碧林
 * 创建时间:  2016/10/22 15:56
 * 描    述：  动态的添加和移除观察者
 */
public class DetailActivity extends BaseActivity {

    @BindView(R.id.flDetailDownload)
    FrameLayout mFlDetailDownload;
    @BindView(R.id.flDetailInfo)
    FrameLayout mFlDetailInfo;
    @BindView(R.id.flDetailSafe)
    FrameLayout mFlDetailSafe;
    @BindView(R.id.flDetailPic)
    FrameLayout mFlDetailPic;
    @BindView(R.id.flDetailDes)
    FrameLayout mFlDetailDes;
    private String               mPackageName;
    private ItemBean             mItemBean;
    private DetailDownloadHolder mDetailDownloadHolder;

    @Override
    public void init() {
        mPackageName = getIntent().getStringExtra("packageName");
    }

    @Override
    public void initActionBar() {
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setTitle("GooglePlay");

        supportActionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * @return
     * @des 在子线程中正在的加载具体的数据
     */
    @Override
    public LoadingPager.LoadedResultEnum onInitData() {
        DetailProtocol protocol = new DetailProtocol(mPackageName);
        try {
            mItemBean = protocol.loadData(0);
            return checkResData(mItemBean);//success
        } catch (IOException e) {
            e.printStackTrace();
            return LoadingPager.LoadedResultEnum.ERROR;
        }
    }

    /**
     * @return
     * @des 初始化对应的成功视图
     */
    @Override
    public View onInitSuccessView() {
        View rootView = View.inflate(UIUtils.getContext(), R.layout.item_detail, null);
        ButterKnife.bind(this, rootView);


        //view-->返回的都是容器
        //data-->mItemBean
        //data+view-->往5个容器里面加东西

        //应用的 信息 部分
        DetailInfoHolder detailInfoHolder = new DetailInfoHolder();
        mFlDetailInfo.addView(detailInfoHolder.mHolderView);
        detailInfoHolder.setDataAndRefreshHolderView(mItemBean);


        //应用的 安全 部分
        DetailSafeHolder detailSafeHolder = new DetailSafeHolder();
        mFlDetailSafe.addView(detailSafeHolder.mHolderView);
        detailSafeHolder.setDataAndRefreshHolderView(mItemBean);

        //应用的 截图 部分
        DetailPicHolder detailPicHolder = new DetailPicHolder();
        mFlDetailPic.addView(detailPicHolder.mHolderView);
        detailPicHolder.setDataAndRefreshHolderView(mItemBean);

        //应用的 描述 部分
        DetailDesHolder detailDesHolder = new DetailDesHolder();
        mFlDetailDes.addView(detailDesHolder.mHolderView);
        detailDesHolder.setDataAndRefreshHolderView(mItemBean);

        //应用的 下载 部分
        mDetailDownloadHolder = new DetailDownloadHolder();
        mFlDetailDownload.addView(mDetailDownloadHolder.mHolderView);
        mDetailDownloadHolder.setDataAndRefreshHolderView(mItemBean);

        //添加观察者到观察者集合中
        DownLoadManager.getInstance().addObserver(mDetailDownloadHolder);


        return rootView;
    }

    public void initListener() {

    }

    @Override
    protected void onResume() {
        //动态的添加观察者
        if (mDetailDownloadHolder != null) {
            DownLoadManager.getInstance().addObserver(mDetailDownloadHolder);

            //手动发布最新的消息
            DownLoadInfo downLoadInfo = DownLoadManager.getInstance().createDownLoadInfo(mItemBean);
            DownLoadManager.getInstance().notifyObservers(downLoadInfo);
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        //动态的移除观察者
        if (mDetailDownloadHolder != null) {
            DownLoadManager.getInstance().deleteObserver(mDetailDownloadHolder);
        }
        super.onPause();
    }
}
