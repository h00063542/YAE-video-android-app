package com.yilos.nailstar.aboutme.model;

import android.app.Activity;
import android.content.Intent;

import com.yilos.nailstar.aboutme.view.LoginActivity;

/**
 * Created by yangdan on 15/11/10.
 */
public class LoginAPI {
    private static LoginAPI instance = new LoginAPI();

    private LoginServiceImpl loginService = LoginServiceImpl.getInstance();

    private LoginAPI(){}

    public static LoginAPI getInstance() {
        return instance;
    }

    public boolean isLogin() {
        return loginService.isLogin();
    }

    public void gotoLoginPage(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivityForResult(intent, 1);
    }
}
