package com.yilos.nailstar.download;

import android.graphics.Bitmap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.okhttp.OkHttpClient;
import com.yilos.nailstar.topic.entity.TopicInfo;
import com.yilos.nailstar.util.Constants;
import com.yilos.nailstar.util.FileUtils;
import com.yilos.nailstar.util.HttpClient;
import com.yilos.nailstar.util.LoggerFactory;

import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by yilos on 15/11/12.
 */
public class DownLoadTaskManager {

    private static Logger logger = LoggerFactory.getLogger(DownLoadTaskManager.class);

    private static final String DOWNLOAD_INFO_FILE = "download_info";

    private String path = Constants.YILOS_NAILSTAR_VIDEOS_PATH;

    private ObjectMapper objectMapper = new ObjectMapper();

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private OkHttpClient client = HttpClient.getOkHttpClient();

    private List<DownLoadInfo> downLoadInfoList = Collections.synchronizedList(new ArrayList<DownLoadInfo>());

    private List<DownLoadTask> downLoadTaskList = Collections.synchronizedList(new ArrayList<DownLoadTask>());

    private static DownLoadTaskManager instance = new DownLoadTaskManager();

    public List<DownLoadInfo> getDownLoadInfoList() {
        return downLoadInfoList;
    }

    public List<DownLoadTask> getDownLoadTaskList() {
        return downLoadTaskList;
    }

    private DownLoadTaskManager() {
        initDownloadInfo();
    }

    private void initDownloadInfo() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                File downloadInfoFile = new File(path, DOWNLOAD_INFO_FILE);

                if (!downloadInfoFile.exists()) {
                    return;
                }

                try {
                    // 从文件中恢复下载信息
                    String stringResult = FileUtils.readFromFile(downloadInfoFile);
                    List downLoadRecord = objectMapper.readValue(stringResult, new TypeReference<List<DownLoadInfo>>() {
                    });
                    downLoadInfoList.addAll(downLoadRecord);

                    // 恢复下载任务
                    resumeAllDownLoadTask();

                } catch (Exception e) {
                    logger.error("initDownloadInfo failed", e);
                }

            }
        }).start();
    }

    public static DownLoadTaskManager getInstance() {
        return instance;
    }

    public boolean isDownLoad(TopicInfo topicInfo) {

        // 判断文件是否存在
        boolean isFileExist = false;
        File file = new File(path, topicInfo.getTitle() + ".mp4");
        if (file.exists()) {
            isFileExist = true;
        }

        // 判断下载信息是否存在
        boolean isDownloadInfoExist = false;
        DownLoadInfo downLoadInfo = new DownLoadInfo(topicInfo);
        for (Iterator<DownLoadInfo> iterator = downLoadInfoList.iterator(); iterator.hasNext(); ) {
            DownLoadInfo item = iterator.next();
            if (item.getUrl().equals(downLoadInfo.getUrl())) {
                isDownloadInfoExist = true;
                break;
            }
        }

        return isFileExist || isDownloadInfoExist;
    }

    public void addDownLoadTask(final TopicInfo topicInfo) {

        // 如果之前保留了下载信息，则删除
        final DownLoadInfo downLoadInfo = new DownLoadInfo(topicInfo);

        for (Iterator<DownLoadInfo> iterator = downLoadInfoList.iterator(); iterator.hasNext(); ) {
            DownLoadInfo item = iterator.next();
            if (item.getUrl().equals(downLoadInfo.getUrl())) {
                iterator.remove();
                break;
            }
        }
        // 添加下载信息
        downLoadInfoList.add(downLoadInfo);

        new Thread(new Runnable() {
            @Override
            public void run() {

                // 保存存储路径
                downLoadInfo.setPath(new File(path, downLoadInfo.getTitle() + ".mp4").getPath());

                // 保存视频图片，讲师图片
                Bitmap image = imageLoader.loadImageSync(topicInfo.getThumbUrl());
                String imagePath = FileUtils.saveBitMap(image, path, downLoadInfo.getTitle() + ".jpg");
                if (imagePath != null) {
                    downLoadInfo.setIamge(imagePath);
                }
                Bitmap photo = imageLoader.loadImageSync(topicInfo.getAuthorPhoto());
                String photoPath = FileUtils.saveBitMap(photo, path, downLoadInfo.getName() + ".jpg");
                if (photoPath != null) {
                    downLoadInfo.setPhoto(photoPath);
                }
//                try {
//                    long length = HttpClient.getFileLength(downLoadInfo.getUrl());
//                    downLoadInfo.setFileSize(length);
//                } catch (IOException e) {
//                    logger.error("addDownLoadTask get file length failed", e);
//                }

                // 下载信息保存
                try {
                    FileUtils.writeToFile(new File(path, DOWNLOAD_INFO_FILE), objectMapper.writeValueAsString(downLoadInfoList));
                } catch (JsonProcessingException e) {
                    logger.error("addDownLoadTask save downLoadInfoList failed", e);
                }
            }
        }).start();

        // 如果之前存在下载任务，则删除下载任务
        pauseDownLoadTask(downLoadInfo);

        // 如果文件已经存在，则删除原有文件
        File file = new File(path, downLoadInfo.getTitle() + ".mp4");
        if (file.exists()) {
            file.delete();
        }
        // 添加下载任务
        final DownLoadTask downLoadTask = new DownLoadTask(client, downLoadInfo.getUrl(), path, downLoadInfo.getTitle() + ".mp4");
        downLoadTask.setProgressListener(new ProgressListener() {
            @Override
            public void update(long bytesRead, long contentLength, boolean done) {
                downLoadInfo.setFileSize(contentLength);
                downLoadInfo.setBytesRead(bytesRead);
                if (bytesRead >= contentLength && done) {
                    downLoadInfo.setFinished(true);
                }
            }
        });
        downLoadTaskList.add(downLoadTask);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    downLoadTask.run();
                } catch (Exception e) {
                    logger.error("download failed, url: " + downLoadTask.getUrl(), e);
                }
            }
        }).start();
    }

    public void pauseDownLoadTask(DownLoadInfo downLoadInfo) {
        for (Iterator<DownLoadTask> iterator = downLoadTaskList.iterator(); iterator.hasNext(); ) {
            DownLoadTask downLoadTask = iterator.next();
            if (downLoadTask.getUrl().equals(downLoadInfo.getUrl())) {
                downLoadTask.cancel();
                iterator.remove();
                break;
            }
        }
    }

    public void deleteDownLoadVideo(DownLoadInfo downLoadInfo) {

        // 删除下载信息
        for (Iterator<DownLoadInfo> iterator = downLoadInfoList.iterator(); iterator.hasNext(); ) {
            DownLoadInfo item = iterator.next();
            if (item.getUrl().equals(downLoadInfo.getUrl())) {
                iterator.remove();
                break;
            }
        }

        try {
            FileUtils.writeToFile(new File(path, DOWNLOAD_INFO_FILE), objectMapper.writeValueAsString(downLoadInfoList));
        } catch (JsonProcessingException e) {
            logger.error("deleteDownLoadInfo save downLoadInfoList failed", e);
        }

        // 删除下载任务
        pauseDownLoadTask(downLoadInfo);

        // 如果视频文件已经存在，则删除视频文件
        File file = new File(path, downLoadInfo.getTitle() + ".mp4");
        if (file.exists()) {
            file.delete();
        }
    }

    public void resumeDownLoadTask(DownLoadInfo downLoadInfo) {
        // TODO
    }

    public void resumeAllDownLoadTask() {
        // TODO
    }

}
