package com.yilos.nailstar.index.view;

import com.yilos.nailstar.index.entity.Category;
import com.yilos.nailstar.index.entity.Poster;
import com.yilos.nailstar.index.entity.Topic;

import java.util.List;

/**
 * Created by yangdan on 15/10/23.
 */
public interface IIndexView {
    void finishRefresh();

    /**
     * 初始化轮播图
     * @param posters
     */
    void initPosters(List<Poster> posters);

    /**
     * 初始化分类菜单
     * @param categories
     */
    void initCategoriesMenu(List<Category> categories);

    /**
     * 初始化最近视频
     * @param topics
     */
    void initLatestTopics(List<Topic> topics);

    /**
     * 初始化最热视频
     * @param topics
     */
    void initHotestTopics(List<Topic> topics);

    /**
     * 初始化关注视频
     * @param topics
     */
    void initWatchTopics(List<Topic> topics);

    void showNotLoginView();

    void showNotWatchView();
}
