package com.yilos.nailstar.index.entity;

import java.io.Serializable;

/**
 * Created by yangdan on 15/10/16.
 * 轮播图信息
 */
public class Poster implements Serializable {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Poster poster = (Poster) o;

        if (!picUrl.equals(poster.picUrl)) return false;
        if (!topicId.equals(poster.topicId)) return false;
        if (!type.equals(poster.type)) return false;
        return !(activityUrl != null ? !activityUrl.equals(poster.activityUrl) : poster.activityUrl != null);

    }

    @Override
    public int hashCode() {
        int result = picUrl.hashCode();
        result = 31 * result + topicId.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + (activityUrl != null ? activityUrl.hashCode() : 0);
        return result;
    }

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
