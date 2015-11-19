package com.yilos.nailstar.framework.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.yilos.nailstar.R;
import com.yilos.nailstar.main.MainActivity;
import com.yilos.nailstar.util.Constants;
import com.yilos.widget.titlebar.TitleBar;

/**
 * Created by ganyue on 15/11/19.
 */
public class YLSWebViewActivity extends BaseActivity {
    String header_title = "";
    String url = "";
    private TitleBar mHead;
    private ImageView mHeadBack;

    private WebView mWebContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_webview);
        init();
    }

    private void init(){
        url = getIntent().getStringExtra(Constants.WEBVIEW_URL);
        header_title = getIntent().getStringExtra(Constants.WEBVIEW_TITLE);
        mHead = (TitleBar) findViewById(R.id.common_webview_title_bar);
        mHead.getTitleView().setText(header_title);

        mHeadBack = mHead.getBackButton();
        mHeadBack.setImageResource(R.mipmap.icon_back_white);
        mHeadBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YLSWebViewActivity.this.finish();
            }
        });

        mWebContent = (WebView) findViewById(R.id.common_webview_content);
        mWebContent.loadUrl(url);
    }
}
