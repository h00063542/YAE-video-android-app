package com.yilos.nailstar.splash.model;

import com.yilos.nailstar.framework.application.NailStarApplication;

/**
 * Created by yangdan on 15/11/10.
 */
public class LoadIndexContent implements SplashBackgroundWork {
    @Override
    public void doWork() {
        NailStarApplication.getApplication().preloadIndexContent();
    }
}
