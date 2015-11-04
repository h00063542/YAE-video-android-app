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

    /**
     * 初始化topic视频
     *
     * @param topicInfo
     */
    void initTopicVideo(TopicInfo topicInfo);

    /**
     * 初始化topic图文分解信息
     *
     * @param topicImageTextInfo
     */
    void initTopicImageTextInfo(TopicImageTextInfo topicImageTextInfo);

    /**
     * 初始化topic评论信息
     *
     * @param topicComments
     */
    void initTopicCommentsInfo(ArrayList<TopicCommentInfo> topicComments);

    /**
     * 初始化topic关联的topic信息
     *
     * @param topicRelatedList
     */
    void initTopicRelatedInfo(ArrayList<TopicRelatedInfo> topicRelatedList);


}
