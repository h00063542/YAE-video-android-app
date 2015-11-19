package com.yilos.nailstar.aboutme.model;

import com.yilos.nailstar.aboutme.entity.MessageCount;
import com.yilos.nailstar.aboutme.entity.UserMessage;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.topic.entity.TopicVideoInfo;
import com.yilos.nailstar.util.Constants;
import com.yilos.nailstar.util.HttpClient;
import com.yilos.nailstar.util.JsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sisilai on 15/11/19.
 */
public class UserMessageServiceImpl implements UserMessageService {
    private static UserMessageServiceImpl userMessageService = new UserMessageServiceImpl();

    public static UserMessageServiceImpl getInstance() {
        return userMessageService;
    }

    @Override
    public List<UserMessage> getUserMessageList(String uid) throws NetworkDisconnectException, JSONException{
        String jsonObject;
        JSONObject userMessageJSONObject;
        JSONArray userMessageArray;
        String url = "/vapi2/nailstar/replyMessages?uid=" + uid;
        jsonObject = //HttpClient.getJson(url);
        "{\"code\":0,\"result\":{\"messages\":[{\"id\":\"06a79050-83a7-11e5-8027-61091fab3b57\",\"topicId\":\"c8a5cfa0-312c-11e5-b553-d7e515311f8d\",\"title\":\"第222期：牛仔贴纸\",\"teacher\":\"董亚坡老师\",\"thumbUrl\":\"http://pic.yilos.com/283be79417e0a310eb9ed95a13a71d43\",\"comment\":{\"content\":\"这尽职尽责尽职尽责\",\"atName\":\"在\",\"createDate\":1446709317872,\"isHomework\":1},\"reply\":{\"accountId\":\"d77348c0-60d7-11e5-ade9-e3d220e2c964\",\"accountName\":\"勿忘我\",\"accountPhoto\":\"http://pic.yilos.com/ec9a2bbc1abb13166af6da31495bea0b\",\"content\":\"嗯嗯\",\"createDate\":1446718910072,\"replyTo\":\"9f59f430-8390-11e5-a74c-839a83b22973\",\"lastReplyTo\":\"b1438670-8390-11e5-a74c-839a83b22973\"}}\n" +
                "]}}";
        userMessageJSONObject = new JSONObject(jsonObject);
        if (userMessageJSONObject.getInt("code") != 0) {
            return null;
        }
        userMessageArray = userMessageJSONObject.getJSONObject("result").getJSONArray("messages");
        List<UserMessage> userMessageList = new ArrayList<>();
        for(int i=0;i<userMessageArray.length();i++){
            JSONObject userMessageObject = userMessageArray.getJSONObject(i);
            JSONObject userMessageCommentObject = userMessageObject.getJSONObject("comment");
            JSONObject userMessageReplyObject = userMessageObject.getJSONObject("reply");

            String id = JsonUtil.optString(userMessageObject, "id");
            String topicId = JsonUtil.optString(userMessageObject, "topicId");
            String title = JsonUtil.optString(userMessageObject, "title");
            String teacher = JsonUtil.optString(userMessageObject, "teacher");
            String thumbUrl = JsonUtil.optString(userMessageObject, "thumbUrl");

            String commentContent = JsonUtil.optString(userMessageCommentObject, "content");
            String atName = JsonUtil.optString(userMessageCommentObject, "atName");
            long commentCreateDate = userMessageCommentObject.getLong("createDate");
            int isHomework = userMessageCommentObject.getInt("isHomework");
            UserMessage.CommentEntity comment = new UserMessage.CommentEntity(commentContent,atName,commentCreateDate,isHomework);

            String accountId = JsonUtil.optString(userMessageReplyObject, "accountId");
            String accountName = JsonUtil.optString(userMessageReplyObject, "accountName");
            String accountPhoto = JsonUtil.optString(userMessageReplyObject,"accountPhoto");
            String replyContent = JsonUtil.optString(userMessageReplyObject, "content");
            long replyCreateDate = userMessageReplyObject.getLong("createDate");
            String replyTo = JsonUtil.optString(userMessageReplyObject, "replyTo");
            String lastReplyTo = JsonUtil.optString(userMessageReplyObject, "lastReplyTo");
            UserMessage.ReplyEntity reply = new UserMessage.ReplyEntity(accountId,accountName,accountPhoto,replyContent,replyCreateDate,replyTo,lastReplyTo);

            UserMessage userMessage = new UserMessage(comment,id,reply,teacher,thumbUrl,title,topicId);
            /**
             * id : 06a79050-83a7-11e5-8027-61091fab3b57
             * topicId : c8a5cfa0-312c-11e5-b553-d7e515311f8d
             * title : 第222期：牛仔贴纸
             * teacher : 董亚坡老师
             * thumbUrl : http://pic.yilos.com/283be79417e0a310eb9ed95a13a71d43
             * comment : {"content":"这尽职尽责尽职尽责","atName":"在","createDate":1446709317872,"isHomework":1}
             * reply : {"accountId":"d77348c0-60d7-11e5-ade9-e3d220e2c964","accountName":"勿忘我","accountPhoto":"http://pic.yilos.com/ec9a2bbc1abb13166af6da31495bea0b",
             * "content":"嗯嗯","createDate":1446718910072,"replyTo":"9f59f430-8390-11e5-a74c-839a83b22973","lastReplyTo":"b1438670-8390-11e5-a74c-839a83b22973"}
             */
            userMessageList.add(userMessage);
        }
        return userMessageList;
    }
}
