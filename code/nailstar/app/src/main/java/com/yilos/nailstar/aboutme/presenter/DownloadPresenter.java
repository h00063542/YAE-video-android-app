package com.yilos.nailstar.aboutme.presenter;

import android.os.Handler;

import com.yilos.nailstar.aboutme.view.IDownloadVideo;
import com.yilos.nailstar.download.DownLoadInfo;
import com.yilos.nailstar.download.DownLoadTaskManager;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by yilos on 15/11/16.
 */
public class DownloadPresenter {

    private Handler mHandler = new Handler();

    private IDownloadVideo downloadVideo;
    private DownLoadTaskManager downLoadTaskManager = DownLoadTaskManager.getInstance();

    private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    List<DownLoadInfo> downLoadInfoList;

    public DownloadPresenter(IDownloadVideo downloadVideo) {
        this.downloadVideo = downloadVideo;
        queryDownLoadInfoList();
        initRefreshTask();
    }

    private void initRefreshTask() {

        Runnable refreshProgress = new Runnable() {
            @Override
            public void run() {
                queryDownLoadInfoList();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        downloadVideo.refreshDownLoadInfoList(downLoadInfoList);
                    }
                });
            }
        };

        scheduledExecutorService.scheduleAtFixedRate(refreshProgress, 0, 1000, TimeUnit.MILLISECONDS);

    }

    private void queryDownLoadInfoList() {
        downLoadInfoList = downLoadTaskManager.getDownLoadInfoList();
    }

    public void pauseDownLoadTask(DownLoadInfo downLoadInfo) {
        downLoadTaskManager.pauseDownLoadTask(downLoadInfo);
    }

    public void resumeDownLoadTask(DownLoadInfo downLoadInfo) {
        downLoadTaskManager.resumeDownLoadTask(downLoadInfo);
    }

    public List<DownLoadInfo> getDownLoadInfoList() {
        return downLoadInfoList;
    }

    public void deleteVideoConfirm(DownLoadInfo downLoadInfo) {
        downloadVideo.deleteVideoConfirm(downLoadInfo);
    }

    public void deleteVideo(DownLoadInfo downLoadInfo) {
        downLoadTaskManager.deleteDownLoadVideo(downLoadInfo);
    }
}
