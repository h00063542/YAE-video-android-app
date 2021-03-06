package com.yilos.nailstar.topic.presenter;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.provider.MediaStore;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yilos.nailstar.download.DownLoadTaskManager;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.topic.entity.TopicCommentInfo;
import com.yilos.nailstar.topic.entity.TopicImageTextInfo;
import com.yilos.nailstar.topic.entity.TopicInfo;
import com.yilos.nailstar.topic.entity.TopicRelatedInfo;
import com.yilos.nailstar.topic.entity.TopicRelatedProduct;
import com.yilos.nailstar.topic.entity.TopicStatusInfo;
import com.yilos.nailstar.topic.entity.TopicVideoInfo;
import com.yilos.nailstar.topic.model.ITopicService;
import com.yilos.nailstar.topic.model.TopicServiceImpl;
import com.yilos.nailstar.topic.view.ITopicVideoPlayerView;
import com.yilos.nailstar.util.Constants;
import com.yilos.nailstar.util.FileUtils;
import com.yilos.nailstar.util.StringUtil;
import com.yilos.nailstar.util.TaskManager;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by yilos on 2015-10-22.
 */
public class TopicVideoPlayerPresenter {
    private ITopicVideoPlayerView topicVideoPlayerView;
    private ITopicService topicsService;
    private ImageLoader imageLoader;


    public TopicVideoPlayerPresenter(ITopicVideoPlayerView topicVideoPlayerView) {
        this.topicVideoPlayerView = topicVideoPlayerView;
        this.topicsService = new TopicServiceImpl();
        this.imageLoader = ImageLoader.getInstance();
    }


    public void initTopicInfo(final String topicId) {
        TaskManager.Task loadTopicInfo = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                try {
                    return topicsService.getTopicInfo(topicId);
                } catch (NetworkDisconnectException e) {
//                    e.printStackTrace();
//                    LOGGER.error(MessageFormat.format("获取topic信息失败，topicId:{0}", topicId), e);
                }
                return null;
            }
        };

        TaskManager.UITask<TopicInfo> updateUi = new TaskManager.UITask<TopicInfo>() {
            @Override
            public Object doWork(TopicInfo topicInfo) {
                topicVideoPlayerView.initTopicInfo(topicInfo);

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
//                    e.printStackTrace();
//                    LOGGER.error(MessageFormat.format("获取topic图文信息失败，topicId:{0}", topicId), e);
                }
                return null;
            }
        };

        TaskManager.UITask<ArrayList<TopicRelatedInfo>> updateUi = new TaskManager.UITask<ArrayList<TopicRelatedInfo>>() {
            @Override
            public Object doWork(ArrayList<TopicRelatedInfo> topicRelatedList) {
                topicVideoPlayerView.initTopicRelatedInfo(topicRelatedList);
                return null;
            }
        };

        new TaskManager()
                .next(loadTopicRelatedInfo)
                .next(updateUi)
                .start();

    }

    public void initTopicRelatedUsedProductList(final String topicId) {
        TaskManager.Task loadTopicRelatedInfo = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                try {
                    return topicsService.getTopicRelatedUsedProductList(topicId);
                } catch (NetworkDisconnectException e) {
//                    e.printStackTrace();
//                    LOGGER.error(MessageFormat.format("获取topic图文信息失败，topicId:{0}", topicId), e);
                }
                return null;
            }
        };

        TaskManager.UITask<ArrayList<TopicRelatedProduct>> updateUi = new TaskManager.UITask<ArrayList<TopicRelatedProduct>>() {
            @Override
            public Object doWork(ArrayList<TopicRelatedProduct> topicRelatedUsedProductList) {
                topicVideoPlayerView.initTopicRelatedUsedProductList(topicRelatedUsedProductList);
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
//                    e.printStackTrace();
//                    LOGGER.error(MessageFormat.format("获取topic图文信息失败，topicId:{0}", topicId), e);
                }
                return null;
            }
        };

        TaskManager.UITask<TopicImageTextInfo> updateUi = new TaskManager.UITask<TopicImageTextInfo>() {
            @Override
            public Object doWork(TopicImageTextInfo videoImageTextInfoEntity) {
                topicVideoPlayerView.initTopicImageTextInfo(videoImageTextInfoEntity);
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
//                    e.printStackTrace();
//                    LOGGER.error(MessageFormat.format("获取topic评论信息失败，topicId:{0}", topicId), e);
                }
                return null;
            }
        };

        TaskManager.UITask<ArrayList<TopicCommentInfo>> updateUi = new TaskManager.UITask<ArrayList<TopicCommentInfo>>() {
            @Override
            public Object doWork(ArrayList<TopicCommentInfo> topicComments) {
                topicVideoPlayerView.initTopicCommentInfo(topicComments, Constants.TOPIC_COMMENTS_INIT_ORDER_BY_ASC);
                return null;
            }
        };

        new TaskManager()
                .next(loadTopicComments)
                .next(updateUi)
                .start();
    }

    public void addVideoPlayCount(final String topicId) {
        TaskManager.Task loadTopicComments = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                try {
                    return topicsService.addVideoPlayCount(topicId);
                } catch (NetworkDisconnectException e) {
//                    e.printStackTrace();
//                    LOGGER.error(MessageFormat.format("视频播放次数+1失败，topicId:{0}", topicId), e);
                }
                return null;
            }
        };

        new TaskManager()
                .next(loadTopicComments)
                .start();
    }

    public boolean isDownloadVideo(TopicInfo topicInfo) {
        return DownLoadTaskManager.getInstance().isDownLoad(topicInfo);
    }

    public void downloadVideo(TopicInfo topicInfo) {
        DownLoadTaskManager.getInstance().addDownLoadTask(topicInfo);
    }


    public void downLoadTopicImage(final String topicId, final String topicName, int index, final String url) {
        String filePath = saveBitmap2File(topicId, topicName, index, url);
        topicVideoPlayerView.setDownloadTopicImageStatus(!StringUtil.isEmpty(filePath), filePath);
    }

    public void downloadTopicImageText(final String topicId, final String topicName, final ArrayList<String> urls) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                int index = 1;
                for (final String url : urls) {
                    String filePath = saveBitmap2File(topicId, topicName, index++, url);
                    topicVideoPlayerView.setDownloadTopicImageTextStatus(!StringUtil.isEmpty(filePath), filePath);
                }
            }
        }.start();
    }

    public void initUserTopicStatus(final String topicId) {
        TaskManager.Task loadTopicStatusInfo = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                try {
                    return topicsService.initUserTopicStatus(topicId);
                } catch (NetworkDisconnectException e) {
//                    e.printStackTrace();
//                    LOGGER.error(MessageFormat.format("获取用户topic状态失败，topicId:{0}", topicId), e);
                }
                return null;
            }
        };

        TaskManager.UITask<TopicStatusInfo> updateUi = new TaskManager.UITask<TopicStatusInfo>() {
            @Override
            public Object doWork(TopicStatusInfo topicStatusInfo) {
                topicVideoPlayerView.initUserTopicStatus(topicStatusInfo);
                return null;
            }
        };

        new TaskManager()
                .next(loadTopicStatusInfo)
                .next(updateUi)
                .start();
    }

    public void setTopicLikeStatus(final String topicId, final boolean isLike) {
        TaskManager.Task loadTopicComments = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                try {
                    return topicsService.setTopicLikeStatus(topicId, isLike);
                } catch (NetworkDisconnectException e) {
//                    e.printStackTrace();
//                    LOGGER.error(MessageFormat.format("设置topic喜欢状态失败，topicId:{0}，isLike:{1}", topicId, isLike), e);
                }
                return null;
            }
        };

        TaskManager.UITask<Boolean> updateUi = new TaskManager.UITask<Boolean>() {
            @Override
            public Object doWork(Boolean isSuccess) {
                topicVideoPlayerView.setTopicLikeStatus(isLike, isSuccess);
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
//                    e.printStackTrace();
//                    LOGGER.error(MessageFormat.format("设置topic收藏状态失败，topicId:{0}，isCollection:{1}", topicId, isCollection), e);
                }
                return null;
            }
        };

        TaskManager.UITask<Boolean> updateUi = new TaskManager.UITask<Boolean>() {
            @Override
            public Object doWork(Boolean isSuccess) {
                topicVideoPlayerView.setTopicCollectionStatus(isCollection, isSuccess);
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

    private String saveBitmap2File(String topicId, final String topicName, int index, String url) {
        Bitmap bitmap = imageLoader.loadImageSync(url);
        return FileUtils.saveBitMap(bitmap, Constants.YILOS_NAILSTAR_PICTURE_PATH, new StringBuffer().append(topicName).append(Constants.UNDERLINE).append(index).append(Constants.PNG_SUFFIX).toString());
    }

    public void createVideoThumbnail(final String url, final int width, final int height) {
        TaskManager.Task loadVideoThumbnail = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                Bitmap bitmap = null;
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                int kind = MediaStore.Video.Thumbnails.MINI_KIND;
                try {
                    if (Build.VERSION.SDK_INT >= 14) {
                        retriever.setDataSource(url, new HashMap<String, String>());
                    } else {
                        retriever.setDataSource(url);
                    }
                    bitmap = retriever.getFrameAtTime();
                } catch (IllegalArgumentException ex) {
                    ex.printStackTrace();
                } catch (RuntimeException ex) {
                    ex.printStackTrace();
                } finally {
                    try {
                        retriever.release();
                    } catch (RuntimeException ex) {
                        ex.printStackTrace();
                    }
                }
                if (kind == MediaStore.Images.Thumbnails.MICRO_KIND && bitmap != null) {
                    bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                            ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
                }
                return bitmap;
            }
        };

        TaskManager.UITask<Bitmap> updateUi = new TaskManager.UITask<Bitmap>() {
            @Override
            public Object doWork(Bitmap bitmap) {
                topicVideoPlayerView.setVideoThumbnail(bitmap);
                return null;
            }
        };

        new TaskManager()
                .next(loadVideoThumbnail)
                .next(updateUi)
                .start();
    }
}
