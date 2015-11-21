package com.yilos.nailstar.aboutme.view;

import com.yilos.nailstar.aboutme.entity.UserMessage;

import java.util.ArrayList;

/**
 * Created by sisilai on 15/11/19.
 */
public interface IMessageView {
    void getUserMessageList(ArrayList<UserMessage> userMessageArrayList);
    void setLocalReplyMessage(ArrayList<UserMessage> userMessageArrayList);
    ArrayList<UserMessage> getLocalReplyMessage();
}
