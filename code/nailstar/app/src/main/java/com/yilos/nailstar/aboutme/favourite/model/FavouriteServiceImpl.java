package com.yilos.nailstar.aboutme.favourite.model;

import com.yilos.nailstar.aboutme.favourite.entity.FavouriteTopic;
import com.yilos.nailstar.framework.exception.JSONParseException;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.util.HttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yangdan on 15/11/23.
 */
public class FavouriteServiceImpl {
    private static FavouriteServiceImpl instance = new FavouriteServiceImpl();

    public static FavouriteServiceImpl getInstance(){
        return instance;
    }

    public List<FavouriteTopic> queryMyFavouriteTopic(String uid) throws NetworkDisconnectException, JSONParseException {
        String favouriteStrResult = null;
        try {
            favouriteStrResult = HttpClient.getJson("/vapi/nailstar/collections?uid=" + uid);
        } catch (IOException e) {
            throw new NetworkDisconnectException("网络不给力哦，请检查网络设置", e);
        } catch (Exception e){
            throw new NetworkDisconnectException("网络不给力哦，请检查网络设置", e);
        }

        try{
            JSONObject jsonResultObject = new JSONObject(favouriteStrResult);
            if(jsonResultObject.getInt("code") != 0){
                return null;
            }

            JSONArray favouriteJsonArray = jsonResultObject.getJSONObject("result").getJSONArray("collections");

            List<FavouriteTopic> result = new ArrayList<FavouriteTopic>(10);
            for(int i = 0, length = favouriteJsonArray.length(); i < length; i++) {
                JSONObject topicObj = favouriteJsonArray.getJSONObject(i);
                FavouriteTopic topic = new FavouriteTopic();
                topic.setTopicId(topicObj.getString("topicId"));
                topic.setThumbUrl(topicObj.getString("thumbUrl"));
                topic.setTitle(topicObj.getString("title"));
                topic.setCreateDate(new Date(topicObj.getLong("createDate")));
                topic.setCollectionId(topicObj.getString("collectionId"));
                topic.setAuthor(topicObj.getString("author"));
                topic.setAuthorPhotoUrl(topicObj.getString("photoUrl"));

                result.add(topic);
            }

            return result;
        }
        catch (JSONException e){
            throw new JSONParseException("解析我的收藏列表失败", e);
        }
    }

    /**
     * 私有化构造函数
     */
    private FavouriteServiceImpl(){}
}
