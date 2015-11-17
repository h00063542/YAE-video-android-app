package com.yilos.nailstar.aboutme.model;

import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.yilos.nailstar.aboutme.entity.AboutMeNumber;
import com.yilos.nailstar.aboutme.entity.FansList;
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
    PersonInfo setPersonInfo(String uid,String nickname,int type,String photoUrl,String profile) throws NetworkDisconnectException,JSONException;
    PersonInfo getPersonInfo() throws NetworkDisconnectException,JSONException;
    AboutMeNumber getAboutMeNumber() throws NetworkDisconnectException,JSONException;
    ArrayList<FollowList> getFollowList(String uid) throws NetworkDisconnectException;
    ArrayList<FansList> getFansList(String uid) throws NetworkDisconnectException;

    /**
     * 上传文件到oss
     *
     * @param localFilePath
     * @param ossFileName
     * @param callback
     * @return
     * @throws NetworkDisconnectException
     */
    void uploadFile2Oss(String localFilePath, String ossFileName, SaveCallback callback) throws NetworkDisconnectException;
}

