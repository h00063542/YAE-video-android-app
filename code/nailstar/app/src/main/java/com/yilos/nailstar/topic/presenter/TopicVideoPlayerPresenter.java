package com.yilos.nailstar.topic.presenter;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.topic.entity.TopicCommentInfo;
import com.yilos.nailstar.topic.entity.TopicImageTextInfo;
import com.yilos.nailstar.topic.entity.TopicInfo;
import com.yilos.nailstar.topic.entity.TopicRelatedInfo;
import com.yilos.nailstar.topic.entity.TopicVideoInfo;
import com.yilos.nailstar.topic.model.ITopicService;
import com.yilos.nailstar.topic.model.TopicServiceImpl;
import com.yilos.nailstar.topic.view.ITopicVideoPlayerView;
import com.yilos.nailstar.util.Constants;
import com.yilos.nailstar.util.FileUtils;
import com.yilos.nailstar.util.LoggerFactory;
import com.yilos.nailstar.util.StringUtil;
import com.yilos.nailstar.util.TaskManager;

import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by yilos on 2015-10-22.
 */
public class TopicVideoPlayerPresenter {
    private static final Logger LOGGER = LoggerFactory.getLogger(TopicVideoPlayerPresenter.class);

    private static TopicVideoPlayerPresenter topicPresenter = new TopicVideoPlayerPresenter();

    private ITopicVideoPlayerView videoPlayerView;
    private ITopicService topicsService = new TopicServiceImpl();
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public static TopicVideoPlayerPresenter getInstance(ITopicVideoPlayerView videoPlayerView) {
        topicPresenter.videoPlayerView = videoPlayerView;
        return topicPresenter;
    }

    public void initTopicInfo(final String topicId) {
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
                videoPlayerView.initTopicInfo(topicInfo);

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


    public void downLoadTopicImage(final String topicId, final String url) {
        String filePath = saveBitmap2File(topicId, url);
        videoPlayerView.showDownloadTopicImageStatus(!StringUtil.isEmpty(filePath), filePath);
    }

    public void downloadTopicImageText(final String topicId, final ArrayList<String> urls) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                for (final String url : urls) {
                    String filePath = saveBitmap2File(topicId, url);
                    videoPlayerView.showDownloadTopicImageTextStatus(!StringUtil.isEmpty(filePath), filePath);
                }
            }
        }.start();
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

    public String buildVideoRemoteUrl(final TopicVideoInfo topicVideoInfo) {
        if (null == topicVideoInfo) {
            return Constants.EMPTY_STRING;
        }
        return !StringUtil.isEmpty(topicVideoInfo.getOssUrl()) ? topicVideoInfo.getOssUrl() : topicVideoInfo.getCcUrl();
    }


    public String buildVideoLocalFilePath(final TopicVideoInfo topicVideoInfo) {
        if (null == topicVideoInfo) {
            return Constants.EMPTY_STRING;
        }
        String videoRemoteUrl = buildVideoRemoteUrl(topicVideoInfo);
        if (StringUtil.isEmpty(videoRemoteUrl)) {
            return Constants.EMPTY_STRING;
        }
        String videoSuffix = videoRemoteUrl.substring(videoRemoteUrl.lastIndexOf(Constants.POINT), videoRemoteUrl.length());

        return new StringBuffer().append(Constants.YILOS_NAILSTAR_VIDEOS_PATH).append(topicVideoInfo.getVideoId()).append(videoSuffix).toString();
    }

    public String buildPictureLocalFileName(final String topicId, final String imageSrc) {
        if (StringUtil.isEmpty(topicId) || StringUtil.isEmpty(imageSrc)) {
            return Constants.EMPTY_STRING;
        }
        return new StringBuffer().append(imageSrc.substring(imageSrc.lastIndexOf("/"), imageSrc.length())).append(Constants.PNG_SUFFIX).toString();
    }

    public String getTopicCommentDateStr(long time) {
        Date date = new Date(time);
        Date today = new Date();
        long result = today.getTime() - time;
        if (result / 1000 <= 60) {
            return "刚刚";
        } else if (result / 1000 > 60 && result / 1000 <= 3600) {
            return (int) Math.floor((result / 1000) / 60) + "分钟前";
        } else if (result / 1000 > 3600 && result / 1000 <= 86400) {
            return date.getHours() + "点" + date.getMinutes() + "分";
        } else {
            return (date.getMonth() + 1) + "月" + date.getDate() + "日";
        }

    }

    public boolean checkHasLocalVideo(String filePath) {
        return StringUtil.isEmpty(filePath) ? false : new File(filePath).exists();
    }

    private String saveBitmap2File(String topicId, String url) {
        Bitmap bitmap = imageLoader.loadImageSync(url);
        return FileUtils.saveBitMap(bitmap, Constants.YILOS_NAILSTAR_PICTURE_PATH, buildPictureLocalFileName(topicId, url));
    }
}
