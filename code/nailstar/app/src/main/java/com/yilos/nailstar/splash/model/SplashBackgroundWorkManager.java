package com.yilos.nailstar.splash.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangdan on 15/11/10.
 */
public class SplashBackgroundWorkManager {
    private static SplashBackgroundWorkManager instance = new SplashBackgroundWorkManager();

    private List<SplashBackgroundWork> workList = new ArrayList<>(4);

    private SplashBackgroundWorkManager(){
        initWork();
    }

    private void initWork(){
        workList.add(new LoadIndexContent());
    }

    public static SplashBackgroundWorkManager getInstance() {
        return instance;
    }

    public void execute(){
        for(SplashBackgroundWork work : workList) {
            work.doWork();
        }
    }
}
