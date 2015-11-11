package com.yilos.nailstar.aboutme.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yilos.nailstar.R;
import com.yilos.widget.titlebar.TitleBar;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
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
    }
}
