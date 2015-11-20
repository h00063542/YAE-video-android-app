package com.yilos.nailstar.social.model;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.yilos.nailstar.framework.application.NailStarApplication;

import java.util.Map;
import java.util.Set;

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
    }

    public void weixinLogin(final Activity activity) {
        mController.doOauthVerify(activity, SHARE_MEDIA.WEIXIN, new SocializeListeners.UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                Toast.makeText(activity, "授权开始", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onComplete(Bundle bundle, SHARE_MEDIA share_media) {
                Toast.makeText(activity, "授权完成", Toast.LENGTH_SHORT).show();
                mController.getPlatformInfo(activity, SHARE_MEDIA.WEIXIN, new SocializeListeners.UMDataListener() {
                    @Override
                    public void onStart() {
                        Toast.makeText(activity, "获取平台数据开始...", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete(int status, Map<String, Object> info) {
                        if(status == 200 && info != null){
                            StringBuilder sb = new StringBuilder();
                            Set<String> keys = info.keySet();
                            for(String key : keys){
                                sb.append(key+"="+info.get(key).toString()+"\r\n");
                            }
                        }else{
                        }
                    }
                });
            }

            @Override
            public void onError(SocializeException e, SHARE_MEDIA share_media) {
                Toast.makeText(activity, "授权错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
                Toast.makeText(activity, "授权取消", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
