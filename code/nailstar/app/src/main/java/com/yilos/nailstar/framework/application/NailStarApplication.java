package com.yilos.nailstar.framework.application;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.multidex.MultiDexApplication;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.Environment;
import com.alibaba.sdk.android.callback.InitResultCallback;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.sina.sinavideo.sdk.utils.VDApplication;
import com.sina.sinavideo.sdk.utils.VDResolutionManager;
import com.umeng.analytics.MobclickAgent;
import com.ut.mini.UTAnalytics;
import com.yilos.nailstar.R;
import com.yilos.nailstar.framework.entity.NailStarApplicationContext;
import com.yilos.nailstar.framework.exception.JSONParseException;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.index.model.IndexServiceImpl;
import com.yilos.nailstar.util.Constants;
import com.yilos.nailstar.util.CrashHandler;

import java.io.File;

/**
 * Created by yangdan on 15/10/16.
 */
public class NailStarApplication extends MultiDexApplication {
    /**
     * 单例
     */
    private static NailStarApplication application;

    /**
     * 返回应用实例
     *
     * @return
     */
    public static NailStarApplication getApplication() {
        return application;
    }


    public void onCreate() {
        super.onCreate();
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.setDebugMode(true);
        com.umeng.socialize.utils.Log.LOG = true;

        initDir();
        initTaobaoSDK();

        CrashHandler.getInstance().init(this);

        application = this;

        Fresco.initialize(getApplicationContext());

        File cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.icon_loading)
                .showImageOnFail(R.mipmap.icon_refresh)
                .resetViewBeforeLoading(false)  // default
                .cacheInMemory(true) // default
                .cacheOnDisk(true) // default
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
                .bitmapConfig(Bitmap.Config.ARGB_8888) // default
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
                .diskCacheExtraOptions(480, 800, null)
                .threadPriority(Thread.NORM_PRIORITY - 2) // default
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(20 * 1024 * 1024))
                .memoryCacheSize(20 * 1024 * 1024)
                .memoryCacheSizePercentage(20) // default
                .diskCache(new UnlimitedDiskCache(cacheDir)) // default
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(getApplicationContext())) // default
                .defaultDisplayImageOptions(options)
                .writeDebugLogs()
                .build();

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);

        // 播放器初始化，要在app启动前进行初始化，才能解压出相应的解码器
        initVideoPlayer();
    }

    public int getScreenWidth(Activity activity) {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);

        return metric.widthPixels;
    }

    public int getScreenHeight(Activity activity) {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);

        return metric.heightPixels;
    }

    /**
     * 根据长宽比获取高度
     * @param activity
     * @param ratio
     * @return
     */
    public int getHeightByScreenWidth(Activity activity, float ratio) {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);

        return (int)(metric.widthPixels * ratio);
    }

    public void preloadIndexContent() {
        try {
            NailStarApplicationContext.getInstance().setIndexContent(new IndexServiceImpl().getIndexContentFromNet());
        } catch (NetworkDisconnectException e) {
            //e.printStackTrace();
        } catch (JSONParseException e) {
            //e.printStackTrace();
        }
    }

    private void initVideoPlayer() {
        VDApplication.getInstance().initPlayer(this);
        VDResolutionManager.getInstance(this).init(
                VDResolutionManager.RESOLUTION_SOLUTION_NONE);
    }

    private void initDir() {
        File sdPath = new File(Constants.YILOS_NAILSTAR_LOGS_PATH);
        if (!sdPath.exists()) {
            sdPath.mkdirs();
        }
        sdPath = new File(Constants.YILOS_NAILSTAR_VIDEOS_PATH);
        if (!sdPath.exists()) {
            sdPath.mkdirs();
        }
        sdPath = new File(Constants.YILOS_NAILSTAR_PICTURE_PATH);
        if (!sdPath.exists()) {
            sdPath.mkdirs();
        }
    }

    private void initTaobaoSDK(){
        UTAnalytics.getInstance().turnOnDebug();
        AlibabaSDK.turnOnDebug();


        int envIndex = 1;
        AlibabaSDK.setEnvironment(Environment.values()[envIndex]);
        AlibabaSDK.asyncInit(this, new InitResultCallback() {

            @Override
            public void onSuccess() {
            }

            @Override
            public void onFailure(int code, String message) {
                Toast.makeText(NailStarApplication.this, "初始化异常" + message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
