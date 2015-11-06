package com.yilos.nailstar.util;

/**
 * Created by yilos on 2015-11-06.
 */
public class UserInfo {

    public UserInfo() {

    }

    public UserInfo(String userId, String nickName, String photo) {
        this.userId = userId;
        this.nickName = nickName;
        this.photo = photo;
    }

    private String userId;
    private String nickName;
    private String photo;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
