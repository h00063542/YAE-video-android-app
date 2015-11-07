package com.yilos.nailstar.topic.entity;

import java.io.Serializable;

/**
 * Created by yilos on 2015-10-27.
 */
public class TopicRelatedInfo implements Serializable {
    private String topicId;
    private String thumbUrl;

    public TopicRelatedInfo() {

    }

    public TopicRelatedInfo(String topicId, String thumbUrl) {
        this.topicId = topicId;
        this.thumbUrl = thumbUrl;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }
}
