package com.yilos.nailstar.aboutme.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yilos.nailstar.R;
import com.yilos.widget.titlebar.TitleBar;

/**
 * Created by sisilai on 15/11/6.
 */
public class LevelActivity extends Activity {
    private ProgressBar progressBar;
    private TitleBar titleBar;
    private ImageView backButton;
    private TextView titleView;
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);
        initViews();
    }

    private void initViews() {
        titleBar = (TitleBar)findViewById(R.id.level_title_bar);
        backButton = titleBar.getBackButton();
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
