package com.yilos.nailstar.aboutme.favourite.presenter;

import com.yilos.nailstar.aboutme.favourite.view.IFavouriteView;

/**
 * Created by yangdan on 15/11/23.
 */
public class FavouritePresenter {
    private static FavouritePresenter instance = new FavouritePresenter();

    private IFavouriteView view;

    public static FavouritePresenter getInstance(IFavouriteView view) {
        instance.view = view;
        return instance;
    }

    public void initFavouriteList() {

    }

    /**
     * 私有化构造函数
     */
    private FavouritePresenter() {}
}
