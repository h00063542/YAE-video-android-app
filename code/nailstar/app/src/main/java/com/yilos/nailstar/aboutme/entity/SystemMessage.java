package com.yilos.nailstar.aboutme.entity;

/**
 * Created by sisilai on 15/11/18.
 */
public class SystemMessage {
    /**
     * id : bd11b8f0-398f-11e5-8cae-ab85db13041f
     * title : 1
     * content : 1
     * publishDate : 1447749989397
     * postId :
     */

    private String id;
    private String title;
    private String content;
    private long publishDate;
    private String postId;

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setPublishDate(long publishDate) {
        this.publishDate = publishDate;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public long getPublishDate() {
        return publishDate;
    }

    public String getPostId() {
        return postId;
    }
}
