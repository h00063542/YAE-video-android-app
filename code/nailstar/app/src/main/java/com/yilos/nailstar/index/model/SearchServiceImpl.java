package com.yilos.nailstar.index.model;

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
 * Created by yangdan on 15/11/16.
 */
public class SearchServiceImpl {
    private static SearchServiceImpl instance = new SearchServiceImpl();

    private SearchServiceImpl(){}

    public static SearchServiceImpl getInstance() {
        return instance;
    }

    /**
     * 根据关键字搜索主题
     * @param keyWord
     * @return
     */
    public List<Topic> searchByKeyWord(String keyWord, int page) throws NetworkDisconnectException, JSONParseException {
        String searchStrResult = null;
        try {
            searchStrResult = HttpClient.getJson("/vapi/nailstar/topics?condition=" + keyWord + "&page=" + page);
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
                result.add(topic);
            }

            return result;
        }
        catch (JSONException e){
            throw new JSONParseException("解析视频列表失败", e);
        }
    }
}
