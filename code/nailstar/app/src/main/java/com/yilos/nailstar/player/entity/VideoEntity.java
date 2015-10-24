package com.yilos.nailstar.player.entity;

/**
 * Created by yilos on 2015-10-22.
 */
public class VideoEntity {

    private String videoId;
    private int playTimes;
    private String ccUrl;
    private String ossUrl;

    public VideoEntity() {
    }

    public VideoEntity(String videoId, int playTimes, String ccUrl, String ossUrl) {
        this.videoId = videoId;
        this.playTimes = playTimes;
        this.ccUrl = ccUrl;
        this.ossUrl = ossUrl;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public int getPlayTimes() {
        return playTimes;
    }

    public void setPlayTimes(int playTimes) {
        this.playTimes = playTimes;
    }

    public String getCcUrl() {
        return ccUrl;
    }

    public void setCcUrl(String ccUrl) {
        this.ccUrl = ccUrl;
    }

    public String getOssUrl() {
        return ossUrl;
    }

    public void setOssUrl(String ossUrl) {
        this.ossUrl = ossUrl;
    }
}
