package com.yilos.nailstar.splash.presenter;

import com.yilos.nailstar.splash.model.SplashBackgroundWorkManager;

/**
 * Created by yangdan on 15/11/10.
 */
public class SplashPresenter {
    private static SplashPresenter instance = new SplashPresenter();

    private SplashPresenter(){}

    public static SplashPresenter getInstance() {
        return instance;
    }

    public void splashWork() {
        SplashBackgroundWorkManager.getInstance().execute();
    }
}
