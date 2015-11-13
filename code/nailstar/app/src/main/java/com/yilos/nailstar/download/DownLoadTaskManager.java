package com.yilos.nailstar.download;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yilos.nailstar.topic.entity.TopicInfo;

import java.util.List;

/**
 * Created by yilos on 15/11/12.
 */
public class DownLoadTaskManager {

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private List<DownLoadInfo> downloadInfo;

    private static DownLoadTaskManager instance = new DownLoadTaskManager();

    private DownLoadTaskManager() {

    }

    public static DownLoadTaskManager getInstance() {
        return instance;
    }

    public void addDownLoadTask(TopicInfo topicInfo) {

    }

    public void cancelDownLoadTask(TopicInfo topicInfo) {

    }

    public void resumeDownLoadTask(TopicInfo topicInfo) {

    }

    public void removeDownLoadTask(TopicInfo topicInfo) {

    }


}
