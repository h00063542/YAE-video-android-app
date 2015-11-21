package com.yilos.nailstar.social.model;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.yilos.nailstar.framework.application.NailStarApplication;

import java.util.Map;

/**
 * Created by yangdan on 15/11/20.
 */
public class SocialLoginAPI {
    private static final String WEIXIN_APP_ID = "wxeedd9356af3a78b3";
    private static final String WEIXIN_APP_SECRET = "0f3bb78c72569ad1ab64a83e24c69c46";

    private static SocialLoginAPI instance = new SocialLoginAPI();

    private final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.login");

    public static SocialLoginAPI getInstance() {
        return instance;
    }

    private SocialLoginAPI(){
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(NailStarApplication.getApplication(), WEIXIN_APP_ID, WEIXIN_APP_SECRET);
        wxHandler.addToSocialSDK();

        mController.getConfig().setSsoHandler(new SinaSsoHandler());
    }

    // 新浪微博SSO回调
    public void authorizeCallbackSina(int requestCode, int resultCode, Intent data) {
        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode) ;
        if(ssoHandler != null){
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    public void weixinLogin(final Activity activity, final SocialLoginResultListener<LoginData> loginListener) {
        login(activity, new WeixinResultParser(), loginListener);
    }

    public void weiboLogin(final Activity activity, final SocialLoginResultListener<LoginData> loginListener) {
        login(activity, new WeiboResultParser(), loginListener);
    }

    public void qqLogin(final Activity activity, final SocialLoginResultListener<LoginData> loginListener) {
        //参数1为当前Activity， 参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(activity, "1104611687", "LGiugygtm1OcHQEh");
        qqSsoHandler.addToSocialSDK();
        login(activity, new QQResultParser(), loginListener);
    }

    public void login(final Activity activity, final LoginResultParser resultParser, final SocialLoginResultListener<LoginData> loginListener) {
        mController.doOauthVerify(activity, resultParser.getPlatform(), new SocializeListeners.UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                Toast.makeText(activity, "授权开始", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete(final Bundle bundle, SHARE_MEDIA share_media) {
                loginListener.beginLoadData();
                mController.getPlatformInfo(activity, share_media, new SocializeListeners.UMDataListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onComplete(int status, Map<String, Object> info) {
                        Toast.makeText(activity, "授权完成", Toast.LENGTH_SHORT).show();
                        if (status == 200 && info != null) {
                            loginListener.loginSuccess(resultParser.parse(bundle, info));
                        } else {
                            loginListener.loginFail("服务器应答错误！");
                        }
                    }
                });
            }

            @Override
            public void onError(SocializeException e, SHARE_MEDIA share_media) {
                loginListener.loginFail("授权错误");
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
                loginListener.loginFail("授权取消");
            }
        });
    }

    public void logoutWeixin() {
        logout(SHARE_MEDIA.WEIXIN);
    }

    public void logoutSinaWeibo() {
        logout(SHARE_MEDIA.SINA);
    }

    public void logoutQQ() {
        logout(SHARE_MEDIA.QQ);
    }

    private void logout(SHARE_MEDIA platform) {
        mController.deleteOauth(NailStarApplication.getApplication(), platform,
                new SocializeListeners.SocializeClientListener() {
                    @Override
                    public void onStart() {
                    }
                    @Override
                    public void onComplete(int status, SocializeEntity entity) {
                    }
                });
    }

    public class LoginData {
        private String unionId;

        private String openId;

        private String nickName;

        private String avatarUrl;

        private String thirdUserId;

        public String getUnionId() {
            return unionId;
        }

        public void setUnionId(String unionId) {
            this.unionId = unionId;
        }

        public String getOpenId() {
            return openId;
        }

        public void setOpenId(String openId) {
            this.openId = openId;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public String getThirdUserId() {
            return thirdUserId;
        }

        public void setThirdUserId(String thirdUserId) {
            this.thirdUserId = thirdUserId;
        }
    }

    public interface SocialLoginResultListener<T> {
        void beginLoadData();

        void loginSuccess(T loginData);

        void loginFail(String errMsg);
    }

    private interface LoginResultParser {
        LoginData parse(Bundle bundle, Map<String, Object> info);

        SHARE_MEDIA getPlatform();
    }

    private class WeixinResultParser implements LoginResultParser {
        @Override
        public LoginData parse(Bundle bundle, Map<String, Object> info) {
            LoginData data = new LoginData();
            data.setUnionId(info.get("unionid").toString());
            data.setOpenId(info.get("openid").toString());
            data.setNickName(info.get("nickname").toString());
            data.setAvatarUrl(info.get("headimgurl").toString());
            data.setThirdUserId(bundle.getString("uid", null));

            if(data.getOpenId() == null) {
                data.setOpenId(bundle.getString("openid", null));
            }

            return data;
        }

        @Override
        public SHARE_MEDIA getPlatform() {
            return SHARE_MEDIA.WEIXIN;
        }
    }

    private class WeiboResultParser implements LoginResultParser {
        @Override
        public LoginData parse(Bundle bundle, Map<String, Object> info) {
            LoginData data = new LoginData();
            data.setNickName(info.get("screen_name").toString());
            data.setAvatarUrl(info.get("profile_image_url").toString());
            data.setThirdUserId(info.get("uid").toString());
            data.setOpenId(bundle.getString("openid", null));

            if(null == data.getThirdUserId()) {
                data.setThirdUserId(bundle.getString("uid", null));
            }

            return data;
        }

        @Override
        public SHARE_MEDIA getPlatform() {
            return SHARE_MEDIA.SINA;
        }
    }

    private class QQResultParser implements LoginResultParser {
        @Override
        public LoginData parse(Bundle bundle, Map<String, Object> info) {
            LoginData data = new LoginData();
            data.setNickName(info.get("screen_name").toString());
            data.setAvatarUrl(info.get("profile_image_url").toString());
            data.setOpenId(bundle.getString("openid", null));
            data.setThirdUserId(bundle.getString("uid", null));

            return data;
        }

        @Override
        public SHARE_MEDIA getPlatform() {
            return SHARE_MEDIA.QQ;
        }
    }
}
