package com.yilos.nailstar.aboutme.presenter;

import com.yilos.nailstar.aboutme.entity.MessageComment;
import com.yilos.nailstar.aboutme.entity.UserMessage;
import com.yilos.nailstar.aboutme.model.UserMessageService;
import com.yilos.nailstar.aboutme.model.UserMessageServiceImpl;
import com.yilos.nailstar.aboutme.view.UserMessageReplyActivity;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.util.TaskManager;

import org.json.JSONException;

/**
 * Created by sisilai on 15/11/20.
 */
public class ReplyUserMessagePresenter {

    private UserMessageService userMessageService = new UserMessageServiceImpl();
    private static ReplyUserMessagePresenter replyUserMessagePresenter = new ReplyUserMessagePresenter();
    private UserMessageReplyActivity userMessageReplyActivity;

    public static ReplyUserMessagePresenter getInstance(UserMessageReplyActivity userMessageReplyActivity) {
        replyUserMessagePresenter.userMessageReplyActivity = userMessageReplyActivity;
        return replyUserMessagePresenter;
    }

    //回复我的消息
    public void replyUserMessage(final MessageComment messageComment, final String topicId) {
        TaskManager.Task setComment = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                try {
                    return userMessageService.setComment(messageComment,topicId);
                } catch (NetworkDisconnectException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        TaskManager.UITask<MessageComment> replyUserMessageUITask = new TaskManager.UITask<MessageComment>() {
            @Override
            public UserMessage doWork(MessageComment data) {
                userMessageReplyActivity.replyUserMessage(data);
                return null;
            }
        };

        new TaskManager().next(setComment).next(replyUserMessageUITask).start();
    }
}
