package com.yilos.nailstar.aboutme.model;

import com.yilos.nailstar.aboutme.entity.AboutMeNumber;
import com.yilos.nailstar.aboutme.entity.FollowList;
import com.yilos.nailstar.aboutme.entity.MessageCount;
import com.yilos.nailstar.aboutme.entity.PersonInfo;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by sisilai on 15/10/24.
 */
public interface AboutMeService {
    MessageCount getMessageCount() throws NetworkDisconnectException, JSONException;
    PersonInfo setPersonInfo() throws NetworkDisconnectException,JSONException;
    PersonInfo getPersonInfo() throws NetworkDisconnectException,JSONException;
    AboutMeNumber getAboutMeNumber() throws NetworkDisconnectException,JSONException;
    ArrayList<FollowList> getFollowList(String uid) throws NetworkDisconnectException;
}

