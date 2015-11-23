package com.yilos.nailstar.aboutme.favourite.entity;

import java.util.Date;

/**
 * Created by yangdan on 15/11/23.
 */
public class FavouriteTopic {
    private String collectionId;

    private String topicId;

    private String thumbUrl;

    private String title;

    private String author;

    private Date createDate;

    private String authorPhotoUrl;

    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getAuthorPhotoUrl() {
        return authorPhotoUrl;
    }

    public void setAuthorPhotoUrl(String photoUrl) {
        this.authorPhotoUrl = photoUrl;
    }
}
