package com.yilos.nailstar.topic.entity;

/**
 * Created by yilos on 2015-11-13.
 */
public class TopicStatusInfo {
    private boolean isLike;
    private boolean isCollect;

    public boolean isLike() {
        return isLike;
    }

    public void setIsLike(boolean isLike) {
        this.isLike = isLike;
    }

    public boolean isCollect() {
        return isCollect;
    }

    public void setIsCollect(boolean isCollect) {
        this.isCollect = isCollect;
    }
}
