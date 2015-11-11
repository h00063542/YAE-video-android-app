package com.yilos.nailstar.aboutme.model;

/**
 * Created by yangdan on 15/11/10.
 */
public class LoginServiceImpl {
    private static LoginServiceImpl instance = new LoginServiceImpl();

    private LoginServiceImpl(){}

    public static LoginServiceImpl getInstance() {
        return instance;
    }

    /**
     * 判断客户端是否已经登录
     * @return
     */
    public boolean isLogin() {
        return false;
    }
}
