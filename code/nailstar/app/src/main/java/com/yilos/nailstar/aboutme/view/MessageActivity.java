package com.yilos.nailstar.aboutme.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.entity.SystemMessage;
import com.yilos.nailstar.aboutme.entity.UserMessage;
import com.yilos.nailstar.aboutme.model.LoginAPI;
import com.yilos.nailstar.aboutme.presenter.SystemMessagePresenter;
import com.yilos.nailstar.aboutme.presenter.UserMessagePresenter;
import com.yilos.nailstar.framework.view.BaseActivity;
import com.yilos.nailstar.util.Constants;
import com.yilos.nailstar.util.DateUtil;
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

    private TitleBar titleBar;
    private TextView titleText;
    private LoginAPI loginAPI= LoginAPI.getInstance();
    
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
        userMessagePresenter.getUserMessageList(loginAPI.getLoginUserId());

        long lt = getLatestMessageTime();
        SystemMessagePresenter systemMessagePresenter = SystemMessagePresenter.getInstance(this);
        systemMessagePresenter.getSystemMessageList(lt);

    }

    public void initUserMessageList(final ArrayList<UserMessage> userMessageList) {
        UserMessageListAdapter userMessageListAdapter = new UserMessageListAdapter(this,userMessageList);
        messageListAdapter.getUserMessageListView().setAdapter(userMessageListAdapter);
    }

    public void initSystemMessageList(final ArrayList<SystemMessage> systemMessageList) {
        SystemMessageListAdapter systemMessageListAdapter = new SystemMessageListAdapter(this,systemMessageList);
        messageListAdapter.getSystemMessageListView().setAdapter(systemMessageListAdapter);
    }

    @Override
    public void getSystemMessageList(List<Object> objectList) {
        if (objectList.size() == 0) {
            initSystemMessageList(getLocalSystemMessage());
        } else {
            ArrayList<SystemMessage> systemMessageArrayList = (ArrayList<SystemMessage>)objectList.get(0);
            long lt = (long) objectList.get(1);
            setLocalSystemMessage(systemMessageArrayList);
            setLatestMessageTime(lt);
            initSystemMessageList(getLocalSystemMessage());
        }
    }

    @Override
    public void setLatestMessageTime(long latestMessageTime) {
        SharedPreferences mySharedPreferences= getSharedPreferences(Constants.MESSAGES + "_" + loginAPI.getLoginUserId(),
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putLong(Constants.LATEST_MESSAGE_TIME, latestMessageTime);
        editor.commit();
    }

    public void updateLocalSystemMessage(ArrayList<SystemMessage> systemMessageArrayList) {
        SharedPreferences mySharedPreferences= getSharedPreferences(Constants.MESSAGES + "_" + loginAPI.getLoginUserId(),
                Activity.MODE_PRIVATE);
        String list = "{\"systemMessageList\":[]}";
        JSONObject jsonObject = null;
        JSONArray jsonArray = null;
        try {
            jsonObject = new JSONObject(list);
            jsonArray = jsonObject.getJSONArray(Constants.SYSTEM_MESSAGE_LIST);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (SystemMessage systemMessage:systemMessageArrayList) {
            JSONObject systemMessageJSONObject = new JSONObject();
            try {
                /**
                 * id : d2f3cdc0-8f7f-11e5-ab99-1f385dee6318
                 * title : 美甲大咖团队
                 * content : 超级实用的纵向晕染技巧
                 * publishDate : 1448021486748
                 * topicId : 39f830b0-8f3a-11e5-ab99-1f385dee6318
                 * hasBeenRead : true
                 */
                systemMessageJSONObject.put(Constants.ID,systemMessage.getId());
                systemMessageJSONObject.put(Constants.TITLE,systemMessage.getTitle());
                systemMessageJSONObject.put(Constants.CONTENT,systemMessage.getContent());
                systemMessageJSONObject.put(Constants.PUBLISH_DATE,systemMessage.getPublishDate());
                systemMessageJSONObject.put(Constants.TOPIC_ID,systemMessage.getTopicId());
                systemMessageJSONObject.put(Constants.HAS_BEEN_READ,systemMessage.getHasBeenRead());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(systemMessageJSONObject);
        }
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(Constants.SYSTEM_MESSAGE_LIST, jsonObject.toString());
        editor.commit();
    }

    @Override
    public void setLocalSystemMessage(ArrayList<SystemMessage> systemMessageArrayList) {
        if (systemMessageArrayList.size() == 0) {
            return;
        }
        SharedPreferences mySharedPreferences= getSharedPreferences(Constants.MESSAGES + "_" + loginAPI.getLoginUserId(),
                Activity.MODE_PRIVATE);
        String list = mySharedPreferences.getString(Constants.SYSTEM_MESSAGE_LIST, "{\"systemMessageList\":[]}");
        JSONObject jsonObject = null;
        JSONArray jsonArray = null;
        try {
            jsonObject = new JSONObject(list);
            jsonArray = jsonObject.getJSONArray(Constants.SYSTEM_MESSAGE_LIST);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (SystemMessage systemMessage:systemMessageArrayList) {
            JSONObject systemMessageJSONObject = new JSONObject();
            try {
                /**
                 * id : d2f3cdc0-8f7f-11e5-ab99-1f385dee6318
                 * title : 美甲大咖团队
                 * content : 超级实用的纵向晕染技巧
                 * publishDate : 1448021486748
                 * topicId : 39f830b0-8f3a-11e5-ab99-1f385dee6318
                 * hasBeenRead : true
                 */
                systemMessageJSONObject.put(Constants.ID,systemMessage.getId());
                systemMessageJSONObject.put(Constants.TITLE,systemMessage.getTitle());
                systemMessageJSONObject.put(Constants.CONTENT,systemMessage.getContent());
                systemMessageJSONObject.put(Constants.PUBLISH_DATE,systemMessage.getPublishDate());
                systemMessageJSONObject.put(Constants.TOPIC_ID,systemMessage.getTopicId());
                systemMessageJSONObject.put(Constants.HAS_BEEN_READ,systemMessage.getHasBeenRead());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(systemMessageJSONObject);
        }
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(Constants.SYSTEM_MESSAGE_LIST, jsonObject.toString());
        editor.commit();
    }

    @Override
    public long getLatestMessageTime() {
        SharedPreferences mySharedPreferences= getSharedPreferences(Constants.MESSAGES + "_" + loginAPI.getLoginUserId(),
                Activity.MODE_PRIVATE);
        long time = mySharedPreferences.getLong(Constants.LATEST_MESSAGE_TIME, 0);
        return time;
    }

    @Override
    public void getUserMessageList(ArrayList<UserMessage> userMessageArrayList) {
        if (userMessageArrayList.size() == 0) {
            ArrayList<UserMessage> userMessageList = getLocalReplyMessage();
            initUserMessageList(userMessageList);
        } else {
            setLocalReplyMessage(userMessageArrayList);
            initUserMessageList(userMessageArrayList);
        }
    }

    @Override
    public ArrayList<UserMessage> getLocalReplyMessage() {
        ArrayList<UserMessage> userMessageArrayList = new ArrayList<>();
        SharedPreferences mySharedPreferences= getSharedPreferences(Constants.MESSAGES + "_" + loginAPI.getLoginUserId(),
                Activity.MODE_PRIVATE);
        String list = mySharedPreferences.getString(Constants.USER_MESSAGE_ARRAY_LIST, "{\"userMessageArrayList\":[]}");
        JSONObject jsonObject = null;
        JSONArray jsonArray = null;
        try {
            jsonObject = new JSONObject(list);
            jsonArray = jsonObject.getJSONArray(Constants.USER_MESSAGE_ARRAY_LIST);
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
    public ArrayList<SystemMessage> getLocalSystemMessage() {
        ArrayList<SystemMessage> systemMessageArrayList = new ArrayList<>();
        SharedPreferences mySharedPreferences= getSharedPreferences(Constants.MESSAGES + "_" + loginAPI.getLoginUserId(),
                Activity.MODE_PRIVATE);
        String list = mySharedPreferences.getString(Constants.SYSTEM_MESSAGE_LIST, "{\"systemMessageArrayList\":[]}");
        JSONObject jsonObject = null;
        JSONArray jsonArray = null;
        try {
            jsonObject = new JSONObject(list);
            jsonArray = jsonObject.getJSONArray(Constants.SYSTEM_MESSAGE_LIST);
            for (int i =0;i<jsonArray.length();i++) {
                JSONObject systemMessageJSONObject = jsonArray.getJSONObject(i);
                String id = systemMessageJSONObject.getString(Constants.ID);
                String title = systemMessageJSONObject.getString(Constants.TITLE);
                String content = systemMessageJSONObject.getString(Constants.CONTENT);
                long publishDate = systemMessageJSONObject.getLong(Constants.PUBLISH_DATE);
                String topicId = systemMessageJSONObject.getString(Constants.TOPIC_ID);
                boolean hasBeenRead = systemMessageJSONObject.getBoolean(Constants.HAS_BEEN_READ);

                SystemMessage systemMessage = new SystemMessage(content,hasBeenRead, id, publishDate, title, topicId);
                systemMessageArrayList.add(systemMessage);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return systemMessageArrayList;
    }

    @Override
    public void setLocalReplyMessage(ArrayList<UserMessage> userMessageArrayList) {
        SharedPreferences mySharedPreferences= getSharedPreferences(Constants.MESSAGES + "_" + loginAPI.getLoginUserId(),
                Activity.MODE_PRIVATE);

        String list = mySharedPreferences.getString(Constants.USER_MESSAGE_ARRAY_LIST, "{\"userMessageArrayList\":[]}");
        JSONObject jsonObject = null;
        JSONArray jsonArray = null;
        try {
            jsonObject = new JSONObject(list);
            jsonArray = jsonObject.getJSONArray(Constants.USER_MESSAGE_ARRAY_LIST);
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
        editor.putString(Constants.USER_MESSAGE_ARRAY_LIST, jsonObject.toString());
        editor.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) {
            case 1:
                String id = data.getStringExtra(Constants.ID);
                ArrayList<UserMessage> userMessageArrayList = getLocalReplyMessage();
                for (UserMessage userMessage : userMessageArrayList) {
                    if (userMessage.getId().equals(id)) {
                        userMessage.setHasBeenReply(true);
                        break;
                    }
                }
                setLocalReplyMessage(userMessageArrayList);
                initUserMessageList(userMessageArrayList);
                break;
            default:
                break;
        }
    }
}
