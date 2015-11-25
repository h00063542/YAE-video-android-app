package com.yilos.nailstar.splash.view;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.yilos.nailstar.R;
import com.yilos.nailstar.main.MainActivity;
import com.yilos.nailstar.splash.presenter.SplashPresenter;
import com.yilos.nailstar.util.SettingUtil;
import com.yilos.nailstar.util.TaskManager;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGHT = 2000; //延迟

    private long loadTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 设置不显示状态栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 设置不允许旋转屏幕
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        loadTime = System.currentTimeMillis();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        loadIndex();
    }

    /**
     * 加载首页内容，并在完成后跳转
     */
    private void loadIndex(){
        TaskManager.BackgroundTask preload = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                SplashPresenter.getInstance().splashWork();
                return null;
            }
        };

        TaskManager.UITask showMain = new TaskManager.UITask() {
            @Override
            public Object doWork(Object data) {
                long nowTime = System.currentTimeMillis();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent;
                        if (SettingUtil.getFirstFlag()) {
                            intent = new Intent(SplashActivity.this, GuideActivity.class);
                        } else {
                            intent = new Intent(SplashActivity.this, MainActivity.class);
                        }
                        startActivity(intent);
                        SplashActivity.this.finish();
                        SplashActivity.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                }, SPLASH_DISPLAY_LENGHT - nowTime + loadTime);
                return null;
            }
        };

        new TaskManager()
                .next(preload)
                .next(showMain)
                .start();
    }
}
