package com.yilos.nailstar.aboutme.model;

import com.yilos.nailstar.aboutme.entity.Message;
import com.yilos.nailstar.framework.exception.JSONParseException;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.util.HttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by sisilai on 15/10/24.
 */
public class AboutMeServiceImpl implements AboutMeService {
    @Override
    public Message getMessageContext(Message message) throws NetworkDisconnectException, JSONParseException {
        String jsonObject = "";
        JSONObject countObject = null;
        try {
            jsonObject = HttpClient.getJson("/vapi/nailstar/messages/count");
        } catch (IOException e) {
            throw new NetworkDisconnectException("读取失败", e);
        } catch (Exception e){
            throw new NetworkDisconnectException("读取失败", e);
        }
        try {
            countObject = new JSONObject(jsonObject);
            if (countObject.getInt("code") != 0) {
                return null;
            }
            message.setCount(2);//countObject.getJSONObject("count").getInt("count"));
            return message;
        }catch (JSONException e) {
            throw new NetworkDisconnectException("网络连接失败", e);
        }

    }
}
