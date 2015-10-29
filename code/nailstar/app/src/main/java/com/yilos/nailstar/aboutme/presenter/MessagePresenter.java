package com.yilos.nailstar.aboutme.presenter;

import com.yilos.nailstar.aboutme.entity.Message;
import com.yilos.nailstar.aboutme.model.AboutMeService;
import com.yilos.nailstar.aboutme.model.AboutMeServiceImpl;
import com.yilos.nailstar.aboutme.view.IAboutMeView;
import com.yilos.nailstar.framework.exception.JSONParseException;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.util.TaskManager;

import org.json.JSONException;


/**
 * Created by sisilai on 15/10/24.
 */
public class MessagePresenter {
    private static MessagePresenter messagePresenter =new MessagePresenter();
    private IAboutMeView aboutMeFragment;
    private AboutMeServiceImpl aboutMeService = new AboutMeService();
    public static MessagePresenter getInstance(IAboutMeView aboutMeFragment) {
        messagePresenter.aboutMeFragment = aboutMeFragment;
        return messagePresenter;
    }

    public void getMessage(){
        TaskManager.Task loadMessageCount = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                try {
                    return aboutMeService.getMessageContext();
                } catch (NetworkDisconnectException e) {
                    e.printStackTrace();
                } catch (JSONParseException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        TaskManager.UITask<Message> messageUITask = new TaskManager.UITask<Message>() {
            @Override
            public Message doWork(Message data) {
                aboutMeFragment.initMessageCount(data);
                return null;
            }
        };

        new TaskManager().next(loadMessageCount).next(messageUITask).start();
    }

}
