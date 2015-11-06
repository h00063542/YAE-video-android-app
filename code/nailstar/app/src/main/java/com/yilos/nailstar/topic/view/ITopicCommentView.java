package com.yilos.nailstar.topic.view;

/**
 * Created by yilos on 2015-11-05.
 */
public interface ITopicCommentView {
    void afterAddTopicComment(String newCommentId);

    void afterAddTopicCommentReplay(String newCommentId);
}
