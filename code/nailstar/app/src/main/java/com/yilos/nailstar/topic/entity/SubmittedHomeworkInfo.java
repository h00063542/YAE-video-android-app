package com.yilos.nailstar.topic.entity;

/**
 * Created by yilos on 2015-11-11.
 */
public class SubmittedHomeworkInfo {
    private String topicId;
    private String userId;
    private String content;
    private String picUrl;

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
