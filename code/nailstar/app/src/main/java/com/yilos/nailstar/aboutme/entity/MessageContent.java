package com.yilos.nailstar.aboutme.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sisilai on 15/11/18.
 */
public class MessageContent implements Serializable {

    /**
     * 回复我的消息列表
     */
    private List<UserMessage> userMessageList;

    /**
     * 系统消息列表
     */
    private List<SystemMessage> systemMessageList;


    public List<UserMessage> getUserMessageList() {
        return userMessageList;
    }

    public void setUserMessageList(List<UserMessage> userMessageList) {
        this.userMessageList = userMessageList;
    }

    public List<SystemMessage> getSystemMessageList() {
        return systemMessageList;
    }

    public void setSystemMessageList(List<SystemMessage> systemMessageList) {
        this.systemMessageList = systemMessageList;
    }

}
