package com.yilos.nailstar.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.yilos.nailstar.framework.application.NailStarApplication;

/**
 * Created by sisilai on 15/11/12.
 */
public class SharedPreferencesUtil {


    public static final String ALLOW_NO_WIFI = "allow_no_wifi_watch_and_download";
    /**
     * @param allow true: 允许非wifi
     *              false: 不允许非WiFi
     */
    public static void setAllowNoWifiSharedPreferences(boolean allow) {
        SharedPreferences mySharedPreferences= NailStarApplication.getApplication().getSharedPreferences(ALLOW_NO_WIFI,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean("allow", allow);
        editor.commit();
    }

    /**
     * @return SharedPreferences对象
     */
    public static SharedPreferences getAllowNoWifiSharedPreferences() {
        SharedPreferences allowNoWifiSharedPreferences= NailStarApplication.getApplication().getSharedPreferences(ALLOW_NO_WIFI,
                Activity.MODE_PRIVATE);
        return allowNoWifiSharedPreferences;
    }

    /**
     * @return allow
     */
    public static boolean getAllowNoWifi() {
        return getAllowNoWifiSharedPreferences().getBoolean("allow",true);
    }


}
