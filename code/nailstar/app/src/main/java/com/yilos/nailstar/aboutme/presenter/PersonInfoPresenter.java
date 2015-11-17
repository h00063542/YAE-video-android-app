package com.yilos.nailstar.aboutme.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.alibaba.sdk.android.oss.model.OSSException;
import com.yilos.nailstar.aboutme.entity.PersonInfo;
import com.yilos.nailstar.aboutme.model.AboutMeService;
import com.yilos.nailstar.aboutme.model.AboutMeServiceImpl;
import com.yilos.nailstar.aboutme.view.PersonInfoActivity;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.topic.entity.UpdateReadyInfo;
import com.yilos.nailstar.util.Constants;
import com.yilos.nailstar.util.ImageUtil;
import com.yilos.nailstar.util.LoggerFactory;
import com.yilos.nailstar.util.TaskManager;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by sisilai on 15/10/30.
 */
public class PersonInfoPresenter {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonInfoPresenter.class);
    private static PersonInfoPresenter personInfoPresenter = new PersonInfoPresenter();
    private AboutMeService aboutMeService = new AboutMeServiceImpl();
    private PersonInfoActivity personInfoActivity;
    public static PersonInfoPresenter getInstance(PersonInfoActivity personInfoActivity) {
        personInfoPresenter.personInfoActivity = personInfoActivity;
        return personInfoPresenter;
    }
    //获取我的资料页面头像
    public void getImage(final String photoUrl) {
        TaskManager.Task loadImage = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                byte[] imageData;
                Bitmap bitmap;
                try {
                    imageData = ImageUtil.getBytes(new URL(photoUrl).openStream());
                    bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                    return bitmap;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        TaskManager.UITask<Bitmap> imageUITask = new TaskManager.UITask<Bitmap>() {
            @Override
            public Bitmap doWork(Bitmap data) {
                personInfoActivity.getImage(data);
                return null;
            }
        };

        new TaskManager().next(loadImage).next(imageUITask).start();
    }

    public void submitMyPhotoToOss(final PersonInfo personInfo) {

        TaskManager.Task uploadMyPicTask = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                final String[] ossPic = new String[1];
                try {
                    aboutMeService.uploadFile2Oss(personInfo.getPhotoUrl(), personInfo.getPicName(), new SaveCallback() {
                        @Override
                        public void onSuccess(String objectKey) {
                            LOGGER.debug("[onSuccess] - " + objectKey + " upload success!");
                            ossPic[0] = Constants.YILOS_PIC_URL + objectKey;
                        }

                        @Override
                        public void onProgress(String objectKey, int byteCount, int totalSize) {
                            LOGGER.debug("[onProgress] - current upload " + objectKey + " bytes: " + byteCount + " in total: " + totalSize);
                        }

                        @Override
                        public void onFailure(String objectKey, OSSException e) {
                            LOGGER.error("上传我的图片到Oss失败，localPath:" + personInfo.getPhotoUrl()
                                    + "，picName:" + personInfo.getPicName()
                                    + ",objectKey:" + objectKey, e);
                            e.printStackTrace();
                        }
                    });
                } catch (NetworkDisconnectException e) {
                    e.printStackTrace();
                    LOGGER.error("上传我的图片到oss失败，uid：" + personInfo.getUid(), e);
                }
                return ossPic[0];
            }
        };

        TaskManager.UITask<String> updateUi = new TaskManager.UITask<String>() {
            @Override
            public Object doWork(String ossUrl) {
                personInfoActivity.submitMyPhotoToOss(ossUrl);
                return null;
            }
        };


        new TaskManager()
                .next(uploadMyPicTask)
                .next(updateUi)
                .start();

        // TODO 测试阶段，先不要调用服务端接口
//        new TaskManager()
//                .next(uploadHomeworkPicTask)
//                .start();
    }
}