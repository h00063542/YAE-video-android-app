package com.yilos.nailstar.index.model;

import com.yilos.nailstar.framework.entity.NailStarApplicationContext;
import com.yilos.nailstar.framework.exception.CommonException;
import com.yilos.nailstar.framework.exception.JSONParseException;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.index.entity.Category;
import com.yilos.nailstar.index.entity.IndexContent;
import com.yilos.nailstar.index.entity.Poster;
import com.yilos.nailstar.index.entity.Topic;
import com.yilos.nailstar.util.HttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yangdan on 15/10/16.
 */
public class IndexServiceImpl implements IndexService{

    @Override
    public IndexContent getIndexContentFromCache() {
        return readIndexContent();
    }

    @Override
    public IndexContent getIndexContentFromNet() throws NetworkDisconnectException, JSONParseException {
        if(!NailStarApplicationContext.getInstance().isNetworkConnected()){
            throw new NetworkDisconnectException("网络没有连接");
        }

        final IndexContent indexContent = new IndexContent();
        indexContent.setPosters(getIndexPostersFromNet());
        indexContent.setCategories(getIndexCategoriesFromNet());
        indexContent.setHotestTopics(getHotestTopicFromNet(1));
        indexContent.setLatestTopics(getLatestTopicFromNet(1));

        return indexContent;
    }

    /**
     * 读取轮播图列表
     * @return
     * @throws NetworkDisconnectException
     * @throws JSONParseException
     */
    @Override
    public List<Poster> getIndexPostersFromNet() throws NetworkDisconnectException, JSONParseException{
        String posterStrResult = null;
        try {
            posterStrResult = HttpClient.getJson("/vapi/nailstar/posters");
        } catch (IOException e) {
            throw new NetworkDisconnectException("读取轮播图列表失败", e);
        } catch (Exception e){
            throw new NetworkDisconnectException("读取轮播图列表失败", e);
        }

        try{
            JSONObject jsonResultObject = new JSONObject(posterStrResult);
            if(jsonResultObject.getInt("code") != 0){
                return null;
            }

            JSONArray posterJsonArray = jsonResultObject.getJSONObject("result").getJSONArray("posters");

            List<Poster> result = new ArrayList<Poster>(8);
            for(int i = 0, length = posterJsonArray.length(); i < length; i++) {
                JSONObject posterObj = posterJsonArray.getJSONObject(i);
                Poster poster = new Poster();
                poster.setPicUrl(posterObj.getString("picUrl"));
                poster.setActivityUrl(posterObj.getString("activityUrl"));
                poster.setTopicId(posterObj.getString("topicId"));
                poster.setType(posterObj.getString("type"));

                if("video".equals(poster.getType())) {
                    result.add(poster);
                }
            }

            return result;
        }
        catch (JSONException e){
            throw new JSONParseException("解析轮播图列表失败", e);
        }
    }

    @Override
    public List<Topic> getLatestTopicFromNet(int page) throws NetworkDisconnectException, JSONParseException{
        String latestTopicStringResult = null;
        try {
            latestTopicStringResult = HttpClient.getJson("/vapi/nailstar/moreTopics?type=latest&page=" + page + "&perPage=" + PAGE_SIZE);
        } catch (IOException e) {
            throw new NetworkDisconnectException("读取最新视频列表失败", e);
        } catch (Exception e){
            throw new NetworkDisconnectException("读取最新视频列表失败", e);
        }
        try{
            JSONObject jsonResultObject = new JSONObject(latestTopicStringResult);
            if(jsonResultObject.getInt("code") != 0){
                return null;
            }

            JSONArray latestTopicJsonArray = jsonResultObject.getJSONObject("result").getJSONArray("topics");

            List<Topic> result = new ArrayList<Topic>(8);
            for(int i = 0, length = latestTopicJsonArray.length(); i < length; i++) {
                JSONObject topicObj = latestTopicJsonArray.getJSONObject(i);
                Topic topic = new Topic();
                topic.setThumbUrl(topicObj.getString("thumbUrl"));
                topic.setSmallThumbUrl(topicObj.getString("smallThumbUrl"));
                topic.setTopicId(topicObj.getString("topicId"));
                topic.setTitle(topicObj.getString("title"));
                topic.setAuthor(topicObj.getString("author"));
                topic.setCreateDate(new Date(topicObj.getLong("createDate")));
                topic.setAuthorPhoto(topicObj.getString("photoUrl"));
                topic.setPlayTimes(topicObj.getInt("playTimes"));
                result.add(topic);
            }

            return result;
        }
        catch (JSONException e){
            throw new JSONParseException("解析最新视频列表失败", e);
        }
    }

    @Override
    public List<Topic> getHotestTopicFromNet(int page) throws NetworkDisconnectException, JSONParseException{
        String hotestTopicStrResult = null;
        try {
            hotestTopicStrResult = HttpClient.getJson("/vapi/nailstar/moreTopics?type=hotest&page=" + page + "&perPage=" + PAGE_SIZE);
        } catch (IOException e) {
            throw new NetworkDisconnectException("读取最热视频列表失败", e);
        } catch (Exception e){
            throw new NetworkDisconnectException("读取最热视频列表失败", e);
        }

        try{
            JSONObject jsonResultObject = new JSONObject(hotestTopicStrResult);
            if(jsonResultObject.getInt("code") != 0){
                return null;
            }

            JSONArray hotestTopicJsonArray = jsonResultObject.getJSONObject("result").getJSONArray("topics");

            List<Topic> result = new ArrayList<Topic>(8);
            for(int i = 0, length = hotestTopicJsonArray.length(); i < length; i++) {
                JSONObject topicObj = hotestTopicJsonArray.getJSONObject(i);
                Topic topic = new Topic();
                topic.setThumbUrl(topicObj.getString("thumbUrl"));
                topic.setSmallThumbUrl(topicObj.getString("smallThumbUrl"));
                topic.setTopicId(topicObj.getString("topicId"));
                topic.setTitle(topicObj.getString("title"));
                topic.setAuthor(topicObj.getString("author"));
                topic.setCreateDate(new Date(topicObj.getLong("createDate")));
                topic.setAuthorPhoto(topicObj.getString("photoUrl"));
                topic.setPlayTimes(topicObj.getInt("playTimes"));
                result.add(topic);
            }

            return result;
        }
        catch (JSONException e){
            throw new JSONParseException("解析最热视频列表失败", e);
        }
    }

    @Override
    public List<Topic> getWatchTopicFromNet(String uid, int page) throws NetworkDisconnectException, JSONParseException, CommonException {
        String watchTopicStrResult = null;
        try {
            watchTopicStrResult = HttpClient.getJson("/vapi/nailstar/topics/getFollowTopics/" + uid + "?page=" + page + "&perPage=" + PAGE_SIZE);
        } catch (IOException e) {
            throw new NetworkDisconnectException("读取关注视频列表失败", e);
        } catch (Exception e){
            throw new NetworkDisconnectException("读取关注视频列表失败", e);
        }

        try{
            JSONObject jsonResultObject = new JSONObject(watchTopicStrResult);
            if(jsonResultObject.getInt("code") != 0){
                if(jsonResultObject.getInt("code") == 1 && "该用户还没关注过其他用户".equals(jsonResultObject.getString("messages"))) {
                    throw new CommonException("该用户还没关注过其他用户");
                }
                return null;
            }

            JSONArray watchTopicJsonArray = jsonResultObject.getJSONObject("result").getJSONArray("topics");

            List<Topic> result = new ArrayList<Topic>(8);
            for(int i = 0, length = watchTopicJsonArray.length(); i < length; i++) {
                JSONObject topicObj = watchTopicJsonArray.getJSONObject(i);
                Topic topic = new Topic();
                topic.setThumbUrl(topicObj.getString("thumbUrl"));
                topic.setSmallThumbUrl(topicObj.getString("smallThumbUrl"));
                topic.setTopicId(topicObj.getString("topicId"));
                topic.setTitle(topicObj.getString("title"));
                topic.setAuthor(topicObj.getString("author"));
                topic.setCreateDate(new Date(topicObj.getLong("createDate")));
                topic.setAuthorPhoto(topicObj.getString("photoUrl"));
                topic.setPlayTimes(topicObj.getInt("playTimes"));
                result.add(topic);
            }

            return result;
        }
        catch (JSONException e){
            throw new JSONParseException("解析关注视频列表失败", e);
        }
    }

    @Override
    public List<Category> getIndexCategoriesFromNet() throws NetworkDisconnectException, JSONParseException{
        String categoryStrResult = null;
        try {
            categoryStrResult = HttpClient.getJson("/vapi/nailstar/categories");
        } catch (IOException e) {
            throw new NetworkDisconnectException("读取主题分类列表失败", e);
        } catch (Exception e){
            throw new NetworkDisconnectException("读取主题分类列表失败", e);
        }

        try{
            JSONObject jsonResultObject = new JSONObject(categoryStrResult);
            if(jsonResultObject.getInt("code") != 0){
                return null;
            }

            JSONArray categoryJsonArray = jsonResultObject.getJSONObject("result").getJSONArray("categories");

            List<Category> result = new ArrayList<Category>(8);
            for(int i = 0, length = categoryJsonArray.length(); i < length; i++) {
                JSONObject categoryObj = categoryJsonArray.getJSONObject(i);
                Category category = new Category();
                category.setId(categoryObj.getString("id"));
                category.setName(categoryObj.getString("name"));
                category.setPicUrl(categoryObj.getString("home_menu_icon"));
                result.add(category);
            }

            return result;
        }
        catch (JSONException e){
            throw new JSONParseException("解析主题分类列表失败", e);
        }
    }

    private IndexContent readIndexContent() {
        File cacheDir = NailStarApplicationContext.getInstance().getExternalCacheDir();
        File indexFile = new File(cacheDir, "indexContent.out");
        IndexContent indexContent = null;
        if(!indexFile.exists()){
            return new IndexContent();
        }

        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try
        {
            fis = new FileInputStream(indexFile);
            ois = new ObjectInputStream(fis);
            indexContent = (IndexContent) ois.readObject();
            fis.close();
        }
        catch (Exception e)
        {
            // TODO e.printStackTrace();
            indexContent = new IndexContent();
        }
        finally {
            try {
                if(fis != null){
                    fis.close();
                }
            } catch (IOException e) {
            }
        }

        return indexContent;
    }

    /**
     * 将首页对象输出到文件
     */
    @Override
    public synchronized void saveIndexContent(IndexContent indexContent) {
        File cacheDir = NailStarApplicationContext.getInstance().getExternalCacheDir();
        File indexFile = new File(cacheDir, "indexContent.out");

        FileOutputStream fos = null;
        try
        {
            fos = new FileOutputStream(indexFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(indexContent);
        }
        catch (Exception e)
        {
            // TODO
        }
        finally {
            try {
                if(null != fos){
                    fos.close();
                }
            } catch (IOException e) {
            }
        }
    }
}
