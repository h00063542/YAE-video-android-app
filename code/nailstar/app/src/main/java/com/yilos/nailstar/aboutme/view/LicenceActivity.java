package com.yilos.nailstar.aboutme.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.yilos.nailstar.R;
import com.yilos.widget.titlebar.TitleBar;

public class LicenceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_licence);

        initView();
    }

    private void initView() {
        final Activity licenceActivity = this;

        //设置titlebar
        TitleBar titleBar = (TitleBar) findViewById(R.id.titleBar);
        ImageView backButton = titleBar.getBackButton();
        backButton.setClickable(true);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                licenceActivity.finish();
            }
        });

        TextView titleView = titleBar.getTitleView();
        titleView.setText(R.string.title_activity_licence);

        ((WebView)findViewById(R.id.webView)).loadUrl("file:///android_asset/licence.html");
    }
}
