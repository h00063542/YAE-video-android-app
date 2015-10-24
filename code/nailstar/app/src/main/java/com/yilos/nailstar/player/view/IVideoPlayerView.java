package com.yilos.nailstar.player.view;

import com.yilos.nailstar.player.entity.VideoImageTextInfoEntity;
import com.yilos.nailstar.player.entity.VideoInfoEntity;

/**
 * Created by yilos on 2015-10-22.
 */
public interface IVideoPlayerView {

    void playVideo(VideoInfoEntity videoInfoEntity);

    void initVideoImageTextInfo(VideoImageTextInfoEntity videoImageTextInfoEntity);

}
