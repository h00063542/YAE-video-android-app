package com.yilos.nailstar.aboutme.presenter;

import com.yilos.nailstar.aboutme.entity.UserMessage;
import com.yilos.nailstar.aboutme.model.UserMessageService;
import com.yilos.nailstar.aboutme.model.UserMessageServiceImpl;
import com.yilos.nailstar.aboutme.view.MessageActivity;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.util.TaskManager;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by sisilai on 15/11/19.
 */
public class UserMessagePresenter {

    private UserMessageService userMessageService = new UserMessageServiceImpl();
    private static UserMessagePresenter userMessagePresenter = new UserMessagePresenter();
    private MessageActivity messageActivity;

    public static UserMessagePresenter getInstance(MessageActivity messageActivity) {
        userMessagePresenter.messageActivity = messageActivity;
        return userMessagePresenter;
    }

    //获取回复我的消息列表
    public void getUserMessageList(final String uid) {
        TaskManager.Task loadUserMessageList = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                try {
                    return userMessageService.getUserMessageList(uid);
                } catch (NetworkDisconnectException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        TaskManager.UITask<ArrayList<UserMessage>> UserMessageListUITask = new TaskManager.UITask<ArrayList<UserMessage>>() {
            @Override
            public ArrayList<UserMessage> doWork(ArrayList<UserMessage> data) {
                messageActivity.getUserMessageList(data);
                return null;
            }
        };

        new TaskManager().next(loadUserMessageList).next(UserMessageListUITask).start();
    }

}
