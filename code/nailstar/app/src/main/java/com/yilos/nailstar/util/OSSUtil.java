package com.yilos.nailstar.util;

import com.alibaba.sdk.android.oss.OSSService;
import com.alibaba.sdk.android.oss.OSSServiceProvider;
import com.alibaba.sdk.android.oss.callback.GetFileCallback;
import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.alibaba.sdk.android.oss.model.AccessControlList;
import com.alibaba.sdk.android.oss.model.AuthenticationType;
import com.alibaba.sdk.android.oss.model.ClientConfiguration;
import com.alibaba.sdk.android.oss.model.ResumableTaskOption;
import com.alibaba.sdk.android.oss.model.TokenGenerator;
import com.alibaba.sdk.android.oss.storage.OSSBucket;
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

    public static final String BUCKET_YPICTURE = "ypicture";

    private static OSSService defaultOssService;

    private static String accessKey = "OA7cwTn8CH2XKE97";
    private static String screctKey = "F1z5IaFaQtB97Y8wRvtvtai2tBN3GT";


    public static OSSService getDefaultOssService() {
        if (null == defaultOssService) {
            initDefaultOssService();
        }
        return defaultOssService;
    }

    private static void initDefaultOssService() {
        // 开启Log
        OSSLog.enableLog();

        defaultOssService = OSSServiceProvider.getService();

        defaultOssService.setApplicationContext(NailStarApplication.getApplication());
        defaultOssService.setGlobalDefaultHostId(Constants.DEFAULT_OSS_HOST_ID); // 设置region host 即 endpoint
        defaultOssService.setGlobalDefaultACL(AccessControlList.PRIVATE); // 默认为private
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


    /**
     * 断点上传
     *
     * @param ossService
     * @param bucket
     * @param localFilePath 本地文件路径
     * @param ossFileName   oss文件名称
     * @param callback      上传回调
     */
    public static void resumableUpload(OSSService ossService, OSSBucket bucket
            , String localFilePath, String ossFileName, SaveCallback callback) {
        try {
            OSSFile ossFile = ossService.getOssFile(bucket, ossFileName);
            ossFile.setUploadFilePath(localFilePath, "application/octet-stream");
            ossFile.ResumableUploadInBackground(callback);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 断点下载
     *
     * @param ossService
     * @param bucket
     * @param localFilePath 本地文件路径
     * @param ossFileName   oss文件名称
     * @param callback      下载回调
     */
    public static void resumableDownload(OSSService ossService, OSSBucket bucket
            , String localFilePath, String ossFileName, GetFileCallback callback) {
        OSSFile ossFile = ossService.getOssFile(bucket, ossFileName);
        ossFile.ResumableDownloadToInBackground(localFilePath, callback);
    }

    /**
     * 设置相关参数的断点续传
     *

     */

    /**
     * 设置相关参数的断点续传
     *
     * @param ossService
     * @param bucket
     * @param localFilePath 本地文件路径
     * @param ossFileName   oss文件名称
     * @param autoRetryTime 默认为2次，最大3次
     * @param threadNum     默认并发3个线程，最大5个
     * @param callback      下载回调
     */
    public static void resumableDownloadWithSpecConfig(OSSService ossService, OSSBucket bucket
            , String localFilePath, String ossFileName
            , int autoRetryTime, int threadNum, GetFileCallback callback) {
        OSSFile ossFile = ossService.getOssFile(bucket, ossFileName);
        ResumableTaskOption option = new ResumableTaskOption();
        option.setAutoRetryTime(autoRetryTime);
        option.setThreadNum(threadNum);
        ossFile.ResumableDownloadToInBackground(localFilePath, callback);
    }
}