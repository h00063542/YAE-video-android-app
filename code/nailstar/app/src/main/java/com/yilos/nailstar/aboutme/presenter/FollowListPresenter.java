package com.yilos.nailstar.aboutme.presenter;

import com.yilos.nailstar.aboutme.entity.FollowList;
import com.yilos.nailstar.aboutme.model.AboutMeService;
import com.yilos.nailstar.aboutme.model.AboutMeServiceImpl;
import com.yilos.nailstar.aboutme.view.FollowListActivity;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.util.TaskManager;

import java.util.ArrayList;

/**
 * Created by sisilai on 15/11/4.
 */
public class FollowListPresenter {

    private AboutMeService aboutMeService = new AboutMeServiceImpl();
    private static FollowListPresenter followListPresenter = new FollowListPresenter();
    private FollowListActivity followListActivity;
    public static FollowListPresenter getInstance(FollowListActivity followListActivity) {
        followListPresenter.followListActivity = followListActivity;
        return followListPresenter;
    }
    //获取关注列表
    public void getFollowList(final String uid) {
        TaskManager.Task loadFollowList = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                try {
                    return aboutMeService.getFollowList(uid);
                } catch (NetworkDisconnectException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        TaskManager.UITask<ArrayList<FollowList>> FollowListUITask = new TaskManager.UITask<ArrayList<FollowList>>() {
            @Override
            public ArrayList<FollowList> doWork(ArrayList<FollowList> data) {
                followListActivity.initFollowList(data);
                return null;
            }
        };

        new TaskManager().next(loadFollowList).next(FollowListUITask).start();
    }
}
