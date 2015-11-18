package com.yilos.nailstar.aboutme.entity;

/**
 * Created by sisilai on 15/11/18.
 */
public class UserMessage {

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

    public static class CommentEntity {
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

    public static class ReplyEntity {
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
