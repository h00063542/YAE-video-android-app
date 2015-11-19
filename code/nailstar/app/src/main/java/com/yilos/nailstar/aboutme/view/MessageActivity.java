package com.yilos.nailstar.aboutme.view;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.entity.UserMessage;
import com.yilos.nailstar.aboutme.presenter.UserMessagePresenter;
import com.yilos.nailstar.framework.view.BaseActivity;
import com.yilos.widget.pageindicator.TabPageIndicator;
import com.yilos.widget.pageindicator.UnderlinePageIndicator;
import com.yilos.widget.titlebar.TitleBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sisilai on 15/11/18.
 */
public class MessageActivity extends BaseActivity implements IMessageView {

    // 消息列表Pager
    private ViewPager messageListPager;
    // 消息列表适配器
    private MessageListAdapter messageListAdapter;

    private TitleBar titleBar;
    private TextView titleText;

    // Provide a reference to the type of views that you are using
    // (custom viewholder)


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        titleBar = (TitleBar) findViewById(R.id.message_title_bar);
        titleText = titleBar.getTitleView();
        titleText.setText(R.string.about_me_message);
        titleBar.getBackButton(MessageActivity.this);

        messageListPager = (ViewPager)findViewById(R.id.messageListPager);
        messageListAdapter = new MessageListAdapter(MessageActivity.this);
        messageListPager.setAdapter(messageListAdapter);

        // 初始化tab页
        final TabPageIndicator tabPageIndicator = (TabPageIndicator)findViewById(R.id.messageListPagerIndicator);
        tabPageIndicator.setViewPager(messageListPager);
        final UnderlinePageIndicator underlinePageIndicator = (UnderlinePageIndicator)findViewById(R.id.messageListPagerLineIndicator);
        underlinePageIndicator.setViewPager(messageListPager);

        UserMessagePresenter userMessagePresenter = UserMessagePresenter.getInstance(this);
        userMessagePresenter.getUserMessageList("a8affd60-efe6-11e4-a908-3132fc2abe39");

    }

    public void initUserMessageList(final List<UserMessage> userMessageList) {
        UserMessageListAdapter userMessageListAdapter = new UserMessageListAdapter(this,userMessageList);
        messageListAdapter.getUserMessageListView().setAdapter(userMessageListAdapter);
    }

    @Override
    public void getUserMessageList(ArrayList<UserMessage> userMessageArrayList) {
        initUserMessageList(userMessageArrayList);
    }
}
