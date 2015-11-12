package com.yilos.nailstar.util;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.alibaba.sdk.android.oss.OSSService;
import com.alibaba.sdk.android.oss.OSSServiceProvider;
import com.alibaba.sdk.android.oss.callback.GetFileCallback;
import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.alibaba.sdk.android.oss.model.AccessControlList;
import com.alibaba.sdk.android.oss.model.AuthenticationType;
import com.alibaba.sdk.android.oss.model.ClientConfiguration;
import com.alibaba.sdk.android.oss.model.OSSException;
import com.alibaba.sdk.android.oss.model.ResumableTaskOption;
import com.alibaba.sdk.android.oss.model.TokenGenerator;
import com.alibaba.sdk.android.oss.storage.OSSFile;
import com.alibaba.sdk.android.oss.util.OSSLog;
import com.alibaba.sdk.android.oss.util.OSSToolKit;
import com.yilos.nailstar.framework.application.NailStarApplication;

import org.apache.log4j.Logger;

import java.io.FileNotFoundException;

/**
 * Created by yilos on 2015-11-12.
 */
public class OSSUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(OSSUtil.class);

    private static OSSService defaultOssService;

    private static String accessKey;
    private static String screctKey;
    private static String bucketName;


    public static OSSService getDefaultOssService() {
        if (null == defaultOssService) {
            initDefaultOssService();
        }
        return defaultOssService;

    }

    private static void initDefaultOssService() {
        NailStarApplication app = NailStarApplication.getApplication();
        try {
            ApplicationInfo appInfo = app.getPackageManager().getApplicationInfo(app.getPackageName(), PackageManager.GET_META_DATA);
            accessKey = appInfo.metaData.getString(Constants.DEFAULT_ACCESS_KEY_NAME);
            screctKey = appInfo.metaData.getString(Constants.DEFAULT_SCRECT_KEY_NAME);
            bucketName = appInfo.metaData.getString(Constants.DEFAULT_BUCKET_NAME);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        // 开启Log
        OSSLog.enableLog();

        defaultOssService = OSSServiceProvider.getService();

        defaultOssService.setApplicationContext(app);
        defaultOssService.setGlobalDefaultHostId(Constants.DEFAULT_OSS_HOST_ID); // 设置region host 即 endpoint
        defaultOssService.setGlobalDefaultACL(AccessControlList.PUBLIC_READ_WRITE); // 默认为private
        defaultOssService.setAuthenticationType(AuthenticationType.ORIGIN_AKSK); // 设置加签类型为原始AK/SK加签
        defaultOssService.setGlobalDefaultTokenGenerator(new TokenGenerator() { // 设置全局默认加签器
            @Override
            public String generateToken(String httpMethod, String md5, String type, String date,
                                        String ossHeaders, String resource) {

                String content = httpMethod + "\n" + md5 + "\n" + type + "\n" + date + "\n" + ossHeaders
                        + resource;

                return OSSToolKit.generateToken(accessKey, screctKey, content);
            }
        });
        defaultOssService.setCustomStandardTimeWithEpochSec(System.currentTimeMillis() / 1000);

        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectTimeout(30 * 1000); // 设置全局网络连接超时时间，默认30s
        conf.setSocketTimeout(30 * 1000); // 设置全局socket超时时间，默认30s
        conf.setMaxConcurrentTaskNum(5); // 替换设置最大连接数接口，设置全局最大并发任务数，默认为6
        conf.setIsSecurityTunnelRequired(false); // 是否使用https，默认为false
        defaultOssService.setClientConfiguration(conf);
    }

    public static String getDefaultBucketName() {
        NailStarApplication app = NailStarApplication.getApplication();
        try {
            ApplicationInfo appInfo = app.getPackageManager().getApplicationInfo(app.getPackageName(), PackageManager.GET_META_DATA);
            return appInfo.metaData.getString("com.yilos.nailstar.ossbucketname");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return Constants.EMPTY_STRING;
    }


    /**
     * 断点上传
     *
     * @param localFilePath 本地文件路径
     * @param ossFileName   oss文件名称
     */
    public void resumableUploadByDefault(String localFilePath, String ossFileName) {
        if (null == defaultOssService) {
            initDefaultOssService();
        }
        OSSFile ossFile = defaultOssService.getOssFile(defaultOssService.getOssBucket(getDefaultBucketName()), ossFileName);
        try {
            ossFile.setUploadFilePath(localFilePath, "application/octet-stream");
            ossFile.ResumableUploadInBackground(new SaveCallback() {

                @Override
                public void onSuccess(String objectKey) {
                    LOGGER.debug("[onSuccess] - " + objectKey + " upload success!");
                }

                @Override
                public void onProgress(String objectKey, int byteCount, int totalSize) {
                    LOGGER.debug("[onProgress] - current upload " + objectKey + " bytes: " + byteCount + " in total: " + totalSize);
                }

                @Override
                public void onFailure(String objectKey, OSSException ossException) {
                    LOGGER.error("[onFailure] - upload " + objectKey + " failed!", ossException);
                    ossException.printStackTrace();
                    ossException.getException().printStackTrace();
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 断点下载
     *
     * @param localFilePath 本地文件路径
     * @param ossFileName   oss文件名称
     */
    public void resumableDownloadByDefault(String localFilePath, String ossFileName) {
        if (null == defaultOssService) {
            initDefaultOssService();
        }
        OSSFile ossFile = defaultOssService.getOssFile(defaultOssService.getOssBucket(getDefaultBucketName()), ossFileName);
        ossFile.ResumableDownloadToInBackground(localFilePath, new GetFileCallback() {

            @Override
            public void onSuccess(String objectKey, String filePath) {
                LOGGER.debug("[onSuccess] - " + objectKey + " storage path: " + filePath);
            }

            @Override
            public void onProgress(String objectKey, int byteCount, int totalSize) {
                LOGGER.debug("[onProgress] - current download: " + objectKey + " bytes:" + byteCount + " in total:" + totalSize);
            }

            @Override
            public void onFailure(String objectKey, OSSException ossException) {

                ossException.printStackTrace();
            }
        });
    }

    /**
     * 设置相关参数的断点续传
     *
     * @param localFilePath 本地文件路径
     * @param ossFileName   oss文件名称
     * @param autoRetryTime 默认为2次，最大3次
     * @param threadNum     默认为2次，最大3次
     */
    public void resumableDownloadWithSpecConfigByDefault(String localFilePath, String ossFileName, int autoRetryTime, int threadNum) {
        if (null == defaultOssService) {
            initDefaultOssService();
        }
        OSSFile ossFile = defaultOssService.getOssFile(defaultOssService.getOssBucket(getDefaultBucketName()), ossFileName);
        ResumableTaskOption option = new ResumableTaskOption();
        option.setAutoRetryTime(autoRetryTime);
        option.setThreadNum(threadNum); // 默认并发3个线程，最大5个
        ossFile.ResumableDownloadToInBackground(localFilePath, new GetFileCallback() {

            @Override
            public void onSuccess(String objectKey, String filePath) {
                LOGGER.debug("[onSuccess] - " + objectKey + " storage path: " + filePath);
            }

            @Override
            public void onProgress(String objectKey, int byteCount, int totalSize) {
                LOGGER.debug("[onProgress] - current download: " + objectKey + " bytes:" + byteCount + " in total:" + totalSize);
            }

            @Override
            public void onFailure(String objectKey, OSSException ossException) {
                LOGGER.error("[onFailure] - download " + objectKey + " failed!", ossException);
                ossException.printStackTrace();
            }
        });
    }
}