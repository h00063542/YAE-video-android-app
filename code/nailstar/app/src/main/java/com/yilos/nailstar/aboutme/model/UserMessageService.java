package com.yilos.nailstar.aboutme.model;

import com.yilos.nailstar.aboutme.entity.UserMessage;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;

import org.json.JSONException;

import java.util.List;

/**
 * Created by sisilai on 15/11/19.
 */
public interface UserMessageService {
    List<UserMessage> getUserMessageList(String uid) throws NetworkDisconnectException, JSONException;
}
