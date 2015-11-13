package com.yilos.nailstar.topic.presenter;

import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.alibaba.sdk.android.oss.model.OSSException;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.topic.entity.AddCommentInfo;
import com.yilos.nailstar.topic.model.ITopicService;
import com.yilos.nailstar.topic.model.TopicServiceImpl;
import com.yilos.nailstar.topic.entity.UpdateReadyInfo;
import com.yilos.nailstar.topic.view.ITopicHomeworkView;
import com.yilos.nailstar.util.Constants;
import com.yilos.nailstar.util.LoggerFactory;
import com.yilos.nailstar.util.TaskManager;

import org.apache.log4j.Logger;

import java.util.ArrayList;


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


    public void submittedHomework(final AddCommentInfo info) {
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

        TaskManager.Task uploadHomeworkPicTask = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                try {
                    topicsService.uploadFile2Oss(info.getPicLocalPath(), info.getPicName(), new SaveCallback() {
                        @Override
                        public void onSuccess(String objectKey) {
                            LOGGER.debug("[onSuccess] - " + objectKey + " upload success!");
                            UpdateReadyInfo updateReadyInfo = new UpdateReadyInfo();
                            updateReadyInfo.setId(info.getTopicId());
                            ArrayList<String> picUrls = new ArrayList<String>();
                            picUrls.add(Constants.YILOS_PIC_URL + objectKey);
                            updateReadyInfo.setPicUrls(picUrls);
                            updateReadyInfo.setTable(Constants.HOMEWORK);
                        }

                        @Override
                        public void onProgress(String objectKey, int byteCount, int totalSize) {
                            LOGGER.debug("[onProgress] - current upload " + objectKey + " bytes: " + byteCount + " in total: " + totalSize);
                        }

                        @Override
                        public void onFailure(String objectKey, OSSException e) {
                            LOGGER.error("上传交作业图片到Oss失败，localPath:" + info.getPicLocalPath()
                                    + "，picName:" + info.getPicName()
                                    + ",objectKey:" + objectKey, e);
                            e.printStackTrace();
                            e.getException().printStackTrace();
                        }
                    });
                } catch (NetworkDisconnectException e) {
                    e.printStackTrace();
                    LOGGER.error("上传交作业图片到oss失败，topicId：" + info.getTopicId(), e);
                }
                return null;
            }
        };


        new TaskManager()
                .next(submittedHomeworkTask)
                .next(updateUi)
                .start();

        // TODO 测试阶段，先不要调用服务端接口
//        new TaskManager()
//                .next(uploadHomeworkPicTask)
//                .start();
    }
}
