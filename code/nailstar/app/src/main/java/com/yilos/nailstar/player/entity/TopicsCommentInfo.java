package com.yilos.nailstar.player.entity;

import java.util.List;

/**
 * Created by yilos on 2015-10-27.
 */
public class TopicsCommentInfo {
    private String id;
    private String userId;
    private String author;
    private String authorPhoto;
    private String content;
    private String contentPic;
    private long createDate;
    private int isHomework;
    private int isMine;
    private int status;
    private TopicsCommentAtInfo at;
    private List<TopicsCommentReplyInfo> replies;

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

    public int getIsMine() {
        return isMine;
    }

    public void setIsMine(int isMine) {
        this.isMine = isMine;
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

    public List<TopicsCommentReplyInfo> getReplies() {
        return replies;
    }

    public void setReplies(List<TopicsCommentReplyInfo> replies) {
        this.replies = replies;
    }
}
