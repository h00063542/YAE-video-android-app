package com.yilos.nailstar.aboutme.view;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.yilos.nailstar.R;
import com.yilos.widget.titlebar.TitleBar;

/**
 * Created by sisilai on 15/11/10.
 */
public class SettingActivity extends Activity {
    private TitleBar titleBar;
    private ImageView backButton;
    private TextView titleTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        titleBar = (TitleBar) findViewById(R.id.setting_title_bar);
        backButton = titleBar.getBackButton(SettingActivity.this);
        backButton.setImageResource(R.drawable.ic_head_back);
        titleTextView = titleBar.getTitleView();
        titleTextView.setText("设置");
    }
}
