package com.yilos.nailstar.aboutme.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.presenter.FansListPresenter;
import com.yilos.nailstar.aboutme.presenter.LevelPresenter;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.framework.view.BaseActivity;
import com.yilos.nailstar.util.ImageUtil;
import com.yilos.nailstar.util.LevelUtil;
import com.yilos.widget.circleimageview.CircleImageView;
import com.yilos.widget.titlebar.TitleBar;

import java.io.IOException;
import java.net.URL;

/**
 * Created by sisilai on 15/11/6.
 */
public class LevelActivity extends BaseActivity {
    private LinearLayout levelProgressbarDirection;
    private ProgressBar progressBar;
    private TextView expText;//经验值
    private TitleBar titleBar;
    private ImageView backButton;
    private TextView titleView;
    private TextView underImageLevel;
    private TextView leftLevel;
    private TextView rightLevel;
    private WebView webView;
    private static int experience;
    private static int level;
    private static String myImageUrl;
    private CircleImageView circleImageView;

    public void getImage(Bitmap bitmap) {
        circleImageView.setImageBitmap(bitmap);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);
        Intent intent = getIntent();
        experience = intent.getIntExtra("experience", 0);
        myImageUrl = intent.getStringExtra("myImageUrl");
        level = LevelUtil.calcLevel(experience);
        LevelPresenter levelPresenter = LevelPresenter.getInstance(this);
        levelPresenter.getImage(myImageUrl);
        initViews();
        initEvents();
    }

    private void initViews() {
        levelProgressbarDirection = (LinearLayout)findViewById(R.id.level_progressbar_direction);
        expText = (TextView)findViewById(R.id.experience);
        progressBar = (ProgressBar)findViewById(R.id.level_progressbar);
        rightLevel = (TextView)findViewById(R.id.right_level);
        underImageLevel = (TextView)findViewById(R.id.under_image_level);
        leftLevel = (TextView)findViewById(R.id.left_level);
        circleImageView = (CircleImageView)findViewById(R.id.level_my_image);
        titleBar = (TitleBar)findViewById(R.id.level_title_bar);
        backButton = titleBar.getBackButton();
        titleView = titleBar.getTitleView();
        progressBar = (ProgressBar)findViewById(R.id.level_progressbar);
        webView = (WebView) findViewById(R.id.webView);
    }

    private void initEvents() {
        progressBar.setProgress((int)(LevelUtil.calcNextExpDiffer(experience,100)));
        expText.setText(String.valueOf(LevelUtil.calcExpDiffer(experience)) + "经验");
        underImageLevel.setText("lv" + String.valueOf(level));
        leftLevel.setText("lv" + String.valueOf(level));
        rightLevel.setText("lv" + String.valueOf(level +1));
        backButton.setImageResource(R.drawable.ic_head_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleView.setText(R.string.level);
        webView.loadUrl("file:///android_asset/experience.html");
    }
}
