package com.yilos.nailstar.topic.entity;

import java.io.Serializable;

/**
 * Created by yilos on 2015-10-27.
 */
public class TopicCommentAtInfo implements Serializable {
    private String userId;
    private String nickName;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
