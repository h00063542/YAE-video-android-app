package com.yilos.nailstar.player.model;

import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.player.entity.TopicsCommentInfo;
import com.yilos.nailstar.player.entity.TopicsImageTextInfo;
import com.yilos.nailstar.player.entity.TopicsInfo;
import com.yilos.nailstar.player.entity.TopicsRelatedInfo;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by yilos on 2015-10-24.
 */
public interface ITopicsService {

    // 获取主题信息
    TopicsInfo getTopicsInfo(String topicsId) throws IOException, JSONException, NetworkDisconnectException;

    // 获取主题图文信息
    TopicsImageTextInfo getTopicsImageTextInfo(String topicsId) throws NetworkDisconnectException, IOException, JSONException;

    // 获取关联的主题信息
    ArrayList<TopicsRelatedInfo> getTopicsRelatedInfoList(String topicsId) throws NetworkDisconnectException, JSONException;

    // 获取主题评论
    ArrayList<TopicsCommentInfo> getTopicsComments(String topicsId, int page) throws NetworkDisconnectException, JSONException;

}
