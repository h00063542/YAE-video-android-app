package com.yilos.nailstar.topic.presenter;

import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.topic.entity.SubmittedHomeworkInfo;
import com.yilos.nailstar.topic.model.ITopicService;
import com.yilos.nailstar.topic.model.TopicServiceImpl;
import com.yilos.nailstar.topic.view.ITopicHomeworkView;
import com.yilos.nailstar.util.LoggerFactory;
import com.yilos.nailstar.util.TaskManager;

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

    public void initSubmittedHomeworkCount(final String topicId) {
        TaskManager.Task loadSubmittedHomeworkCount = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                try {
                    return topicsService.getSubmittedHomeworkCount(topicId);
                } catch (NetworkDisconnectException e) {
                    e.printStackTrace();
                    LOGGER.error("获取交作业人数失败，topicId：" + topicId, e);
                }
                return null;
            }
        };

        TaskManager.UITask<Integer> updateUi = new TaskManager.UITask<Integer>() {
            @Override
            public Object doWork(Integer count) {
                topicHomeworkView.initSubmittedHomeworkCount(count);
                return null;
            }
        };

        new TaskManager()
                .next(loadSubmittedHomeworkCount)
                .next(updateUi)
                .start();
    }


    public void submittedHomework(final SubmittedHomeworkInfo info) {
        TaskManager.Task submittedHomeworkTask = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
//                try {
                // TODO 测试阶段，先不要调用服务端接口
                return "003";
//                    return topicsService.submittedHomework(info);
//                } catch (NetworkDisconnectException e) {
//                    e.printStackTrace();
//                    LOGGER.error("添加评论信息失败，topicId：" + info.getTopicId(), e);
//                }
//                return null;
            }
        };

        TaskManager.UITask<String> updateUi = new TaskManager.UITask<String>() {
            @Override
            public Object doWork(String commentId) {
                topicHomeworkView.afterSubmittedHomework(commentId);
                return null;
            }
        };

        new TaskManager()
                .next(submittedHomeworkTask)
                .next(updateUi)
                .start();
    }
}
