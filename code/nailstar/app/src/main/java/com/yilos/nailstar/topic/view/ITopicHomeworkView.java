package com.yilos.nailstar.topic.view;

/**
 * Created by yilos on 2015-11-05.
 */
public interface ITopicHomeworkView {
    void initSubmittedHomeworkCount(int count);

    void afterSubmittedHomework(String newCommentId);
}
