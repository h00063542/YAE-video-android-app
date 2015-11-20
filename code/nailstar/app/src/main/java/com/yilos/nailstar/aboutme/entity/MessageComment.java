package com.yilos.nailstar.aboutme.entity;

/**
 * Created by sisilai on 15/11/20.
 */
public class MessageComment {

    /**
     * author : djfafdfalfafk
     * atUser : fdafsdfafsf
     * content : 我也来评论一句
     * contentPic : http://st.yilos.com/pic/dalfjlafjafdfrere.png
     * replyTo : fdasjfkajdfkarere
     * lastReplyTo : adsfafasfasfasf
     * score : 1
     * ready : 0
     */

    private String author;
    private String atUser;
    private String content;
    private String contentPic;
    private String replyTo;
    private String lastReplyTo;
    private int score;
    private int ready;

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setAtUser(String atUser) {
        this.atUser = atUser;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setContentPic(String contentPic) {
        this.contentPic = contentPic;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public void setLastReplyTo(String lastReplyTo) {
        this.lastReplyTo = lastReplyTo;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setReady(int ready) {
        this.ready = ready;
    }

    public String getAuthor() {
        return author;
    }

    public String getAtUser() {
        return atUser;
    }

    public String getContent() {
        return content;
    }

    public String getContentPic() {
        return contentPic;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public String getLastReplyTo() {
        return lastReplyTo;
    }

    public int getScore() {
        return score;
    }

    public int getReady() {
        return ready;
    }
}
