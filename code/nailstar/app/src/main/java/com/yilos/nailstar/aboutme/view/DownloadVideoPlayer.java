package com.yilos.nailstar.aboutme.view;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;

import com.sina.sinavideo.sdk.VDVideoExtListeners;
import com.sina.sinavideo.sdk.VDVideoView;
import com.sina.sinavideo.sdk.data.VDVideoInfo;
import com.sina.sinavideo.sdk.data.VDVideoListInfo;
import com.yilos.nailstar.R;
import com.yilos.nailstar.framework.view.BaseActivity;

/**
 * Created by yilos on 15/11/21.
 */
public class DownloadVideoPlayer extends BaseActivity implements
        VDVideoExtListeners.OnVDVideoPlaylistListener {

    public static final String TITLE = "title";

    public static final String URL = "url";

    private VDVideoView mVDVideoView = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.download_video_player);
        mVDVideoView = (VDVideoView) findViewById(R.id.vv1);
        mVDVideoView.setVDVideoViewContainer((ViewGroup) mVDVideoView.getParent());

        VDVideoListInfo infoList = new VDVideoListInfo();
        VDVideoInfo info = new VDVideoInfo();
        info.mTitle = getIntent().getStringExtra(TITLE);
        info.mPlayUrl = getIntent().getStringExtra(URL);
        infoList.addVideoInfo(info);

        registerListener();

        mVDVideoView.setIsFullScreen(true);
        mVDVideoView.open(DownloadVideoPlayer.this, infoList);
        mVDVideoView.play(0);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mVDVideoView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVDVideoView.onPause();
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (!mVDVideoView.onVDKeyDown(keyCode, event)) {
//            return super.onKeyDown(keyCode, event);
//        }
//        return true;
//    }

    private void registerListener() {
        mVDVideoView.setPlaylistListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mVDVideoView.onStop();
    }

    @Override
    protected void onDestroy() {
        mVDVideoView.release(false);
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mVDVideoView.setIsFullScreen(true);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            mVDVideoView.setIsFullScreen(false);
        }

    }

    @Override
    public void onPlaylistClick(VDVideoInfo info, int p) {
        if (info == null) {
//            logger.error("onPlaylistClick info is null");
        }
        mVDVideoView.play(p);
    }

}
