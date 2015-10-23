package com.yilos.nailstar.index.entity;

/**
 * Created by yangdan on 15/10/16.
 * 主题分类
 */
public class Category {
    /**
     * ID
     */
    private String id;

    /**
     * 图片地址
     */
    private String picUrl;

    /**
     * 分类名称
     */
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
