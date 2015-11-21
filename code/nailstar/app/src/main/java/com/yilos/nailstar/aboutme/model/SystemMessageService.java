package com.yilos.nailstar.aboutme.model;

import com.yilos.nailstar.framework.exception.NetworkDisconnectException;

import org.json.JSONException;

import java.util.List;

/**
 * Created by sisilai on 15/11/21.
 */
public interface SystemMessageService {
    List<Object> getSystemMessage(long lt) throws NetworkDisconnectException, JSONException;
}
