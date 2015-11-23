package com.yilos.nailstar.social.model;

import android.app.Activity;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import com.yilos.nailstar.framework.application.NailStarApplication;

/**
 * Created by yangdan on 15/11/20.
 */
public class SocialAPI {
    private static final String WEIXIN_APP_ID = "wxeedd9356af3a78b3";
    private static final String WEIXIN_APP_SECRET = "0f3bb78c72569ad1ab64a83e24c69c46";

    private static SocialAPI instance = new SocialAPI();

    private final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");

    public static SocialAPI getInstance() {
        return instance;
    }

    private SocialAPI(){
        mController.getConfig().removePlatform(SHARE_MEDIA.QQ);
        mController.getConfig().removePlatform(SHARE_MEDIA.QZONE);
        mController.getConfig().removePlatform(SHARE_MEDIA.TENCENT);

        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(NailStarApplication.getApplication(), WEIXIN_APP_ID, WEIXIN_APP_SECRET);
        wxHandler.addToSocialSDK();
        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(NailStarApplication.getApplication(), WEIXIN_APP_ID, WEIXIN_APP_SECRET);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
    }

    public void share(Activity activity, String title, String content, String url, int photoResId) {
        share(activity, title, content, url, new UMImage(activity, photoResId));
    }

    public void share(Activity activity, String title, String content, String url, String photoUrl) {
        share(activity, title, content, url, new UMImage(activity, photoUrl));
    }

    private void share(Activity activity, String title, String content, String url, UMImage photo) {
        setShareContent(content, url, photo);
        setWeixinContent(title, content, url, photo);
        setCircleShareContent(title, content, url, photo);

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
}
