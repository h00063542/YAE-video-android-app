package com.yilos.nailstar.aboutme.entity;

/**
 * Created by sisilai on 15/11/4.
 */

public class MyData {
    private String dataString;
    private int imageId;

    public MyData(String dataString, int imageId) {
        // TODO Auto-generated constructor stub
        this.dataString = dataString;
        this.imageId = imageId;
    }

    public String getDataString() {
        return dataString;
    }

    public int getImageId() {
        return imageId;
    }
}