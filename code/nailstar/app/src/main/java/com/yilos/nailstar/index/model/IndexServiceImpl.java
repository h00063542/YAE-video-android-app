package com.yilos.nailstar.index.model;

import com.yilos.nailstar.framework.entity.NailStarApplicationContext;
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
    /**
     * 首页内容缓存
     */
    private IndexContent indexContent = readIndexContent();

    @Override
    public IndexContent getIndexContent(){
        try {
            IndexContent indexContent = getIndexContentFromNet();
            return indexContent;
        } catch (NetworkDisconnectException e) {
//            e.printStackTrace();
        } catch (JSONParseException e) {
//            e.printStackTrace();
        }

        return indexContent;
    }

    @Override
    public IndexContent getIndexContentFromNet() throws NetworkDisconnectException, JSONParseException {
        if(!NailStarApplicationContext.getInstance().isNetworkConnected()){
            throw new NetworkDisconnectException("网络没有连接");
        }

        final IndexContent indexContent = new IndexContent();
        indexContent.setPosters(getIndexPosterFromNet());
        indexContent.setCategories(getCategoryFromNet());
        indexContent.setHotestTopics(getHotestTopicFromNet());
        indexContent.setLatestTopics(getLatestTopicFromNet());

        return indexContent;
    }

    @Override
    public List<Poster> getIndexPosters(){
        try {
            List<Poster> posters = getIndexPosterFromNet();
            return posters;
        } catch (NetworkDisconnectException e) {
            //e.printStackTrace();
        } catch (JSONParseException e) {
            //e.printStackTrace();
        }

        return indexContent.getPosters();
    }

    @Override
    public List<Topic> getLatestTopic(){
        try {
            List<Topic> topics = getLatestTopicFromNet();
            return topics;
        } catch (NetworkDisconnectException e) {
            //e.printStackTrace();
        } catch (JSONParseException e) {
            //e.printStackTrace();
        }

        return indexContent.getLatestTopics();
    }

    @Override
    public List<Topic> getHotestTopic(){
        try {
            List<Topic> topics = getHotestTopicFromNet();
            return topics;
        } catch (NetworkDisconnectException e) {
            //e.printStackTrace();
        } catch (JSONParseException e) {
            //e.printStackTrace();
        }

        return indexContent.getHotestTopics();
    }

    @Override
    public List<Category> getIndexCategories(){
        try {
            List<Category> categories = getCategoryFromNet();

            return categories;
        } catch (NetworkDisconnectException e) {
//            e.printStackTrace();
        } catch (JSONParseException e) {
//            e.printStackTrace();
        }

        return indexContent.getCategories();
    }

    /**
     * 读取轮播图列表
     * @return
     * @throws NetworkDisconnectException
     * @throws JSONParseException
     */
    private List<Poster> getIndexPosterFromNet() throws NetworkDisconnectException, JSONParseException{
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
                result.add(poster);
            }

            return result;
        }
        catch (JSONException e){
            throw new JSONParseException("解析轮播图列表失败", e);
        }
    }

    private List<Topic> getLatestTopicFromNet() throws NetworkDisconnectException, JSONParseException{
        String latestTopicStringResult = null;
        try {
            latestTopicStringResult = HttpClient.getJson("/vapi/nailstar/latestTopic");
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
                topic.setTopicId(topicObj.getString("topicId"));
                topic.setAuthor(topicObj.getString("authorName"));
                topic.setCreateDate(new Date(topicObj.getLong("time")));
                topic.setAuthorPhoto(topicObj.getString("authorPhoto"));
                topic.setPhotoUrl(topicObj.getString("picUrl"));
                result.add(topic);
            }

            return result;
        }
        catch (JSONException e){
            throw new JSONParseException("解析最新视频列表失败", e);
        }
    }

    private List<Topic> getHotestTopicFromNet() throws NetworkDisconnectException, JSONParseException{
        String hotestTopicStrResult = null;
        try {
            hotestTopicStrResult = HttpClient.getJson("/vapi/nailstar/hotestTopic");
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
                topic.setTopicId(topicObj.getString("topicId"));
                topic.setAuthor(topicObj.getString("authorName"));
                topic.setAuthorPhoto(topicObj.getString("authorPhoto"));
                topic.setPhotoUrl(topicObj.getString("picUrl"));
                topic.setPlayTimes(topicObj.getInt("playTimes"));
                result.add(topic);
            }

            return result;
        }
        catch (JSONException e){
            throw new JSONParseException("解析最热视频列表失败", e);
        }
    }

    private List<Category> getCategoryFromNet() throws NetworkDisconnectException, JSONParseException{
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
                category.setPicUrl(categoryObj.getString("pic_url"));
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
    public synchronized void saveIndexContent() {
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
