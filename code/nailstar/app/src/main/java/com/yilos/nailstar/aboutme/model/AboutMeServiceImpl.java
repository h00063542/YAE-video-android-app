package com.yilos.nailstar.aboutme.model;

import com.yilos.nailstar.aboutme.entity.MessageCount;
import com.yilos.nailstar.aboutme.entity.PersonInfo;
import com.yilos.nailstar.framework.exception.JSONParseException;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;

import org.json.JSONException;

/**
 * Created by sisilai on 15/10/24.
 */
public interface AboutMeServiceImpl {
    MessageCount getMessageCount() throws NetworkDisconnectException, JSONException;
    PersonInfo setPersonInfo() throws NetworkDisconnectException,JSONException;
    PersonInfo getPersonInfo() throws NetworkDisconnectException,JSONException;
}

