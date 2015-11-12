package com.yilos.nailstar.aboutme.view;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leaking.slideswitch.SlideSwitch;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.yilos.nailstar.R;
import com.yilos.nailstar.framework.view.BaseActivity;
import com.yilos.nailstar.util.DataCleanManager;
import com.yilos.nailstar.util.SharedPreferencesUtil;
import com.yilos.widget.titlebar.TitleBar;

import java.io.File;

/**
 * Created by sisilai on 15/11/10.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener, SlideSwitch.SlideListener {
    private TitleBar titleBar;
    private ImageView backButton;
    private TextView titleTextView;
    private RelativeLayout clearCache;
    private TextView cacheNumber;
    private SlideSwitch slideSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initViews();
        initEvents();
    }

    private void initViews() {
        slideSwitch = (SlideSwitch) findViewById(R.id.slide_switch);
        titleBar = (TitleBar) findViewById(R.id.setting_title_bar);
        backButton = titleBar.getBackButton(SettingActivity.this);
        titleTextView = titleBar.getTitleView();
        cacheNumber = (TextView) findViewById(R.id.cache_number);
        clearCache = (RelativeLayout) findViewById(R.id.clear_cache);

    }


    private void initEvents() {
        boolean allow = SharedPreferencesUtil.getAllowNoWifi(getBaseContext());
        slideSwitch.setState(allow);
        slideSwitch.setSlideListener(this);
        titleTextView.setText(R.string.about_me_setting);
        clearCache.setOnClickListener(this);
        cacheNumber.setText(setCacheNumber());
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
                showMessageDialogWithEvent(null, content,setSureEvent,null);
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
//
//    private void setAllowNoWifiSharedPreferences(boolean allow) {
//        SharedPreferences allowNoWifiSharedPreferences= getSharedPreferences("allow_no_wifi_watch_and_download",
//                Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = allowNoWifiSharedPreferences.edit();
//        editor.putBoolean("allow", allow);
//        editor.commit();
//    }
//
//    private SharedPreferences getAllowNoWifiSharedPreferences() {
//        SharedPreferences allowNoWifiSharedPreferences= getSharedPreferences("allow_no_wifi_watch_and_download",
//                Activity.MODE_PRIVATE);
//        return allowNoWifiSharedPreferences;
//    }

    @Override
    public void open() {
        SharedPreferencesUtil.setAllowNoWifiSharedPreferences(getBaseContext(), true);
        showShortToast(R.string.allow_no_wifi_watch);
    }

    @Override
    public void close() {
        SharedPreferencesUtil.setAllowNoWifiSharedPreferences(getBaseContext(),false);
        showShortToast(R.string.not_allow_no_wifi_watch);
    }
}
