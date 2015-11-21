package com.yilos.nailstar.aboutme.entity;

import com.yilos.nailstar.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by sisilai on 15/11/18.
 */
public class UserMessage implements Serializable{


    public static CommentEntity parseCommentEntity(JSONObject commentJson) throws JSONException{
        String atName = commentJson.getString(Constants.ATNAME);
        String content = commentJson.getString(Constants.CONTENT);
        long createDate = commentJson.getLong(Constants.CREATE_DATE);
        int isHomework = commentJson.getInt(Constants.IS_HOME_WORK);

        CommentEntity commentEntity = new CommentEntity(atName, content, createDate, isHomework);
        return commentEntity;
    }



    /**
     * accountId : d77348c0-60d7-11e5-ade9-e3d220e2c964
     * accountName : 勿忘我
     * accountPhoto : http://pic.yilos.com/ec9a2bbc1abb13166af6da31495bea0b
     * content : 嗯嗯
     * createDate : 1446718910072
     * replyTo : 9f59f430-8390-11e5-a74c-839a83b22973
     * lastReplyTo : b1438670-8390-11e5-a74c-839a83b22973
     */
    public static ReplyEntity parseReplyEntity(JSONObject replyJson) throws JSONException{
        String accountId = replyJson.getString(Constants.ACCOUNTID);
        String accountName = replyJson.getString(Constants.ACCOUNTNAME);
        String accountPhoto = replyJson.getString(Constants.ACCOUNTPHOTO);
        String content = replyJson.getString(Constants.CONTENT);
        long createDate = replyJson.getLong(Constants.CREATE_DATE);
        String lastReplyTo = replyJson.getString(Constants.LAST_REPLY_TO);
        String replyTo = replyJson.getString(Constants.REPLY_TO);
        ReplyEntity replyEntity = new ReplyEntity(accountId, accountName, accountPhoto, content,createDate, lastReplyTo, replyTo);
        return replyEntity;
    }

    public UserMessage() {

    }

    public UserMessage(CommentEntity comment, String id, ReplyEntity reply, String teacher, String thumbUrl, String title, String topicId, boolean hasBeenReply) {
        this.comment = comment;
        this.id = id;
        this.reply = reply;
        this.teacher = teacher;
        this.thumbUrl = thumbUrl;
        this.title = title;
        this.topicId = topicId;
        this.hasBeenReply = hasBeenReply;
    }

    /**
     * id : 06a79050-83a7-11e5-8027-61091fab3b57
     * topicId : c8a5cfa0-312c-11e5-b553-d7e515311f8d
     * title : 第222期：牛仔贴纸
     * teacher : 董亚坡老师
     * thumbUrl : http://pic.yilos.com/283be79417e0a310eb9ed95a13a71d43
     * comment : {"content":"这尽职尽责尽职尽责","atName":"在","createDate":1446709317872,"isHomework":1}
     * reply : {"accountId":"d77348c0-60d7-11e5-ade9-e3d220e2c964","accountName":"勿忘我","accountPhoto":"http://pic.yilos.com/ec9a2bbc1abb13166af6da31495bea0b","content":"嗯嗯","createDate":1446718910072,"replyTo":"9f59f430-8390-11e5-a74c-839a83b22973","lastReplyTo":"b1438670-8390-11e5-a74c-839a83b22973"}
     */

    private String id;
    private String topicId;
    private String title;
    private String teacher;
    private String thumbUrl;
    /**
     * content : 这尽职尽责尽职尽责
     * atName : 在
     * createDate : 1446709317872
     * isHomework : 1
     */

    private CommentEntity comment;

    /**
     * accountId : d77348c0-60d7-11e5-ade9-e3d220e2c964
     * accountName : 勿忘我
     * accountPhoto : http://pic.yilos.com/ec9a2bbc1abb13166af6da31495bea0b
     * content : 嗯嗯
     * createDate : 1446718910072
     * replyTo : 9f59f430-8390-11e5-a74c-839a83b22973
     * lastReplyTo : b1438670-8390-11e5-a74c-839a83b22973
     */

    private ReplyEntity reply;
    private boolean hasBeenReply;

    public void setHasBeenReply(boolean hasBeenReply) {
        this.hasBeenReply = hasBeenReply;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public void setComment(CommentEntity comment) {
        this.comment = comment;
    }

    public void setReply(ReplyEntity reply) {
        this.reply = reply;
    }

    public boolean getHasBeenReply() {
        return hasBeenReply;
    }

    public String getId() {
        return id;
    }

    public String getTopicId() {
        return topicId;
    }

    public String getTitle() {
        return title;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public CommentEntity getComment() {
        return comment;
    }

    public ReplyEntity getReply() {
        return reply;
    }

    public static class CommentEntity implements Serializable{
        public CommentEntity(String atName, String content, long createDate, int isHomework) {
            this.atName = atName;
            this.content = content;
            this.createDate = createDate;
            this.isHomework = isHomework;
        }

        private String content;
        private String atName;
        private long createDate;
        private int isHomework;

        public void setContent(String content) {
            this.content = content;
        }

        public void setAtName(String atName) {
            this.atName = atName;
        }

        public void setCreateDate(long createDate) {
            this.createDate = createDate;
        }

        public void setIsHomework(int isHomework) {
            this.isHomework = isHomework;
        }

        public String getContent() {
            return content;
        }

        public String getAtName() {
            return atName;
        }

        public long getCreateDate() {
            return createDate;
        }

        public int getIsHomework() {
            return isHomework;
        }
    }

    public static class ReplyEntity implements Serializable{
        public ReplyEntity(String accountId, String accountName, String accountPhoto, String content, long createDate, String lastReplyTo, String replyTo) {
            this.accountId = accountId;
            this.accountName = accountName;
            this.accountPhoto = accountPhoto;
            this.content = content;
            this.createDate = createDate;
            this.lastReplyTo = lastReplyTo;
            this.replyTo = replyTo;
        }

        private String accountId;
        private String accountName;
        private String accountPhoto;
        private String content;
        private long createDate;
        private String replyTo;
        private String lastReplyTo;

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }

        public void setAccountName(String accountName) {
            this.accountName = accountName;
        }

        public void setAccountPhoto(String accountPhoto) {
            this.accountPhoto = accountPhoto;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setCreateDate(long createDate) {
            this.createDate = createDate;
        }

        public void setReplyTo(String replyTo) {
            this.replyTo = replyTo;
        }

        public void setLastReplyTo(String lastReplyTo) {
            this.lastReplyTo = lastReplyTo;
        }

        public String getAccountId() {
            return accountId;
        }

        public String getAccountName() {
            return accountName;
        }

        public String getAccountPhoto() {
            return accountPhoto;
        }

        public String getContent() {
            return content;
        }

        public long getCreateDate() {
            return createDate;
        }

        public String getReplyTo() {
            return replyTo;
        }

        public String getLastReplyTo() {
            return lastReplyTo;
        }
    }
}
