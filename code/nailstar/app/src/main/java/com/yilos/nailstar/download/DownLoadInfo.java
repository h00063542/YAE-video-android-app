package com.yilos.nailstar.download;

import com.yilos.nailstar.topic.entity.TopicInfo;

import java.io.Serializable;

/**
 * Created by yilos on 15/11/12.
 */
public class DownLoadInfo implements Serializable {

    /**
     * 视频id
     */
    private String topicId;

    /**
     * 视频名称
     */
    private String title;

    /**
     * 视频照片
     */
    private String iamge;

    /**
     * 讲师照片
     */
    private String photo;

    /**
     * 讲师姓名
     */
    private String name;

    /**
     * 视频大小
     */
    private long fileSize;

    /**
     * 视频url
     */
    private String url;

    /**
     * 下载状态 -1:停止中 0:下载中 1:下载完成
     */
    private int status;
    /**
     * 已下载文件大小
     */
    private transient long bytesRead;

    /**
     * 保存路径
     */
    private String path;

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIamge() {
        return iamge;
    }

    public void setIamge(String iamge) {
        this.iamge = iamge;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getBytesRead() {
        return bytesRead;
    }

    public void setBytesRead(long bytesRead) {
        this.bytesRead = bytesRead;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public DownLoadInfo() {

    }

    public DownLoadInfo(TopicInfo topicInfo) {
        if (topicInfo != null) {
            this.topicId = topicInfo.getId();
            this.title = topicInfo.getTitle();
            this.name = topicInfo.getAuthor();
            if (!topicInfo.getVideos().isEmpty()) {
                if (topicInfo.getVideos().get(0).getOssUrl() != null && !"".equals(topicInfo.getVideos().get(0).getOssUrl())) {
                    this.url = topicInfo.getVideos().get(0).getOssUrl();
                } else if (topicInfo.getVideos().get(0).getCcUrl() != null && !"".equals(topicInfo.getVideos().get(0).getCcUrl())) {
                    this.url = topicInfo.getVideos().get(0).getCcUrl();
                }

            }
        }
    }
}
