package com.yilos.nailstar.index.model;

import com.yilos.nailstar.framework.exception.CommonException;
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
     * 分页大小
     */
    int PAGE_SIZE = 18;

    /**
     * 从网络上获取最新的Index内容
     */
    IndexContent getIndexContentFromNet() throws NetworkDisconnectException, JSONParseException;

    /**
     * 从本地缓存中读取首页内容
     * 如果缓存中没有，则返回空
     * @return
     */
    IndexContent getIndexContentFromCache();

    /**
     * 获取首页轮播图列表
     * @return
     * @throws NetworkDisconnectException
     * @throws JSONParseException
     */
    List<Poster> getIndexPostersFromNet() throws NetworkDisconnectException, JSONParseException;

    /**
     * 获取首页主题分类列表
     * @return
     * @throws NetworkDisconnectException
     * @throws JSONParseException
     */
    List<Category> getIndexCategoriesFromNet() throws NetworkDisconnectException, JSONParseException;

    /**
     * 获取首页最新视频列表
     * @return
     * @throws NetworkDisconnectException
     * @throws JSONParseException
     */
    List<Topic> getLatestTopicFromNet(int page) throws NetworkDisconnectException, JSONParseException;

    /**
     * 获取首页最热主题列表
     * @return
     * @throws NetworkDisconnectException
     * @throws JSONParseException
     */
    List<Topic> getHotestTopicFromNet(int page) throws NetworkDisconnectException, JSONParseException;

    /**
     * 获取用户关注的视频列表
     * @return
     * @throws NetworkDisconnectException
     * @throws JSONParseException
     */
    List<Topic> getWatchTopicFromNet(String uid, int page) throws NetworkDisconnectException, JSONParseException, CommonException;

    /**
     * 将首页内容保存到本地缓存中
     * @param indexContent
     */
    void saveIndexContent(IndexContent indexContent);
}
