package com.yilos.nailstar.aboutme.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.entity.PersonInfo;
import com.yilos.nailstar.aboutme.model.LoginAPI;
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

    PersonInfo personInfo = new PersonInfo();
    LoginAPI loginAPI = LoginAPI.getInstance();

    public void submitMyPhotoToOss(String ossUrl) {
        personInfo.setPhotoUrl(ossUrl);
    }

    public void setPersonInfo(PersonInfo personInfo) {
        if (personInfo == null) {
            showShortToast(R.string.person_info_update_fail);
            return;
        }
        showShortToast(R.string.person_info_update_succeed);
        loginAPI.saveLoginStatus(loginAPI.getLoginUserName(), personInfo);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        initViews();
        Bundle bundle = this.getIntent().getExtras();
        personInfo = (PersonInfo)bundle.getSerializable(Constants.PERSON_INFO);
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
    private void showClearNickNameIcon() {
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
        if(personInfo.getPhotoUrl() == null || personInfo.getPhotoUrl().trim().equals("")) {
            circleImageView.setImageResource(R.mipmap.ic_default_photo);
        } else {
            circleImageView.setImageSrc(personInfo.getPhotoUrl());
        }
        showClearNickNameIcon();
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
                personInfoPresenter.setPersonInfo(personInfo);
            }
        });

        nickNameText.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int selectionStart ;
            private int selectionEnd ;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                showClearNickNameIcon();
                temp  = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                showClearNickNameIcon();
            }

            @Override
            public void afterTextChanged(Editable s) {
                showClearNickNameIcon();
                selectionStart = nickNameText.getSelectionStart();
                selectionEnd = nickNameText.getSelectionEnd();
                if (temp.length() > Constants.PERSON_INFO_NAME_MAX_LENGTH) {
                    showShortToast(R.string.person_info_name_tips);
                    s.delete(selectionStart-1, selectionEnd);
                    int tempSelection = selectionStart;
                    nickNameText.setText(s);
                    nickNameText.setSelection(tempSelection);
                }
            }
        });
        circleImageView.setOnClickListener(this);
        personInfoIdentity.setOnClickListener(this);
        // 上传照片
        takeImage = new TakeImage.Builder().context(this).uri(Constants.YILOS_PATH).callback(new TakeImageCallback() {
            @Override
            public void callback(Uri uri) {
                circleImageView.setImageURI(uri);
                StringBuilder picName = new StringBuilder()
                        .append(UUIDUtil.getUUID());
                personInfoPresenter.submitMyPhotoToOss(uri.getPath(),picName.toString());
            }
        }).build();
    }

    private void setLoopView() {
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);

        LoopView loopView = new LoopView(this);
        ArrayList<String> list = new ArrayList();
        for (int i = 0; i < 6; i++) {
            list.add(IdentityUtil.getIdentity(i + 1));
        }

        //设置是否循环播放
        loopView.setNotLoop();
        //滚动监听
        loopView.setListener(new LoopListener() {
            @Override
            public void onItemSelect(int item) {
                personInfo.setType(item + 1);
                personInfoIdentity.setText(IdentityUtil.getIdentity(personInfo.getType()));
            }
        });
        //设置原始数据
        loopView.setArrayList(list);
        //设置初始位置
        loopView.setPosition(personInfo.getType() - 1);
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
