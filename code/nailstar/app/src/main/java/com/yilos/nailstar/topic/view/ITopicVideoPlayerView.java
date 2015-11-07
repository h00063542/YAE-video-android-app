package com.yilos.nailstar.topic.view;

import com.yilos.nailstar.topic.entity.TopicCommentInfo;
import com.yilos.nailstar.topic.entity.TopicImageTextInfo;
import com.yilos.nailstar.topic.entity.TopicInfo;
import com.yilos.nailstar.topic.entity.TopicRelatedInfo;

import java.util.ArrayList;

/**
 * Created by yilos on 2015-10-22.
 */
public interface ITopicVideoPlayerView {

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
     * 主题评论数
     *
     * @param count
     */
    void initTopicCommentCount(int count);

    /**
     * 初始化topic评论信息
     *
     * @param topicComments
     */
    void initTopicCommentsInfo(ArrayList<TopicCommentInfo> topicComments, int orderBy);

    /**
     * 初始化topic关联的topic信息
     *
     * @param topicRelatedList
     */
    void initTopicRelatedInfo(ArrayList<TopicRelatedInfo> topicRelatedList);


}