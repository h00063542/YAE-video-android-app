package com.yilos.nailstar.aboutme.model;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

import com.yilos.nailstar.aboutme.view.LoginActivity;
import com.yilos.nailstar.framework.application.NailStarApplication;

/**
 * Created by yangdan on 15/11/10.
 */
public class LoginAPI {
    public static final String LOGIN_STATUS = "loginStatus";
    public static final String LOGIN_USER_NAME = "loginUserName";
    public static final String LOGIN_USER_ID = "loginUserId";

    private static LoginAPI instance = new LoginAPI();

    private String loginUserName;
    private String loginUserId;

    private LoginAPI(){
        getLoginInfo();
    }

    public static LoginAPI getInstance() {
        return instance;
    }

    public boolean isLogin() {
        return loginUserId != null;
    }

    public void gotoLoginPage(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivityForResult(intent, 1);
    }

    public void logout() {
        loginUserName = null;
        loginUserId = null;

        SharedPreferences sharedPreferences = NailStarApplication.getApplication().getSharedPreferences(LOGIN_STATUS, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LOGIN_USER_NAME, null);
        editor.putString(LOGIN_USER_ID, null);
        editor.apply();
    }

    public void saveLoginStatus(String userName, String uid) {
        loginUserName = userName;
        loginUserId = uid;

        SharedPreferences sharedPreferences = NailStarApplication.getApplication().getSharedPreferences(LOGIN_STATUS, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LOGIN_USER_NAME, userName);
        editor.putString(LOGIN_USER_ID, uid);
        editor.apply();
    }

    /**
     * 获取登录的用户ID
     * @return
     */
    public String getLoginUserId(){
        return loginUserId;
    }

    public String getLoginUserName() {
        return loginUserName;
    }

    private void getLoginInfo() {
        SharedPreferences sharedPreferences = NailStarApplication.getApplication().getSharedPreferences(LOGIN_STATUS, Activity.MODE_PRIVATE);

        loginUserId = sharedPreferences.getString(LOGIN_USER_ID, null);
        loginUserName = sharedPreferences.getString(LOGIN_USER_ID, null);
    }
}