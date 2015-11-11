package com.yilos.nailstar.topic.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yilos.nailstar.R;
import com.yilos.nailstar.framework.view.BaseActivity;
import com.yilos.nailstar.topic.presenter.TopicHomeworkPresenter;
import com.yilos.nailstar.util.Constants;
import com.yilos.nailstar.util.UserUtil;
import com.yilos.widget.titlebar.TitleBar;

/**
 * Created by yilos on 2015-11-10.
 */
public class TopicHomeworkActivity extends BaseActivity implements ITopicHomeworkView {
    private TitleBar mTbTopicHomeworkHead;
    private ImageView mIvTopicHomeworkCancel;
    private TextView mTvTopicHomeworkTitle;
    private TextView mTvTopicHomeworkSubmitted;

    private TextView mTvTopicHomeworkWaitingReviews;
    private ImageView mIvTopicHomework;


    private EditText mEtTopicHomeworkContent;

    private String mTopicId;
    private String mContentPic;
    private String mContent;

    private TopicHomeworkPresenter mTopicCommentPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_homework);
        init();
        if (!UserUtil.isLogin(this)) {
            // TODO 调用到登录界面
            return;
        }
    }

    private void init() {
        Bundle data = getIntent().getExtras();
        mTopicId = data.getString(Constants.TOPIC_ID, Constants.EMPTY_STRING);
        mContentPic = data.getString(Constants.CONTENT_PIC, Constants.EMPTY_STRING);
        // 初始化控件
        initControl();
        // 初始化控件事件
        initControlEvent();

        mTopicCommentPresenter = TopicHomeworkPresenter.getInstance(this);
    }

    private void initControl() {
        mTbTopicHomeworkHead = (TitleBar) findViewById(R.id.tb_topic_homework_head);
        mIvTopicHomeworkCancel = mTbTopicHomeworkHead.getBackButton();
        mTvTopicHomeworkTitle = mTbTopicHomeworkHead.getTitleView();
        mTvTopicHomeworkSubmitted = mTbTopicHomeworkHead.getRightTextButton();
        mEtTopicHomeworkContent = (EditText) findViewById(R.id.et_topic_homework_content);

        mIvTopicHomeworkCancel.setImageResource(R.mipmap.icon_back_white);

        mTvTopicHomeworkTitle.setText(getString(R.string.submitted_homework));
        mTvTopicHomeworkSubmitted.setText(getString(R.string.submitted));

        mTvTopicHomeworkWaitingReviews = (TextView) findViewById(R.id.tv_topic_homework_waiting_for_reviews);
        mIvTopicHomework = (ImageView) findViewById(R.id.iv_topic_homework);
        mIvTopicHomework.setImageURI(Uri.parse(mContentPic));
    }

    private void initControlEvent() {
        mIvTopicHomeworkCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TopicHomeworkActivity.this, TopicVideoPlayerActivity.class);
                intent.putExtra(Constants.TOPIC_ID, mTopicId);
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });

        mTvTopicHomeworkSubmitted.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mContent = mEtTopicHomeworkContent.getText().toString();
                mTopicCommentPresenter.submittedHomework(null);
            }
        });

    }

    @Override
    public void afterSubmittedHomework(String newCommentId) {
        Intent intent = new Intent(this, TopicVideoPlayerActivity.class);
        //设置返回数据
        intent.putExtra(Constants.TOPIC_ID, mTopicId);
        intent.putExtra(Constants.CONTENT, mContent);
        intent.putExtra(Constants.CONTENT_PIC, "");
        setResult(RESULT_OK, intent);
        finish();
    }
}