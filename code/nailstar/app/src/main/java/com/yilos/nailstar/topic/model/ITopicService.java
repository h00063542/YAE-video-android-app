package com.yilos.nailstar.topic.model;

import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.topic.entity.TopicCommentInfo;
import com.yilos.nailstar.topic.entity.TopicImageTextInfo;
import com.yilos.nailstar.topic.entity.TopicInfo;
import com.yilos.nailstar.topic.entity.TopicRelatedInfo;

import java.util.ArrayList;

/**
 * Created by yilos on 2015-10-24.
 */
public interface ITopicService {

    /**
     * 获取主题信息
     *
     * @param topicId
     * @return
     * @throws NetworkDisconnectException
     */
    TopicInfo getTopicInfo(String topicId) throws NetworkDisconnectException;

    /**
     * 获取主题图文信息
     *
     * @param topicId
     * @return
     * @throws NetworkDisconnectException
     */
    TopicImageTextInfo getTopicImageTextInfo(String topicId) throws NetworkDisconnectException;

    /**
     * 获取关联的主题信息
     *
     * @param topicId
     * @return
     * @throws NetworkDisconnectException
     */
    ArrayList<TopicRelatedInfo> getTopicRelatedInfoList(String topicId) throws NetworkDisconnectException;

    /**
     * 获取主题的评论数量
     *
     * @param topicId
     * @return
     * @throws NetworkDisconnectException
     */
    int getTopicCommentCount(String topicId) throws NetworkDisconnectException;

    /**
     * 获取主题评论
     *
     * @param topicId
     * @param page
     * @return
     * @throws NetworkDisconnectException
     */
    ArrayList<TopicCommentInfo> getTopicComments(String topicId, int page) throws NetworkDisconnectException;

    /**
     * 下载主题视频
     *
     * @param topicId
     * @throws NetworkDisconnectException
     */
    boolean downloadVideo(String topicId) throws NetworkDisconnectException;

    /**
     * 收藏
     *
     * @param topicId
     * @throws NetworkDisconnectException
     */
    boolean collection(String topicId) throws NetworkDisconnectException;

    /**
     * 取消收藏
     *
     * @param topicId
     * @throws NetworkDisconnectException
     */
    boolean cancelCollection(String topicId) throws NetworkDisconnectException;

    /**
     * 评论
     *
     * @param topicId
     * @throws NetworkDisconnectException
     */
    boolean addComment(String topicId) throws NetworkDisconnectException;

    /**
     * 交作业
     *
     * @param topicId
     * @throws NetworkDisconnectException
     */
    boolean submittedHomework(String topicId) throws NetworkDisconnectException;


    /**
     * 视频播放次数+1
     *
     * @param topicId
     * @throws NetworkDisconnectException
     */
    boolean addVideoPlayCount(String topicId) throws NetworkDisconnectException;
}