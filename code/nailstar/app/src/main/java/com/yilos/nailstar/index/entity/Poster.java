package com.yilos.nailstar.index.entity;

/**
 * Created by yangdan on 15/10/16.
 * 轮播图信息
 */
public class Poster {
    /**
     * 图片地址
     */
    private String picUrl;

    /**
     * 主题ID
     */
    private String topicId;

    /**
     * 轮播图类型
     */
    private String type;

    /**
     *
     */
    private String activityUrl;

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getActivityUrl() {
        return activityUrl;
    }

    public void setActivityUrl(String activityUrl) {
        this.activityUrl = activityUrl;
    }
}
