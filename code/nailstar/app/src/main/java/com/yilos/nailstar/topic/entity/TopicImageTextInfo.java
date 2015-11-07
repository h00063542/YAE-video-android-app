package com.yilos.nailstar.topic.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by yilos on 2015-10-22.
 */
public class TopicImageTextInfo implements Serializable {
    private String id;
    private ArrayList<String> articles;
    private ArrayList<String> pictures;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getArticles() {
        return articles;
    }

    public void setArticles(ArrayList<String> articles) {
        this.articles = articles;
    }

    public ArrayList<String> getPictures() {
        return pictures;
    }

    public void setPictures(ArrayList<String> pictures) {
        this.pictures = pictures;
    }
}
