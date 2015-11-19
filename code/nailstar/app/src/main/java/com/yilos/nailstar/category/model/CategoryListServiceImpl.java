package com.yilos.nailstar.category.model;

import com.yilos.nailstar.framework.entity.NailStarApplicationContext;
import com.yilos.nailstar.framework.exception.JSONParseException;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.index.entity.Topic;
import com.yilos.nailstar.util.HttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yangdan on 15/11/18.
 */
public class CategoryListServiceImpl {
    private static final CategoryListServiceImpl instance = new CategoryListServiceImpl();

    public static CategoryListServiceImpl getInstance() {
        return instance;
    }

    private CategoryListServiceImpl(){}

    public List<Topic> queryCategoryList(String category, int page) throws NetworkDisconnectException, JSONParseException {
        if(!NailStarApplicationContext.getInstance().isNetworkConnected()) {
            throw new NetworkDisconnectException("网络不给力哦，请检查网络设置");
        }

        String searchStrResult = null;
        try {
            searchStrResult = HttpClient.getJson("/vapi/nailstar/moreTopics?category=" + category + "&page=" + page + "&perPage=10");
        } catch (IOException e) {
            throw new NetworkDisconnectException("网络不给力哦，请检查网络设置", e);
        } catch (Exception e){
            throw new NetworkDisconnectException("网络不给力哦，请检查网络设置", e);
        }

        try{
            JSONObject jsonResultObject = new JSONObject(searchStrResult);
            if(jsonResultObject.getInt("code") != 0){
                return null;
            }

            JSONArray posterJsonArray = jsonResultObject.getJSONObject("result").getJSONArray("topics");

            List<Topic> result = new ArrayList<Topic>(10);
            for(int i = 0, length = posterJsonArray.length(); i < length; i++) {
                JSONObject topicObj = posterJsonArray.getJSONObject(i);
                Topic topic = new Topic();
                topic.setTopicId(topicObj.getString("topicId"));
                topic.setThumbUrl(topicObj.getString("thumbUrl"));
                topic.setAuthorPhoto(topicObj.getString("photoUrl"));
                topic.setAuthor(topicObj.getString("author"));
                topic.setCreateDate(new Date(topicObj.getLong("createDate")));
                topic.setTitle(topicObj.getString("title"));
                topic.setPlayTimes(topicObj.getInt("playTimes"));
                result.add(topic);
            }

            return result;
        }
        catch (JSONException e){
            throw new JSONParseException("解析视频列表失败", e);
        }
    }
}
