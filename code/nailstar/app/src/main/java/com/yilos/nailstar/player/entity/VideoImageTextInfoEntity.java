package com.yilos.nailstar.player.entity;

import java.util.ArrayList;

/**
 * Created by yilos on 2015-10-22.
 */
public class VideoImageTextInfoEntity {
    private String id;
    private ArrayList articles;
    private ArrayList pictures;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList getArticles() {
        return articles;
    }

    public void setArticles(ArrayList articles) {
        this.articles = articles;
    }

    public ArrayList getPictures() {
        return pictures;
    }

    public void setPictures(ArrayList pictures) {
        this.pictures = pictures;
    }
}
