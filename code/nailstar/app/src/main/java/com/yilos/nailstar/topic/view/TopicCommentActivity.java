package com.yilos.nailstar.topic.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.yilos.nailstar.R;
import com.yilos.nailstar.framework.view.BaseActivity;
import com.yilos.nailstar.topic.presenter.TopicCommentPresenter;
import com.yilos.nailstar.util.Constants;
import com.yilos.nailstar.util.StringUtil;

/**
 * Created by yilos on 2015-11-05.
 */
public class TopicCommentActivity extends BaseActivity implements ITopicCommentView {

    private TextView mTvTopicCommentCancel;
    private TextView mTvTopicCommentSubmitted;
    private EditText mEtTopicCommentContent;

    private String mTopicId;
    private int mCommentType;
    private String mCommentId;
    private String mCommentUserId;
    private String mCommentAuthor;

    private String mCommentReplyId;
    private String mCommentReplyUserId;
    private String mCommentReplyAuthor;

    private String mContent;

    private TopicCommentPresenter mTopicCommentPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_comment);
        init();
    }

    private void init() {
        Bundle data = getIntent().getExtras();
        mTopicId = data.getString(Constants.TOPIC_ID, Constants.EMPTY_STRING);
        mCommentType = data.getInt(Constants.TYPE, Constants.TOPIC_COMMENT_TYPE_COMMENT);
        mCommentId = data.getString(Constants.TOPIC_COMMENT_ID, Constants.EMPTY_STRING);
        mCommentUserId = data.getString(Constants.TOPIC_COMMENT_USER_ID, Constants.EMPTY_STRING);
        mCommentAuthor = data.getString(Constants.TOPIC_COMMENT_AUTHOR, Constants.EMPTY_STRING);
        if (mCommentType == Constants.TOPIC_COMMENT_TYPE_REPLY) {
            mCommentReplyId = data.getString(Constants.TOPIC_COMMENT_REPLY_ID, Constants.EMPTY_STRING);
            mCommentReplyUserId = data.getString(Constants.TOPIC_COMMENT_REPLY_USER_ID, Constants.EMPTY_STRING);
            mCommentReplyAuthor = data.getString(Constants.TOPIC_COMMENT_REPLY_AUTHOR, Constants.EMPTY_STRING);
        }
        // 初始化控件
        initControl();
        // 初始化控件事件
        initControlEvent();

        mTopicCommentPresenter = TopicCommentPresenter.getInstance(this);
    }

    private void initControl() {
        mTvTopicCommentCancel = (TextView) findViewById(R.id.tv_topic_comment_cancel);
        mTvTopicCommentSubmitted = (TextView) findViewById(R.id.tv_topic_comment_submitted);
        mEtTopicCommentContent = (EditText) findViewById(R.id.et_topic_comment_content);
        StringBuilder stringBuilder = new StringBuilder();
        if (Constants.TOPIC_COMMENT_TYPE_REPLY == mCommentType) {
            stringBuilder.append(getString(R.string.reply));
            stringBuilder.append(" ");
            stringBuilder.append(mCommentAuthor);
        } else if (Constants.TOPIC_COMMENT_TYPE_REPLY_AGAIN == mCommentType) {
            stringBuilder.append(getString(R.string.reply));
            stringBuilder.append(" ");
            stringBuilder.append(mCommentReplyAuthor);
        } else {
            stringBuilder.append("写下你的学习心得");
        }
        mEtTopicCommentContent.setHint(stringBuilder);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEtTopicCommentContent, InputMethodManager.SHOW_FORCED);
    }

    private void initControlEvent() {
        mTvTopicCommentCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TopicCommentActivity.this, TopicVideoPlayerActivity.class);
                intent.putExtra(Constants.TOPIC_ID, mTopicId);
                startActivity(intent);
                finish();
            }
        });

        mTvTopicCommentSubmitted.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mContent = mEtTopicCommentContent.getText().toString();
                if (mCommentType == Constants.TOPIC_COMMENT_TYPE_REPLY) {
                    mTopicCommentPresenter.addTopicCommentReply(null, null);
                } else {
                    mTopicCommentPresenter.addTopicComment(null);
                }
            }
        });

    }

    @Override
    public void afterAddTopicComment(String newCommentId) {
        backTopicVideoPlayerPage(newCommentId);
    }

    @Override
    public void afterAddTopicCommentReplay(String newCommentId) {
        backTopicVideoPlayerPage(newCommentId);
    }

    private void backTopicVideoPlayerPage(String newCommentId) {
        if (StringUtil.isEmpty(newCommentId)) {

        }
        Intent intent = new Intent(TopicCommentActivity.this, TopicVideoPlayerActivity.class);
        //设置返回数据
        intent.putExtra(Constants.TOPIC_ID, mTopicId);
        intent.putExtra(Constants.TYPE, mCommentType);
        intent.putExtra(Constants.TOPIC_COMMENT_ID, mCommentId);
        intent.putExtra(Constants.TOPIC_NEW_COMMENT_ID, newCommentId);
        intent.putExtra(Constants.TOPIC_COMMENT_USER_ID, mCommentUserId);
        intent.putExtra(Constants.TOPIC_COMMENT_AUTHOR, mCommentAuthor);
        intent.putExtra(Constants.CONTENT, mContent);
        if (mCommentType == Constants.TOPIC_COMMENT_TYPE_REPLY) {
            intent.putExtra(Constants.TOPIC_COMMENT_REPLY_ID, mCommentReplyId);
            intent.putExtra(Constants.TOPIC_COMMENT_REPLY_USER_ID, mCommentReplyUserId);
            intent.putExtra(Constants.TOPIC_COMMENT_REPLY_AUTHOR, mCommentReplyAuthor);
        }
        setResult(RESULT_OK, intent);
        startActivity(intent);
        finish();
    }
}