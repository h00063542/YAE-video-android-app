package com.yilos.nailstar.aboutme.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.utils.StorageUtils;
import com.yilos.nailstar.R;
import com.yilos.nailstar.framework.view.BaseActivity;
import com.yilos.nailstar.util.DataCleanManager;
import com.yilos.widget.titlebar.TitleBar;

import java.io.File;

/**
 * Created by sisilai on 15/11/10.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private TitleBar titleBar;
    private ImageView backButton;
    private TextView titleTextView;
    private RelativeLayout clearCache;
    private TextView cacheNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initViews();
        initEvents();
    }

    private void initViews() {

        titleBar = (TitleBar) findViewById(R.id.setting_title_bar);
        backButton = titleBar.getBackButton(SettingActivity.this);
        titleTextView = titleBar.getTitleView();
        cacheNumber = (TextView) findViewById(R.id.cache_number);
        clearCache = (RelativeLayout) findViewById(R.id.clear_cache);

    }

    private void initEvents() {

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
                showMessageDialogWithEvent(null, content);
            default:
                break;
        }
    }

    @Override
    protected void setSureEvent() {
        super.setSureEvent();
        DataCleanManager.cleanExternalCache(getApplicationContext());
        String formatCacheNumber = setCacheNumber();
        if (formatCacheNumber.equals("0KB")) {
            cacheNumber.setText(formatCacheNumber);
            showShortToast(R.string.clear_cache_end);
        }
    }

    @Override
    protected void setCancelEvent() {
        super.setCancelEvent();
    }
}
