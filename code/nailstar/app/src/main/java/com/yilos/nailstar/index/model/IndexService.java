package com.yilos.nailstar.index.model;

import com.yilos.nailstar.framework.exception.JSONParseException;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.index.entity.Category;
import com.yilos.nailstar.index.entity.IndexContent;
import com.yilos.nailstar.index.entity.Poster;
import com.yilos.nailstar.index.entity.Topic;

import java.util.List;

/**
 * Created by yangdan on 15/10/16.
 */
public interface IndexService {
    /**
     * 从网络上获取最新的Index内容
     */
    IndexContent getIndexContentFromNet() throws NetworkDisconnectException, JSONParseException;

    /**
     * 获取首页内容
     * @return 首页信息
     */
    IndexContent getIndexContent();

    /**
     * 获取首页轮播图列表
     * @return
     * @throws NetworkDisconnectException
     * @throws JSONParseException
     */
    List<Poster> getIndexPosters();

    /**
     * 获取首页最新视频列表
     * @return
     * @throws NetworkDisconnectException
     * @throws JSONParseException
     */
    List<Topic> getLatestTopic();

    /**
     * 获取首页最热主题列表
     * @return
     * @throws NetworkDisconnectException
     * @throws JSONParseException
     */
    List<Topic> getHotestTopic();

    /**
     * 获取首页主题分类列表
     * @return
     * @throws NetworkDisconnectException
     * @throws JSONParseException
     */
    List<Category> getIndexCategories();

    void saveIndexContent();
}
