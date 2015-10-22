package com.yilos.nailstar.player;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.sinavideo.sdk.VDVideoExtListeners;
import com.sina.sinavideo.sdk.VDVideoView;
import com.sina.sinavideo.sdk.data.VDVideoInfo;
import com.sina.sinavideo.sdk.data.VDVideoListInfo;
import com.sina.sinavideo.sdk.utils.VDVideoFullModeController;
import com.yilos.nailstar.R;
import com.yilos.nailstar.main.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class VideoPlayerActivity extends Activity implements
        VDVideoExtListeners.OnVDVideoFrameADListener,
        VDVideoExtListeners.OnVDVideoInsertADListener,
        VDVideoExtListeners.OnVDVideoPlaylistListener {
    private static final String TAG = "VideoPlayerActivity";

    // 视频播放控件
    private VDVideoView mVDVideoView;
    // 播放的视频列表
    private VDVideoListInfo mVDVideoListInfo;
    // 记录上次播放的视频
    private static int mVideoIndex = 0;
    // 记录上次播放视频的位置
    private static long mVideoPosition = 0;


    private Button mBtnVideoPlayerBack;
    private Button mBtnVideoShare;
    private TextView mTvVideoName;

    private LinearLayout mLayoutShowImageTextContent;

    private String mVideoId;
    private String mVideoName;
    private String mVideoUrl;

    // 图文分解
    private LinearLayout mLayoutVideoDetailContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        init();
    }


    private void init() {
        mVDVideoView = (VDVideoView) findViewById(R.id.video_player);
        mBtnVideoPlayerBack = (Button) findViewById(R.id.btn_video_player_back);
        mBtnVideoShare = (Button) findViewById(R.id.btn_video_share);
        mTvVideoName = (TextView) findViewById(R.id.tv_video_name);

        mLayoutShowImageTextContent = (LinearLayout) findViewById(R.id.layout_show_image_text_content);
        // 获取视频Id，名称，url地址
        mVideoId = getIntent().getStringExtra("");
        mVideoName = getIntent().getStringExtra("");
        mVideoUrl = getIntent().getStringExtra("");

        mLayoutVideoDetailContent = (LinearLayout) findViewById(R.id.layout_video_detail_content);
        mBtnVideoPlayerBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoPlayerActivity.this, MainActivity.class);
                startActivity(intent);
                VideoPlayerActivity.this.finish();
                VideoPlayerActivity.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });


        mBtnVideoShare.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });


        mLayoutShowImageTextContent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showOrHideImageTextDetail();
            }
        });

        // 初始化视频播放器
        initVideoPlayer();
        // 初始化图文分解
        initImageTextDetail();
    }

    private void initVideoPlayer() {
        // 手动这是播放窗口父类，横屏的时候，会用这个做为容器使用，如果不设置，那么默认直接跳转到DecorView
        mVDVideoView.setVDVideoViewContainer((ViewGroup) mVDVideoView
                .getParent());
        mVDVideoListInfo = new VDVideoListInfo();
        VDVideoInfo info = new VDVideoInfo();
        // TODO 调用后台接口，获取信息和评论信息
        info.mTitle = "测试视频";
        info.mPlayUrl = "http://v.yilos.com/d364fc2cf2caadf0fd81463fe912daf7.mp4";
        mVDVideoListInfo.addVideoInfo(info);
        mVDVideoView.open(this, mVDVideoListInfo);
        mVDVideoView.play(0);
    }

    private void initImageTextDetail() {

        String data = "{\"code\":0,\"result\":{\"id\":\"9d923cb0-76d0-11e5-976b-35813a43b6ad\",\"pictures\":[\"http://pic.yilos.com/51131fb2d9fde17361e23774b895a5ac\",\"http://pic.yilos.com/9ac3a65b4395cf9ad0b26aa992523611\",\"http://pic.yilos.com/653915fce88403bef6222de7ad6a72f8\",\"http://pic.yilos.com/1151e381c42c86a6ff86aa26113f655f\",\"http://pic.yilos.com/d260f778a82242d2fa47e30397aa9722\",\"http://pic.yilos.com/42ee7dc6d2e3ff70506faf397a1e6090\",\"http://pic.yilos.com/565cf047d4e8092c1cc969e81eabbf7f\",\"http://pic.yilos.com/88e832ca5df1810c5308a01806dee4e1\",\"http://pic.yilos.com/e631e13d5117d3aa6dc18f829945788c\",\"http://pic.yilos.com/bf117d92cc6b3eb6615a1d81f6b11e80\",\"http://pic.yilos.com/701e5a742529c7b9c8b987dce9a54554\",\"http://pic.yilos.com/a6285fbca2972384583478d3bc4f2962\",\"http://pic.yilos.com/26f6d71bb90c8e98bf26423f450b6f93\",\"http://pic.yilos.com/9fee7e85f9eaf125bb5575a9530c3b56\",\"http://pic.yilos.com/6472164f9b17a1b24079950c7e5b4a31\",\"http://pic.yilos.com/cb43c690d592c321e59a4d439b58b430\",\"http://pic.yilos.com/c04a241e68ac5be7ab0289b679d0ffe8\",\"http://pic.yilos.com/890971326abdd79f82540e1eae922626\",\"http://pic.yilos.com/926cf5efcb458f8b7fac04166ff80afb\",\"http://pic.yilos.com/6032d3b5e5cff25adf304778669be1ca\",\"http://pic.yilos.com/2ff82a21a23121158bfaa8270d6dbb91\"],\"articles\":[\"\",\"1 整甲均匀涂抹两遍乳白色做底色 分别照灯\",\"2 整甲涂抹底胶\",\"3 取适量咖啡色横向在甲面涂抹不规则宽线条\",\"4 取适量深红色横向在甲面涂抹不规则宽线条\",\"5 取适量乳白色横向在甲面涂抹不规则宽线条\",\"取适量乳白色横向在甲面涂抹不规则宽线条\",\"6 用干净的小毛笔将颜色做横向勾绘 照灯\",\"用干净的小毛笔将颜色做横向勾绘 照灯\",\"用干净的小毛笔将颜色做横向勾绘 照灯\",\"7 整甲涂抹底胶\",\"8 取少量黑色横向在甲面涂抹不规则线条\",\"9 取少量白色横向在甲面涂抹不规则线条\",\"10 用干净的小毛笔将颜色做横向勾绘 照灯\",\"用干净的小毛笔将颜色做横向勾绘 照灯\",\"11 整甲涂抹底胶\",\"12 取少量白色彩绘胶在甲面横向涂抹不规则线条 照灯\",\"13 整甲均匀涂抹封层 照灯\",\"14 用清洁液清洁表面浮胶\",\"15 完成 展示\",\"16款式与应用\"]}}";
        JSONArray pictures = null;
        JSONArray articles = null;
        try {
            JSONObject jsonResult = new JSONObject(data).getJSONObject("result");
            pictures = jsonResult.getJSONArray("pictures");
            articles = jsonResult.getJSONArray("articles");
            if (null == pictures || null == articles) {
                // TODO 提示没有图文分解
                return;
            } else if (pictures.length() != articles.length()) {
                // TODO 提示接口返回图文分解错误
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // TODO 提示没有图文分解
            return;
        } catch (NullPointerException e) {
            e.printStackTrace();
            // TODO 提示没有图文分解
            return;
        }

        MarginLayoutParams layoutParams = new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10, 10, 10, 10);
        for (int i = 0; i < pictures.length(); i++) {
            try {
//                ImageView imageView = new ImageView(this);
//                imageView.setLayoutParams(layoutParams);
//                imageView.setImageURI(Uri.parse(pictures.getString(i)));
//                mLayoutVideoDetailContent.addView(imageView);

                TextView textView = new TextView(this);
                textView.setLayoutParams(layoutParams);
                textView.setText(articles.getString(i));
                mLayoutVideoDetailContent.addView(textView);
            } catch (JSONException e) {
                e.printStackTrace();
                // TODO 提示解析图文分解失败
                return;
            }

        }

    }

    /**
     * 显示或隐藏图文分解详情
     */
    private void showOrHideImageTextDetail() {
        int visibility = mLayoutVideoDetailContent.getVisibility();
        // 显示图文分解
        if (View.GONE == visibility) {
            mLayoutVideoDetailContent.setVisibility(View.VISIBLE);
        } else {// 隐藏图文分解
            mLayoutVideoDetailContent.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (!mVDVideoView.onVDKeyDown(keyCode, event)) {
            return super.onKeyDown(keyCode, event);
        }
        return true;
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        mVDVideoView.onResume();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        mVDVideoView.onPause();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        mVDVideoView.onStop();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mVDVideoView.setIsFullScreen(true);
            Log.e(VDVideoFullModeController.TAG, "onConfigurationChanged---横屏");
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            mVDVideoView.setIsFullScreen(false);
            Log.e(VDVideoFullModeController.TAG, "onConfigurationChanged---竖屏");
        }

    }

    /**
     * 播放列表里面点击了某个视频，触发外部事件
     */
    @Override
    public void onPlaylistClick(VDVideoInfo info, int p) {
        // TODO Auto-generated method stub
        if (info == null) {
            Log.e(TAG, "info is null");
        }
        mVDVideoView.play(p);
    }

    /**
     * 视频插入广告传回的接口，表明当前的广告被点击了『了解更多』
     */
    @Override
    public void onInsertADClick(VDVideoInfo info) {
        // TODO Auto-generated method stub
        Toast.makeText(this, "插入广告被点击了", Toast.LENGTH_LONG).show();
    }

    /**
     * 视频插入广告传回的接口，表明当前的广告被点击了『去掉广告』，按照其他视频逻辑，直接跳转会员页
     */
    @Override
    public void onInsertADStepOutClick(VDVideoInfo info) {
        // TODO Auto-generated method stub
        Toast.makeText(this, "去掉广告被点击了", Toast.LENGTH_LONG).show();
    }

    /**
     * 静帧广告换图，从这儿来
     */
    @Override
    public void onFrameADPrepared(VDVideoInfo info) {
        // TODO Auto-generated method stub
        Toast.makeText(this, "开始换图", Toast.LENGTH_LONG).show();
    }

}

class AsyncImageLoader {

    private HashMap<String, SoftReference<Drawable>> imageCache;

    public AsyncImageLoader() {
        imageCache = new HashMap<String, SoftReference<Drawable>>();
    }

    public Drawable loadDrawable(final String imageUrl, final ImageCallback imageCallback) {
        if (imageCache.containsKey(imageUrl)) {
            SoftReference<Drawable> softReference = imageCache.get(imageUrl);
            Drawable drawable = softReference.get();
            if (drawable != null) {
                return drawable;
            }
        }
        final Handler handler = new Handler() {
            public void handleMessage(Message message) {
                imageCallback.imageLoaded((Drawable) message.obj, imageUrl);
            }
        };
        new Thread() {
            @Override
            public void run() {
                Drawable drawable = loadImageFromUrl(imageUrl);
                imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
                Message message = handler.obtainMessage(0, drawable);
                handler.sendMessage(message);
            }
        }.start();
        return null;
    }

    public static Drawable loadImageFromUrl(String url) {
        URL m;
        InputStream i = null;
        try {
            m = new URL(url);
            i = (InputStream) m.getContent();
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Drawable d = Drawable.createFromStream(i, "src");
        return d;
    }

    public interface ImageCallback {
        public void imageLoaded(Drawable imageDrawable, String imageUrl);
    }

}