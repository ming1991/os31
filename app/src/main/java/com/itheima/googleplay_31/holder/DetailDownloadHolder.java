package com.itheima.googleplay_31.holder;

import android.view.View;

import com.itheima.googleplay_31.R;
import com.itheima.googleplay_31.base.BaseHolder;
import com.itheima.googleplay_31.base.MyApplication;
import com.itheima.googleplay_31.bean.DownLoadInfo;
import com.itheima.googleplay_31.bean.ItemBean;
import com.itheima.googleplay_31.manager.DownLoadManager;
import com.itheima.googleplay_31.utils.CommonUtils;
import com.itheima.googleplay_31.utils.PrintDownLoadInfo;
import com.itheima.googleplay_31.utils.UIUtils;
import com.itheima.progressbtn.ProgressBtn;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 类    名:  DetailInfoHolder
 * 创 建 者:  伍碧林
 * 创建时间:  2016/10/20 15:06
 * 描    述： 2.根据不同的状态给用户提示
 * 描    述： 3.根据不同的状态触发不同的操作
 * 描    述： 观察者-->收到消息(DownLoadInfo)
 */
public class DetailDownloadHolder extends BaseHolder<ItemBean> implements DownLoadManager.DownLoadInfoObserver {


    @BindView(R.id.app_detail_download_btn_download)
    ProgressBtn mAppDetailProgressBtn;
    private ItemBean mItemBean;

    @Override
    public View initHolderView() {
        return View.inflate(UIUtils.getContext(), R.layout.item_detail_download, null);
    }

    @Override
    public void refresHolderView(ItemBean itemBean) {
        //保存数据到成员变量
        mItemBean = itemBean;
        /*--------------- 2.根据不同的状态给用户提示 ---------------*/
        //curState-->DownLoadInfo-->根据ItemBean得到与之对应的DownLoadInfo
        DownLoadInfo downLoadInfo = DownLoadManager.getInstance().createDownLoadInfo(itemBean);
        refreshProgressBtnUI(downLoadInfo);
    }

    /**
     * 根据downloadInfo修改ProgressBtn的ui
     *
     * @param downLoadInfo
     */
    private void refreshProgressBtnUI(DownLoadInfo downLoadInfo) {
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
        mAppDetailProgressBtn.setBackgroundResource(R.drawable.selector_app_detail_bottom_normal);
        switch (curState) {
            case DownLoadManager.STATE_UNDOWNLOAD://未下载
                mAppDetailProgressBtn.setText("下载");
                break;
            case DownLoadManager.STATE_DOWNLOADING://下载中
                mAppDetailProgressBtn.setProgressEnable(true);
                mAppDetailProgressBtn.setBackgroundResource(R.drawable.selector_app_detail_bottom_downloading);
                int index = (int) (downLoadInfo.progress * 1.0f / downLoadInfo.max * 100 + .5f);
                mAppDetailProgressBtn.setText(index + "%");
                mAppDetailProgressBtn.setMax(downLoadInfo.max);
                mAppDetailProgressBtn.setProgress(downLoadInfo.progress);
                break;
            case DownLoadManager.STATE_PAUSEDOWNLOAD://暂停下载
                mAppDetailProgressBtn.setText("继续下载");
                break;
            case DownLoadManager.STATE_WAITINGDOWNLOAD://等待下载
                mAppDetailProgressBtn.setText("等待中...");
                break;
            case DownLoadManager.STATE_DOWNLOADFAILED://下载失败
                mAppDetailProgressBtn.setText("重试");
                break;
            case DownLoadManager.STATE_DOWNLOADED://下载完成
                mAppDetailProgressBtn.setProgressEnable(false);
                mAppDetailProgressBtn.setText("安装");
                break;
            case DownLoadManager.STATE_INSTALLED://已安装
                mAppDetailProgressBtn.setText("打开");
                break;

            default:
                break;
        }
    }

    @OnClick(R.id.app_detail_download_btn_download)
    public void onClick() {
        /*DownLoadInfo downloadInfo = new DownLoadInfo();
        String dir = FileUtils.getDir("apk");
        String fileName = mItemBean.packageName + ".apk";
        File saveFile = new File(dir, fileName);


        downloadInfo.downloadUrl = mItemBean.downloadUrl;
        downloadInfo.savePath = saveFile.getAbsolutePath();
        downloadInfo.packageName = mItemBean.packageName;

        DownLoadManager.getInstance().downLoad(downloadInfo);*/
        /*---------------  3.根据不同的状态触发不同的操作 ---------------*/
        //curState-->DownLoadInfo-->根据ItemBean得到与之对应的DownLoadInfo
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
                refreshProgressBtnUI(downLoadInfo);
            }
        });
    }
}
