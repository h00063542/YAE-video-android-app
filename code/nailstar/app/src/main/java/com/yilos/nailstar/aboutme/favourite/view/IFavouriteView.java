package com.yilos.nailstar.aboutme.favourite.view;

import com.yilos.nailstar.aboutme.favourite.entity.FavouriteTopic;
import com.yilos.nailstar.framework.view.IView;

import java.util.List;

/**
 * Created by yangdan on 15/11/23.
 */
public interface IFavouriteView extends IView {
    /**
     * 初始化收藏列表
     * @param favouriteTopicList
     */
    void setFavouriteList(List<FavouriteTopic> favouriteTopicList);
}
