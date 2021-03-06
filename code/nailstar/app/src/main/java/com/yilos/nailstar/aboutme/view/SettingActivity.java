package com.yilos.nailstar.aboutme.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leaking.slideswitch.SlideSwitch;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.entity.Sdcard;
import com.yilos.nailstar.aboutme.model.LoginAPI;
import com.yilos.nailstar.framework.view.BaseActivity;
import com.yilos.nailstar.util.Constants;
import com.yilos.nailstar.util.DataCleanManager;
import com.yilos.nailstar.util.SettingUtil;
import com.yilos.widget.titlebar.TitleBar;

import java.io.File;

/**
 * Created by sisilai on 15/11/10.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener, SlideSwitch.SlideListener {
    private TitleBar titleBar;
    private TextView titleTextView;
    private RelativeLayout clearCache;
    private TextView cacheNumber;
    private SlideSwitch slideSwitch;
    private TextView versionInfoNumber;
    private RelativeLayout goToAboutUs;
    private String versionName;
    private TextView loginOut;
    private TextView downloadSdcard;
    private RelativeLayout settingFolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initViews();
        initEvents();

    }

    private void initViews() {
        settingFolder = (RelativeLayout) findViewById(R.id.setting_folder);
        loginOut = (TextView) findViewById(R.id.login_out);
        versionInfoNumber = (TextView) findViewById(R.id.version_info_number);
        slideSwitch = (SlideSwitch) findViewById(R.id.slide_switch);
        titleBar = (TitleBar) findViewById(R.id.setting_title_bar);
        titleBar.getBackButton(SettingActivity.this);
        titleTextView = titleBar.getTitleView();
        cacheNumber = (TextView) findViewById(R.id.cache_number);
        clearCache = (RelativeLayout) findViewById(R.id.clear_cache);
        goToAboutUs = (RelativeLayout) findViewById(R.id.go_to_about_us);
        downloadSdcard = (TextView) findViewById(R.id.download_sdcard);
    }

    private void initEvents() {
        if (LoginAPI.getInstance().isLogin()) {
            loginOut.setVisibility(View.VISIBLE);
            loginOut.setOnClickListener(this);
        }
        goToAboutUs.setOnClickListener(this);
        settingFolder.setOnClickListener(this);
        versionName = SettingUtil.getVersion();
        versionInfoNumber.setText(versionName);
        boolean allow = SettingUtil.getAllowNoWifi();
        slideSwitch.setState(allow);
        slideSwitch.setSlideListener(this);
        titleTextView.setText(R.string.about_me_setting);
        clearCache.setOnClickListener(this);
        cacheNumber.setText(setCacheNumber());
        downloadSdcard.setText(SettingUtil.getSdcardName());
    }

    private String setCacheNumber() {
        File cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());
        String formatCacheNumber;
        try {
            formatCacheNumber = DataCleanManager.getCacheSize(cacheDir);
            return formatCacheNumber;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clear_cache:
                if (cacheNumber.getText().equals("0KB")) {
                    showShortToast(R.string.with_no_cache);
                    return;
                }
                String content = getResources().getString(R.string.sure_clear_cache);
                DialogInterface.OnClickListener setSureEvent = setSureEvent();
                showMessageDialogWithEvent(null, content, setSureEvent, null);
                break;
            case R.id.go_to_about_us:
                Intent aboutUsIntent = new Intent(SettingActivity.this, AboutUsActivity.class);
                aboutUsIntent.putExtra(Constants.VERSION_NAME, versionName);
                startActivity(aboutUsIntent);
                break;
            case R.id.login_out:
                LoginAPI.getInstance().getLoginUserId();  //获取登录的用户ID
                LoginAPI.getInstance().logout(); //退出登录
                loginOut.setVisibility(View.GONE);
                showShortToast(R.string.has_been_login_out);
                finish();
                break;
            case R.id.setting_folder:
                final SettingFolderAdapter settingFolderAdapter = new SettingFolderAdapter(this,SettingUtil.getSdcardList(),SettingUtil.getSdcardName());
                DialogPlus dialog = DialogPlus.newDialog(this)
                        .setAdapter(settingFolderAdapter)
                                .setOnItemClickListener(new OnItemClickListener() {
                                    @Override
                                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                                        if (item == null) {
                                            return;
                                        }
                                        Sdcard sdcard  = (Sdcard) item;
                                        SettingUtil.setSdcard(sdcard.getSdcardName(), sdcard.getSdcardPath());
                                        downloadSdcard.setText(SettingUtil.getSdcardName());
                                        dialog.dismiss();
                                    }
                                })
                                .setGravity(Gravity.CENTER)
                                .setHeader(R.layout.setting_folder_dialog_header)
                                .create();
                dialog.show();
                break;
            default:
                break;
        }
    }

    protected DialogInterface.OnClickListener setSureEvent() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DataCleanManager.cleanExternalCache(getApplicationContext());
                String formatCacheNumber = setCacheNumber();
                if (formatCacheNumber.equals("0KB")) {
                    cacheNumber.setText(formatCacheNumber);
                    showShortToast(R.string.clear_cache_end);
                }
            }
        };
    }

    @Override
    public void open() {
        SettingUtil.setAllowNoWifi(true);
        showShortToast(R.string.allow_no_wifi_watch);
    }

    @Override
    public void close() {
        SettingUtil.setAllowNoWifi(false);
        showShortToast(R.string.not_allow_no_wifi_watch);
    }
}
