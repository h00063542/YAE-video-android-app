package com.yilos.nailstar.aboutme.requirelesson.entity;

/**
 * Created by sisilai on 15/11/23.
 */
public class RequireLesson {

    public RequireLesson(long createDate, int no, String thumbUrl) {
        this.createDate = createDate;
        this.no = no;
        this.thumbUrl = thumbUrl;
    }

    /**
     * no : 21
     * thumbUrl : http://pic.yilos.com/f5a60719425c62c4c7c494fc96d2e787
     * createDate : 1437989431228
     */

    private int no;
    private String thumbUrl;
    private long createDate;

    public void setNo(int no) {
        this.no = no;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public int getNo() {
        return no;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public long getCreateDate() {
        return createDate;
    }
}
