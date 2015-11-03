package com.yilos.nailstar.aboutme.presenter;

import com.yilos.nailstar.aboutme.entity.AboutMeNumber;
import com.yilos.nailstar.aboutme.entity.MessageCount;
import com.yilos.nailstar.aboutme.entity.PersonInfo;
import com.yilos.nailstar.aboutme.model.AboutMeService;
import com.yilos.nailstar.aboutme.model.AboutMeServiceImpl;
import com.yilos.nailstar.aboutme.view.IAboutMeView;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.util.TaskManager;

import org.json.JSONException;


/**
 * Created by sisilai on 15/10/24.
 */
public class AboutMePresenter {
    private static AboutMePresenter aboutMePresenter =new AboutMePresenter();
    private IAboutMeView aboutMeFragment;
    private AboutMeServiceImpl aboutMeService = new AboutMeService();
    public static AboutMePresenter getInstance(IAboutMeView aboutMeFragment) {
        aboutMePresenter.aboutMeFragment = aboutMeFragment;
        return aboutMePresenter;
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
