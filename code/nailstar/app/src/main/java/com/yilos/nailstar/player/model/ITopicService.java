package com.yilos.nailstar.player.model;

import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.player.entity.TopicCommentInfo;
import com.yilos.nailstar.player.entity.TopicImageTextInfo;
import com.yilos.nailstar.player.entity.TopicInfo;
import com.yilos.nailstar.player.entity.TopicRelatedInfo;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by yilos on 2015-10-24.
 */
public interface ITopicService {

    // 获取主题信息
    TopicInfo getTopicInfo(String topicId) throws NetworkDisconnectException;

    // 获取主题图文信息
    TopicImageTextInfo getTopicImageTextInfo(String topicId) throws NetworkDisconnectException;

    // 获取关联的主题信息
    ArrayList<TopicRelatedInfo> getTopicRelatedInfoList(String topicId) throws NetworkDisconnectException;

    // 获取主题评论
    ArrayList<TopicCommentInfo> getTopicComments(String topicId, int page) throws NetworkDisconnectException;

}
