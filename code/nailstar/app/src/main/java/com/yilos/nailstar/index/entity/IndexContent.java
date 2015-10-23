package com.yilos.nailstar.index.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yangdan on 15/10/16.
 * 首页内容类
 */
public class IndexContent implements Serializable{
    /**
     * 轮播图列表
     */
    private List<Poster> posters;

    /**
     * 最新视频列表
     */
    private List<Topic> latestTopics;

    /**
     * 最热视频列表
     */
    private List<Topic> hotestTopics;

    /**
     * 主题分类列表
     */
    private List<Category> categories;

    public List<Poster> getPosters() {
        return posters;
    }

    public void setPosters(List<Poster> posters) {
        this.posters = posters;
    }

    public List<Topic> getLatestTopics() {
        return latestTopics;
    }

    public void setLatestTopics(List<Topic> latestTopics) {
        this.latestTopics = latestTopics;
    }

    public List<Topic> getHotestTopics() {
        return hotestTopics;
    }

    public void setHotestTopics(List<Topic> hotestTopics) {
        this.hotestTopics = hotestTopics;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
