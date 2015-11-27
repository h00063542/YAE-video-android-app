package com.yilos.nailstar.aboutme.view;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.android.tedcoder.wkvideoplayer.view.SuperVideoPlayer;
import com.yilos.nailstar.R;

/**
 * Created by yilos on 15/11/21.
 */
public class DownloadVideoPlayer extends AppCompatActivity {

    public static final String TITLE = "title";

    public static final String URL = "url";

    private SuperVideoPlayer mVDVideoView = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.download_video_player);
        mVDVideoView = (SuperVideoPlayer) findViewById(R.id.vv1);

        mVDVideoView.loadLocalVideo(getIntent().getStringExtra(URL));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVDVideoView.goOnPlay();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVDVideoView.pausePlay(false);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mVDVideoView.pausePlay(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVDVideoView.close();
    }

}
