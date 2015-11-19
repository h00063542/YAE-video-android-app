package com.yilos.nailstar.aboutme.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.presenter.LoginPresenter;
import com.yilos.nailstar.framework.view.BaseActivity;
import com.yilos.widget.titlebar.TitleBar;

public class LoginActivity extends BaseActivity implements ILoginView {
    final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");

    private LoginPresenter presenter;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        presenter = LoginPresenter.getInstance(this);

        initViews();
    }

    @Override
    public String getUserAccount() {
        return ((EditText)findViewById(R.id.userNameText)).getText().toString();
    }

    @Override
    public String getPassword() {
        return ((EditText)findViewById(R.id.passwordText)).getText().toString();
    }

    @Override
    public CharSequence getResourceStringById(int id) {
        return this.getApplicationContext().getResources().getText(id);
    }

    @Override
    public void setLoginButtonEnable(boolean enable) {
        loginButton.setEnabled(enable);
    }

    @Override
    public void close() {
        this.finish();
    }

    private void initViews() {
        final LoginActivity loginActivity = this;
        TitleBar titleBar = (TitleBar) findViewById(R.id.titleBar);
        titleBar.getBackButton(this);

        TextView titleView = titleBar.getTitleView();
        titleView.setText(R.string.title_activity_login);

        TextView titleBarRightTextButton = titleBar.getRightTextButton();
        titleBarRightTextButton.setText(R.string.sign_up);
        titleBarRightTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToRegisterIntent = new Intent(loginActivity, RegisterActivity.class);
                goToRegisterIntent.putExtra(RegisterActivity.TYPE, RegisterActivity.REGISTER_TYPE);
                startActivityForResult(goToRegisterIntent, 1);
            }
        });

        //登录按钮事件
        loginButton = (Button)findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.login();
            }
        });

        // 忘记密码点击事件
        TextView forgetPasswordText = (TextView)findViewById(R.id.forgetPasswordText);
        forgetPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToRegisterIntent = new Intent(loginActivity, RegisterActivity.class);
                goToRegisterIntent.putExtra(RegisterActivity.TYPE, RegisterActivity.FIND_PASSWORD_TYPE);
                startActivityForResult(goToRegisterIntent, 1);
            }
        });

        String appId = "wxeedd9356af3a78b3";
        String appSecret = "0f3bb78c72569ad1ab64a83e24c69c46";
// 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(this,appId,appSecret);
        wxHandler.addToSocialSDK();
// 添加微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(this,appId,appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();

        //设置微信好友分享内容
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        //设置分享文字
        weixinContent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，微信");
        //设置title
        weixinContent.setTitle("友盟社会化分享组件-微信");
        //设置分享内容跳转URL
        weixinContent.setTargetUrl("http://www.yilos.com");
        //设置分享图片
        //weixinContent.setShareImage(localImage);
        mController.setShareMedia(weixinContent);

        //设置微信朋友圈分享内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，朋友圈");
//设置朋友圈title
        circleMedia.setTitle("友盟社会化分享组件-朋友圈");
//        circleMedia.setShareImage(localImage);
        circleMedia.setTargetUrl("http://www.yilos.com");
        mController.setShareMedia(circleMedia);

//        mController.setShareContent("友盟社会化组件（SDK）让移动应用快速整合社交分享功能，http://www.umeng.com/social");
//        // 设置分享图片, 参数2为图片的url地址
//        mController.setShareMedia(new UMImage(this,
//                "http://www.umeng.com/images/404.png"));
        final Activity activity = this;
        RadioButton weiboLoginButton = (RadioButton)findViewById(R.id.weiboLoginButton);
        weiboLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mController.openShare(activity, false);
            }
        });
    }
}
