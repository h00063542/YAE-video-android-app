package com.yilos.nailstar.aboutme.view;

import android.os.Bundle;
import android.webkit.WebView;

import com.yilos.nailstar.R;
import com.yilos.nailstar.framework.view.BaseActivity;
import com.yilos.widget.titlebar.TitleBar;

/**
 * Created by sisilai on 15/11/12.
 */
public class UserAgreementActivity extends BaseActivity {
    private TitleBar titleBar;
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_agreement);
        titleBar = (TitleBar) findViewById(R.id.user_agreement_title_bar);
        titleBar.getBackButton(UserAgreementActivity.this);
        String title = getResources().getString(R.string.title_activity_licence);
        titleBar.getTitleView(title);
        webView = (WebView) findViewById(R.id.user_agreement_web_view);
        webView.loadUrl("file:///android_asset/licence.html");
    }
}
