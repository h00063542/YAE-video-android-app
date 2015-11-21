package com.yilos.nailstar.aboutme.entity;

/**
 * Created by sisilai on 15/11/18.
 */
public class SystemMessage {
    public SystemMessage(String content, boolean hasBeenRead, String id, long publishDate, String title, String topicId) {
        this.content = content;
        this.hasBeenRead = hasBeenRead;
        this.id = id;
        this.publishDate = publishDate;
        this.title = title;
        this.topicId = topicId;
    }

    /**
     * id : d2f3cdc0-8f7f-11e5-ab99-1f385dee6318
     * title : 美甲大咖团队
     * content : 超级实用的纵向晕染技巧
     * publishDate : 1448021486748
     * topicId : 39f830b0-8f3a-11e5-ab99-1f385dee6318
     */

    private String id;
    private String title;
    private String content;
    private long publishDate;
    private String topicId;
    private boolean hasBeenRead;

    public boolean getHasBeenRead() {
        return hasBeenRead;
    }

    public void setHasBeenRead(boolean hasBeenRead) {
        this.hasBeenRead = hasBeenRead;
    }

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

    public void setTopicId(String topicId) {
        this.topicId = topicId;
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

    public String getTopicId() {
        return topicId;
    }
}
