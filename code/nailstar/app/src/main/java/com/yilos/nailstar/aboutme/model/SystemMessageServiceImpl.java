package com.yilos.nailstar.aboutme.model;

import android.support.annotation.NonNull;

import com.yilos.nailstar.aboutme.entity.SystemMessage;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.util.Constants;
import com.yilos.nailstar.util.JsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by sisilai on 15/11/21.
 */
public class SystemMessageServiceImpl implements SystemMessageService {
    Map<String,String> map = new Map<String, String>() {
        @Override
        public void clear() {

        }

        @Override
        public boolean containsKey(Object key) {
            return false;
        }

        @Override
        public boolean containsValue(Object value) {
            return false;
        }

        @NonNull
        @Override
        public Set<Entry<String, String>> entrySet() {
            return null;
        }

        @Override
        public String get(Object key) {
            return null;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @NonNull
        @Override
        public Set<String> keySet() {
            return null;
        }

        @Override
        public String put(String key, String value) {
            return null;
        }

        @Override
        public void putAll(Map<? extends String, ? extends String> map) {

        }

        @Override
        public String remove(Object key) {
            return null;
        }

        @Override
        public int size() {
            return 0;
        }

        @NonNull
        @Override
        public Collection<String> values() {
            return null;
        }
    };
    @Override
    public List<Object> getSystemMessage(long lt) throws NetworkDisconnectException, JSONException {
        List<Object> objectList = new ArrayList<>();
        List<SystemMessage> systemMessageList = new ArrayList<>();
        String jsonObject;
        JSONObject systemMessageJSONObject;
        JSONArray systemMessageArray;
        String url = "/vapi/nailstar/messages?lt=" + lt;
        jsonObject = //HttpClient.getJson(url);
                "{\"code\":0,\"result\":{" +
                        "\"latestMessageTime\":1448021486748," +
                        "\"messages\":[" +
                "{\"id\":\"d2f3cdc0-8f7f-11e5-ab99-1f385dee6318\",\"title\":\"美甲大咖团队\"," +
                        "\"content\":\"超级实用的纵向晕染技巧\",\"publishDate\":1448021486748," +
                        "\"topicId\":\"39f830b0-8f3a-11e5-ab99-1f385dee6318\"}" +
                        "]}}";
        systemMessageJSONObject = new JSONObject(jsonObject);
        if (systemMessageJSONObject.getInt(Constants.CODE) != 0) {
            return objectList;
        }
        systemMessageArray = systemMessageJSONObject.getJSONObject(Constants.RESULT).getJSONArray(Constants.MESSAGES);

        long latestMessageTime = systemMessageJSONObject.getJSONObject(Constants.RESULT).getLong(Constants.LATEST_MESSAGE_TIME);

        for(int i=0;i<systemMessageArray.length();i++){
            JSONObject systemMessageObject = systemMessageArray.getJSONObject(i);

            String id = JsonUtil.optString(systemMessageObject, Constants.ID);
            String topicId = JsonUtil.optString(systemMessageObject, Constants.TOPIC_ID);
            String title = JsonUtil.optString(systemMessageObject, Constants.TITLE);
            String content = JsonUtil.optString(systemMessageObject, Constants.CONTENT);
            long publishDate = systemMessageObject.getLong(Constants.PUBLISH_DATE);

            SystemMessage systemMessage = new SystemMessage(content, false, id, publishDate, title, topicId);

            systemMessageList.add(systemMessage);
        }
        objectList.add(0,systemMessageList);
        objectList.add(1,latestMessageTime);
        return objectList;
    }

}
