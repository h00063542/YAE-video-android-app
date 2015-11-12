package com.yilos.nailstar.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

/**
 * Created by sisilai on 15/11/12.
 */
public class SharedPreferencesUtil {

    /**
     * @param mContext Context对象
     * @param allow true: 允许非wifi
     *              false: 不允许非WiFi
     */
    public static void setAllowNoWifiSharedPreferences(Context mContext,boolean allow) {
        SharedPreferences mySharedPreferences= mContext.getSharedPreferences("allow_no_wifi_watch_and_download",
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean("allow", allow);
        editor.commit();
    }

    /**
     * @param mContext Context对象
     * @return SharedPreferences对象
     */
    public static SharedPreferences getAllowNoWifiSharedPreferences(Context mContext) {
        SharedPreferences allowNoWifiSharedPreferences= mContext.getSharedPreferences("allow_no_wifi_watch_and_download",
                Activity.MODE_PRIVATE);
        return allowNoWifiSharedPreferences;
    }

    /**
     * @param mContext Context对象
     * @return allow
     */
    public static boolean getAllowNoWifi(Context mContext) {
        return getAllowNoWifiSharedPreferences(mContext).getBoolean("allow",true);
    }


}
