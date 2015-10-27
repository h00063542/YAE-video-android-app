package com.yilos.nailstar.player.entity;

/**
 * Created by yilos on 2015-10-27.
 */
public class TopicsCommentReplyInfo {
    private String id;
    private String userId;
    private String nickname;
    private String author;
    private String content;
    private String contentPic;
    private long createDate;
    private int isHomework;
    private int status;
    private TopicsCommentAtInfo at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentPic() {
        return contentPic;
    }

    public void setContentPic(String contentPic) {
        this.contentPic = contentPic;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public int getIsHomework() {
        return isHomework;
    }

    public void setIsHomework(int isHomework) {
        this.isHomework = isHomework;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public TopicsCommentAtInfo getAt() {
        return at;
    }

    public void setAt(TopicsCommentAtInfo at) {
        this.at = at;
    }
}
