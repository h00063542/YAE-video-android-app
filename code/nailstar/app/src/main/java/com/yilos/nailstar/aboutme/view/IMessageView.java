package com.yilos.nailstar.aboutme.view;

import com.yilos.nailstar.aboutme.entity.SystemMessage;
import com.yilos.nailstar.aboutme.entity.UserMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sisilai on 15/11/19.
 */
public interface IMessageView {
    void getUserMessageList(ArrayList<UserMessage> userMessageArrayList);

    void setLocalReplyMessage(ArrayList<UserMessage> userMessageArrayList);
    ArrayList<UserMessage> getLocalReplyMessage();

    void getSystemMessageList(List<Object> objectList);

    void setLocalSystemMessage(ArrayList<SystemMessage> systemMessageArrayList);

    void setLatestMessageTime(long latestMessageTime);

    long getLatestMessageTime();
}
