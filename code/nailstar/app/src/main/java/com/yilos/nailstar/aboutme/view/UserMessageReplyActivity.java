package com.yilos.nailstar.aboutme.view;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.entity.MessageComment;
import com.yilos.nailstar.aboutme.entity.UserMessage;
import com.yilos.nailstar.aboutme.model.LoginAPI;
import com.yilos.nailstar.aboutme.presenter.ReplyUserMessagePresenter;
import com.yilos.nailstar.aboutme.presenter.UserMessagePresenter;
import com.yilos.nailstar.framework.view.BaseActivity;
import com.yilos.widget.titlebar.TitleBar;

/**
 * Created by sisilai on 15/11/20.
 */
public class UserMessageReplyActivity extends BaseActivity implements IReplyMessageView {
    private TitleBar titleBar;
    private TextView sureButton;
    private EditText messageReplyContent;
    MessageComment messageComment = new MessageComment();
    ReplyUserMessagePresenter replyUserMessagePresenter = ReplyUserMessagePresenter.getInstance(this);
    private String topicId;
    LoginAPI loginAPI =LoginAPI.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_message_reply);
        titleBar = (TitleBar) findViewById(R.id.messageReplyHead);
        titleBar.getBackButton(UserMessageReplyActivity.this);
        sureButton = titleBar.getRightTextButton();
        messageReplyContent = (EditText) findViewById(R.id.messageReplyContent);
        UserMessage userMessage = (UserMessage) getIntent().getSerializableExtra("userMessage");
        final String atUser = userMessage.getReply().getAccountName();
        messageReplyContent.setHint("回复" + atUser + ":");
        topicId = userMessage.getTopicId();
        sureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageComment.setAtUser(atUser);
                messageComment.setAuthor(loginAPI.getLoginUserNickname());
                messageComment.setContent(messageReplyContent.getText().toString().trim());
                messageComment.setReady(1);
                replyUserMessagePresenter.replyUserMessage(messageComment, topicId);
            }
        });
    }

    @Override
    public void replyUserMessage(MessageComment messageComment) {
        showShortToast("回复成功");
        finish();
    }
}
