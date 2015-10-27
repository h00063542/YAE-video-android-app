package com.yilos.nailstar.player.view;

import com.yilos.nailstar.player.entity.TopicsCommentInfo;
import com.yilos.nailstar.player.entity.TopicsImageTextInfo;
import com.yilos.nailstar.player.entity.TopicsInfo;

import java.util.ArrayList;

/**
 * Created by yilos on 2015-10-22.
 */
public interface IVideoPlayerView {

    void playVideo(TopicsInfo videoInfoEntity);

    void initVideoImageTextInfo(TopicsImageTextInfo videoImageTextInfoEntity);

    void initTopicsCommentsInfo(ArrayList<TopicsCommentInfo> topicsComments);

}
