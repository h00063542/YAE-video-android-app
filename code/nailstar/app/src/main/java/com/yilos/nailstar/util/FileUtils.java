package com.yilos.nailstar.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;

/**
 * Created by yilos on 15/11/9.
 */
public class FileUtils {

    /**
     * 保存图片到本地
     *
     * @param bitmap
     * @param path
     * @param fileName
     * @return 保存的路径，如果为null表示保存失败
     */
    public static String saveBitMap(Bitmap bitmap, String path, String fileName) {

        if (bitmap == null || path == null || fileName == null) {
            return null;
        }

        File storePath = new File(path);

        String result = saveBitMap(bitmap, storePath, fileName);

        return result;
    }

    /**
     * 保存图片到本地
     *
     * @param bitmap
     * @param path
     * @param fileName
     * @return
     */
    public static String saveBitMap(Bitmap bitmap, File path, String fileName) {

        if (bitmap == null || path == null || fileName == null) {
            return null;
        }

        if (!path.exists()) {
            if (!path.mkdirs()) {
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
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                }
            }
        }
        return result;
    }

    /**
     * 刷新图片到相册中
     *
     * @param context
     * @param path
     */
    public static void mediaRefresh(Activity context, File path) {
        MediaScannerConnection.scanFile(context, new String[]{path.getAbsolutePath()}, null, null);
    }

    /**
     * 刷新图片到相册中
     *
     * @param context
     * @param path
     */
    public static void mediaRefresh(Activity context, String path) {
        MediaScannerConnection.scanFile(context, new String[]{path}, null, null);
    }

    /**
     * 从指定文件中读取String
     *
     * @param fileName
     * @return
     */
    public static String readFromFile(File fileName) {

        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(fileName));

            String tempString;

            while ((tempString = reader.readLine()) != null) {
                sb.append(tempString);
            }
        } catch (IOException e) {
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
            }
        }

        return sb.toString();
    }

    /**
     * 把String写入指定文件
     *
     * @param fileName
     * @param text
     */
    public static void writeToFile(File fileName, String text) {

        BufferedWriter writer = null;

        try {
            writer = new BufferedWriter(new FileWriter(fileName));

            writer.write(text);
            writer.flush();

        } catch (IOException e) {
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
            }
        }

    }
}
