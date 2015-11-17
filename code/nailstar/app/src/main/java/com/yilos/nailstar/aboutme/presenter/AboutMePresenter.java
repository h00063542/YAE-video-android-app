package com.yilos.nailstar.aboutme.presenter;

import com.alibaba.sdk.android.oss.callback.GetFileCallback;
import com.alibaba.sdk.android.oss.model.OSSException;
import com.yilos.nailstar.aboutme.entity.AboutMeNumber;
import com.yilos.nailstar.aboutme.entity.MessageCount;
import com.yilos.nailstar.aboutme.entity.PersonInfo;
import com.yilos.nailstar.aboutme.model.AboutMeServiceImpl;
import com.yilos.nailstar.aboutme.model.AboutMeService;
import com.yilos.nailstar.aboutme.view.IAboutMeView;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.util.LoggerFactory;
import com.yilos.nailstar.util.TaskManager;

import org.apache.log4j.Logger;
import org.json.JSONException;


/**
 * Created by sisilai on 15/10/24.
 */
public class AboutMePresenter {
    private static final Logger LOGGER = LoggerFactory.getLogger(AboutMePresenter.class);
    private static AboutMePresenter aboutMePresenter =new AboutMePresenter();
    private IAboutMeView aboutMeFragment;
    private AboutMeService aboutMeService = new AboutMeServiceImpl();
    public static AboutMePresenter getInstance(IAboutMeView aboutMeFragment) {
        aboutMePresenter.aboutMeFragment = aboutMeFragment;
        return aboutMePresenter;
    }

    public void downloadOss2File(final String localPath, final String picName) {

        TaskManager.Task downloadMyPicTask = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                final String[] localPic = new String[1];
                try {
                    aboutMeService.downloadOss2File(localPath, picName, new GetFileCallback() {
                        @Override
                        public void onSuccess(String objectKey, String filePath) {
                            LOGGER.info("[onSuccess] - objectKey:" + objectKey + ",filePath" + filePath);
                            aboutMeFragment.getMyPhotoToLocalPath(localPic[0]);
                        }

                        @Override
                        public void onProgress(String objectKey, int byteCount, int totalSize) {
                            LOGGER.debug("[onProgress] - current upload " + objectKey + " bytes: " + byteCount + " in total: " + totalSize);
                        }

                        @Override
                        public void onFailure(String objectKey, OSSException e) {
                            LOGGER.error("下载我的图片到本地失败，localPath:" + localPath
                                    + "，picName:" + picName
                                    + ",objectKey:" + objectKey, e);
                            e.printStackTrace();
                        }
                    });
                } catch (NetworkDisconnectException e) {
                    e.printStackTrace();
                    LOGGER.error("下载我的图片到本地失败", e);
                }
                return localPic[0];
            }
        };

        new TaskManager()
                .next(downloadMyPicTask)
                .start();

    }
    //获取消息数
    public void getMessageCount(){
        TaskManager.Task loadMessageCount = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                try {
                    return aboutMeService.getMessageCount();
                } catch (NetworkDisconnectException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        TaskManager.UITask<MessageCount> messageUITask = new TaskManager.UITask<MessageCount>() {
            @Override
            public MessageCount doWork(MessageCount data) {
                aboutMeFragment.initMessageCount(data);
                return null;
            }
        };

        new TaskManager().next(loadMessageCount).next(messageUITask).start();
    }

    //获取我的页面的经验、咖币、粉丝数和关注数信息
    public void getAboutMeNumber(){
        TaskManager.Task loadAboutMeNumber = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                try {
                    return aboutMeService.getAboutMeNumber();
                } catch (NetworkDisconnectException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        TaskManager.UITask<AboutMeNumber> aboutMeNumberUITask = new TaskManager.UITask<AboutMeNumber>() {
            @Override
            public MessageCount doWork(AboutMeNumber data) {
                aboutMeFragment.getAboutMeNumber(data);
                return null;
            }
        };

        new TaskManager().next(loadAboutMeNumber).next(aboutMeNumberUITask).start();
    }

    //获取个人资料
    public void getPersonInfo(){
        TaskManager.Task loadPersonInfo = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                try {
                    return aboutMeService.getPersonInfo();
                } catch (NetworkDisconnectException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        TaskManager.UITask<PersonInfo> loadPersonInfoUITask = new TaskManager.UITask<PersonInfo>() {
            @Override
            public MessageCount doWork(PersonInfo data) {
                aboutMeFragment.getPersonInfo(data);
                return null;
            }
        };

        new TaskManager().next(loadPersonInfo).next(loadPersonInfoUITask).start();
    }
}
