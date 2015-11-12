package com.yilos.nailstar.aboutme.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yilos.nailstar.R;
import com.yilos.widget.titlebar.TitleBar;

/**
 * Created by sisilai on 15/11/12.
 */
public class AboutUsActivity extends Activity {
    private TitleBar titleBar;
    private TextView titleView;
    private TextView userAgreement;
    private TextView versionNameText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        titleBar = (TitleBar) findViewById(R.id.about_us_title_bar);
        titleView = titleBar.getTitleView();
        titleView.setText(R.string.app_name);
        titleBar.getBackButton(AboutUsActivity.this);
        Intent intent = getIntent();
        String versionName = intent.getStringExtra("versionName");
        versionNameText = (TextView) findViewById(R.id.version_name);
        versionNameText.setText( "Version " + versionName );
        userAgreement = (TextView) findViewById(R.id.user_agreement);
        userAgreement.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //下划线
        userAgreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userAgreementIntent = new Intent(AboutUsActivity.this,UserAgreementActivity.class);
                startActivity(userAgreementIntent);
            }
        });
    }
}
