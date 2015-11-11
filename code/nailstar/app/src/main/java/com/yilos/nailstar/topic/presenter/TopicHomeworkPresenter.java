package com.yilos.nailstar.topic.presenter;

import com.yilos.nailstar.topic.entity.TopicCommentInfo;
import com.yilos.nailstar.topic.entity.TopicCommentReplyInfo;
import com.yilos.nailstar.topic.model.ITopicService;
import com.yilos.nailstar.topic.model.TopicServiceImpl;
import com.yilos.nailstar.topic.view.ITopicCommentView;
import com.yilos.nailstar.topic.view.ITopicHomeworkView;
import com.yilos.nailstar.util.LoggerFactory;

import org.apache.log4j.Logger;


/**
 * Created by yilos on 2015-10-22.
 */
public class TopicHomeworkPresenter {
    private static final Logger LOGGER = LoggerFactory.getLogger(TopicHomeworkPresenter.class);

    private static TopicHomeworkPresenter topicHomeworkPresenter = new TopicHomeworkPresenter();

    private ITopicHomeworkView topicHomeworkView;
    private ITopicService topicsService = new TopicServiceImpl();

    public static TopicHomeworkPresenter getInstance(ITopicHomeworkView topicHomeworkView) {
        topicHomeworkPresenter.topicHomeworkView = topicHomeworkView;
        return topicHomeworkPresenter;
    }


    public void submittedHomework(TopicCommentInfo topicCommentInfo) {
        topicHomeworkView.afterSubmittedHomework("001");
    }
}
