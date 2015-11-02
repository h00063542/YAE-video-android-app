package com.yilos.nailstar.aboutme.presenter;

import com.yilos.nailstar.aboutme.entity.MessageCount;
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
}
