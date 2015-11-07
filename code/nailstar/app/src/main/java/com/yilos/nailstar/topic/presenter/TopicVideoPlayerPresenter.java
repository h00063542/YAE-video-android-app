package com.yilos.nailstar.topic.presenter;

import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.topic.entity.TopicCommentInfo;
import com.yilos.nailstar.topic.entity.TopicImageTextInfo;
import com.yilos.nailstar.topic.entity.TopicInfo;
import com.yilos.nailstar.topic.entity.TopicRelatedInfo;
import com.yilos.nailstar.topic.model.ITopicService;
import com.yilos.nailstar.topic.model.TopicServiceImpl;
import com.yilos.nailstar.topic.view.ITopicVideoPlayerView;
import com.yilos.nailstar.util.Constants;
import com.yilos.nailstar.util.LoggerFactory;
import com.yilos.nailstar.util.TaskManager;

import org.apache.log4j.Logger;

import java.util.ArrayList;


/**
 * Created by yilos on 2015-10-22.
 */
public class TopicVideoPlayerPresenter {
    private static final Logger LOGGER = LoggerFactory.getLogger(TopicVideoPlayerPresenter.class);

    private static TopicVideoPlayerPresenter topicPresenter = new TopicVideoPlayerPresenter();

    private ITopicVideoPlayerView videoPlayerView;
    private ITopicService topicsService = new TopicServiceImpl();

    public static TopicVideoPlayerPresenter getInstance(ITopicVideoPlayerView videoPlayerView) {
        topicPresenter.videoPlayerView = videoPlayerView;
        return topicPresenter;
    }

    public void initTopicVideo(final String topicId) {
        TaskManager.Task loadTopicInfo = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                try {
                    return topicsService.getTopicInfo(topicId);
                } catch (NetworkDisconnectException e) {
                    e.printStackTrace();
                    LOGGER.error("获取topic信息失败，topicId：" + topicId, e);
                }
                return null;
            }
        };

        TaskManager.UITask<TopicInfo> updateUi = new TaskManager.UITask<TopicInfo>() {
            @Override
            public Object doWork(TopicInfo topicInfo) {
                videoPlayerView.initTopicVideo(topicInfo);

                return null;
            }
        };

        new TaskManager()
                .next(loadTopicInfo)
                .next(updateUi)
                .start();
    }

    public void initTopicRelatedInfo(final String topicId) {
        TaskManager.Task loadTopicRelatedInfo = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                try {
                    return topicsService.getTopicRelatedInfoList(topicId);
                } catch (NetworkDisconnectException e) {
                    e.printStackTrace();
                    LOGGER.error("获取topic图文信息失败，topicId：" + topicId, e);
                }
                return null;
            }
        };

        TaskManager.UITask<ArrayList<TopicRelatedInfo>> updateUi = new TaskManager.UITask<ArrayList<TopicRelatedInfo>>() {
            @Override
            public Object doWork(ArrayList<TopicRelatedInfo> topicRelatedList) {
                videoPlayerView.initTopicRelatedInfo(topicRelatedList);
                return null;
            }
        };

        new TaskManager()
                .next(loadTopicRelatedInfo)
                .next(updateUi)
                .start();

    }

    public void initTopicImageTextInfo(final String topicId) {
        TaskManager.Task loadTopicImageTextInfo = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                try {
                    return topicsService.getTopicImageTextInfo(topicId);
                } catch (NetworkDisconnectException e) {
                    e.printStackTrace();
                    LOGGER.error("获取topic图文信息失败，topicId：" + topicId, e);
                }
                return null;
            }
        };

        TaskManager.UITask<TopicImageTextInfo> updateUi = new TaskManager.UITask<TopicImageTextInfo>() {
            @Override
            public Object doWork(TopicImageTextInfo videoImageTextInfoEntity) {
                videoPlayerView.initTopicImageTextInfo(videoImageTextInfoEntity);
                return null;
            }
        };

        new TaskManager()
                .next(loadTopicImageTextInfo)
                .next(updateUi)
                .start();

    }


    public void initTopicComments(final String topicId, final int page) {
        TaskManager.Task loadTopicComments = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                try {
                    return topicsService.getTopicComments(topicId, page);
                } catch (NetworkDisconnectException e) {
                    e.printStackTrace();
                    LOGGER.error("获取topic评论信息失败，topicId：" + topicId, e);
                }
                return null;
            }
        };

        TaskManager.UITask<ArrayList<TopicCommentInfo>> updateUi = new TaskManager.UITask<ArrayList<TopicCommentInfo>>() {
            @Override
            public Object doWork(ArrayList<TopicCommentInfo> topicComments) {
                videoPlayerView.initTopicCommentsInfo(topicComments, Constants.TOPIC_COMMENTS_INIT_ORDER_BY_ASC);
                return null;
            }
        };

        new TaskManager()
                .next(loadTopicComments)
                .next(updateUi)
                .start();
    }

    public void download() {

    }

    public void shareTopic(String topicId) {

    }

    public void setTopicLikeStatus(final String topicId, final boolean isLike) {
        TaskManager.Task loadTopicComments = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                try {
                    return topicsService.setTopicLikeStatus(topicId, isLike);
                } catch (NetworkDisconnectException e) {
                    e.printStackTrace();
                    LOGGER.error("设置topic喜欢状态失败，topicId：" + topicId + "，isLike：" + isLike, e);
                }
                return null;
            }
        };

        TaskManager.UITask<Boolean> updateUi = new TaskManager.UITask<Boolean>() {
            @Override
            public Object doWork(Boolean isSuccess) {
                videoPlayerView.showTopicLikeStatus(isLike, isSuccess);
                return null;
            }
        };

        new TaskManager()
                .next(loadTopicComments)
                .next(updateUi)
                .start();
    }

    public void setTopicCollectionStatus(final String topicId, final boolean isCollection) {
        TaskManager.Task loadTopicComments = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                try {
                    return topicsService.setTopicCollectionStatus(topicId, isCollection);
                } catch (NetworkDisconnectException e) {
                    e.printStackTrace();
                    LOGGER.error("设置topic收藏状态失败，topicId：" + topicId, e);
                }
                return null;
            }
        };

        TaskManager.UITask<Boolean> updateUi = new TaskManager.UITask<Boolean>() {
            @Override
            public Object doWork(Boolean isSuccess) {
                videoPlayerView.showTopicCollectionStatus(isCollection, isSuccess);
                return null;
            }
        };

        new TaskManager()
                .next(loadTopicComments)
                .next(updateUi)
                .start();
    }
}
