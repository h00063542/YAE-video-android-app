package com.yilos.nailstar.requirelession.entity;

import java.io.Serializable;

/**
 * 已录制教程
 */
public class VideoLession implements Serializable {

    /**
     * 已录制教程Id
     */
    private String  topicId;

    /**
     * 已录制教程的图片
     */
    private String  picUrl;

    /**
     * 已录制教程名称
     */
    private String  title;

    /**
     * 期数
     */
    private int no;

    /**
     * 已录制教程的老师名称
     */
    private String  authorName;

    /**
     * 已录制教程的老师头像
     */
    private String  authorPhoto;

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorPhoto() {
        return authorPhoto;
    }

    public void setAuthorPhoto(String authorPhoto) {
        this.authorPhoto = authorPhoto;
    }
}
