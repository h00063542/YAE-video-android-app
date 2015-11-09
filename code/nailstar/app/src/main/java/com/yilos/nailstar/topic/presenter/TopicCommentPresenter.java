package com.yilos.nailstar.topic.presenter;

import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.topic.entity.TopicCommentInfo;
import com.yilos.nailstar.topic.entity.TopicCommentReplyInfo;
import com.yilos.nailstar.topic.entity.TopicImageTextInfo;
import com.yilos.nailstar.topic.entity.TopicInfo;
import com.yilos.nailstar.topic.entity.TopicRelatedInfo;
import com.yilos.nailstar.topic.model.ITopicService;
import com.yilos.nailstar.topic.model.TopicServiceImpl;
import com.yilos.nailstar.topic.view.ITopicCommentView;
import com.yilos.nailstar.topic.view.ITopicVideoPlayerView;
import com.yilos.nailstar.util.LoggerFactory;
import com.yilos.nailstar.util.TaskManager;
import com.yilos.nailstar.util.UserUtil;

import org.apache.log4j.Logger;

import java.util.ArrayList;


/**
 * Created by yilos on 2015-10-22.
 */
public class TopicCommentPresenter {
    private static final Logger LOGGER = LoggerFactory.getLogger(TopicCommentPresenter.class);

    private static TopicCommentPresenter topicCommentPresenter = new TopicCommentPresenter();

    private ITopicCommentView topicCommentView;
    private ITopicService topicsService = new TopicServiceImpl();

    public static TopicCommentPresenter getInstance(ITopicCommentView topicCommentView) {
        topicCommentPresenter.topicCommentView = topicCommentView;
        return topicCommentPresenter;
    }


    public void addTopicComment(TopicCommentInfo topicCommentInfo) {
        topicCommentView.afterAddTopicComment("001");
    }

    public void addTopicCommentReply(String topicCommentId, TopicCommentReplyInfo topicCommentReplyInfo) {
        topicCommentView.afterAddTopicCommentReplay("002");
    }
}
