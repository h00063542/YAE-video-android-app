package com.yilos.nailstar.aboutme.presenter;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.entity.Message;
import com.yilos.nailstar.aboutme.model.AboutMeServiceImpl;
import com.yilos.nailstar.aboutme.model.AboutMeService;
import com.yilos.nailstar.aboutme.view.IAboutMeView;
import com.yilos.nailstar.framework.exception.JSONParseException;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.util.TaskManager;
import com.yilos.nailstar.aboutme.view.AboutMeFragment;


/**
 * Created by sisilai on 15/10/24.
 */
public class MessagePresenter {
//    private AboutMeFragment aboutMeFragment = new AboutMeFragment();
//    private AboutMeService aboutMeService = new AboutMeServiceImpl();
//    private Message message = new Message();
//    public void getMessage(){
//        TaskManager.Task loadMessageCount = new TaskManager.BackgroundTask() {
//            @Override
//            public Object doWork(Object data) {
//                try {
//                    message = aboutMeService.getMessageContext(message);
//                } catch (NetworkDisconnectException e) {
//                } catch (JSONParseException e) {
//                }
//                return message;
//            }
//        };
//
//        TaskManager.UITask<Message> messageUITask = new TaskManager.UITask<Message>() {
//            @Override
//            public Message doWork(Message data) {
//                aboutMeFragment.initMessageCount(data);
//                return data;
//            }
//        };
//
//        new TaskManager().next(loadMessageCount).next(messageUITask).start();
//    }

}
