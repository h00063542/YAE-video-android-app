package com.yilos.nailstar.aboutme.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.presenter.RegisterPresenter;
import com.yilos.nailstar.framework.view.BaseActivity;
import com.yilos.widget.titlebar.TitleBar;

public class RegisterActivity extends BaseActivity implements IRegisterView {
    public static final String TYPE = "type";
    public static final int FIND_PASSWORD_TYPE = 0;
    public static final int REGISTER_TYPE = 1;
    private RegisterPresenter presenter;

    private AppCompatActivity activity = this;
    private int disableSeconds = 0;

    private Button getValidateCodeButton;
    private Button registerButton;

    private Handler timeHandler = new Handler(){
        public void handleMessage(Message msg) {
            if(msg.what > 0) {
                disableSeconds = msg.what;
                getValidateCodeButton.setText(msg.what + "S");
                final int nextWhat = msg.what - 1;
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        timeHandler.sendEmptyMessage(nextWhat);
                    }
                }, 1000);
            } else {
                disableSeconds = 0;
                getValidateCodeButton.setBackgroundColor(ContextCompat.getColor(activity, R.color.orange));
                getValidateCodeButton.setEnabled(true);
                getValidateCodeButton.setText(R.string.get_validate_code);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        presenter = RegisterPresenter.getInstance(this);

        initViews();

        if(null != savedInstanceState) {
            ((EditText)findViewById(R.id.phoneNumberText)).setText(savedInstanceState.getString("phoneNumber"));
            if(savedInstanceState.getInt("disableSeconds") > 0) {
                ((EditText)findViewById(R.id.validateCodeText)).setText(savedInstanceState.getString("validateCode"));
                timeHandler.sendEmptyMessage(savedInstanceState.getInt("disableSeconds"));
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("phoneNumber", getPhoneNumber());
        outState.putString("validateCode", getValidateCode());
        outState.putInt("disableSeconds", disableSeconds);
    }

    private void initViews(){
        final Activity registerActivity = this;
        //设置titlebar
        TitleBar titleBar = (TitleBar) findViewById(R.id.titleBar);
        ImageView backButton = titleBar.getBackButton();
        backButton.setClickable(true);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerActivity.finish();
            }
        });

        TextView titleView = titleBar.getTitleView();
        titleView.setText(R.string.title_activity_register);

        // 设置协议跳转
        TextView licenceTextButton = (TextView)findViewById(R.id.licenceTextButton);
        licenceTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToLicenceIntent = new Intent(registerActivity, LicenceActivity.class);
                startActivityForResult(goToLicenceIntent, 1);
            }
        });

        //获取验证码添加事件
        getValidateCodeButton = (Button)findViewById(R.id.getValidateCodeButton);
        getValidateCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getValidateCode();
            }
        });

        // 注册按钮事件
        registerButton = (Button)findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.register();
            }
        });

        // checkBox事件
        CheckBox checkBox = (CheckBox)findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    registerButton.setBackgroundColor(ContextCompat.getColor(activity, R.color.orange));
                    registerButton.setEnabled(true);
                } else {
                    registerButton.setBackgroundColor(ContextCompat.getColor(activity, R.color.grey));
                    registerButton.setEnabled(false);
                }
            }
        });
    }

    private void initCommonView() {

    }

    private void initRegisterView() {

    }

    private void initFindPasswordView() {

    }

    @Override
    public String getPhoneNumber() {
        return ((EditText)findViewById(R.id.phoneNumberText)).getText().toString();
    }

    @Override
    public String getValidateCode() {
        return ((EditText)findViewById(R.id.validateCodeText)).getText().toString();
    }

    @Override
    public String getPassword() {
        return ((EditText)findViewById(R.id.passwordText)).getText().toString();
    }

    @Override
    public void timeValidateCodeButton(int seconds) {
        getValidateCodeButton.setEnabled(false);
        getValidateCodeButton.setBackgroundColor(ContextCompat.getColor(this, R.color.grey));
        timeHandler.sendEmptyMessage(seconds);
    }

    @Override
    public void close() {
        this.finish();
    }

    @Override
    public void showToast(String content) {
        Toast.makeText(this, content, Toast.LENGTH_LONG);
    }
}
