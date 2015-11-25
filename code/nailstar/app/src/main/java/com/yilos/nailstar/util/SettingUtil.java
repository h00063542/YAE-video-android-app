package com.yilos.nailstar.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.StatFs;

import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.entity.Sdcard;
import com.yilos.nailstar.aboutme.entity.StorageList;
import com.yilos.nailstar.framework.application.NailStarApplication;

import java.util.ArrayList;

/**
 * Created by sisilai on 15/11/12.
 */
public class SettingUtil {

    private static Context context = NailStarApplication.getApplication().getApplicationContext();
    public static SharedPreferences getSettingSharedPreferences() {
        return NailStarApplication.getApplication().getSharedPreferences(Constants.APPLICATION_SETTING + Constants.UNDERLINE + getVersion(),
                Activity.MODE_PRIVATE);
    }
    /**
     * @param allow_no_wifi true: 允许非wifi
     *              false: 不允许非WiFi
     */
    public static void setAllowNoWifi(boolean allow_no_wifi) {
        SharedPreferences settingSharedPreferences= getSettingSharedPreferences();
        SharedPreferences.Editor editor = settingSharedPreferences.edit();
        editor.putBoolean(Constants.ALLOW_NO_WIFI, allow_no_wifi);
        editor.commit();
    }

    /**
     * @return allow_no_wifi
     */
    public static boolean getAllowNoWifi() {
        return getSettingSharedPreferences().getBoolean(Constants.ALLOW_NO_WIFI, true);
    }


    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getVersion() {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return context.getString(R.string.can_not_find_version_name);
        }
    }


    /**
     * @param isFirstIntoVersion ：是否是第一次进入该版本
     */
    public static void setFirstFlag(boolean isFirstIntoVersion) {
        SharedPreferences settingSharedPreferences= getSettingSharedPreferences();
        SharedPreferences.Editor editor = settingSharedPreferences.edit();
        editor.putBoolean(Constants.IS_FIRST_INTO_VERSION, isFirstIntoVersion);
        editor.commit();
    }

    /**
     * @return isFirstIntoVersion
     */
    public static boolean getFirstFlag() {
        return getSettingSharedPreferences().getBoolean(Constants.IS_FIRST_INTO_VERSION,true);
    }

    /**
     * @param sdcard_path
     */
    public static void setSdcard(String sdcard_name,String sdcard_path) {
        SharedPreferences settingSharedPreferences = getSettingSharedPreferences();
        SharedPreferences.Editor editor = settingSharedPreferences.edit();
        editor.putString(Constants.SDCARD_NAME, sdcard_name);
        editor.putString(Constants.SDCARD_PATH, sdcard_path);
        editor.commit();
    }

    public static ArrayList<Sdcard> getSdcardList() {

        StorageList storageList = new StorageList(context);
        String[] paths;
        paths = storageList.getVolumePaths();

        ArrayList<Sdcard> sdcardArrayList = new ArrayList<>();

        for (int index = 0; index < paths.length; index++) {
            Sdcard sdcard = new Sdcard();
            sdcard.setSdcardName("存储卡" + String.valueOf(index + 1));
            sdcard.setSdcardPath(paths[index]);
            StatFs sf = new StatFs(sdcard.getSdcardPath());
            long blockSize = sf.getBlockSize(); //每个block大小
            long blockCount = sf.getBlockCount(); //总大小
            long availCount = sf.getAvailableBlocks(); //有效大小
            sdcard.setBlockCount(blockSize * blockCount);
            sdcard.setAvailCount(blockSize * availCount);
            try {
                sdcard.setBlockCountFormat(DataCleanManager.getFormatSize(sdcard.getBlockCount()));
                sdcard.setAvailCountFormat(DataCleanManager.getFormatSize(sdcard.getAvailCount()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (sdcard.getAvailCount() > 0) {
                sdcardArrayList.add(sdcard);
            }
        }

        return sdcardArrayList;
    }


    /**
     * @return 获取SD卡存储路径
     */
    public static String getSdcardPath() {
        return getSettingSharedPreferences().getString(Constants.SDCARD_PATH, "");
    }

    /**
     * @return 获取SD卡中文名字（如:存储卡1）
     */
    public static String getSdcardName() {
        return getSettingSharedPreferences().getString(Constants.SDCARD_NAME, "");
    }
}