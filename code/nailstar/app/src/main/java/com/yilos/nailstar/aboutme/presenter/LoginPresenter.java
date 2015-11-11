package com.yilos.nailstar.aboutme.presenter;

import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.view.ILoginView;

/**
 * Created by yangdan on 15/11/10.
 */
public class LoginPresenter {
    private static LoginPresenter instance = new LoginPresenter();

    private ILoginView loginView;

    private LoginPresenter(){}

    public static LoginPresenter getInstance(ILoginView loginView){
        instance.loginView = loginView;
        return instance;
    }

    /**
     * 账号登录
     */
    public void login(){
        String userName = loginView.getUserAccount();
        String password = loginView.getPassword();

        if(userName == null || userName.trim().equals("")) {
            loginView.showMessageDialog(loginView.getResourceStringById(R.string.enter_account).toString());
            return;
        }
        if(password == null || password.trim().equals("")) {
            loginView.showMessageDialog(loginView.getResourceStringById(R.string.enter_password).toString());
            return;
        }

        //HttpClient.post("/nailstar/account/login", )
    }
}
