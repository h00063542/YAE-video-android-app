package com.yilos.nailstar.topic.model;

import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.topic.entity.AddCommentInfo;
import com.yilos.nailstar.topic.entity.TopicCommentInfo;
import com.yilos.nailstar.topic.entity.TopicImageTextInfo;
import com.yilos.nailstar.topic.entity.TopicInfo;
import com.yilos.nailstar.topic.entity.TopicRelatedInfo;
import com.yilos.nailstar.topic.entity.UpdateReadyInfo;

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
     * 获取主题评论
     *
     * @param topicId
     * @param page
     * @return
     * @throws NetworkDisconnectException
     */
    ArrayList<TopicCommentInfo> getTopicComments(String topicId, int page) throws NetworkDisconnectException;


    /**
     * 新增评论
     *
     * @param info
     * @return
     * @throws NetworkDisconnectException
     */
    String addComment(AddCommentInfo info) throws NetworkDisconnectException;

    /**
     * 下载
     *
     * @param url
     * @param filePath
     * @throws NetworkDisconnectException
     */
    boolean download(String url, String filePath) throws NetworkDisconnectException;

    /**
     * 设置topic喜欢状态
     *
     * @param topicId
     * @throws NetworkDisconnectException
     */
    boolean setTopicLikeStatus(String topicId, boolean isLike) throws NetworkDisconnectException;

    /**
     * 设置topic收藏状态
     *
     * @param topicId
     * @throws NetworkDisconnectException
     */
    boolean setTopicCollectionStatus(String topicId, boolean isCollection) throws NetworkDisconnectException;

    /**
     * 视频播放次数+1
     *
     * @param topicId
     * @throws NetworkDisconnectException
     */
    boolean addVideoPlayCount(String topicId) throws NetworkDisconnectException;


    /**
     * 获取交作业人数
     *
     * @param topicId
     * @return
     * @throws NetworkDisconnectException
     */
    int getSubmittedHomeworkCount(String topicId) throws NetworkDisconnectException;

    /**
     * 上传文件到oss
     *
     * @param localFilePath
     * @param ossFileName
     * @param callback
     * @return
     * @throws NetworkDisconnectException
     */
    void uploadFile2Oss(String localFilePath, String ossFileName, SaveCallback callback) throws NetworkDisconnectException;

    /**
     * 更新ready状态
     *
     * @param info
     * @return
     * @throws NetworkDisconnectException
     */
    boolean updateReady(UpdateReadyInfo info) throws NetworkDisconnectException;
}