package com.yilos.nailstar.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by yilos on 2015-11-05.
 */
public final class UserUtil {

    public static UserInfo getUserInfo(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.USER_INFO, context.MODE_WORLD_READABLE);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(sharedPreferences.getString(Constants.USER_ID, Constants.EMPTY_STRING));
        userInfo.setNickName(sharedPreferences.getString(Constants.NICKNAME, Constants.EMPTY_STRING));
        userInfo.setPhoto(sharedPreferences.getString(Constants.PHOTO, Constants.EMPTY_STRING));
        return userInfo;
    }

    public static void saveUserInfo(Context context, UserInfo userInfo) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.USER_INFO, context.MODE_WORLD_WRITEABLE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(Constants.USER_ID, userInfo.getUserId());
        edit.putString(Constants.NICKNAME, userInfo.getNickName());
        edit.putString(Constants.PHOTO, userInfo.getPhoto());
        edit.commit();
    }

}
