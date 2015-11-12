package com.yilos.nailstar.topic.entity;

/**
 * Created by yilos on 2015-11-11.
 */
public class AddCommentInfo {
    // 主题Id
    private String topicId;
    // 当前登录用户Id
    private String userId;
    // at用户Id
    private String atUserId;
    // 评论或回复内容
    private String content;
    // 交作业图片
    private String contentPic;
    // commentId
    private String replayTo;
    // replayId
    private String lastReplayTO;
    // 0交作业，1评论
    private int ready;

    // 文件本地路径，上传文件到Oss使用
    private String picLocalPath;
    // 文件名称，上传文件到Oss使用
    private String picName;

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAtUserId() {
        return atUserId;
    }

    public void setAtUserId(String atUserId) {
        this.atUserId = atUserId;
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

    public String getReplayTo() {
        return replayTo;
    }

    public void setReplayTo(String replayTo) {
        this.replayTo = replayTo;
    }

    public String getLastReplayTO() {
        return lastReplayTO;
    }

    public void setLastReplayTO(String lastReplayTO) {
        this.lastReplayTO = lastReplayTO;
    }

    public int getReady() {
        return ready;
    }

    public void setReady(int ready) {
        this.ready = ready;
    }

    public String getPicLocalPath() {
        return picLocalPath;
    }

    public void setPicLocalPath(String picLocalPath) {
        this.picLocalPath = picLocalPath;
    }

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }
}
