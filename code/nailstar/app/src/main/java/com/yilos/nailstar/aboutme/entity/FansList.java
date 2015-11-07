package com.yilos.nailstar.aboutme.entity;

import android.graphics.Bitmap;

/**
 * Created by sisilai on 15/11/5.
 */
public class FansList {

    /**
     * accountId : dc282890-f87c-11e4-b13e-57eb04c66d6e
     * nickname : 大咖程序猿
     * type : 6
     * photoUrl : http://pic.yilos.com/5f8d77bef850f6dd90a95688803b2929
     * profile :
     */

    private String accountId;
    private String nickname;
    private int type;
    private String photoUrl;
    private String profile;
    private Bitmap imageBitmap;

    public FansList(String accountId, String nickname, String photoUrl, String profile, int type,Bitmap imageBitmap) {
        this.accountId = accountId;
        this.nickname = nickname;
        this.photoUrl = photoUrl;
        this.profile = profile;
        this.type = type;
        this.imageBitmap = imageBitmap;
    }

    public void setImageBitmap(Bitmap bitmap) {
        this.imageBitmap = bitmap;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
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

    public String getAccountId() {
        return accountId;
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
