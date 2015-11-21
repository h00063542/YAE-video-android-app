package com.yilos.nailstar.mall.entity;

/**
 * Created by ganyue on 15/11/21.
 */
public class MallIndexBanner {
    private String activity_url;
    private int height;
    private int width;

    public int getWidth() {
        return width;
    }

    public MallIndexBanner(String activity_url, int height, int width) {
        this.activity_url = activity_url;
        this.height = height;
        this.width = width;
    }

    public void setWidth(int width) {
        this.width = width;

    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getActivity_url() {
        return activity_url;
    }

    public void setActivity_url(String activity_url) {
        this.activity_url = activity_url;
    }


}
