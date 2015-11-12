package com.yilos.nailstar.util;

import android.app.Activity;
import android.content.SharedPreferences;
import android.widget.Toast;

/**
 * Created by sisilai on 15/11/12.
 */
public class SharedPreferencesUtil {

    private boolean wifiStatus;

    public boolean isWifiStatus() {
        return wifiStatus;
    }

    public void setWifiStatus(boolean wifiStatus) {
//        //1、使用SharedPreferences保存数据方法如下：
//
//        //实例化SharedPreferences对象（第一步）
//        SharedPreferences mySharedPreferences= getSharedPreferences("test",
//                Activity.MODE_PRIVATE);
//        //实例化SharedPreferences.Editor对象（第二步）
//        SharedPreferences.Editor editor = mySharedPreferences.edit();
////用putString的方法保存数据
//        editor.putString("name", "Karl");
//        editor.putString("habit", "sleep");
////提交当前数据
//        editor.commit();
////使用toast信息提示框提示成功写入数据
//        Toast.makeText(this, "数据成功写入SharedPreferences！", Toast.LENGTH_LONG).show();
//        this.wifiStatus = wifiStatus;
    }


}
