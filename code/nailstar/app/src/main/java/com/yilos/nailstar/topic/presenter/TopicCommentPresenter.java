package com.yilos.nailstar.topic.presenter;

import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.topic.entity.AddCommentInfo;
import com.yilos.nailstar.topic.model.ITopicService;
import com.yilos.nailstar.topic.model.TopicServiceImpl;
import com.yilos.nailstar.topic.view.ITopicCommentView;
import com.yilos.nailstar.util.TaskManager;


/**
 * Created by yilos on 2015-10-22.
 */
public class TopicCommentPresenter {
    private ITopicCommentView topicCommentView;
    private ITopicService topicsService;

    public TopicCommentPresenter(ITopicCommentView topicCommentView) {
        this.topicCommentView = topicCommentView;
        this.topicsService = new TopicServiceImpl();
    }


    public void addTopicComment(final AddCommentInfo info) {
        // 调用服务端接口，等待返回成功后，将数据显示在界面上
        TaskManager.Task addTopicCommentTask = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                try {
                    return topicsService.addComment(info);
//                return "001";
                } catch (NetworkDisconnectException e) {
//                    e.printStackTrace();
//                    LOGGER.error("添加评论信息失败，topicId：" + info.getTopicId(), e);
                }
                return null;
            }
        };

        TaskManager.UITask<String> updateUi = new TaskManager.UITask<String>() {
            @Override
            public Object doWork(String commentId) {
                topicCommentView.afterAddTopicComment(commentId);
                return null;
            }
        };

        new TaskManager()
                .next(addTopicCommentTask)
                .next(updateUi)
                .start();

    }

    public void addTopicCommentReply(final AddCommentInfo info) {
        // 调用服务端接口，等待返回成功后，将数据显示在界面上
        TaskManager.Task addTopicCommentReplyTask = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                try {
                    return topicsService.addComment(info);
//                return "002";
                } catch (NetworkDisconnectException e) {
//                    e.printStackTrace();
//                    LOGGER.error("添加评论信息失败，topicId：" + info.getTopicId(), e);
                }
                return null;
            }
        };

        TaskManager.UITask<String> updateUi = new TaskManager.UITask<String>() {
            @Override
            public Object doWork(String commentId) {
                topicCommentView.afterAddTopicCommentReplay(commentId);
                return null;
            }
        };

        new TaskManager()
                .next(addTopicCommentReplyTask)
                .next(updateUi)
                .start();
    }

}
