package com.yilos.nailstar.player.view;

import com.yilos.nailstar.player.entity.TopicCommentInfo;
import com.yilos.nailstar.player.entity.TopicImageTextInfo;
import com.yilos.nailstar.player.entity.TopicInfo;
import com.yilos.nailstar.player.entity.TopicRelatedInfo;

import java.util.ArrayList;

/**
 * Created by yilos on 2015-10-22.
 */
public interface IVideoPlayerView {

    void playVideo(TopicInfo topicInfo);

    void initTopicImageTextInfo(TopicImageTextInfo topicImageTextInfo);

    void initTopicCommentsInfo(ArrayList<TopicCommentInfo> topicComments);

    void initTopicRelatedInfo(ArrayList<TopicRelatedInfo> topicRelateds);


}
