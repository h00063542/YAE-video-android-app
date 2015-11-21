package com.yilos.nailstar.aboutme.presenter;

import com.yilos.nailstar.aboutme.entity.UserMessage;
import com.yilos.nailstar.aboutme.model.SystemMessageService;
import com.yilos.nailstar.aboutme.model.SystemMessageServiceImpl;
import com.yilos.nailstar.aboutme.model.UserMessageService;
import com.yilos.nailstar.aboutme.model.UserMessageServiceImpl;
import com.yilos.nailstar.aboutme.view.MessageActivity;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.util.TaskManager;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sisilai on 15/11/21.
 */
public class SystemMessagePresenter {

    private SystemMessageService systemMessageService = new SystemMessageServiceImpl();
    private static SystemMessagePresenter systemMessagePresenter = new SystemMessagePresenter();
    private MessageActivity messageActivity;

    public static SystemMessagePresenter getInstance(MessageActivity messageActivity) {
        systemMessagePresenter.messageActivity = messageActivity;
        return systemMessagePresenter;
    }

    //获取系统消息列表
    public void getSystemMessageList(final long lt) {
        TaskManager.Task loadSystemMessageList = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                try {
                    return systemMessageService.getSystemMessage(lt);
                } catch (NetworkDisconnectException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        TaskManager.UITask<List<Object>> systemMessageListUITask = new TaskManager.UITask<List<Object>>() {
            @Override
            public List<Object> doWork(List<Object> data) {
                messageActivity.getSystemMessageList(data);
                return null;
            }
        };

        new TaskManager().next(loadSystemMessageList).next(systemMessageListUITask).start();
    }

}
