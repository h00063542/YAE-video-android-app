package com.yilos.nailstar.aboutme.model;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

import com.yilos.nailstar.aboutme.entity.PersonInfo;
import com.yilos.nailstar.aboutme.view.LoginActivity;
import com.yilos.nailstar.framework.application.NailStarApplication;

/**
 * Created by yangdan on 15/11/10.
 */
public class LoginAPI {
    // SharePrefrence中的标识
    public static final String LOGIN_STATUS = "loginStatus";
    public static final String LOGIN_USER_NAME = "loginUserName";
    public static final String LOGIN_USER_ID = "loginUserId";
    public static final String LOGIN_USER_NICKNAME = "loginUserNickname";
    public static final String LOGIN_USER_TYPE = "loginUserType";
    public static final String LOGIN_USER_PHOTOURL = "loginUserPhotoUrl";
    public static final String LOGIN_USER_PROFILE = "loginUserProfile";

    // 单例
    private static LoginAPI instance = new LoginAPI();

    private String loginUserName;
    private PersonInfo loginPersonInfo;

    private LoginAPI(){
        getLoginInfo();
    }

    public static LoginAPI getInstance() {
        return instance;
    }

    /**
     * 判断用户是否登录
     * @return
     */
    public boolean isLogin() {
        return loginPersonInfo.getUid() != null;
    }

    public void gotoLoginPage(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivityForResult(intent, 1);
    }

    public void logout() {
        loginUserName = null;
        loginPersonInfo = null;

        SharedPreferences sharedPreferences = NailStarApplication.getApplication().getSharedPreferences(LOGIN_STATUS, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    /**
     * 保存用户的登录状态
     * @param userName
     * @param personInfo
     */
    public void saveLoginStatus(String userName, PersonInfo personInfo) {
        loginUserName = userName;
        loginPersonInfo = personInfo;

        SharedPreferences sharedPreferences = NailStarApplication.getApplication().getSharedPreferences(LOGIN_STATUS, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LOGIN_USER_NAME, userName);
        editor.putString(LOGIN_USER_ID, personInfo.getUid());
        editor.putString(LOGIN_USER_NICKNAME, personInfo.getNickname());
        editor.putInt(LOGIN_USER_TYPE, personInfo.getType());
        editor.putString(LOGIN_USER_PHOTOURL, personInfo.getPhotoUrl());
        editor.putString(LOGIN_USER_PROFILE, personInfo.getProfile());
        editor.apply();
    }

    /**
     * 获取登录的用户ID
     * @return
     */
    public String getLoginUserId(){
        return loginPersonInfo == null ? null : loginPersonInfo.getUid();
    }

    public String getLoginUserName() {
        return loginUserName;
    }

    public String getLoginUserNickname() {
        return loginPersonInfo == null ? null : loginPersonInfo.getNickname();
    }

    public int getLoginUserType() {
        return loginPersonInfo == null ? 0 : loginPersonInfo.getType();
    }

    public String getLoginUserPhotourl() {
        return loginPersonInfo == null ? null : loginPersonInfo.getPhotoUrl();
    }

    private void getLoginInfo() {
        SharedPreferences sharedPreferences = NailStarApplication.getApplication().getSharedPreferences(LOGIN_STATUS, Activity.MODE_PRIVATE);

        PersonInfo personInfo = new PersonInfo();
        personInfo.setUid(sharedPreferences.getString(LOGIN_USER_ID, null));
        personInfo.setType(sharedPreferences.getInt(LOGIN_USER_TYPE, 0));
        personInfo.setProfile(sharedPreferences.getString(LOGIN_USER_PROFILE, null));
        personInfo.setNickname(sharedPreferences.getString(LOGIN_USER_NICKNAME, null));
        personInfo.setPhotoUrl(sharedPreferences.getString(LOGIN_USER_PHOTOURL, null));
        loginUserName = sharedPreferences.getString(LOGIN_USER_NAME, null);
        this.loginPersonInfo = personInfo;
    }
}