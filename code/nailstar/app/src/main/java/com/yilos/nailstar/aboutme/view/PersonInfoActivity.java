package com.yilos.nailstar.aboutme.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yilos.nailstar.R;
import com.yilos.nailstar.util.IdentityUtil;
import com.yilos.widget.titlebar.TitleBar;
import com.yilos.widget.wheelView.LoopListener;
import com.yilos.widget.wheelView.LoopView;

import java.util.ArrayList;

/**
 * Created by sisilai on 15/10/30.
 */
public class PersonInfoActivity extends Activity implements View.OnClickListener {
    private TitleBar titleBar;
    private TextView titleBarTitle;
    private TextView rightTextButton;
    private RelativeLayout personInfoIdentityPopup;
    private TextView personInfoIdentity;

    private RelativeLayout.LayoutParams layoutParams;

    private TextView textView;

    private int identityType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_me_person_info);
        titleBar = (TitleBar)findViewById(R.id.edit_person_info_title_bar);
        personInfoIdentity = (TextView) findViewById(R.id.person_info_identity);
        personInfoIdentityPopup = (RelativeLayout)findViewById(R.id.person_info_identity_popup);
        titleBar.getBackButton(PersonInfoActivity.this);
        titleBarTitle = titleBar.getTitleView();
        titleBarTitle.setText(R.string.edit_person_info);
        rightTextButton = titleBar.getRightTextButton();
        rightTextButton.setOnClickListener(this);

        personInfoIdentity.setOnClickListener(this);

        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);

        textView = (TextView) findViewById(R.id.person_info_identity);

        LoopView loopView = new LoopView(this);
        ArrayList<String> list = new ArrayList();
        for (int i = 1; i < 7; i++) {
            list.add(IdentityUtil.getIdentity(i));
        }

        //设置是否循环播放
        loopView.setNotLoop();
        //滚动监听
        loopView.setListener(new LoopListener() {
            @Override
            public void onItemSelect(int item) {
                textView.setText(IdentityUtil.getIdentity(item + 1));
                identityType = item;
            }
        });
        //设置原始数据
        loopView.setArrayList(list);
        //设置初始位置
        loopView.setPosition(2);
        //设置字体大小
        loopView.setTextSize(20);

        loopView.setBackgroundColor(getResources().getColor(R.color.white));
        personInfoIdentityPopup.addView(loopView, layoutParams);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.person_info_identity:
                if (personInfoIdentityPopup.getVisibility() == View.GONE) {
                    personInfoIdentityPopup.setVisibility(View.VISIBLE);
                } else {
                    personInfoIdentityPopup.setVisibility(View.GONE);
                }
            break;
        }
    }
}
