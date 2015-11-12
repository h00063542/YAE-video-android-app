package com.yilos.nailstar.topic.model;

import java.util.ArrayList;

/**
 * Created by yilos on 2015-11-12.
 */
public class UpdateReadyInfo {
    private String id;
    private String table;
    private ArrayList<String> picUrls;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public ArrayList<String> getPicUrls() {
        return picUrls;
    }

    public void setPicUrls(ArrayList<String> picUrls) {
        this.picUrls = picUrls;
    }
}
