package com.yilos.nailstar.aboutme.view;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import com.yilos.nailstar.R;
import com.yilos.widget.titlebar.TitleBar;

/**
 * Created by sisilai on 15/11/12.
 */
public class UserAgreementActivity extends Activity {
    private TitleBar titleBar;
    private TextView titleText;
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_agreement);
        titleBar = (TitleBar) findViewById(R.id.user_agreement_title_bar);
        titleBar.getBackButton(UserAgreementActivity.this);
        titleText = titleBar.getTitleView("用户协议");
        //titleText.setText("用户协议");
        webView = (WebView) findViewById(R.id.user_agreement_web_view);
        webView.loadUrl("file:///android_asset/licence.html");
    }
}
