package com.yilos.nailstar.framework.entity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

import com.yilos.nailstar.framework.application.NailStarApplication;
import com.yilos.nailstar.index.entity.IndexContent;

import java.io.File;

/**
 * Created by yangdan on 15/10/19.
 */
public class NailStarApplicationContext {
    /**
     * 单例
     */
    private static NailStarApplicationContext context = new NailStarApplicationContext();

    /**
     * 首页对象
     */
    private IndexContent indexContent = null;

    /**
     * 返回单例
     *
     * @return
     */
    public static NailStarApplicationContext getInstance() {
        return context;
    }

    /**
     * 返回首页数据
     *
     * @return
     */
    public IndexContent getIndexContent() {
        return indexContent;
    }

    public void setIndexContent(IndexContent indexContent) {
        this.indexContent = indexContent;
    }

    public File getCacheDir() {
        return NailStarApplication.getApplication().getApplicationContext().getCacheDir();
    }

    public File getExternalCacheDir() {
        return NailStarApplication.getApplication().getApplicationContext().getExternalCacheDir();
    }

    public File getFileDir() {
        return NailStarApplication.getApplication().getApplicationContext().getFilesDir();
    }

    public File getExternalFileDir() {
        return NailStarApplication.getApplication().getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_MOVIES);
    }

    /**
     * 判断当前网络是否连通
     * @return
     */
    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) NailStarApplication.getApplication().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();

        return info != null && info.isConnectedOrConnecting();
    }

    // 判断当前网络是否为wifi
    public boolean isWifi() {
        ConnectivityManager connectivityManager = (ConnectivityManager) NailStarApplication.getApplication().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }
}
