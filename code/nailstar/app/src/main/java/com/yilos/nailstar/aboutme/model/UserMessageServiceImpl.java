package com.yilos.nailstar.aboutme.model;

import com.yilos.nailstar.aboutme.entity.MessageComment;
import com.yilos.nailstar.aboutme.entity.UserMessage;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
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
    public ArrayList<UserMessage> getUserMessageList(String uid) throws NetworkDisconnectException, JSONException {
        ArrayList<UserMessage> userMessageList = new ArrayList<>();
        String jsonObject = null;
        JSONObject userMessageJSONObject;
        JSONArray userMessageArray;
        String url = "/vapi/nailstar/replyMessages?uid=" + uid;
        try {
            jsonObject = HttpClient.getJson(url);
//                    "{\"code\":0,\"result\":{\"messages\":[" +
//                            "{\"id\":\"06a79050-83a7-11e5-8027-61091fab3b57\",\"topicId\":\"c8a5cfa0-312c-11e5-b553-d7e515311f8d\"," +
//                            "\"title\":\"第222期：牛仔贴纸\",\"teacher\":\"董亚坡老师\"," +
//                            "\"thumbUrl\":\"http://pic.yilos.com/283be79417e0a310eb9ed95a13a71d43\"," +
//                            "\"comment\":{\"content\":\"这尽职尽责尽职尽责\",\"atName\":\"在\"," +
//                            "\"createDate\":1446709317872,\"isHomework\":1},\"reply\":{\"accountId\":\"d77348c0-60d7-11e5-ade9-e3d220e2c964\"," +
//                            "\"accountName\":\"勿忘我\",\"accountPhoto\":\"http://pic.yilos.com/ec9a2bbc1abb13166af6da31495bea0b\"," +
//                            "\"content\":\"嗯嗯\",\"createDate\":1446718910072,\"replyTo\":\"9f59f430-8390-11e5-a74c-839a83b22973\"," +
//                            "\"lastReplyTo\":\"b1438670-8390-11e5-a74c-839a83b22973\"}},\n" +
//                            "{\"id\":\"06a79050-83a7-11e5-8027-61091fab3b57\",\"topicId\":\"c8a5cfa0-312c-11e5-b553-d7e515311f8d\"," +
//                            "\"title\":\"第222期：牛仔贴纸\",\"teacher\":\"董亚坡老师\"," +
//                            "\"thumbUrl\":\"http://pic.yilos.com/283be79417e0a310eb9ed95a13a71d43\"," +
//                            "\"comment\":{\"content\":\"这尽职尽责尽职尽责\",\"atName\":\"在\"," +
//                            "\"createDate\":1446709317872,\"isHomework\":1},\"reply\":{\"accountId\":\"d77348c0-60d7-11e5-ade9-e3d220e2c964\"," +
//                            "\"accountName\":\"勿忘我\",\"accountPhoto\":\"http://pic.yilos.com/ec9a2bbc1abb13166af6da31495bea0b\"," +
//                            "\"content\":\"嗯嗯\",\"createDate\":1448591997582,\"replyTo\":\"9f59f430-8390-11e5-a74c-839a83b22973\"," +
//                            "\"lastReplyTo\":\"b1438670-8390-11e5-a74c-839a83b22973\"}}\n" +
//                            "]}}";
            userMessageJSONObject = new JSONObject(jsonObject);
            if (userMessageJSONObject.getInt(Constants.CODE) != 0) {
                return userMessageList;
            }
            userMessageArray = userMessageJSONObject.getJSONObject(Constants.RESULT).getJSONArray("messages");
            for (int i = 0; i < userMessageArray.length(); i++) {
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
                UserMessage.CommentEntity comment = new UserMessage.CommentEntity(atName, commentContent, commentCreateDate, isHomework);

                String accountId = JsonUtil.optString(userMessageReplyObject, "accountId");
                String accountName = JsonUtil.optString(userMessageReplyObject, "accountName");
                String accountPhoto = JsonUtil.optString(userMessageReplyObject, "accountPhoto");
                String replyContent = JsonUtil.optString(userMessageReplyObject, "content");
                long replyCreateDate = userMessageReplyObject.getLong("createDate");
                String replyTo = JsonUtil.optString(userMessageReplyObject, "replyTo");
                String lastReplyTo = JsonUtil.optString(userMessageReplyObject, "lastReplyTo");
                UserMessage.ReplyEntity reply = new UserMessage.ReplyEntity(accountId, accountName, accountPhoto, replyContent, replyCreateDate, replyTo, lastReplyTo);

                UserMessage userMessage = new UserMessage(comment, id, reply, teacher, thumbUrl, title, topicId, false);
                userMessageList.add(userMessage);
            }
        } catch (IOException e) {
            throw new NetworkDisconnectException("网络获取我的消息失败", e);
        } catch (JSONException e) {
            throw new JSONException("我的消息解析失败");
        } finally {
            return userMessageList;
        }
    }

    @Override
    public MessageComment setComment(MessageComment messageComment, String topicId) throws NetworkDisconnectException, JSONException {
        String result;
        JSONObject messageCommentObject;
        String url = "/vapi/nailstar/topics/" + topicId + "/comments";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constants.AUTHOR, messageComment.getAuthor());
        jsonObject.put(Constants.AT_USER, messageComment.getAtUser());
        jsonObject.put(Constants.CONTENT, messageComment.getContent());
        jsonObject.put(Constants.CONTENT_PIC, messageComment.getContentPic());
        jsonObject.put(Constants.REPLY_TO, messageComment.getReplyTo());
        jsonObject.put(Constants.LAST_REPLY_TO, messageComment.getLastReplyTo());
        jsonObject.put(Constants.SCORE, messageComment.getScore());
        jsonObject.put(Constants.READY, messageComment.getReady());
        try {
            result = HttpClient.post(url, jsonObject.toString());
            messageCommentObject = new JSONObject(result);
            if (messageCommentObject.getInt(Constants.CODE) == 0) {
                return messageComment;
            }
        } catch (IOException e) {
            throw new NetworkDisconnectException("回复失败", e);
        } catch (JSONException e) {
            throw new JSONException("消息装箱失败");
        }
        return messageComment;
    }
}
