package com.yilos.nailstar.aboutme.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yilos.nailstar.R;
import com.yilos.widget.titlebar.TitleBar;

/**
 * Created by sisilai on 15/10/30.
 */
public class PersonInfoActivity extends Activity implements View.OnClickListener {
    private TitleBar titleBar;
    private TextView titleBarTitle;
    private TextView rightTextButton;
    private RelativeLayout personInfoIdentityLayout;
    private RelativeLayout personInfoIdentityPopup;
    private ImageView backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_me_person_info);
        titleBar = (TitleBar)findViewById(R.id.edit_person_info_title_bar);
        personInfoIdentityLayout = (RelativeLayout)findViewById(R.id.person_info_identity_layout);
        personInfoIdentityPopup = (RelativeLayout)findViewById(R.id.person_info_identity_popup);
        backButton = titleBar.getBackButton();
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleBarTitle = titleBar.getTitleView();
        titleBarTitle.setText(R.string.edit_person_info);
        rightTextButton = titleBar.getRightTextButton();
        rightTextButton.setOnClickListener(this);

        personInfoIdentityLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personInfoIdentityPopup.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //case :break;
        }
    }
}
