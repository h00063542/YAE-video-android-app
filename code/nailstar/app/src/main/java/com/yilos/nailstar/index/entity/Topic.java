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

    /**
     * 视频图片地址
     */
    private String thumbUrl;

    /**
     * 视频缩略图地址
     */
    private String smallThumbUrl;

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

    /**
     * 创建日期
     */
    private Date createDate;

    /**
     * 播放次数
     */
    private int playTimes;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Topic topic = (Topic) o;

        if (!topicId.equals(topic.topicId)) return false;
        if (thumbUrl != null ? !thumbUrl.equals(topic.thumbUrl) : topic.thumbUrl != null)
            return false;
        if (smallThumbUrl != null ? !smallThumbUrl.equals(topic.smallThumbUrl) : topic.smallThumbUrl != null)
            return false;
        if (title != null ? !title.equals(topic.title) : topic.title != null) return false;
        if (author != null ? !author.equals(topic.author) : topic.author != null) return false;
        if (authorPhoto != null ? !authorPhoto.equals(topic.authorPhoto) : topic.authorPhoto != null)
            return false;
        return !(createDate != null ? !createDate.equals(topic.createDate) : topic.createDate != null);

    }

    @Override
    public int hashCode() {
        int result = topicId.hashCode();
        result = 31 * result + (thumbUrl != null ? thumbUrl.hashCode() : 0);
        result = 31 * result + (smallThumbUrl != null ? smallThumbUrl.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (authorPhoto != null ? authorPhoto.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        return result;
    }

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

    public String getSmallThumbUrl() {
        return smallThumbUrl;
    }

    public void setSmallThumbUrl(String smallThumbUrl) {
        this.smallThumbUrl = smallThumbUrl;
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

    public String getAuthorPhoto() {
        return authorPhoto;
    }

    public void setAuthorPhoto(String authorPhoto) {
        this.authorPhoto = authorPhoto;
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
