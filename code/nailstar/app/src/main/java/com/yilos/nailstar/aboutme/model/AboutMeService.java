package com.yilos.nailstar.aboutme.model;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.yilos.nailstar.aboutme.entity.MessageCount;
import com.yilos.nailstar.aboutme.entity.PersonInfo;
import com.yilos.nailstar.framework.exception.JSONParseException;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.util.HttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okio.BufferedSink;

/**
 * Created by sisilai on 15/10/24.
 */
public class AboutMeService implements AboutMeServiceImpl {
    PersonInfo personInfo = new PersonInfo();

//    GET /vapi2/nailstar/account/profile?uid=12345
//
//            ```
//    {
//        uid: "kjfksdfs5645",
//                nickname: “昵称”,
//        type: “1”,
//        photoUrl: "http://sdsdsdsdsdsdsds/sdsd",
//                profile: "这是我的个人签名"
//    }
//    ```
//
//    不返回gender, location, birthday字段
    @Override
    public PersonInfo getPersonInfo() throws NetworkDisconnectException,JSONException{
        String jsonObject;
        JSONObject personInfoObject;
        JSONObject resultObject;
        String uid = "a8affd60-efe6-11e4-a908-3132fc2abe39";
        String url = "/vapi2/nailstar/account/profile?uid=" + uid;
        try {
            jsonObject = HttpClient.getJson(url);
        } catch (IOException e) {
            throw new NetworkDisconnectException("网络获取消息数失败", e);
        }
        try {
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
            return personInfo;
        }catch (JSONException e) {
            throw new JSONException("消息数解析失败");
        }
    }

    //    ##修改个人资料（增加分支）
//
//    POST /vapi2/nailstar/account/profile/12345
//
//            ```
//    {
//        nickname: “昵称”,
//        type: “1”,
//        photoUrl: "http://sssdsdsds/sdsdsdsd",
//        profile: "这是个人签名"
//    }
//    ```
//
//            ```
//    {
//        messages: “ok”
//    }
//    ```
//
//    去掉了gender, location, birthday字段，如果没有，服务端就不存
    @Override
    public PersonInfo setPersonInfo() throws NetworkDisconnectException, JSONException {
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

    MessageCount messageCount = new MessageCount();
    @Override
    public MessageCount getMessageCount() throws NetworkDisconnectException, JSONException {
        String jsonObject;
        JSONObject messageCountObject;
        String lt = "1445669825802";
        String uid = "a8affd60-efe6-11e4-a908-3132fc2abe39";
        int type = 5;
        String url = "/vapi2/nailstar/messages/count?lt="+ lt + "&uid=" + uid + "&type=" + type;
        try {
            jsonObject = HttpClient.getJson(url);//"{\"code\":0,\"result\":{\"count\":3}}";
        } catch (IOException e) {
            throw new NetworkDisconnectException("网络获取消息数失败", e);
        }
        try {
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
}
