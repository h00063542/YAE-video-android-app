package com.yilos.nailstar.aboutme.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;
import com.yilos.nailstar.aboutme.entity.AboutMeNumber;
import com.yilos.nailstar.aboutme.entity.FansList;
import com.yilos.nailstar.aboutme.entity.FollowList;
import com.yilos.nailstar.aboutme.entity.MessageCount;
import com.yilos.nailstar.aboutme.entity.PersonInfo;
import com.yilos.nailstar.framework.entity.NailStarApplicationContext;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.util.HttpClient;
import com.yilos.nailstar.util.ImageUtil;
import com.yilos.nailstar.util.JsonUtil;
import com.yilos.nailstar.util.LoggerFactory;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by sisilai on 15/10/24.
 */
public class AboutMeServiceImpl implements AboutMeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AboutMeServiceImpl.class);
    @Override
    public PersonInfo getPersonInfo() throws NetworkDisconnectException, JSONException {
        PersonInfo personInfo = new PersonInfo();
        String jsonObject;
        JSONObject personInfoObject;
        JSONObject resultObject;
        String uid = "a8affd60-efe6-11e4-a908-3132fc2abe39";
        String url = "/vapi/nailstar/account/profile?uid=" + uid;
        try {
            jsonObject = //"{\"code\":0,\"result\":{\"uid\":\"a8affd60-efe6-11e4-a908-3132fc2abe39\",\"nickname\":\"Lolo\",\"type\":6,\"photoUrl\":\"http://pic.yilos.com/7e9ab6e7981380efb88c8ee19ecd0269\",\"profile\":null}}";
            HttpClient.getJson(url);
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
        } catch (IOException e) {
            throw new NetworkDisconnectException("网络获取消息数失败", e);
        } catch (JSONException e) {
            throw new JSONException("消息数解析失败");
        }

    }

    @Override
    public PersonInfo setPersonInfo(String uid,String nickname,int type,String photoUrl,String profile) throws NetworkDisconnectException, JSONException {
        PersonInfo personInfo = new PersonInfo();
        String jsonObject;
        JSONObject personInfoObject;

//        String uid = "a8affd60-efe6-11e4-a908-3132fc2abe39";
//        String nickname = "昵称";
//        int type = 1;
//        String photoUrl = "http://sssdsdsds/sdsdsdsd";
//        String profile = "这是个人签名";

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
        String url = "/vapi/nailstar/messages/count?lt="+ lt + "&uid=" + uid + "&type=" + type;
        //String url = "/vapi2/nailstar/messages/count";
        try {
            jsonObject = HttpClient.getJson(url);
            //"{\"code\":0,\"result\":{\"count\":3}}";
            messageCountObject = new JSONObject(jsonObject);
            if (messageCountObject.getInt("code") != 0) {
                return null;
            }
            messageCount.setCount(messageCountObject.getJSONObject("result").getInt("count"));
            return messageCount;
        } catch (IOException e) {
            throw new NetworkDisconnectException("网络获取消息数失败", e);
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
                return aboutMeNumber;
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

    @Override
    public ArrayList<FollowList> getFollowList(String uid) throws NetworkDisconnectException {
        if (!NailStarApplicationContext.getInstance().isNetworkConnected()) {
            throw new NetworkDisconnectException("网络没有连接");
        }
        String jsonObject;
        JSONObject followListObject;
        JSONArray followListUsersArray;
        ArrayList<FollowList> followLists = new ArrayList<FollowList>();
        Bitmap imageBitmap = null;
        String photoUrl;
        //String uid = "a8affd60-efe6-11e4-a908-3132fc2abe39";
        //http://api2.naildaka.com/vapi/nailstar/account/a8affd60-efe6-11e4-a908-3132fc2abe39/followList?page=1
        String url = "/vapi/nailstar/account/" + uid + "/followList?page=1";
        try {
            jsonObject = HttpClient.getJson(url);
            //"{\"code\":0,\"result\":{\"users\":[{\"accountId\":\"066cf7d0-20cc-11e5-8c5f-976ea1df5a0b\",\"nickname\":\"sluor\",\"type\":5,\"photoUrl\":null,\"profile\":null},{\"accountId\":\"0690bcf0-0b56-11e5-ab1c-19acfe3e02c3\",\"nickname\":\"PN~大象\",\"type\":5,\"photoUrl\":\"http://pic.yilos.com/4856ce582e504bb62917759606ed2e3a\",\"profile\":null},{\"accountId\":\"07bf2ff0-10ea-11e5-aac6-adb2ba47dce6\",\"nickname\":\"格格，格格\",\"type\":5,\"photoUrl\":\"http://pic.yilos.com/2c0325ccb60f7d7046af4ffeae555ba8\",\"profile\":null},{\"accountId\":\"096d2e00-f944-11e4-a616-5fd981a864f9\",\"nickname\":\"王红老师\",\"type\":5,\"photoUrl\":\"http://pic.yilos.com/da89a693f41d092aa6ac6636a2682624\",\"profile\":null},{\"accountId\":\"09e75780-0f73-11e5-bbc3-1b530b7063a3\",\"nickname\":\"手机用户4043\",\"type\":5,\"photoUrl\":\"http://pic.yilos.com/b379b447bc76f0644ca88dbe85c91469\",\"profile\":null},{\"accountId\":\"0d979ea0-117e-11e5-aac6-adb2ba47dce6\",\"nickname\":\"稻草老板\",\"type\":5,\"photoUrl\":\"http://pic.yilos.com/b5e3d07ef7fec4ad27bc698e81257829\",\"profile\":null},{\"accountId\":\"0f0ed700-2653-11e5-a88e-fdfbd1e95ab4\",\"nickname\":\"指爱你\",\"type\":5,\"photoUrl\":\"http://pic.yilos.com/2ed56a15dda4b84ee3e017df2d9f004c\",\"profile\":null},{\"accountId\":\"1\",\"nickname\":\"Kimi老师\",\"type\":5,\"photoUrl\":\"http://pic.yilos.com/5180d65d1c25151ce7157ef510e35cd1\",\"profile\":null},{\"accountId\":\"1154f3f0-f944-11e4-a616-5fd981a864f9\",\"nickname\":\"董亚坡老师\",\"type\":5,\"photoUrl\":\"http://pic.yilos.com/02f411645410a3d32f94b59466dfc019\",\"profile\":\"哥哥哥哥哥哥恍恍惚惚恍恍惚惚后悔恍恍惚惚恍恍惚惚隐隐约约\"},{\"accountId\":\"14be1160-29dd-11e5-8eb7-e53278ec7d31\",\"nickname\":\"手机用户3147\",\"type\":5,\"photoUrl\":null,\"profile\":null},{\"accountId\":\"17ab47e0-1113-11e5-8b80-a3f63c025f8f\",\"nickname\":\"东子\",\"type\":5,\"photoUrl\":\"http://pic.yilos.com/c27e2b0a0aa77745ac99c78119191703\",\"profile\":null},{\"accountId\":\"1959a1e0-222f-11e5-be2b-e35c4c3fc600\",\"nickname\":\"vicky\",\"type\":5,\"photoUrl\":\"http://pic.yilos.com/469319a26eca94622fc559112281a5f6\",\"profile\":null},{\"accountId\":\"1d6484c0-0480-11e5-9619-4387ab0c5156\",\"nickname\":\"汤汤老师\",\"type\":5,\"photoUrl\":\"http://pic.yilos.com/672875f0b8474bc41269897b5019dcfe\",\"profile\":null},{\"accountId\":\"29aafd60-fb1e-11e4-8889-8f2786c0b7fc\",\"nickname\":\"东东老师\",\"type\":5,\"photoUrl\":\"http://pic.yilos.com/939daa8860d5a6c83be1a29c9c885b64\",\"profile\":null},{\"accountId\":\"24f912a0-1b1e-11e5-a63f-dda38fcc1100\",\"nickname\":\"手机用户0606\",\"type\":5,\"photoUrl\":\"http://pic.yilos.com/d0f6d1bb5c5f51e1ea59dfce8758faac\",\"profile\":null}]}}";
            followListObject = new JSONObject(jsonObject);
            if (followListObject.getInt("code") != 0) {
                return followLists;
            }
            followListUsersArray = followListObject.getJSONObject("result").getJSONArray("users");
            for (int index = 0;index < followListUsersArray.length(); index ++) {
                photoUrl = JsonUtil.optString(followListUsersArray.optJSONObject(index), "photoUrl");
                if(photoUrl != null) {
                    byte[] data;
                    try {
                        data = ImageUtil.getBytes(new URL(photoUrl).openStream());
                        imageBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    } catch (IOException e) {
                        e.printStackTrace();
                        LOGGER.error("获取imageBitmap失败，imageBitmap:" + imageBitmap + "index:" + index + "photoUrl:" + photoUrl, e);
                    }
                }

                followLists.add(new FollowList(
                        JsonUtil.optString(followListUsersArray.optJSONObject(index),"accountId"),
                        JsonUtil.optString(followListUsersArray.optJSONObject(index),"nickname"),
                        photoUrl,
                        JsonUtil.optString(followListUsersArray.optJSONObject(index),"profile"),
                        followListUsersArray.optJSONObject(index).getInt("type"),
                        imageBitmap
                ));
            }
        } catch (IOException e) {
            throw new NetworkDisconnectException("网络获取我的关注列表失败", e);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return followLists;
    }

    @Override
    public ArrayList<FansList> getFansList(String uid) throws NetworkDisconnectException {
        if (!NailStarApplicationContext.getInstance().isNetworkConnected()) {
            throw new NetworkDisconnectException("网络没有连接");
        }
        String jsonObject;
        JSONObject fansListObject;
        JSONArray fansListUsersArray;
        ArrayList<FansList> fansLists = new ArrayList<FansList>();
        Bitmap imageBitmap = null;
        String photoUrl;
        //String uid = "a8affd60-efe6-11e4-a908-3132fc2abe39";
        //http://api2.naildaka.com/vapi/nailstar/account/a8affd60-efe6-11e4-a908-3132fc2abe39/fansList?page=1
        String url = "/vapi/nailstar/account/" + uid + "/fansList?page=1";
        try {
            jsonObject = HttpClient.getJson(url);
            //"{\"code\":0,\"result\":{\"users\":[{\"accountId\":\"dc282890-f87c-11e4-b13e-57eb04c66d6e\",\"nickname\":\"大咖程序猿\",\"type\":6,\"photoUrl\":\"http://pic.yilos.com/5f8d77bef850f6dd90a95688803b2929\",\"profile\":null},{\"accountId\":\"dc282890-f87c-11e4-b13e-57eb04c66d6e\",\"nickname\":\"大咖程序猿\",\"type\":6,\"photoUrl\":\"http://pic.yilos.com/5f8d77bef850f6dd90a95688803b2929\",\"profile\":null},{\"accountId\":\"dc282890-f87c-11e4-b13e-57eb04c66d6e\",\"nickname\":\"大咖程序猿\",\"type\":5,\"photoUrl\":\"http://pic.yilos.com/5f8d77bef850f6dd90a95688803b2929\",\"profile\":null}]}}";
            fansListObject = new JSONObject(jsonObject);
            if (fansListObject.getInt("code") != 0) {
                return fansLists;
            }
            fansListUsersArray = fansListObject.getJSONObject("result").getJSONArray("users");
            for (int index = 0;index < fansListUsersArray.length(); index ++) {
                photoUrl = JsonUtil.optString(fansListUsersArray.optJSONObject(index), "photoUrl");
                if(photoUrl != null) {
                    byte[] data;
                    try {
                        data = ImageUtil.getBytes(new URL(photoUrl).openStream());
                        imageBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    } catch (IOException e) {
                        e.printStackTrace();
                        LOGGER.error("获取imageBitmap失败，imageBitmap:" + imageBitmap + "index:" + index + "photoUrl:" + photoUrl, e);
                    }
                }

                fansLists.add(new FansList(
                        JsonUtil.optString(fansListUsersArray.optJSONObject(index),"accountId"),
                        JsonUtil.optString(fansListUsersArray.optJSONObject(index),"nickname"),
                        photoUrl,
                        JsonUtil.optString(fansListUsersArray.optJSONObject(index),"profile"),
                        fansListUsersArray.optJSONObject(index).getInt("type"),
                        imageBitmap
                ));
            }
        } catch (IOException e) {
            throw new NetworkDisconnectException("网络获取我的关注列表失败", e);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return fansLists;
    }
}
