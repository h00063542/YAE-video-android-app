package com.yilos.nailstar.aboutme.favourite.presenter;

import android.app.Activity;

import com.yilos.nailstar.aboutme.favourite.entity.FavouriteTopic;
import com.yilos.nailstar.aboutme.favourite.model.FavouriteServiceImpl;
import com.yilos.nailstar.aboutme.favourite.view.IFavouriteView;
import com.yilos.nailstar.aboutme.model.LoginAPI;
import com.yilos.nailstar.framework.exception.JSONParseException;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.util.TaskManager;

import java.util.List;

/**
 * Created by yangdan on 15/11/23.
 */
public class FavouritePresenter {
    private static FavouritePresenter instance = new FavouritePresenter();

    private IFavouriteView view;

    private FavouriteServiceImpl service = FavouriteServiceImpl.getInstance();

    public static FavouritePresenter getInstance(IFavouriteView view) {
        instance.view = view;
        return instance;
    }

    public void initFavouriteList() {
        if(!LoginAPI.getInstance().isLogin()) {
            LoginAPI.getInstance().gotoLoginPage((Activity)view);
            return;
        }
        view.showLoading(null);

        TaskManager.BackgroundTask backgroundTask = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                try {
                    List<FavouriteTopic> favouriteTopicList = service.queryMyFavouriteTopic(LoginAPI.getInstance().getLoginUserId());
                    return favouriteTopicList;
                } catch (NetworkDisconnectException e) {
                    e.printStackTrace();
                } catch (JSONParseException e) {
                    e.printStackTrace();
                }

                return null;
            }
        };

        TaskManager.UITask<List<FavouriteTopic>> uiTask = new TaskManager.UITask<List<FavouriteTopic>>() {
            @Override
            public Object doWork(List<FavouriteTopic> data) {
                view.setFavouriteList(data);
                view.hideLoading();
                return null;
            }
        };

        new TaskManager()
                .next(backgroundTask)
                .next(uiTask)
                .start();
    }

    /**
     * 私有化构造函数
     */
    private FavouritePresenter() {}
}
