package com.yilos.nailstar.player.presenter;

import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.player.entity.TopicCommentInfo;
import com.yilos.nailstar.player.entity.TopicImageTextInfo;
import com.yilos.nailstar.player.entity.TopicInfo;
import com.yilos.nailstar.player.model.ITopicService;
import com.yilos.nailstar.player.model.TopicServiceImpl;
import com.yilos.nailstar.player.view.IVideoPlayerView;
import com.yilos.nailstar.util.TaskManager;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by yilos on 2015-10-22.
 */
public class TopicPresenter {
    private static TopicPresenter topicPresenter = new TopicPresenter();

    private IVideoPlayerView videoPlayerView;
    private ITopicService topicsService = new TopicServiceImpl();

    public static TopicPresenter getInstance(IVideoPlayerView videoPlayerView) {
        topicPresenter.videoPlayerView = videoPlayerView;
        return topicPresenter;
    }

    public void playerVideo(final String topicId) {
        TaskManager.Task loadTopicInfo = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                try {
                    return topicsService.getTopicInfo(topicId);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NetworkDisconnectException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        TaskManager.UITask<TopicInfo> updateUi = new TaskManager.UITask<TopicInfo>() {
            @Override
            public Object doWork(TopicInfo topicInfo) {
                videoPlayerView.playVideo(topicInfo);

                return null;
            }
        };

        new TaskManager()
                .next(loadTopicInfo)
                .next(updateUi)
                .start();
    }

    public void initTopicImageTextInfo(final String topicId) {
        TaskManager.Task loadTopicImageTextInfo = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                try {
                    return topicsService.getTopicImageTextInfo(topicId);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NetworkDisconnectException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        TaskManager.UITask<TopicImageTextInfo> updateUi = new TaskManager.UITask<TopicImageTextInfo>() {
            @Override
            public Object doWork(TopicImageTextInfo videoImageTextInfoEntity) {
                videoPlayerView.initVideoImageTextInfo(videoImageTextInfoEntity);
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
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NetworkDisconnectException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        TaskManager.UITask<ArrayList<TopicCommentInfo>> updateUi = new TaskManager.UITask<ArrayList<TopicCommentInfo>>() {
            @Override
            public Object doWork(ArrayList<TopicCommentInfo> topicComments) {
                videoPlayerView.initTopicCommentsInfo(topicComments);
                return null;
            }
        };

        new TaskManager()
                .next(loadTopicComments)
                .next(updateUi)
                .start();
    }
}
