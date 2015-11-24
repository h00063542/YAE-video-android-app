package com.yilos.nailstar.aboutme.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.entity.MessageComment;
import com.yilos.nailstar.aboutme.entity.UserMessage;
import com.yilos.nailstar.aboutme.model.LoginAPI;
import com.yilos.nailstar.aboutme.presenter.ReplyUserMessagePresenter;
import com.yilos.nailstar.framework.view.BaseActivity;
import com.yilos.nailstar.util.Constants;
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
        titleBar.getTitleView(R.string.reply);
        sureButton = titleBar.getRightTextButton();
        sureButton.setText(R.string.sureButtonText);
        messageReplyContent = (EditText) findViewById(R.id.messageReplyContent);
        final UserMessage userMessage = (UserMessage) getIntent().getSerializableExtra(Constants.USERMESSAGE);
        final String atUser = userMessage.getReply().getAccountName();
        messageReplyContent.setHint("回复" + atUser + ":");
        topicId = userMessage.getTopicId();
        sureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageComment.setAtUser(userMessage.getReply().getAccountId());
                messageComment.setAuthor(loginAPI.getLoginUserId());
                messageComment.setContent(messageReplyContent.getText().toString().trim());
                messageComment.setReady(1);
                messageComment.setLastReplyTo(userMessage.getId());
                messageComment.setReplyTo(userMessage.getReply().getReplyTo());
                replyUserMessagePresenter.replyUserMessage(messageComment, topicId);
            }
        });
    }

    @Override
    public void replyUserMessage(MessageComment messageComment) {
        showShortToast(messageComment.getContent() + "回复成功");
        String id = messageComment.getLastReplyTo();
        Intent intent = new Intent(UserMessageReplyActivity.this,MessageActivity.class);
        intent.putExtra(Constants.ID, id);
        setResult(1, intent);
        finish();
    }
}
