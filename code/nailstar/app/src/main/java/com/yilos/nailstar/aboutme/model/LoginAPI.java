package com.yilos.nailstar.aboutme.model;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

import com.yilos.nailstar.aboutme.entity.PersonInfo;
import com.yilos.nailstar.aboutme.view.LoginActivity;
import com.yilos.nailstar.framework.application.NailStarApplication;
import com.yilos.nailstar.social.model.SocialLoginAPI;

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
    public static final String LOGIN_FROM_TYPE = "loginFormType"; //登录来源

    // 单例
    private static LoginAPI instance = new LoginAPI();

    private String loginUserName;
    private PersonInfo loginPersonInfo;
    private LoginFromType loginFromType;

    private LoginAPI(){
        getLoginInfo();
    }

    public static LoginAPI getInstance() {
        return instance;
    }

    public enum LoginFromType {
        PHONE,
        WEI_XIN,
        SINA_WEI_BO,
        QQ
    }

    /**
     * 判断用户是否登录
     * @return
     */
    public boolean isLogin() {
        return loginPersonInfo != null;
    }

    public void gotoLoginPage(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivityForResult(intent, 1);
    }

    public void logout() {
        if(loginFromType == LoginFromType.WEI_XIN) {
            SocialLoginAPI.getInstance().logoutWeixin();
        } else if(loginFromType == LoginFromType.SINA_WEI_BO) {
            SocialLoginAPI.getInstance().logoutSinaWeibo();
        } else if(loginFromType == LoginFromType.QQ) {
            SocialLoginAPI.getInstance().logoutQQ();
        }

        loginUserName = null;
        loginPersonInfo = null;
        loginFromType = null;

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
    public void saveLoginStatus(String userName, LoginFromType loginFromType, PersonInfo personInfo) {
        loginUserName = userName;
        loginPersonInfo = personInfo;
        this.loginFromType = loginFromType;

        SharedPreferences sharedPreferences = NailStarApplication.getApplication().getSharedPreferences(LOGIN_STATUS, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LOGIN_USER_NAME, userName);
        editor.putString(LOGIN_USER_ID, personInfo.getUid());
        editor.putString(LOGIN_USER_NICKNAME, personInfo.getNickname());
        editor.putInt(LOGIN_USER_TYPE, personInfo.getType());
        editor.putString(LOGIN_USER_PHOTOURL, personInfo.getPhotoUrl());
        editor.putString(LOGIN_USER_PROFILE, personInfo.getProfile());
        editor.putString(LOGIN_FROM_TYPE, loginFromType.name());
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

    public String getLoginUserProfile() {
        return loginPersonInfo == null ? null : loginPersonInfo.getProfile();
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
        loginFromType = LoginFromType.valueOf(sharedPreferences.getString(LOGIN_FROM_TYPE, "PHONE"));
        if(personInfo.getUid() == null) {
            this.loginPersonInfo = null;
        } else {
            this.loginPersonInfo = personInfo;
        }
    }
}