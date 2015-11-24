package com.yilos.nailstar.aboutme.model;

import com.yilos.nailstar.aboutme.entity.MessageComment;
import com.yilos.nailstar.aboutme.entity.UserMessage;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by sisilai on 15/11/19.
 */
public interface UserMessageService {
    ArrayList<UserMessage> getUserMessageList(String uid) throws NetworkDisconnectException, JSONException;
    MessageComment setComment(MessageComment messageComment, String topicId) throws NetworkDisconnectException, JSONException;
}
