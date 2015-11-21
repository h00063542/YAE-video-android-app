package com.yilos.nailstar.aboutme.presenter;

import com.yilos.nailstar.aboutme.entity.AboutMeNumber;
import com.yilos.nailstar.aboutme.entity.MessageCount;
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

}
