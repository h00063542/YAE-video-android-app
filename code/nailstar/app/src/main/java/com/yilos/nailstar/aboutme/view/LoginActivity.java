package com.yilos.nailstar.aboutme.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.presenter.LoginPresenter;
import com.yilos.nailstar.framework.view.BaseActivity;
import com.yilos.widget.titlebar.TitleBar;

public class LoginActivity extends BaseActivity implements ILoginView {
    private LoginPresenter presenter;

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
    public void showMessageDialog(String content) {
        //super.showMessageDialog("登录错误", content);
        super.showLoading(content);
    }

    @Override
    public CharSequence getResourceStringById(int id) {
        return this.getApplicationContext().getResources().getText(id);
    }

    private void initViews() {
        final LoginActivity loginActivity = this;
        TitleBar titleBar = (TitleBar) findViewById(R.id.titleBar);
        ImageView backButton = titleBar.getBackButton();
        backButton.setClickable(true);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginActivity.finish();
            }
        });

        TextView titleView = titleBar.getTitleView();
        titleView.setText(R.string.title_activity_login);

        TextView titleBarRightTextButton = titleBar.getRightTextButton();
        titleBarRightTextButton.setText(R.string.sign_up);

        Button loginButton = (Button)findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.login();
            }
        });
    }
}
