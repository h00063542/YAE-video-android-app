package com.yilos.nailstar.util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by yilos on 15/11/9.
 */
public class FileUtils {

    private static Logger logger = LoggerFactory.getLogger(FileUtils.class);

    /**
     * 保存图片到本地
     * @param bitmap
     * @param path
     * @param fileName
     * @return 保存的路径，如果为null表示保存失败
     */
    public static String saveBitMap(Bitmap bitmap, String path, String fileName) {

        if (bitmap == null || path == null || fileName == null ) {
            return null;
        }

        File storePath = new File(path);

        String result = saveBitMap(bitmap, storePath, fileName);

        return result;
    }

    /**
     * 保存图片到本地
     * @param bitmap
     * @param path
     * @param fileName
     * @return
     */
    public static String saveBitMap(Bitmap bitmap, File path, String fileName) {

        if (bitmap == null || path == null || fileName == null ) {
            return null;
        }

        if (!path.exists()) {
            if(!path.mkdirs()) {
                return null;
            }
        }

        File destFile = new File(path, fileName);
        String result = destFile.getAbsolutePath();
        OutputStream os = null;
        try {
            os = new FileOutputStream(destFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (IOException e) {
            result = null;
            logger.error("saveBitMap failed", e);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    logger.error("saveBitMap close OutputStream failed", e);
                }
            }
        }
        return result;
    }

    /**
     * 刷新图片到相册中
     * @param context
     * @param path
     */
    public static void mediaRefresh (Activity context, File path) {
        MediaScannerConnection.scanFile(context, new String[]{path.getAbsolutePath()}, null, null);
    }

    /**
     * 刷新图片到相册中
     * @param context
     * @param path
     */
    public static void mediaRefresh(Activity context, String path) {
        MediaScannerConnection.scanFile(context, new String[]{path}, null, null);
    }

}
