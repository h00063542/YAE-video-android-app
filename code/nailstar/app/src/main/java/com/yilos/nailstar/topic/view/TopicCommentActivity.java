package com.yilos.nailstar.topic.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.model.LoginAPI;
import com.yilos.nailstar.framework.view.BaseActivity;
import com.yilos.nailstar.topic.entity.AddCommentInfo;
import com.yilos.nailstar.topic.presenter.TopicCommentPresenter;
import com.yilos.nailstar.util.Constants;
import com.yilos.nailstar.util.StringUtil;
import com.yilos.widget.titlebar.TitleBar;

/**
 * Created by yilos on 2015-11-05.
 */
public class TopicCommentActivity extends BaseActivity implements ITopicCommentView {

    private TitleBar mTbTopicCommentHead;
    private ImageView mIvTopicCommentCancel;
    private TextView mTvTopicCommentTitle;
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
    private String mCommentLastReplyId;

    private String mContent;

    private TopicCommentPresenter mTopicCommentPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (!LoginAPI.getInstance().isLogin()) {
            LoginAPI.getInstance().gotoLoginPage(this);
            return;
        }
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
//        if (mCommentType == Constants.TOPIC_COMMENT_TYPE_REPLY_AGAIN) {
        mCommentReplyId = data.getString(Constants.TOPIC_COMMENT_REPLY_ID, Constants.EMPTY_STRING);
        mCommentReplyUserId = data.getString(Constants.TOPIC_COMMENT_REPLY_USER_ID, Constants.EMPTY_STRING);
        mCommentReplyAuthor = data.getString(Constants.TOPIC_COMMENT_REPLY_AUTHOR, Constants.EMPTY_STRING);
        mCommentLastReplyId = data.getString(Constants.LAST_REPLY_TO, Constants.EMPTY_STRING);
//        }
        // 初始化控件
        initControl();
        // 初始化控件事件
        initControlEvent();

        mTopicCommentPresenter = new TopicCommentPresenter(this);
    }

    private void initControl() {
        mTbTopicCommentHead = (TitleBar) findViewById(R.id.tb_topic_comment_head);
        mIvTopicCommentCancel = mTbTopicCommentHead.getBackButton();
        mTvTopicCommentTitle = mTbTopicCommentHead.getTitleView();
        mTvTopicCommentSubmitted = mTbTopicCommentHead.getRightTextButton();
        mEtTopicCommentContent = (EditText) findViewById(R.id.et_topic_comment_content);

        mIvTopicCommentCancel.setImageResource(R.mipmap.icon_back_white);


        mTvTopicCommentTitle.setText(getString(R.string.comment));
        mTvTopicCommentSubmitted.setText(getString(R.string.submitted));


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
            stringBuilder.append(getString(R.string.write_down_your_learning_experience));
        }
        mEtTopicCommentContent.setHint(stringBuilder);
    }

    private void initControlEvent() {
        mIvTopicCommentCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TopicCommentActivity.this, TopicVideoPlayerActivity.class);
                intent.putExtra(Constants.TOPIC_ID, mTopicId);
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });

        mTvTopicCommentSubmitted.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mContent = mEtTopicCommentContent.getText().toString();
                String userId = LoginAPI.getInstance().getLoginUserId();
                AddCommentInfo info = new AddCommentInfo();
                info.setTopicId(mTopicId);
                info.setUserId(userId);
                info.setContent(mContent);
                info.setContentPic(Constants.EMPTY_STRING);

                // 评论不需要设置
//                info.setReplayTo(mCommentId);
                info.setReady(Constants.READY_COMMENT);

                if (mCommentType == Constants.TOPIC_COMMENT_TYPE_REPLY) {
                    info.setAtUserId(mCommentUserId);
                    info.setReplayTo(mCommentId);
                    info.setLastReplayTO(mCommentLastReplyId);
                    mTopicCommentPresenter.addTopicCommentReply(info);
                } else if (mCommentType == Constants.TOPIC_COMMENT_TYPE_REPLY_AGAIN) {
                    info.setReplayTo(mCommentId);
                    info.setAtUserId(mCommentReplyAuthor);
                    info.setLastReplayTO(mCommentLastReplyId);
                    mTopicCommentPresenter.addTopicCommentReply(info);
                } else {//Constants.TOPIC_COMMENT_TYPE_COMMENT
                    info.setAtUserId(mCommentUserId);
                    mTopicCommentPresenter.addTopicComment(info);
                }


//                if (mCommentType == Constants.TOPIC_COMMENT_TYPE_REPLY_AGAIN) {
//                    info.setAtUserId(mCommentReplyAuthor);
//                    info.setLastReplayTO(mCommentReplyId);
//                    mTopicCommentPresenter.addTopicCommentReply(info);
//                } else {
//                    info.setAtUserId(mCommentUserId);
//                    mTopicCommentPresenter.addTopicComment(info);
//                }
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
        Intent intent = new Intent(this, TopicVideoPlayerActivity.class);
        intent.putExtra(Constants.TOPIC_ID, mTopicId);
        if (StringUtil.isEmpty(newCommentId)) {
            setResult(RESULT_FIRST_USER, intent);
            finish();
            return;
        }
        //设置返回数据
        intent.putExtra(Constants.TYPE, mCommentType);
        intent.putExtra(Constants.TOPIC_COMMENT_ID, mCommentId);
        intent.putExtra(Constants.TOPIC_NEW_COMMENT_ID, newCommentId);
        intent.putExtra(Constants.TOPIC_COMMENT_USER_ID, mCommentUserId);
        intent.putExtra(Constants.TOPIC_COMMENT_AUTHOR, mCommentAuthor);
        intent.putExtra(Constants.CONTENT, mContent);
        if (mCommentType == Constants.TOPIC_COMMENT_TYPE_REPLY_AGAIN) {
            intent.putExtra(Constants.TOPIC_COMMENT_REPLY_ID, mCommentReplyId);
            intent.putExtra(Constants.TOPIC_COMMENT_REPLY_USER_ID, mCommentReplyUserId);
            intent.putExtra(Constants.TOPIC_COMMENT_REPLY_AUTHOR, mCommentReplyAuthor);
        }
        setResult(RESULT_OK, intent);
        finish();
    }
}