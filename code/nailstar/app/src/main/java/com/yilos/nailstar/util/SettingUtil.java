package com.yilos.nailstar.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;

import com.tencent.bugly.crashreport.BuglyLog;
import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.entity.Sdcard;
import com.yilos.nailstar.framework.application.NailStarApplication;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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

    public static List<Sdcard> getSdcardList() {
        List<Sdcard> sdcardArrayList = getCardListByHideMethod();
        if(null != sdcardArrayList && sdcardArrayList.size() > 0) {
            return sdcardArrayList;
        }

        sdcardArrayList = new ArrayList<>();
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Sdcard sdcard = new Sdcard();
            sdcard.setSdcardPath(Environment.getExternalStorageDirectory().getAbsolutePath());
            if(Environment.isExternalStorageEmulated()) {
                sdcard.setSdcardName("手机存储");
            } else {
                sdcard.setSdcardName("SD存储卡");
            }
            initSdcardSize(sdcard);

            sdcardArrayList.add(sdcard);
        }

        if(!Environment.isExternalStorageEmulated()) {
            Sdcard sdcard = new Sdcard();
            sdcard.setSdcardName("手机存储");
            sdcard.setSdcardPath(context.getFilesDir().getAbsolutePath());
            initSdcardSize(sdcard);
            sdcardArrayList.add(sdcard);
        }

        return sdcardArrayList;
    }

    private static List<Sdcard> getCardListByHideMethod() {
        StorageManager storageManager = (StorageManager)context.getSystemService(Context.STORAGE_SERVICE);

        try {
            Method getVolumePathsMethod = StorageManager.class.getMethod("getVolumePaths");
            getVolumePathsMethod.setAccessible(true);

            String[] paths = (String[])getVolumePathsMethod.invoke(storageManager);
            if(null == paths || paths.length <= 0) {
                return null;
            }

            ArrayList<Sdcard> sdcardArrayList = new ArrayList<>();
            int cardCount = 1;
            for(String path : paths) {
                File filePath = new File(path);
                boolean validate = false;
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    validate = filePath.exists()
                            && Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState(filePath));
                } else {
                    validate = filePath.exists();
                }
                if(validate) {
                    Sdcard sdcard = new Sdcard();
                    sdcard.setSdcardPath(path);
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        if(Environment.isExternalStorageEmulated(filePath)) {
                            sdcard.setSdcardName("手机存储");
                        } else {
                            sdcard.setSdcardName("SD存储卡" + cardCount++);
                        }
                    } else {
                        if(Environment.isExternalStorageEmulated()
                                && Environment.getExternalStorageDirectory() != null
                                && Environment.getExternalStorageDirectory().getAbsolutePath().equals(path)) {
                            sdcard.setSdcardName("手机存储");
                        } else {
                            sdcard.setSdcardName("SD存储卡" + cardCount++);
                        }
                    }

                    initSdcardSize(sdcard);
                    if(sdcard.getAvailCount() > 0) {
                        sdcardArrayList.add(sdcard);
                    }
                }
            }

            return sdcardArrayList;
        } catch (NoSuchMethodException e) {
            BuglyLog.e("SettingUtil getCardListByHideMethod", "没有该方法getVolumePaths异常", e);
        } catch (InvocationTargetException e) {
            BuglyLog.e("SettingUtil getCardListByHideMethod", "调用方法getVolumePaths异常", e);
        } catch (IllegalAccessException e) {
            BuglyLog.e("SettingUtil getCardListByHideMethod", "没有权限调用该方法getVolumePaths异常", e);
        } catch (Exception e) {
            BuglyLog.e("SettingUtil getCardListByHideMethod", "未知异常异常", e);
        }

        return null;
    }

    private static void initSdcardSize(Sdcard sdcard) {
        try {
            StatFs statFs = new StatFs(sdcard.getSdcardPath());
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                sdcard.setAvailCount(statFs.getAvailableBytes());
                sdcard.setBlockCount(statFs.getTotalBytes());

                sdcard.setBlockCountFormat(DataCleanManager.getFormatSize(sdcard.getBlockCount()));
                sdcard.setAvailCountFormat(DataCleanManager.getFormatSize(sdcard.getAvailCount()));
            } else {
                long blockSize = statFs.getBlockSize(); //每个block大小
                long blockCount = statFs.getBlockCount(); //总大小
                long availCount = statFs.getAvailableBlocks(); //有效大小
                sdcard.setBlockCount(blockSize * blockCount);
                sdcard.setAvailCount(blockSize * availCount);

                sdcard.setBlockCountFormat(DataCleanManager.getFormatSize(sdcard.getBlockCount()));
                sdcard.setAvailCountFormat(DataCleanManager.getFormatSize(sdcard.getAvailCount()));
            }

        } catch (Exception e) {
            //获取空间信息异常
            BuglyLog.e("SettingUtil.class initSdcardSize", "获取路径的空间使用出错！", e);
        }
    }


    /**
     * @return 获取SD卡存储路径
     */
    public static String getSdcardPath() {
        String path = getSettingSharedPreferences().getString(Constants.SDCARD_PATH, null);
        if(null == path) {
            initSdCardSetting();
        }

        return getSettingSharedPreferences().getString(Constants.SDCARD_PATH, context.getFilesDir().getAbsolutePath());
    }

    /**
     * @return 获取SD卡中文名字（如:存储卡1）
     */
    public static String getSdcardName() {
        String name = getSettingSharedPreferences().getString(Constants.SDCARD_NAME, null);
        if(null == name) {
            initSdCardSetting();
        }
        return getSettingSharedPreferences().getString(Constants.SDCARD_NAME, "内部存储");
    }

    private static void initSdCardSetting() {
        List<Sdcard> sdCardList = getSdcardList();
        if(null == sdCardList || sdCardList.size() <= 0) {
            return;
        }

        setSdcard(sdCardList.get(0).getSdcardName(), sdCardList.get(0).getSdcardPath());
    }
}