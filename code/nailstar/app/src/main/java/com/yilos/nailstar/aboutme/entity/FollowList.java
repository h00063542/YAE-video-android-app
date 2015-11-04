package com.yilos.nailstar.aboutme.entity;

/**
 * Created by sisilai on 15/11/4.
 */
public class FollowList {
    /**
     * nickname : niuniu
     * type : 5
     * photoUrl : http://pic.yilos.com/YYYYYYYYY
     * accountId : asdf-123456-ghjkl
     * profile : TA很懒，什么也没有说
     */

    private String nickname;
    private int type;
    private String photoUrl;
    private String accountId;
    private String profile;

    public FollowList(String accountId, String nickname, String photoUrl, String profile, int type) {
        this.accountId = accountId;
        this.nickname = nickname;
        this.photoUrl = photoUrl;
        this.profile = profile;
        this.type = type;
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

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setProfile(String profile) {
        this.profile = profile;
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

    public String getAccountId() {
        return accountId;
    }

    public String getProfile() {
        return profile;
    }
}
