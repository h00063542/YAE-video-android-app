package com.yilos.nailstar.topic.presenter;

import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.alibaba.sdk.android.oss.model.OSSException;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.topic.entity.AddCommentInfo;
import com.yilos.nailstar.topic.entity.UpdateReadyInfo;
import com.yilos.nailstar.topic.model.ITopicService;
import com.yilos.nailstar.topic.model.TopicServiceImpl;
import com.yilos.nailstar.topic.view.ITopicHomeworkView;
import com.yilos.nailstar.util.Constants;
import com.yilos.nailstar.util.LoggerFactory;
import com.yilos.nailstar.util.TaskManager;

import org.apache.log4j.Logger;

import java.text.MessageFormat;
import java.util.ArrayList;


/**
 * Created by yilos on 2015-10-22.
 */
public class TopicHomeworkPresenter {
    private final Logger LOGGER = LoggerFactory.getLogger(TopicHomeworkPresenter.class);

    private ITopicHomeworkView topicHomeworkView;
    private ITopicService topicsService;

    public TopicHomeworkPresenter(ITopicHomeworkView topicHomeworkView) {
        this.topicHomeworkView = topicHomeworkView;
        this.topicsService = new TopicServiceImpl();
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
//                    return topicsService.addComment(info);
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
                            LOGGER.debug(MessageFormat.format("正在上传交作业图片到Oss成功，topicId:{0}，localPath:{1}，picName:{2}，objectKey:{3}"
                                    , info.getTopicId(), info.getPicLocalPath(), info.getPicName(), objectKey));
                            UpdateReadyInfo updateReadyInfo = new UpdateReadyInfo();
                            updateReadyInfo.setId(info.getTopicId());
                            ArrayList<String> picUrls = new ArrayList<String>();
                            picUrls.add(Constants.YILOS_PIC_URL + objectKey);
                            updateReadyInfo.setPicUrls(picUrls);
                            updateReadyInfo.setTable(Constants.HOMEWORK);
//                            try {
                            // 1、更新homework状态
                            // TODO 测试阶段，先不要调用服务端接口
//                                topicsService.updateReady(updateReadyInfo);
                            //2、删除本地文件
//                            if (!StringUtil.isEmpty(info.getPicLocalPath())) {
//                                File file = new File(info.getPicLocalPath());
//                                if (file.exists()) {
//                                    file.delete();
//                                }
//                            }
//                            } catch (NetworkDisconnectException e) {
//                                LOGGER.error(MessageFormat.format("修改ready状态失败，id:{0}", info.getTopicId()), e);
//                                e.printStackTrace();
//                            }
                        }

                        @Override
                        public void onProgress(String objectKey, int byteCount, int totalSize) {
                            LOGGER.debug(MessageFormat.format("正在上传交作业图片到Oss，topicId:{0}，objectKey:{1}，byteCount:{2}，totalSize:{3}"
                                    , info.getTopicId(), objectKey, byteCount, totalSize));
                        }

                        @Override
                        public void onFailure(String objectKey, OSSException e) {
                            LOGGER.error(MessageFormat.format("上传交作业图片到Oss失败，topicId:{0}，localPath:{1}，picName:{2}，objectKey:{3}"
                                    , info.getTopicId(), info.getPicLocalPath(), info.getPicName(), objectKey), e);
                            e.printStackTrace();
                            e.getException().printStackTrace();
                        }
                    });
                } catch (NetworkDisconnectException e) {
                    e.printStackTrace();
                    LOGGER.error(
                            MessageFormat.format("上传交作业图片到Oss失败，topicId:{0}，localPath:{1}，picName:{2}，objectKey:{3}"
                                    , info.getTopicId(), info.getPicLocalPath(), info.getPicName()), e);
                }
                return null;
            }
        };


        new TaskManager()
                .next(submittedHomeworkTask)
                .next(updateUi)
                .start();

        // TODO 测试阶段，先不要调用服务端接口
        new TaskManager()
                .next(uploadHomeworkPicTask)
                .start();
    }
}
