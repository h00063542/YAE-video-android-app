package com.yilos.nailstar.aboutme.view;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.entity.UserMessage;
import com.yilos.nailstar.aboutme.presenter.UserMessagePresenter;
import com.yilos.nailstar.framework.view.BaseActivity;
import com.yilos.nailstar.util.Constants;
import com.yilos.widget.pageindicator.TabPageIndicator;
import com.yilos.widget.pageindicator.UnderlinePageIndicator;
import com.yilos.widget.titlebar.TitleBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    //用户uid
    private String uid;

    private TitleBar titleBar;
    private TextView titleText;

    // Provide a reference to the type of views that you are using
    // (custom viewholder)


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        uid = getIntent().getStringExtra("uid");
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
        userMessagePresenter.getUserMessageList(uid);

    }

    public void initUserMessageList(final List<UserMessage> userMessageList) {
        UserMessageListAdapter userMessageListAdapter = new UserMessageListAdapter(this,userMessageList);
        messageListAdapter.getUserMessageListView().setAdapter(userMessageListAdapter);
    }



    @Override
    public void getUserMessageList(ArrayList<UserMessage> userMessageArrayList) {
        if (userMessageArrayList.size() != 0) {
            setLocalReplyMessage(userMessageArrayList);
        }
        ArrayList<UserMessage> userMessageList = getLocalReplyMessage();
        initUserMessageList(userMessageList);
    }

    @Override
    public void replyUserMessage(UserMessage userMessage) {
        showShortToast("回复成功！");
    }

    @Override
    public ArrayList<UserMessage> getLocalReplyMessage() {
        ArrayList<UserMessage> userMessageArrayList = new ArrayList<>();
        SharedPreferences mySharedPreferences= getSharedPreferences("reply_message",
                Activity.MODE_PRIVATE);
        String list = mySharedPreferences.getString("userMessageArrayList", "{\"userMessageArrayList\":[]}");
        JSONObject jsonObject = null;
        JSONArray jsonArray = null;
        try {
            jsonObject = new JSONObject(list);
            jsonArray = jsonObject.getJSONArray("userMessageArrayList");
            for (int i =0;i<jsonArray.length();i++) {
                JSONObject userMessageJSONObject = jsonArray.getJSONObject(i);
                JSONObject commentJSONObject = userMessageJSONObject.getJSONObject(Constants.COMMENT);
                UserMessage.CommentEntity comment = UserMessage.parseCommentEntity(commentJSONObject);

                String id = userMessageJSONObject.getString(Constants.ID);

                JSONObject replyJSONObject = userMessageJSONObject.getJSONObject(Constants.REPLY);
                UserMessage.ReplyEntity reply = UserMessage.parseReplyEntity(replyJSONObject);

                String teacher = userMessageJSONObject.getString(Constants.TEACHER);
                String thumbUrl = userMessageJSONObject.getString(Constants.THUMB_URL);
                String title = userMessageJSONObject.getString(Constants.TITLE);
                String topicId = userMessageJSONObject.getString(Constants.TOPIC_ID);
                boolean hasBeenReply = userMessageJSONObject.getBoolean(Constants.HASBEENREPLY);
                UserMessage userMessage = new UserMessage(comment,id, reply, teacher, thumbUrl, title, topicId, hasBeenReply);
                userMessageArrayList.add(userMessage);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userMessageArrayList;
    }

    @Override
    public void setLocalReplyMessage(ArrayList<UserMessage> userMessageArrayList) {
        if (userMessageArrayList.size() == 0) {
            return;
        }

        SharedPreferences mySharedPreferences= getSharedPreferences("reply_message",
                Activity.MODE_PRIVATE);

        String list = mySharedPreferences.getString("userMessageArrayList", "{\"userMessageArrayList\":[]}");
        JSONObject jsonObject = null;
        JSONArray jsonArray = null;
        try {
            jsonObject = new JSONObject(list);
            jsonArray = jsonObject.getJSONArray("userMessageArrayList");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (UserMessage userMessage:userMessageArrayList) {
            JSONObject userMessageJSONObject = new JSONObject();
            try {
                userMessageJSONObject.put(Constants.ID,userMessage.getId());
                userMessageJSONObject.put(Constants.TOPIC_ID,userMessage.getTopicId());
                userMessageJSONObject.put(Constants.TITLE,userMessage.getTitle());
                userMessageJSONObject.put(Constants.TEACHER,userMessage.getTeacher());
                userMessageJSONObject.put(Constants.THUMB_URL,userMessage.getThumbUrl());

                JSONObject commentJSONObject = new JSONObject();
                UserMessage.CommentEntity commentEntity = userMessage.getComment();
                commentJSONObject.put(Constants.CONTENT,commentEntity.getContent());
                commentJSONObject.put(Constants.ATNAME,commentEntity.getAtName());
                commentJSONObject.put(Constants.CREATE_DATE,commentEntity.getCreateDate());
                commentJSONObject.put(Constants.IS_HOME_WORK,commentEntity.getIsHomework());
                userMessageJSONObject.put(Constants.COMMENT,commentJSONObject);

                JSONObject replyJSONObject = new JSONObject();
                UserMessage.ReplyEntity replyEntity = userMessage.getReply();
                replyJSONObject.put(Constants.ACCOUNTID,replyEntity.getAccountId());
                replyJSONObject.put(Constants.ACCOUNTNAME,replyEntity.getAccountName());
                replyJSONObject.put(Constants.ACCOUNTPHOTO,replyEntity.getAccountPhoto());
                replyJSONObject.put(Constants.CONTENT,replyEntity.getContent());
                replyJSONObject.put(Constants.CREATE_DATE,replyEntity.getCreateDate());
                replyJSONObject.put(Constants.REPLY_TO,replyEntity.getReplyTo());
                replyJSONObject.put(Constants.LAST_REPLY_TO, replyEntity.getLastReplyTo());
                userMessageJSONObject.put(Constants.REPLY,replyJSONObject);

                userMessageJSONObject.put(Constants.HASBEENREPLY,userMessage.getHasBeenReply());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(userMessageJSONObject);
        }
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString("userMessageArrayList", jsonObject.toString());
        editor.commit();
    }
}
