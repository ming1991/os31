package com.itheima.googleplay_31.bean;

import com.itheima.googleplay_31.manager.DownLoadManager;

/**
 * 类    名:  DownLoadInfo
 * 创 建 者:  伍碧林
 * 创建时间:  2016/10/22 09:44
 * 描    述： 封装和相关相关的参数
 */
public class DownLoadInfo {
    public String downloadUrl;
    public String savePath;
    public int curState = DownLoadManager.STATE_UNDOWNLOAD;//默认是未下载
    public String   packageName;
    public long     max;
    public long     progress;
    public Runnable task;
}
