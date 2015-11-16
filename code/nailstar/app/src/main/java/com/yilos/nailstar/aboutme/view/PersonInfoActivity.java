package com.yilos.nailstar.aboutme.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    private RelativeLayout personInfoIdentityLayout;
    private RelativeLayout personInfoIdentityPopup;
    private ImageView backButton;



    //private RelativeLayout rootview;
    private RelativeLayout.LayoutParams layoutParams;

    private TextView textView;

    private int identityType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_me_person_info);
        titleBar = (TitleBar)findViewById(R.id.edit_person_info_title_bar);
        personInfoIdentityLayout = (RelativeLayout)findViewById(R.id.person_info_identity_layout);
        personInfoIdentityPopup = (RelativeLayout)findViewById(R.id.person_info_identity_popup);
        backButton = titleBar.getBackButton(PersonInfoActivity.this);
//        backButton.setImageResource(R.drawable.ic_head_back);
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
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






        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);

        //rootview = (RelativeLayout) findViewById(R.id.rootview);

        textView = (TextView) findViewById(R.id.person_info_identity);

        LoopView loopView = new LoopView(this);
        ArrayList<String> list = new ArrayList();
        for (int i = 1; i < 7; i++) {
            //list.add("item " + i);
            list.add(IdentityUtil.getIdentity(i));
        }

        //设置是否循环播放
        loopView.setNotLoop();
        //滚动监听
        loopView.setListener(new LoopListener() {
            @Override
            public void onItemSelect(int item) {
                Log.d("debug", "Item " + item);
                textView.setText(IdentityUtil.getIdentity(item));
                identityType = item;
            }
        });
        //设置原始数据
        loopView.setArrayList(list);
        //设置初始位置
        loopView.setPosition(1);
        //设置字体大小
        loopView.setTextSize(20);

        loopView.setBackgroundColor(getResources().getColor(R.color.white));
        personInfoIdentityPopup.addView(loopView, layoutParams);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //case :break;
        }
    }
}
