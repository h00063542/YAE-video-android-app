package com.yilos.nailstar.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.yilos.nailstar.framework.application.NailStarApplication;

/**
 * Created by sisilai on 15/11/12.
 */
public class SettingUtil {


    public static final String SETTING = "application_setting";

    public static SharedPreferences getSettingSharedPreferences() {
        return NailStarApplication.getApplication().getSharedPreferences(SETTING,
                Activity.MODE_PRIVATE);
    }
    /**
     * @param allow true: 允许非wifi
     *              false: 不允许非WiFi
     */
    public static void setAllowNoWifiSharedPreferences(boolean allow) {
        SharedPreferences allowNoWifi= getSettingSharedPreferences();
        SharedPreferences.Editor editor = allowNoWifi.edit();
        editor.putBoolean("allow", allow);
        editor.commit();
    }

    /**
     * @return allow
     */
    public static boolean getAllowNoWifi() {
        return getSettingSharedPreferences().getBoolean("allow",true);
    }

    /**
     * @param sdcard_path
     */
    public static void setSdcardPathSharedPreferences(String sdcard_name,String sdcard_path) {
        SharedPreferences sdcardPath = getSettingSharedPreferences();
        SharedPreferences.Editor editor = sdcardPath.edit();
        editor.putString("sdcard_name", sdcard_name);
        editor.putString("sdcard_path", sdcard_path);
        editor.commit();
    }

    /**
     * @return
     */
    public static String getSdcardPath() {
        return getSettingSharedPreferences().getString("sdcard_path", "");
    }

    /**
     * @return
     */
    public static String getSdcardName() {
        return getSettingSharedPreferences().getString("sdcard_name", "");
    }
}