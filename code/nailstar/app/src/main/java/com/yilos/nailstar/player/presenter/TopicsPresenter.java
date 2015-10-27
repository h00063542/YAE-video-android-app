package com.yilos.nailstar.player.presenter;

import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.player.entity.TopicsCommentInfo;
import com.yilos.nailstar.player.entity.TopicsImageTextInfo;
import com.yilos.nailstar.player.entity.TopicsInfo;
import com.yilos.nailstar.player.model.ITopicsService;
import com.yilos.nailstar.player.model.TopicsServiceImpl;
import com.yilos.nailstar.player.view.IVideoPlayerView;
import com.yilos.nailstar.util.TaskManager;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by yilos on 2015-10-22.
 */
public class TopicsPresenter {
    private static TopicsPresenter topicsPresenter = new TopicsPresenter();

    private IVideoPlayerView videoPlayerView;
    private ITopicsService topicsService = new TopicsServiceImpl();

    public static TopicsPresenter getInstance(IVideoPlayerView videoPlayerView) {
        topicsPresenter.videoPlayerView = videoPlayerView;
        return topicsPresenter;
    }

    public void playerVideo(final String topicsId) {
        TaskManager.Task loadVideoInfo = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                try {
                    return topicsService.getTopicsInfo(topicsId);
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

        TaskManager.UITask<TopicsInfo> updateUi = new TaskManager.UITask<TopicsInfo>() {
            @Override
            public Object doWork(TopicsInfo videoInfoEntity) {
                videoPlayerView.playVideo(videoInfoEntity);

                return null;
            }
        };

        new TaskManager()
                .next(loadVideoInfo)
                .next(updateUi)
                .start();
    }

    public void initTopicsImageTextInfo(final String topicsId) {
        TaskManager.Task loadVideoImageTextInfo = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                try {
                    return topicsService.getTopicsImageTextInfo(topicsId);
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

        TaskManager.UITask<TopicsImageTextInfo> updateUi = new TaskManager.UITask<TopicsImageTextInfo>() {
            @Override
            public Object doWork(TopicsImageTextInfo videoImageTextInfoEntity) {
                videoPlayerView.initVideoImageTextInfo(videoImageTextInfoEntity);
                return null;
            }
        };

        new TaskManager()
                .next(loadVideoImageTextInfo)
                .next(updateUi)
                .start();

    }

    public void initTopicsComments(final String topicsId, final int page) {
        TaskManager.Task loadTopicsComments = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                try {
                    return topicsService.getTopicsComments(topicsId, page);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NetworkDisconnectException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        TaskManager.UITask<ArrayList<TopicsCommentInfo>> updateUi = new TaskManager.UITask<ArrayList<TopicsCommentInfo>>() {
            @Override
            public Object doWork(ArrayList<TopicsCommentInfo> topicsComments) {
                videoPlayerView.initTopicsCommentsInfo(topicsComments);
                return null;
            }
        };

        new TaskManager()
                .next(loadTopicsComments)
                .next(updateUi)
                .start();
    }
}
