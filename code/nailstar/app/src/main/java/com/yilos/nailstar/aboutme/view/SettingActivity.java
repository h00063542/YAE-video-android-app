package com.yilos.nailstar.aboutme.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StatFs;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leaking.slideswitch.SlideSwitch;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.entity.Sdcard;
import com.yilos.nailstar.aboutme.entity.StorageList;
import com.yilos.nailstar.aboutme.model.LoginAPI;
import com.yilos.nailstar.framework.view.BaseActivity;
import com.yilos.nailstar.util.DataCleanManager;
import com.yilos.nailstar.util.SettingUtil;
import com.yilos.widget.titlebar.TitleBar;

import java.io.File;
import java.util.ArrayList;

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

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return this.getString(R.string.can_not_find_version_name);
        }
    }


    private void initEvents() {
        if (LoginAPI.getInstance().isLogin()) {
            loginOut.setVisibility(View.VISIBLE);
            loginOut.setOnClickListener(this);
        }
        goToAboutUs.setOnClickListener(this);
        settingFolder.setOnClickListener(this);
        versionName = getVersion();
        versionInfoNumber.setText(versionName);
        boolean allow = SettingUtil.getAllowNoWifi();
        slideSwitch.setState(allow);
        slideSwitch.setSlideListener(this);
        titleTextView.setText(R.string.about_me_setting);
        clearCache.setOnClickListener(this);
        cacheNumber.setText(setCacheNumber());
        ArrayList<Sdcard> sdcardArrayList = new ArrayList<>();
        sdcardArrayList = getSdcard();
        Sdcard sdcard = sdcardArrayList.get(sdcardArrayList.size() - 1);
        SettingUtil.setSdcardPathSharedPreferences(sdcard.getSdcardName(),sdcard.getSdcardPath());
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
                aboutUsIntent.putExtra("versionName", versionName);
                startActivity(aboutUsIntent);
                break;
            case R.id.login_out:
                LoginAPI.getInstance().getLoginUserId();  //获取登录的用户ID
                LoginAPI.getInstance().logout(); //退出登录
                break;
            case R.id.setting_folder:
                DialogPlus dialog = DialogPlus.newDialog(this)
                        .setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new String[]{"asdfa"}))
                                .setOnItemClickListener(new OnItemClickListener() {
                                    @Override
                                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                                    }
                                })
                                .setGravity(Gravity.CENTER)
                                        .setHeader(R.layout.setting_folder_dialog_header)
                                //.setExpanded(false)  // This will enable the expand feature, (similar to android L share dialog)
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
        SettingUtil.setAllowNoWifiSharedPreferences(true);
        showShortToast(R.string.allow_no_wifi_watch);
    }

    @Override
    public void close() {
        SettingUtil.setAllowNoWifiSharedPreferences(false);
        showShortToast(R.string.not_allow_no_wifi_watch);
    }

    private ArrayList<Sdcard> getSdcard() {

        StorageList storageList = new StorageList(SettingActivity.this);
        String[] paths;
        paths = storageList.getVolumePaths();

        ArrayList<Sdcard> sdcardArrayList = new ArrayList<>();

        for (int index = 0; index < paths.length; index++) {
            Sdcard sdcard = new Sdcard();
            sdcard.setSdcardName("存储卡" + String.valueOf(index + 1));
            sdcard.setSdcardPath(paths[index]);
            StatFs sf = new StatFs(sdcard.getSdcardPath());
            long blockSize = sf.getBlockSize(); //每个block大小
            long blockCount = sf.getBlockCount(); //总大小
            long availCount = sf.getAvailableBlocks(); //有效大小
            sdcard.setBlockCount(blockSize * blockCount);
            sdcard.setAvailCount(blockSize * availCount);
            try {
                sdcard.setBlockCountFormat(DataCleanManager.getFormatSize(sdcard.getBlockCount()));
                sdcard.setAvailCountFormat(DataCleanManager.getFormatSize(sdcard.getAvailCount()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            sdcardArrayList.add(sdcard);
        }

        return sdcardArrayList;
    }
}
