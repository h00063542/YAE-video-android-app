package com.yilos.nailstar.aboutme.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yilos.nailstar.R;
import com.yilos.nailstar.main.MainActivity;
import com.yilos.widget.titlebar.TitleBar;

/**
 * Created by sisilai on 15/11/4.
 */
public class FollowListActivity extends Activity implements View.OnClickListener {
    private TitleBar titleBar;
    private ImageView backButton;
    private TextView titleText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_list);
        titleBar = (TitleBar)findViewById(R.id.follow_list_title_bar);
        backButton = titleBar.getBackButton();
        titleText = titleBar.getTitleView();
        titleText.setText(R.string.my_follow_list);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
}
