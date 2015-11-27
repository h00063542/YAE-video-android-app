package com.yilos.nailstar.aboutme.presenter;

import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.alibaba.sdk.android.oss.model.OSSException;
import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.entity.PersonInfo;
import com.yilos.nailstar.aboutme.model.AboutMeService;
import com.yilos.nailstar.aboutme.model.AboutMeServiceImpl;
import com.yilos.nailstar.aboutme.view.PersonInfoActivity;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.util.Constants;
import com.yilos.nailstar.util.TaskManager;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by sisilai on 15/10/30.
 */
public class PersonInfoPresenter {
    private static PersonInfoPresenter personInfoPresenter = new PersonInfoPresenter();
    private AboutMeService aboutMeService = new AboutMeServiceImpl();
    private PersonInfoActivity personInfoActivity;
    public static PersonInfoPresenter getInstance(PersonInfoActivity personInfoActivity) {
        personInfoPresenter.personInfoActivity = personInfoActivity;
        return personInfoPresenter;
    }

    //设置个人资料
    public void setPersonInfo(final PersonInfo personInfo) {
        String tips = personInfoActivity.getResources().getString(R.string.setting_person_info_show_tips);
        personInfoActivity.showLoading(tips);
        TaskManager.Task setPersonInfo = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                try {
                    return aboutMeService.setPersonInfo(personInfo);
                } catch (NetworkDisconnectException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        TaskManager.UITask<PersonInfo> setPersonInfoUITask = new TaskManager.UITask<PersonInfo>() {
            @Override
            public ArrayList<PersonInfo> doWork(PersonInfo data) {
                personInfoActivity.hideLoading();
                personInfoActivity.setPersonInfo(data);
                return null;
            }
        };

        new TaskManager().next(setPersonInfo).next(setPersonInfoUITask).start();
    }

    public void submitMyPhotoToOss(final String localPath, final String picName) {

        TaskManager.Task uploadMyPicTask = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                final String[] ossPic = new String[1];
                try {
                    aboutMeService.uploadFile2Oss(localPath, picName, new SaveCallback() {
                        @Override
                        public void onSuccess(String objectKey) {
                            ossPic[0] = Constants.YILOS_PIC_URL + objectKey;
                            personInfoActivity.submitMyPhotoToOss(ossPic[0]);
                        }

                        @Override
                        public void onProgress(String objectKey, int byteCount, int totalSize) {
                        }

                        @Override
                        public void onFailure(String objectKey, OSSException e) {
//                            LOGGER.error("上传我的图片到Oss失败，localPath:" + localPath
//                                    + "，picName:" + picName
//                                    + ",objectKey:" + objectKey, e);
//                            e.printStackTrace();
                        }
                    });
                } catch (NetworkDisconnectException e) {
//                    e.printStackTrace();
//                    LOGGER.error("上传我的图片到oss失败", e);
                }
                return ossPic[0];
            }
        };

        new TaskManager()
                .next(uploadMyPicTask)
                .start();

    }

}