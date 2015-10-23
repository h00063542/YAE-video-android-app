package com.yilos.nailstar.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.yilos.nailstar.R;
import com.yilos.nailstar.framework.exception.JSONParseException;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.index.presenter.IndexPresenter;
import com.yilos.nailstar.main.MainActivity;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGHT = 2000; //延迟

    private long loadTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadTime = System.currentTimeMillis();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        loadIndex();
    }

    /**
     * 加载首页内容，并在完成后跳转
     */
    private void loadIndex(){
        final Handler mainHandler = new Handler(){
            public void handleMessage(Message msg){
                long nowTime = System.currentTimeMillis();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        SplashActivity.this.finish();
                        SplashActivity.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                }, SPLASH_DISPLAY_LENGHT - nowTime + loadTime);
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    IndexPresenter.getInstance().preloadIndexContent();
                } catch (NetworkDisconnectException e) {
                    //e.printStackTrace();
                } catch (JSONParseException e) {
                    //e.printStackTrace();
                }

                Message msg = mainHandler.obtainMessage();
                msg.what = 1;
                msg.setTarget(mainHandler);
                msg.sendToTarget();
            }
        }).start();
    }
}
