package com.yilos.nailstar.aboutme.entity;

/**
 * Created by sisilai on 15/10/30.
 */
public class PersonInfo {
    /**
     * uid : a8affd60-efe6-11e4-a908-3132fc2abe39
     * nickname : Lolo
     * type : 6
     * photoUrl : http://pic.yilos.com/7e9ab6e7981380efb88c8ee19ecd0269
     * profile :
     */

    private String uid;
    private String nickname;
    private int type;
    private String photoUrl;
    private String profile;

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getUid() {
        return uid;
    }

    public String getNickname() {
        return nickname;
    }

    public int getType() {
        return type;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getProfile() {
        return profile;
    }
}
