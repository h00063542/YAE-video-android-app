package com.yilos.nailstar.index.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by yangdan on 15/10/16.
 * 主题信息
 */
public class Topic implements Serializable {
    /**
     * 主题ID
     */
    private String topicId;

    private String thumbUrl;

    /**
     * 标题
     */
    private String title;

    /**
     * 作者
     */
    private String author;

    /**
     * 作者头像地址
     */
    private String authorPhoto;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Topic topic = (Topic) o;

        if (playTimes != topic.playTimes) return false;
        if (!topicId.equals(topic.topicId)) return false;
        if (thumbUrl != null ? !thumbUrl.equals(topic.thumbUrl) : topic.thumbUrl != null)
            return false;
        if (!title.equals(topic.title)) return false;
        if (!author.equals(topic.author)) return false;
        if (!authorPhoto.equals(topic.authorPhoto)) return false;
        if (!photoUrl.equals(topic.photoUrl)) return false;
        return createDate.equals(topic.createDate);

    }

    @Override
    public int hashCode() {
        int result = topicId.hashCode();
        result = 31 * result + (thumbUrl != null ? thumbUrl.hashCode() : 0);
        result = 31 * result + title.hashCode();
        result = 31 * result + author.hashCode();
        result = 31 * result + authorPhoto.hashCode();
        result = 31 * result + photoUrl.hashCode();
        result = 31 * result + createDate.hashCode();
        result = 31 * result + playTimes;
        return result;
    }

    /**
     * 主题图片地址
     */
    private String photoUrl;

    /**
     * 创建日期
     */
    private Date createDate;

    /**
     * 播放次数
     */
    private int playTimes;

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorPhoto(){
        return authorPhoto;
    }

    public void setAuthorPhoto(String authorPhoto) {
        this.authorPhoto = authorPhoto;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getPlayTimes() {
        return playTimes;
    }

    public void setPlayTimes(int playTimes) {
        this.playTimes = playTimes;
    }
}
