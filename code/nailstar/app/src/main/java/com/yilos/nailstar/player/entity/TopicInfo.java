package com.yilos.nailstar.player.entity;

import java.util.ArrayList;

/**
 * Created by yilos on 2015-10-22.
 */
public class TopicInfo {
    // 视频Id
    private String id;
    // 视频title
    private String title;
    private String type;
    private String authorPhoto;
    private String thumbUrl;
    private long createDate;
    private ArrayList tags;
    private ArrayList<VideoInfo> videos;
    private String authorId;
    private String author;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAuthorPhoto() {
        return authorPhoto;
    }

    public void setAuthorPhoto(String authorPhoto) {
        this.authorPhoto = authorPhoto;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public ArrayList getTags() {
        return tags;
    }

    public void setTags(ArrayList tags) {
        this.tags = tags;
    }

    public ArrayList<VideoInfo> getVideos() {
        return videos;
    }

    public void setVideos(ArrayList<VideoInfo> videos) {
        this.videos = videos;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
