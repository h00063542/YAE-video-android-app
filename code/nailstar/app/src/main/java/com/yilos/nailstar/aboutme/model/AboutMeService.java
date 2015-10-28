package com.yilos.nailstar.aboutme.model;

import com.yilos.nailstar.aboutme.entity.Message;
import com.yilos.nailstar.framework.exception.JSONParseException;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;

/**
 * Created by sisilai on 15/10/24.
 */
public interface AboutMeService {
//    int getMessageCount() throws NetworkDisconnectException, JSONParseException;
    Message getMessageContext() throws NetworkDisconnectException, JSONParseException;
}

