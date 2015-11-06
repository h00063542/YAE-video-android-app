package com.yilos.nailstar.aboutme.presenter;

import com.yilos.nailstar.aboutme.entity.FansList;
import com.yilos.nailstar.aboutme.entity.FollowList;
import com.yilos.nailstar.aboutme.model.AboutMeService;
import com.yilos.nailstar.aboutme.model.AboutMeServiceImpl;
import com.yilos.nailstar.aboutme.view.FansListActivity;
import com.yilos.nailstar.aboutme.view.MainActivity;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.util.TaskManager;

import java.util.ArrayList;

/**
 * Created by sisilai on 15/11/5.
 */
public class FansListPresenter {
    private AboutMeService aboutMeService = new AboutMeServiceImpl();
    private static FansListPresenter fansListPresenter = new FansListPresenter();
    private MainActivity mainActivity;
    public static FansListPresenter getInstance(MainActivity mainActivity) {
        fansListPresenter.mainActivity = mainActivity;
        return fansListPresenter;
    }
    //获取粉丝列表
    public void getFansList(final String uid) {
        TaskManager.Task loadFansList = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                try {
                    return aboutMeService.getFansList(uid);
                } catch (NetworkDisconnectException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        TaskManager.UITask<ArrayList<FansList>> fansListUITask = new TaskManager.UITask<ArrayList<FansList>>() {
            @Override
            public ArrayList<FollowList> doWork(ArrayList<FansList> data) {
                mainActivity.initFansList(data);
                return null;
            }
        };

        new TaskManager().next(loadFansList).next(fansListUITask).start();
    }
}
