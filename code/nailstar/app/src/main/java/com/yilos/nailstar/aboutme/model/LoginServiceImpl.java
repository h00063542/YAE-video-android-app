package com.yilos.nailstar.aboutme.model;

import com.yilos.nailstar.aboutme.entity.PersonInfo;
import com.yilos.nailstar.framework.entity.NailStarApplicationContext;
import com.yilos.nailstar.framework.exception.CommonException;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.util.HttpClient;
import com.yilos.nailstar.util.JsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by yangdan on 15/11/10.
 */
public class LoginServiceImpl {
    private static LoginServiceImpl instance = new LoginServiceImpl();

    private LoginServiceImpl() {
    }

    public static LoginServiceImpl getInstance() {
        return instance;
    }

    /**
     * 判断客户端是否已经登录
     *
     * @return
     */
    public boolean isLogin() {
        return false;
    }

    /**
     * 向指定手机发送验证码
     *
     * @param phoneNumber
     */
    public void sendValidateCode(final String phoneNumber) throws CommonException {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phoneMobile", phoneNumber);
            String result = HttpClient.post("/vapi/nailstar/vc/getCode", jsonObject.toString());
            JSONObject resultObject = new JSONObject(result);
            if (!resultObject.has("code") || resultObject.optInt("code") != 0) {
                throw new CommonException("获取验证码失败，请联系客服！");
            }
        } catch (JSONException e) {
            throw new CommonException("获取验证码失败，请联系客服！", e);
        } catch (IOException e) {
            throw new CommonException("获取验证码失败，请联系客服！", e);
        }
    }

    /**
     * 检查验证码是否正确
     *
     * @param phoneNumber
     * @param validateCode
     * @return
     * @throws CommonException
     */
    public boolean checkValidateCode(String phoneNumber, String validateCode) throws CommonException {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phoneMobile", phoneNumber);
            jsonObject.put("verificationCode", validateCode);
            String result = HttpClient.post("/vapi/nailstar/vc/checkCode", jsonObject.toString());
            JSONObject resultObject = new JSONObject(result);
            if (!resultObject.has("code") || resultObject.optInt("code") != 0) {
                if ("32322323".equals(JsonUtil.optString(resultObject, "errorCode"))) {
                    throw new CommonException("验证码已失效，请重新获取");
                } else if ("30040002".equals(JsonUtil.optString(resultObject, "errorCode"))) {
                    throw new CommonException("验证码错误，请重新输入");
                } else {
                    throw new CommonException("验证码已失效，请重新获取");
                }
            }
            return true;
        } catch (JSONException e) {
            throw new CommonException("校验验证码失败，请联系客服", e);
        } catch (IOException e) {
            throw new CommonException("校验验证码失败，请联系客服", e);
        }
    }

    /**
     * 注册手机号
     *
     * @param phoneNumber
     * @param password
     */
    public void register(String phoneNumber, String password) throws CommonException {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", phoneNumber);
            jsonObject.put("password", password);
            String result = HttpClient.post("/vapi/nailstar/account/signup", jsonObject.toString());
            JSONObject resultObject = new JSONObject(result);
            if (!resultObject.has("code") || resultObject.optInt("code") != 0) {
                if (1 == resultObject.optInt("errorCode")) {
                    throw new CommonException("手机号已被注册");
                } else {
                    throw new CommonException("注册失败，请重新注册");
                }
            }
        } catch (JSONException e) {
            throw new CommonException("注册失败，请重新注册", e);
        } catch (IOException e) {
            throw new CommonException("服务器出错，请联系客服", e);
        }
    }

    /**
     * 重置密码
     *
     * @param phoneNumber
     * @param password
     */
    public void resetPassword(String phoneNumber, String password, String validateCode) throws CommonException {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", phoneNumber);
            jsonObject.put("password", password);
            String result = HttpClient.post("/vapi/nailstar/account/resetPwd", jsonObject.toString());
            JSONObject resultObject = new JSONObject(result);
            if (!resultObject.has("code") || resultObject.optInt("code") != 0) {
                if (1 == resultObject.optInt("errorCode")) {
                    throw new CommonException("用户不存在");
                } else {
                    throw new CommonException("重新设置密码失败，请重试");
                }
            }
        } catch (JSONException e) {
            throw new CommonException("重新设置密码失败，请重试", e);
        } catch (IOException e) {
            throw new CommonException("服务器出错，请联系客服", e);
        }
    }

    public String login(String userName, String password) throws CommonException, NetworkDisconnectException {
        if (!NailStarApplicationContext.getInstance().isNetworkConnected()) {
            throw new NetworkDisconnectException("网络好像不给力哦，请检查网络设置");
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", userName);
            jsonObject.put("password", password);
            String result = HttpClient.post("/vapi/nailstar/account/login", jsonObject.toString());
            JSONObject resultObject = new JSONObject(result);
            if (!resultObject.has("code") || resultObject.optInt("code") != 0) {
                if (1 == resultObject.optInt("errorCode")) {
                    throw new CommonException("用户名不存在");
                } else if (2 == resultObject.optInt("errorCode")) {
                    throw new CommonException("用户名或密码错误");
                } else {
                    throw new CommonException("登陆失败，请联系客服");
                }
            } else {
                JSONObject loginResult = resultObject.getJSONObject("result");
                return loginResult.getString("uid");
            }
        } catch (JSONException e) {
            throw new CommonException("登录失败，请稍后重试", e);
        } catch (IOException e) {
            throw new CommonException("网络好像不给力哦，请检查网络设置", e);
        }
    }

    public String weixinLogin(String unionId, String openId, String nickName, String avatar) throws CommonException, NetworkDisconnectException {
        if (!NailStarApplicationContext.getInstance().isNetworkConnected()) {
            throw new NetworkDisconnectException("网络好像不给力哦，请检查网络设置");
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("openId", openId);
            jsonObject.put("unionId", unionId);
            jsonObject.put("nickname", nickName);
            jsonObject.put("avatar", avatar);
            String result = HttpClient.post("/vapi/nailstar/account/wxLogin", jsonObject.toString());
            JSONObject resultObject = new JSONObject(result);
            if (!resultObject.has("code") || resultObject.optInt("code") != 0) {
                throw new CommonException("登陆失败，请联系客服");
            } else {
                JSONObject loginResult = resultObject.getJSONObject("result");
                return loginResult.getString("userId");
            }
        } catch (JSONException e) {
            throw new CommonException("登录失败，请稍后重试", e);
        } catch (IOException e) {
            throw new CommonException("网络好像不给力哦，请检查网络设置", e);
        }
    }

    public String weiboLogin(String unionId, String openId, String nickName, String avatar, String weiboUserId) throws CommonException, NetworkDisconnectException {
        if (!NailStarApplicationContext.getInstance().isNetworkConnected()) {
            throw new NetworkDisconnectException("网络好像不给力哦，请检查网络设置");
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("openId", openId);
            jsonObject.put("unionId", unionId);
            jsonObject.put("nickname", nickName);
            jsonObject.put("avatar", avatar);
            jsonObject.put("weiboUserId", weiboUserId);
            String result = HttpClient.post("/vapi/nailstar/account/weiboLogin", jsonObject.toString());
            JSONObject resultObject = new JSONObject(result);
            if (!resultObject.has("code") || resultObject.optInt("code") != 0) {
                throw new CommonException("登陆失败，请联系客服");
            } else {
                JSONObject loginResult = resultObject.getJSONObject("result");
                return loginResult.getString("userId");
            }
        } catch (JSONException e) {
            throw new CommonException("登录失败，请稍后重试", e);
        } catch (IOException e) {
            throw new CommonException("网络好像不给力哦，请检查网络设置", e);
        }
    }

    public String qqLogin(String unionId, String openId, String nickName, String avatar, String qqUserId) throws CommonException, NetworkDisconnectException {
        if (!NailStarApplicationContext.getInstance().isNetworkConnected()) {
            throw new NetworkDisconnectException("网络好像不给力哦，请检查网络设置");
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("openId", openId);
            jsonObject.put("unionId", unionId);
            jsonObject.put("nickname", nickName);
            jsonObject.put("avatar", avatar);
            jsonObject.put("qqUserId", qqUserId);
            String result = HttpClient.post("/vapi/nailstar/account/qqLogin", jsonObject.toString());
            JSONObject resultObject = new JSONObject(result);
            if (!resultObject.has("code") || resultObject.optInt("code") != 0) {
                throw new CommonException("登陆失败，请联系客服");
            } else {
                JSONObject loginResult = resultObject.getJSONObject("result");
                return loginResult.getString("userId");
            }
        } catch (JSONException e) {
            throw new CommonException("登录失败，请稍后重试", e);
        } catch (IOException e) {
            throw new CommonException("网络好像不给力哦，请检查网络设置", e);
        }
    }

    /**
     * 查询用户的个人信息
     *
     * @param uid
     * @return
     * @throws NetworkDisconnectException
     * @throws CommonException
     */
    public PersonInfo queryPersonInfo(String uid) throws NetworkDisconnectException, CommonException {
        if (!NailStarApplicationContext.getInstance().isNetworkConnected()) {
            throw new NetworkDisconnectException("网络好像不给力哦，请检查网络设置");
        }

        PersonInfo personInfo = new PersonInfo();
        try {
            String result = HttpClient.getJson("/vapi/nailstar/account/profile?uid=" + uid);
            JSONObject resultObject = new JSONObject(result);
            if (!resultObject.has("code") || resultObject.optInt("code") != 0) {
                return personInfo;
            } else {
                JSONObject profileResult = resultObject.getJSONObject("result");
                personInfo.setUid(uid);
                personInfo.setPhotoUrl(JsonUtil.optString(profileResult, "photoUrl"));
                personInfo.setNickname(JsonUtil.optString(profileResult, "nickname"));
                personInfo.setProfile(JsonUtil.optString(profileResult, "profile"));
                personInfo.setType(profileResult.getInt("type"));

                return personInfo;
            }
        } catch (JSONException e) {
            throw new CommonException("登录失败，请稍后重试", e);
        } catch (IOException e) {
            throw new CommonException("网络好像不给力哦，请检查网络设置", e);
        }
    }
}
