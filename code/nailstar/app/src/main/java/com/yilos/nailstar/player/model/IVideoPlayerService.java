package com.yilos.nailstar.player.model;

import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.player.entity.VideoImageTextInfoEntity;
import com.yilos.nailstar.player.entity.VideoInfoEntity;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by yilos on 2015-10-24.
 */
public interface IVideoPlayerService {

    // 获取视频信息
    VideoInfoEntity getVideoInfo(String topicsId) throws IOException, JSONException, NetworkDisconnectException;

    // 获取视频图文信息
    VideoImageTextInfoEntity getVideoImageTextInfo(String topicsId) throws NetworkDisconnectException, IOException, JSONException;
}
