package com.yilos.nailstar.framework.application;

import android.graphics.Bitmap;

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
import com.yilos.nailstar.R;
import com.yilos.nailstar.framework.entity.NailStarApplicationContext;
import com.yilos.nailstar.framework.exception.JSONParseException;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.index.model.IndexServiceImpl;

import java.io.File;

/**
 * Created by yangdan on 15/10/16.
 */
public class NailStarApplication extends android.app.Application {
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

    @Override
    public void onCreate() {
        super.onCreate();

        application = this;

        File cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_launcher)
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
}
