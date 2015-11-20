package com.yilos.nailstar.aboutme.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.presenter.LoginPresenter;
import com.yilos.nailstar.framework.view.BaseActivity;
import com.yilos.nailstar.social.model.SocialAPI;
import com.yilos.widget.titlebar.TitleBar;

public class LoginActivity extends BaseActivity implements ILoginView {
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

        final Activity activity = this;
        RadioButton weiboLoginButton = (RadioButton)findViewById(R.id.weiboLoginButton);
        weiboLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SocialAPI.getInstance().share(activity, "美甲大咖，行业最专业的视频教学App", "我试过很多美甲App，最后还是选择了美甲大咖。真爱，经得起等待！", "http://s.naildaka.com/site/share_app.html", R.mipmap.ic_default_photo);
            }
        });
    }
}
