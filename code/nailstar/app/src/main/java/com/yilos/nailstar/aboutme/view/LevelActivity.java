package com.yilos.nailstar.aboutme.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yilos.nailstar.R;
import com.yilos.nailstar.util.ImageUtil;
import com.yilos.widget.circleimageview.CircleImageView;
import com.yilos.widget.titlebar.TitleBar;

import java.io.IOException;
import java.net.URL;

/**
 * Created by sisilai on 15/11/6.
 */
public class LevelActivity extends Activity {
    private ProgressBar progressBar;
    private TitleBar titleBar;
    private ImageView backButton;
    private TextView titleView;
    private WebView webView;
    private static int experience;
    private static Bitmap myImageBitmap;
    private CircleImageView circleImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);
        Intent intent = getIntent();
        experience = intent.getIntExtra("experience",0);
        myImageBitmap = intent.getParcelableExtra("myImageBitmap");
        initViews();
    }

    private void initViews() {
        circleImageView = (CircleImageView)findViewById(R.id.level_my_image);
        if (myImageBitmap != null) {
            circleImageView.setImageBitmap(myImageBitmap);
        }
        titleBar = (TitleBar)findViewById(R.id.level_title_bar);
        backButton = titleBar.getBackButton();
        backButton.setImageResource(R.drawable.ic_head_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleView = titleBar.getTitleView();
        titleView.setText(R.string.level);
        progressBar = (ProgressBar)findViewById(R.id.level_progressbar);
        webView = (WebView) findViewById(R.id.webView);
        webView.loadUrl("file:///android_asset/experience.html");
    }
}
