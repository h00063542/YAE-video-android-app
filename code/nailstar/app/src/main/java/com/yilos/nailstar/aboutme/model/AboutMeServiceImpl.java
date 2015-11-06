package com.yilos.nailstar.aboutme.model;

import android.graphics.BitmapFactory;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;
import com.yilos.nailstar.aboutme.entity.AboutMeNumber;
import com.yilos.nailstar.aboutme.entity.MessageCount;
import com.yilos.nailstar.aboutme.entity.PersonInfo;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.util.HttpClient;
import com.yilos.nailstar.util.ImageUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

/**
 * Created by sisilai on 15/10/24.
 */
public class AboutMeServiceImpl implements AboutMeService {
    @Override
    public PersonInfo getPersonInfo() throws NetworkDisconnectException, JSONException {
        PersonInfo personInfo = new PersonInfo();
        String jsonObject;
        JSONObject personInfoObject;
        JSONObject resultObject;
        String uid = "a8affd60-efe6-11e4-a908-3132fc2abe39";
        String url = "/vapi2/nailstar/account/profile?uid=" + uid;
        try {
            jsonObject = "{\"code\":0,\"result\":{\"uid\":\"a8affd60-efe6-11e4-a908-3132fc2abe39\",\"nickname\":\"Lolo\",\"type\":6,\"photoUrl\":\"http://pic.yilos.com/7e9ab6e7981380efb88c8ee19ecd0269\",\"profile\":null}}";
            //HttpClient.getJson(url);
//        } catch (IOException e) {
//            throw new NetworkDisconnectException("网络获取消息数失败", e);
//        }
//        try {
            personInfoObject = new JSONObject(jsonObject);
            if (personInfoObject.getInt("code") != 0) {
                return null;
            }
            resultObject = personInfoObject.getJSONObject("result");
            personInfo.setUid(resultObject.getString("uid"));
            personInfo.setNickname(resultObject.getString("nickname"));
            personInfo.setPhotoUrl(resultObject.getString("photoUrl"));
            personInfo.setType(resultObject.getInt("type"));
            personInfo.setProfile(resultObject.getString("profile"));
            byte[] data;
            try {
                data = ImageUtil.getBytes(new URL(personInfo.getPhotoUrl()).openStream());
                personInfo.setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.length));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return personInfo;
        } catch (JSONException e) {
            throw new JSONException("消息数解析失败");
        }

    }

    @Override
    public PersonInfo setPersonInfo() throws NetworkDisconnectException, JSONException {
        PersonInfo personInfo = new PersonInfo();
        String jsonObject;
        JSONObject personInfoObject;

        String uid = "a8affd60-efe6-11e4-a908-3132fc2abe39";
        String nickname = "昵称";
        int type = 1;
        String photoUrl = "http://sssdsdsds/sdsdsdsd";
        String profile = "这是个人签名";

        personInfo.setNickname(nickname);
        personInfo.setType(type);
        personInfo.setProfile(profile);
        personInfo.setPhotoUrl(photoUrl);
        personInfo.setUid(uid);
        String url = "/vapi2/nailstar/account/profile/" + personInfo.getUid();
        RequestBody formBody = new FormEncodingBuilder()
                .add("nickname", personInfo.getNickname())
                .add("type", String.valueOf(personInfo.getType()))
                .add("photoUrl", personInfo.getPhotoUrl())
                .add("profile",personInfo.getProfile())
                .build();
        try {
            jsonObject = HttpClient.post(url,formBody);
        } catch (IOException e) {
            throw new NetworkDisconnectException("网络更新个人资料失败",e);
        }
        personInfoObject = new JSONObject(jsonObject);
        if (personInfoObject.getInt("code") != 0) {
            return null;
        }
        if (personInfoObject.getJSONObject("result").getString("messages") == "ok" ) {
            return personInfo;
        }
        return null;
    }

    @Override
    public MessageCount getMessageCount() throws NetworkDisconnectException, JSONException {
        MessageCount messageCount = new MessageCount();
        String jsonObject;
        JSONObject messageCountObject;
        String lt = "1445669825802";
        String uid = "a8affd60-efe6-11e4-a908-3132fc2abe39";
        int type = 5;
        //String url = "/vapi2/nailstar/messages/count?lt="+ lt + "&uid=" + uid + "&type=" + type;
        String url = "/vapi2/nailstar/messages/count";
        try {
            jsonObject = "{\"code\":0,\"result\":{\"count\":3}}";//HttpClient.getJson(url);
//        } catch (IOException e) {
//            throw new NetworkDisconnectException("网络获取消息数失败", e);
//        }
//        try {
            messageCountObject = new JSONObject(jsonObject);
            if (messageCountObject.getInt("code") != 0) {
                return null;
            }
            messageCount.setCount(messageCountObject.getJSONObject("result").getInt("count"));
            return messageCount;
        }catch (JSONException e) {
            throw new JSONException("消息数解析失败");
        }
    }

    @Override
    public AboutMeNumber getAboutMeNumber() throws NetworkDisconnectException, JSONException {
        AboutMeNumber aboutMeNumber = new AboutMeNumber();
        String jsonObject;
        JSONObject aboutMeNumberObject;
        JSONObject aboutMeNumberResult;
        String uid = "a8affd60-efe6-11e4-a908-3132fc2abe39";
        //http://api2.naildaka.com/vapi/nailstar/account/myInfo/a8affd60-efe6-11e4-a908-3132fc2abe39
        String url = "/vapi/nailstar/account/myInfo/" + uid;
        try {
            jsonObject = HttpClient.getJson(url);
            //"{\"code\":0,\"result\":{\"fansNumber\":2,\"focusNumber\":105,\"exp\":544,\"dakaCoin\":264}}";
            aboutMeNumberObject = new JSONObject(jsonObject);
            if (aboutMeNumberObject.getInt("code") != 0) {
                return null;
            }
            aboutMeNumberResult = aboutMeNumberObject.getJSONObject("result");
            aboutMeNumber.setDakaCoin(aboutMeNumberResult.getInt("dakaCoin"));
            aboutMeNumber.setExp(aboutMeNumberResult.getInt("exp"));
            aboutMeNumber.setFansNumber(aboutMeNumberResult.getInt("fansNumber"));
            aboutMeNumber.setFocusNumber(aboutMeNumberResult.getInt("focusNumber"));
            return aboutMeNumber;
        } catch (IOException e) {
            throw new NetworkDisconnectException("网络获取我的页面的经验、咖币、粉丝数和关注数信息失败", e);
        }
    }
}
