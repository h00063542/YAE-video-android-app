package com.yilos.nailstar.social.model;

import android.app.Activity;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import com.yilos.nailstar.framework.application.NailStarApplication;

/**
 * Created by yangdan on 15/11/20.
 */
public class SocialAPI {
    static final String WEIXIN_APP_ID = "wxeedd9356af3a78b3";
    static final String WEIXIN_APP_SECRET = "0f3bb78c72569ad1ab64a83e24c69c46";
    static final String QQ_APP_ID = "1104611687";
    static final String QQ_APP_KEY = "LGiugygtm1OcHQEh";

    private static SocialAPI instance = new SocialAPI();

    private final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");

    public static SocialAPI getInstance() {
        return instance;
    }

    private SocialAPI(){
        //设置新浪SSO handler
        mController.getConfig().setSsoHandler(new SinaSsoHandler());

        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(NailStarApplication.getApplication(), WEIXIN_APP_ID, WEIXIN_APP_SECRET);
        wxHandler.addToSocialSDK();
        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(NailStarApplication.getApplication(), WEIXIN_APP_ID, WEIXIN_APP_SECRET);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();

        mController.getConfig().setPlatformOrder(
                SHARE_MEDIA.WEIXIN,
                SHARE_MEDIA.WEIXIN_CIRCLE,
                SHARE_MEDIA.QQ,
                SHARE_MEDIA.QZONE,
                SHARE_MEDIA.SINA,
                SHARE_MEDIA.TENCENT);
    }

    public void share(Activity activity, String title, String content, String url, int photoResId) {
        share(activity, title, content, url, new UMImage(activity, photoResId));
    }

    public void share(Activity activity, String title, String content, String url, String photoUrl) {
        share(activity, title, content, url, new UMImage(activity, photoUrl));
    }

    private void share(Activity activity, String title, String content, String url, UMImage photo) {
        // QQ分享
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(activity, QQ_APP_ID, QQ_APP_KEY);
        qqSsoHandler.addToSocialSDK();
        // QQ空间分享
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(activity, QQ_APP_ID, QQ_APP_KEY);
        qZoneSsoHandler.addToSocialSDK();

        setShareContent(content, url, photo);
        setWeixinContent(title, content, url, photo);
        setCircleShareContent(title, content, url, photo);
        setQQShareContent(title, content, url, photo);
        setQQZoneShareContent(title, content, url, photo);

        mController.openShare(activity, false);
    }

    // 设置分享内容
    private void setShareContent(String content, String url, UMImage photo) {
        mController.setShareContent(content + "  " + url);
        mController.setShareMedia(photo);
    }
    //设置微信好友分享内容
    private void setWeixinContent(String title, String content, String url, UMImage photo) {
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setShareContent(content);
        weixinContent.setTitle(title);
        weixinContent.setTargetUrl(url);
        weixinContent.setShareImage(photo);
        mController.setShareMedia(weixinContent);
    }
    //设置微信朋友圈分享内容
    private void setCircleShareContent(String title, String content, String url, UMImage photo) {
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent(content);
        circleMedia.setTitle(title);
        circleMedia.setShareImage(photo);
        circleMedia.setTargetUrl(url);
        mController.setShareMedia(circleMedia);
    }
    // 设置QQ分享内容
    private void setQQShareContent(String title, String content, String url, UMImage photo) {
        QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setShareContent(content);
        qqShareContent.setTitle(title);
        qqShareContent.setShareImage(photo);
        qqShareContent.setTargetUrl(url);
        mController.setShareMedia(qqShareContent);
    }
    // 设置QQ空间分享内容
    private void setQQZoneShareContent(String title, String content, String url, UMImage photo) {
        QZoneShareContent qzone = new QZoneShareContent();
        qzone.setShareContent(content);
        qzone.setTargetUrl(url);
        qzone.setTitle(title);
        qzone.setShareImage(photo);
        mController.setShareMedia(qzone);
    }
}
