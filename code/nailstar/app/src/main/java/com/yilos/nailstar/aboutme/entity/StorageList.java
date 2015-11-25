package com.yilos.nailstar.aboutme.entity;

/**
 * Created by sisilai on 15/11/13.
 */

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import android.app.Activity;
import android.content.Context;
import android.os.storage.StorageManager;

public class StorageList {
//    private Activity mActivity;
//    private StorageManager mStorageManager;
//    private Method mMethodGetPaths;
//
//    public StorageList(Activity activity) {
//        mActivity = activity;
//        if (mActivity != null) {
//            mStorageManager = (StorageManager)mActivity
//                    .getSystemService(Activity.STORAGE_SERVICE);
//            try {
//                mMethodGetPaths = mStorageManager.getClass()
//                        .getMethod("getVolumePaths");
//            } catch (NoSuchMethodException e) {
//                e.printStackTrace();
//            }
//        }
//    }
    private Context context;
    private StorageManager mStorageManager;
    private Method mMethodGetPaths;

    public StorageList(Context context) {
        this.context = context;
        if (this.context != null) {
            mStorageManager = (StorageManager)this.context
                    .getSystemService(this.context.STORAGE_SERVICE);
            try {
                mMethodGetPaths = mStorageManager.getClass()
                        .getMethod("getVolumePaths");
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    public String[] getVolumePaths() {
        String[] paths = null;
        try {
            paths = (String[]) mMethodGetPaths.invoke(mStorageManager);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return paths;
    }
}
