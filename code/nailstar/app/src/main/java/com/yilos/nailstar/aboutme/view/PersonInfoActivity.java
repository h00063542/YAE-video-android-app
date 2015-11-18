package com.yilos.nailstar.aboutme.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.entity.PersonInfo;
import com.yilos.nailstar.aboutme.presenter.PersonInfoPresenter;
import com.yilos.nailstar.framework.view.BaseActivity;
import com.yilos.nailstar.takeImage.TakeImage;
import com.yilos.nailstar.takeImage.TakeImageCallback;
import com.yilos.nailstar.util.Constants;
import com.yilos.nailstar.util.IdentityUtil;
import com.yilos.nailstar.util.UUIDUtil;
import com.yilos.widget.circleimageview.CircleImageView;
import com.yilos.widget.titlebar.TitleBar;
import com.yilos.widget.wheelView.LoopListener;
import com.yilos.widget.wheelView.LoopView;

import java.util.ArrayList;

/**
 * Created by sisilai on 15/10/30.
 */
public class PersonInfoActivity extends BaseActivity implements View.OnClickListener {
    private TitleBar titleBar;
    private TextView titleBarTitle;
    private TextView rightTextButton;
    private RelativeLayout personInfoIdentityPopup;
    private TextView personInfoIdentity;
    private TakeImage takeImage;
    private RelativeLayout.LayoutParams layoutParams;
    private CircleImageView circleImageView;
    private EditText nickNameText;
    private TextView identityText;
    private EditText profileText;
    private LinearLayout errorClear;

    private String myImageUrl;
    private int identityType;
    private String nickName;
    private String profile;
    private String uid;

    PersonInfo personInfo = new PersonInfo();

    public void submitMyPhotoToOss(String ossUrl) {
        personInfo.setPhotoUrl(ossUrl);
    }

    public void setPersonInfo(PersonInfo personInfo) {
        showShortToast("个人资料更新成功");
    }

    public void getImage(Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }
        circleImageView.setImageBitmap(bitmap);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        initViews();
        Bundle bundle = this.getIntent().getExtras();
//        myImageUrl = getBundle.getString("myImageUrl");
//        identityType = getBundle.getInt("identityType");
//        uid = "a8affd60-efe6-11e4-a908-3132fc2abe39";//getBundle.getString("uid");
//        profile = getBundle.getString("profile");
//        nickName = getBundle.getString("nickName");
        personInfo = (PersonInfo)bundle.getSerializable("personInfo");

//        personInfo.setUid(uid);
//        personInfo.setPhotoUrl(myImageUrl);
//        personInfo.setProfile(profile);
//        personInfo.setNickname(nickName);
        setLoopView();
        initEvents();
    }

    private void initViews() {
        errorClear = (LinearLayout) findViewById(R.id.error_clear);
        identityText = (TextView) findViewById(R.id.person_info_identity);
        nickNameText = (EditText) findViewById(R.id.person_info_nick_name);
        profileText = (EditText) findViewById(R.id.person_info_introduction);
        titleBar = (TitleBar)findViewById(R.id.edit_person_info_title_bar);
        personInfoIdentity = (TextView) findViewById(R.id.person_info_identity);
        personInfoIdentityPopup = (RelativeLayout)findViewById(R.id.person_info_identity_popup);
        circleImageView = (CircleImageView) findViewById(R.id.my_photo);
    }

    /**
     * 决定是否显示清空图标
     */
    private void showError() {
        if (TextUtils.isEmpty(nickNameText.getText())) {
            errorClear.setVisibility(View.GONE);
        } else {
            errorClear.setVisibility(View.VISIBLE);
            errorClear.setOnClickListener(this);
        }
    }

    private void initEvents() {
        identityText.setText(IdentityUtil.getIdentity(personInfo.getType()));
        nickNameText.setText(personInfo.getNickname());
        profileText.setText(personInfo.getProfile());
        Bitmap bm = BitmapFactory.decodeFile(personInfo.getPhotoUrl());
        circleImageView.setImageBitmap(bm);
        showError();
        final PersonInfoPresenter personInfoPresenter = PersonInfoPresenter.getInstance(this);
        titleBar.getBackButton(PersonInfoActivity.this);
        titleBarTitle = titleBar.getTitleView();
        titleBarTitle.setText(R.string.edit_person_info);
        rightTextButton = titleBar.getRightTextButton();
        rightTextButton.setText(R.string.sureButtonText);
        rightTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personInfo.setNickname(nickNameText.getText().toString().trim());
                personInfo.setProfile(profileText.getText().toString().trim());
                personInfoPresenter.setPersonInfo(personInfo.getUid(), personInfo.getNickname(), personInfo.getType(), personInfo.getPhotoUrl(), personInfo.getProfile());
            }
        });

        nickNameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                showError();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                showError();
            }

            @Override
            public void afterTextChanged(Editable s) {
                showError();
            }
        });
        circleImageView.setOnClickListener(this);
        personInfoIdentity.setOnClickListener(this);
        // 上传照片
        takeImage = new TakeImage.Builder().context(this).uri(Constants.YILOS_PATH).callback(new TakeImageCallback() {
            @Override
            public void callback(Uri uri) {
                circleImageView.setImageURI(uri);
                String path = uri.getPath();
                StringBuilder picName = new StringBuilder()
                        .append(UUIDUtil.getUUID())
                        .append(Constants.JPG_SUFFIX);
                personInfo.setPicName(picName.toString());
                personInfoPresenter.submitMyPhotoToOss(path,personInfo.getPicName());
            }
        }).build();
    }

    private void setLoopView() {
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);

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
                identityType = item + 1;
                personInfo.setType(identityType);
                personInfoIdentity.setText(IdentityUtil.getIdentity(personInfo.getType()));
            }
        });
        //设置原始数据
        loopView.setArrayList(list);
        //设置初始位置
        loopView.setPosition(2);
        //设置字体大小
        loopView.setTextSize(17);

        loopView.setBackgroundColor(getResources().getColor(R.color.white));
        personInfoIdentityPopup.addView(loopView, layoutParams);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        takeImage.onActivityResult(requestCode, resultCode, data);
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
            case R.id.my_photo:
                takeImage.initTakeImage();
                break;
            case R.id.error_clear:
                nickNameText.setText("");
                break;
            default:
                break;
        }
    }
}
