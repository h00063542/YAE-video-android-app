package com.yilos.nailstar.aboutme.model;

import android.accounts.NetworkErrorException;

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
    public int getMessageCount() throws NetworkDisconnectException, JSONParseException {
        Message message = new Message();
        String jsonObject = "";
        JSONObject countObject = null;
        //{"code":0,"result":{"count":0}}
        try {
            jsonObject = HttpClient.getJson("/vapi/nailstar/messages/count");
            countObject = new JSONObject(jsonObject);
            if (countObject.getInt("code") != 0) {
                return 0;
            }
            message.setCount(countObject.getJSONObject("count").getInt("count"));
        } catch (JSONException e) {

        } catch (Exception e) {
            throw new NetworkDisconnectException("网络连接失败", e);
        }

        return message.getCount();
    }
}
