package com.yilos.nailstar.aboutme.presenter;

import com.yilos.nailstar.aboutme.entity.Message;
import com.yilos.nailstar.aboutme.model.AboutMeServiceImpl;
import com.yilos.nailstar.aboutme.model.AboutMeService;
import com.yilos.nailstar.framework.exception.JSONParseException;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.index.entity.Poster;
import com.yilos.nailstar.util.TaskManager;

import java.io.IOException;
import java.util.List;

/**
 * Created by sisilai on 15/10/24.
 */
public class MessagePresenter {
    public AboutMeService aboutMeService = new AboutMeServiceImpl();
    Message message = new Message();

    public Message getMessage() throws NetworkDisconnectException, JSONParseException {
//        try {
//            message = aboutMeService.getMessageContext();
//        } catch (NetworkDisconnectException e) {
//            //throw new NetworkDisconnectException("网络连接失败",e);
//        } catch (JSONParseException e) {
//            //throw new JSONParseException("网络解析失败",e);
//        }
//        return message.getCount();

        TaskManager.Task loadMessageCount = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                try {
                    message = aboutMeService.getMessageContext();
                } catch (NetworkDisconnectException e) {
                    //throw new NetworkDisconnectException("网络连接失败",e);
                } catch (JSONParseException e) {
                    //throw new JSONParseException("网络解析失败",e);
                }
                return message;
            }
        };
        new TaskManager().next(loadMessageCount).start();
        return message;
    }

}
