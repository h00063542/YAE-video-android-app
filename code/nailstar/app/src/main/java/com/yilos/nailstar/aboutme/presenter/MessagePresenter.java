package com.yilos.nailstar.aboutme.presenter;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.entity.Message;
import com.yilos.nailstar.aboutme.model.AboutMeServiceImpl;
import com.yilos.nailstar.aboutme.model.AboutMeService;
import com.yilos.nailstar.framework.exception.JSONParseException;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.util.TaskManager;
import com.yilos.nailstar.aboutme.view.AboutMeFragment;


/**
 * Created by sisilai on 15/10/24.
 */
public class MessagePresenter {
    public AboutMeService aboutMeService = new AboutMeServiceImpl();
    Message message = new Message();
    AboutMeFragment aboutMeFragment = new AboutMeFragment();

    public void getMessage(){
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

        TaskManager.UITask<Message> messageUITask = new TaskManager.UITask<Message>() {
            @Override
            public Message doWork(Message data) {
                aboutMeFragment.initMessageCount(data);
                return data;
            }
        };

        new TaskManager().next(loadMessageCount).next(messageUITask).start();
    }

}
