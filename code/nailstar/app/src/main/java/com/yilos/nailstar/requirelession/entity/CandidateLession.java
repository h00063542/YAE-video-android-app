package com.yilos.nailstar.requirelession.entity;

import java.io.Serializable;

/**
 * 候选教程
 */
public class CandidateLession implements Serializable {

    /**
     * 候选教程Id
     */
    private String candidateId;

    /**
     * 候选教程的图片地址
     */
    private String picUrl;

    /**
     * 上传候选教程的用户名
     */
    private String authorName;

    /**
     * 上传候选教程的用户头像
     */
    private String authorPhoto;

    /**
     * 候选教程的支持票数
     */
    private int voteCount;

    /**
     * 是否已经为该候选教程投票
     */
    private int voted;

    /**
     * 候选教程的创建时间
     */
    private long createDate;

    public String getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorPhoto() {
        return authorPhoto;
    }

    public void setAuthorPhoto(String authorPhoto) {
        this.authorPhoto = authorPhoto;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public int getVoted() {
        return voted;
    }

    public void setVoted(int voted) {
        this.voted = voted;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }
}
