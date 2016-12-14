package com.itheima.googleplay_31.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.itheima.googleplay_31.R;
import com.itheima.googleplay_31.base.BaseHolder;
import com.itheima.googleplay_31.base.MyApplication;
import com.itheima.googleplay_31.bean.DownLoadInfo;
import com.itheima.googleplay_31.bean.ItemBean;
import com.itheima.googleplay_31.conf.Constants;
import com.itheima.googleplay_31.manager.DownLoadManager;
import com.itheima.googleplay_31.utils.CommonUtils;
import com.itheima.googleplay_31.utils.PrintDownLoadInfo;
import com.itheima.googleplay_31.utils.StringUtils;
import com.itheima.googleplay_31.utils.UIUtils;
import com.itheima.progressview.ProgressView;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 类    名:  ItemHolder
 * 创 建 者:  伍碧林
 * 创建时间:  2016/10/15 17:08
 * 描    述： 1.提供视图
 * 描    述： 2.接收数据
 * 描    述： 3.数据和视图的绑定
 */
public class ItemHolder extends BaseHolder<ItemBean> implements DownLoadManager.DownLoadInfoObserver {

    @BindView(R.id.item_appinfo_iv_icon)
    ImageView mItemAppinfoIvIcon;
    @BindView(R.id.item_appinfo_tv_title)
    TextView  mItemAppinfoTvTitle;
    @BindView(R.id.item_appinfo_rb_stars)
    RatingBar mItemAppinfoRbStars;
    @BindView(R.id.item_appinfo_tv_size)
    TextView  mItemAppinfoTvSize;
    @BindView(R.id.item_appinfo_tv_des)
    TextView  mItemAppinfoTvDes;

    @BindView(R.id.item_appinfo_progressview)
    ProgressView mProgressView;
    private ItemBean mItemBean;

    /**
     * @return
     * @des 决定成功视图长什么样子
     */
    @Override
    public View initHolderView() {
        return View.inflate(UIUtils.getContext(), R.layout.item_list, null);
    }

    /**
     * @param itemBean
     * @des 数据和视图的绑定
     */
    @Override
    public void refresHolderView(ItemBean itemBean) {
        //保存数据到成员变量
        mItemBean = itemBean;

        /*--------------- 置空(清除复用对象上面自带的值) begin---------------*/
        mProgressView.setProgress(0);
        /*--------------- 置空  end---------------*/


        mItemAppinfoTvDes.setText(itemBean.des);
        mItemAppinfoTvTitle.setText(itemBean.name);
        mItemAppinfoTvSize.setText(StringUtils.formatFileSize(itemBean.size));
        //ratingbar(展示评分,进行评分)
        mItemAppinfoRbStars.setRating(itemBean.stars);

        //图标的加载
//        mItemAppinfoIvIcon
        Picasso.with(UIUtils.getContext()).load(Constants.URLS.IMGBASEURL + itemBean.iconUrl).into(mItemAppinfoIvIcon);

        /*--------------- 根据不同的状态展示不同的ui ---------------*/
        DownLoadInfo downLoadInfo = DownLoadManager.getInstance().createDownLoadInfo(itemBean);
        refreshProgressViewUI(downLoadInfo);
    }

    @OnClick(R.id.item_appinfo_progressview)
    public void clickProgressView() {
          /*--------------- 根据不同的状态触发不同的操作 ---------------*/
        DownLoadInfo downLoadInfo = DownLoadManager.getInstance().createDownLoadInfo(mItemBean);
        int curState = downLoadInfo.curState;
        /*
        状态(编程记录)  | 用户行为(触发操作)
        ------------------| -----------------
        未下载			    | 去下载
        下载中				| 暂停下载
        暂停下载				| 断点继续下载
        等待下载				| 取消下载
        下载失败 			| 重试下载
        下载完成 			| 安装应用
        已安装 				| 打开应用
         */
        switch (curState) {
            case DownLoadManager.STATE_UNDOWNLOAD://未下载
                doDownLoad(downLoadInfo);
                break;
            case DownLoadManager.STATE_DOWNLOADING://下载中
                doPause(downLoadInfo);
                break;
            case DownLoadManager.STATE_PAUSEDOWNLOAD://暂停下载
                doDownLoad(downLoadInfo);
                break;
            case DownLoadManager.STATE_WAITINGDOWNLOAD://等待下载
                doCancel(downLoadInfo);
                break;
            case DownLoadManager.STATE_DOWNLOADFAILED://下载失败
                doDownLoad(downLoadInfo);
                break;
            case DownLoadManager.STATE_DOWNLOADED://下载完成
                installApk(downLoadInfo);
                break;
            case DownLoadManager.STATE_INSTALLED://已安装
                openApk(downLoadInfo);
                break;

            default:
                break;
        }
    }

    /**
     * 打开apk
     *
     * @param downLoadInfo
     */
    private void openApk(DownLoadInfo downLoadInfo) {
        CommonUtils.openApp(UIUtils.getContext(), downLoadInfo.packageName);
    }

    /**
     * 安装apk
     *
     * @param downLoadInfo
     */
    private void installApk(DownLoadInfo downLoadInfo) {
        CommonUtils.installApp(UIUtils.getContext(), new File(downLoadInfo.savePath));
    }


    /**
     * 去下载,断点继续下载,重试下载
     *
     * @param downLoadInfo
     */
    private void doDownLoad(DownLoadInfo downLoadInfo) {
        DownLoadManager.getInstance().downLoad(downLoadInfo);
    }

    /**
     * 取消下载
     *
     * @param downLoadInfo
     */
    private void doCancel(DownLoadInfo downLoadInfo) {
        DownLoadManager.getInstance().cancel(downLoadInfo);
    }

    /**
     * 暂停下载
     *
     * @param downLoadInfo
     */
    private void doPause(DownLoadInfo downLoadInfo) {
        DownLoadManager.getInstance().pause(downLoadInfo);
    }

    private void refreshProgressViewUI(DownLoadInfo downLoadInfo) {
        int curState = downLoadInfo.curState;
        /*
        状态(编程记录)  	|  给用户的提示(ui展现)
        ----------------|-----------------------
        未下载			|下载
        下载中			|显示进度条
        暂停下载			|继续下载
        等待下载			|等待中...
        下载失败 		|重试
        下载完成 		|安装
        已安装 			|打开
         */
        switch (curState) {
            case DownLoadManager.STATE_UNDOWNLOAD://未下载
                mProgressView.setNote("下载");
                mProgressView.setIcon(R.drawable.ic_download);
                break;
            case DownLoadManager.STATE_DOWNLOADING://下载中
                mProgressView.setIcon(R.drawable.ic_pause);
                mProgressView.setProgressEnable(true);
                int index = (int) (downLoadInfo.progress * 1.0f / downLoadInfo.max * 100 + .5f);
                mProgressView.setNote(index + "%");
                mProgressView.setMax(downLoadInfo.max);
                mProgressView.setProgress(downLoadInfo.progress);
                break;
            case DownLoadManager.STATE_PAUSEDOWNLOAD://暂停下载
                mProgressView.setIcon(R.drawable.ic_resume);
                mProgressView.setNote("继续");
                break;
            case DownLoadManager.STATE_WAITINGDOWNLOAD://等待下载
                mProgressView.setIcon(R.drawable.ic_pause);
                mProgressView.setNote("等待");
                break;
            case DownLoadManager.STATE_DOWNLOADFAILED://下载失败
                mProgressView.setIcon(R.drawable.ic_redownload);
                mProgressView.setNote("重试");
                break;
            case DownLoadManager.STATE_DOWNLOADED://下载完成
                mProgressView.setIcon(R.drawable.ic_install);
                mProgressView.setProgressEnable(false);
                mProgressView.setNote("安装");
                break;
            case DownLoadManager.STATE_INSTALLED://已安装
                mProgressView.setIcon(R.drawable.ic_install);
                mProgressView.setNote("打开");
                break;

            default:
                break;
        }
    }

    /*=============== 接收到被观察者发布的消息 ===============*/
    /*
        被观察者发布消息在什么线程,观察者接收消息的时候就在什么线程
     */
    @Override
    public void onDownLoadInfoChanged(final DownLoadInfo downLoadInfo) {
        //过滤DownLoadInfo
        if (!downLoadInfo.packageName.equals(mItemBean.packageName)) {
            return;
        }

        PrintDownLoadInfo.printDownLoadInfo(downLoadInfo);
//        System.out.println("downLoadInfo.progress:" + downLoadInfo.progress);
        //刷新按钮的ui
        MyApplication.getHandler().post(new Runnable() {
            @Override
            public void run() {
                refreshProgressViewUI(downLoadInfo);
            }
        });
    }
}
