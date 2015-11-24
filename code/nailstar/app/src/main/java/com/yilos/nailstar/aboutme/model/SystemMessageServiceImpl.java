package com.yilos.nailstar.aboutme.model;

import com.yilos.nailstar.aboutme.entity.SystemMessage;
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
 * Created by sisilai on 15/11/21.
 */
public class SystemMessageServiceImpl implements SystemMessageService {
    @Override
    public List<Object> getSystemMessage(long lt) throws NetworkDisconnectException, JSONException {
        List<Object> objectList = new ArrayList<>();
        ArrayList<SystemMessage> systemMessageList = new ArrayList<>();
        String jsonObject = null;
        JSONObject systemMessageJSONObject;
        JSONArray systemMessageArray;
        String url = "/vapi/nailstar/messages?lt=" + lt;
        try {
            jsonObject = HttpClient.getJson(url);
            systemMessageJSONObject = new JSONObject(jsonObject);
            if (systemMessageJSONObject.getInt(Constants.CODE) != 0) {
                return objectList;
            }
            systemMessageArray = systemMessageJSONObject.getJSONObject(Constants.RESULT).getJSONArray(Constants.MESSAGES);

            long latestMessageTime = systemMessageJSONObject.getJSONObject(Constants.RESULT).getLong(Constants.LATEST_MESSAGE_TIME);

            for (int i = 0; i < systemMessageArray.length(); i++) {
                JSONObject systemMessageObject = systemMessageArray.getJSONObject(i);

                String id = JsonUtil.optString(systemMessageObject, Constants.ID);
                String topicId = JsonUtil.optString(systemMessageObject, Constants.TOPIC_ID);
                String title = JsonUtil.optString(systemMessageObject, Constants.TITLE);
                String content = JsonUtil.optString(systemMessageObject, Constants.CONTENT);
                long publishDate = systemMessageObject.getLong(Constants.PUBLISH_DATE);

                SystemMessage systemMessage = new SystemMessage(content, false, id, publishDate, title, topicId);

                systemMessageList.add(systemMessage);
            }
            objectList.add(0, systemMessageList);
            objectList.add(1, latestMessageTime);

        } catch (IOException e) {
            throw new NetworkDisconnectException("网络获取系统消息失败", e);
        } catch (JSONException e) {
            throw new JSONException("系统消息解析失败");
        } finally {
            return objectList;
        }
    }

}
