package com.yilos.nailstar.player.presenter;

import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.player.entity.VideoImageTextInfoEntity;
import com.yilos.nailstar.player.entity.VideoInfoEntity;
import com.yilos.nailstar.player.model.IVideoPlayerService;
import com.yilos.nailstar.player.model.VideoPlayerServiceImpl;
import com.yilos.nailstar.player.view.IVideoPlayerView;
import com.yilos.nailstar.util.TaskManager;

import org.json.JSONException;

import java.io.IOException;


/**
 * Created by yilos on 2015-10-22.
 */
public class VideoPlayerPresenter {
    private static VideoPlayerPresenter videoPlayerPresenter = new VideoPlayerPresenter();

    private IVideoPlayerView videoPlayerView;
    private IVideoPlayerService videoPlayerService = new VideoPlayerServiceImpl();

    public static VideoPlayerPresenter getInstance(IVideoPlayerView videoPlayerView) {
        videoPlayerPresenter.videoPlayerView = videoPlayerView;
        return videoPlayerPresenter;
    }

    public void playerVideo(final String topicsId) {
        TaskManager.Task loadVideoInfo = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                try {
                    return videoPlayerService.getVideoInfo(topicsId);
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

        TaskManager.UITask<VideoInfoEntity> updateUi = new TaskManager.UITask<VideoInfoEntity>() {
            @Override
            public Object doWork(VideoInfoEntity videoInfoEntity) {
                videoPlayerView.playVideo(videoInfoEntity);

                return null;
            }
        };

        new TaskManager()
                .next(loadVideoInfo)
                .next(updateUi)
                .start();
    }

    public void initVideoImageTextInfo(final String topicsId) {
        TaskManager.Task loadVideoImageTextInfo = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                try {
                    return videoPlayerService.getVideoImageTextInfo(topicsId);
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

        TaskManager.UITask<VideoImageTextInfoEntity> updateUi = new TaskManager.UITask<VideoImageTextInfoEntity>() {
            @Override
            public Object doWork(VideoImageTextInfoEntity videoImageTextInfoEntity) {
                videoPlayerView.initVideoImageTextInfo(videoImageTextInfoEntity);
                return null;
            }
        };

        new TaskManager()
                .next(loadVideoImageTextInfo)
                .next(updateUi)
                .start();

    }
}
