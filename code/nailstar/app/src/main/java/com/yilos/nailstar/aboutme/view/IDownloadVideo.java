package com.yilos.nailstar.aboutme.view;

import com.yilos.nailstar.download.DownLoadInfo;

import java.util.List;

/**
 * Created by yilos on 15/11/16.
 */
public interface IDownloadVideo {

    void refreshDownLoadInfoList(List<DownLoadInfo> downLoadInfoList);

    void deleteVideoConfirm(DownLoadInfo downLoadInfo);
}
