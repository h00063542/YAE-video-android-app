package com.yilos.nailstar.player;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.sinavideo.sdk.VDVideoExtListeners;
import com.sina.sinavideo.sdk.VDVideoView;
import com.sina.sinavideo.sdk.VDVideoViewController;
import com.sina.sinavideo.sdk.data.VDVideoInfo;
import com.sina.sinavideo.sdk.data.VDVideoListInfo;
import com.sina.sinavideo.sdk.utils.VDVideoFullModeController;
import com.yilos.nailstar.R;
import com.yilos.nailstar.framework.view.BaseActivity;
import com.yilos.nailstar.main.MainActivity;
import com.yilos.nailstar.player.entity.VideoEntity;
import com.yilos.nailstar.player.entity.VideoImageTextInfoEntity;
import com.yilos.nailstar.player.entity.VideoInfoEntity;
import com.yilos.nailstar.player.presenter.VideoPlayerPresenter;
import com.yilos.nailstar.player.view.IVideoPlayerView;
import com.yilos.widget.view.ImageCacheView;

import org.json.JSONException;

import java.util.ArrayList;

public class VideoPlayerActivity extends BaseActivity implements
        IVideoPlayerView,
        VDVideoExtListeners.OnVDVideoFrameADListener,
        VDVideoExtListeners.OnVDVideoInsertADListener,
        VDVideoExtListeners.OnVDVideoPlaylistListener {
    private static final String TAG = "VideoPlayerActivity";

    private VideoPlayerPresenter mVideoPlayerPresenter = new VideoPlayerPresenter(this);

    // 视频播放控件
    private VDVideoView mVDVideoView;
    // 播放的视频列表
    private VDVideoListInfo mVDVideoListInfo;


    private Button mBtnVideoPlayerBack;
    private Button mBtnVideoShare;
    private TextView mTvVideoName;
    private ImageView mIvVideoAuthorPhoto;
    private TextView mTvVideoAuthorName;
    private TextView mTvVideoPlayingTimes;
    private ImageView mIvMoreVideosIcon;

    private LinearLayout mLayoutShowImageTextContent;

    private ViewGroup mLayoutVideoDetailContentParent;

    private String mVideoId;


    // 图文分解
    private LinearLayout mLayoutVideoDetailContent;

    private VideoInfoEntity mVideoInfoEntity;
    private VideoEntity mVideoEntity;

    private VideoImageTextInfoEntity mVideoImageTextInfoEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        init();
        try {
            mVideoPlayerPresenter.playerVideo(mVideoId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void init() {
        mBtnVideoPlayerBack = (Button) findViewById(R.id.btn_video_player_back);
        mBtnVideoShare = (Button) findViewById(R.id.btn_video_share);
        mTvVideoName = (TextView) findViewById(R.id.tv_video_name);
        mVDVideoView = (VDVideoView) findViewById(R.id.video_player);
        // 手动这是播放窗口父类，横屏的时候，会用这个做为容器使用，如果不设置，那么默认直接跳转到DecorView
        mVDVideoView.setVDVideoViewContainer((ViewGroup) mVDVideoView
                .getParent());
        mIvVideoAuthorPhoto = (ImageView) findViewById(R.id.iv_video_author_photo);
        mTvVideoAuthorName = (TextView) findViewById(R.id.tv_video_author_name);
        mTvVideoPlayingTimes = (TextView) findViewById(R.id.tv_video_playing_times);
        mIvMoreVideosIcon = (ImageView) findViewById(R.id.iv_more_videos_icon);

        mLayoutShowImageTextContent = (LinearLayout) findViewById(R.id.layout_show_image_text_content);
        // 获取视频Id，名称，url地址
        mVideoId = getIntent().getStringExtra("");

        mLayoutVideoDetailContent = (LinearLayout) findViewById(R.id.layout_video_detail_content);
        mLayoutVideoDetailContentParent = (ViewGroup) mLayoutVideoDetailContent.getParent();

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

        // 初始化图文分解
        initImageTextDetail();
    }


    private void initImageTextDetail() {
        try {
            mVideoPlayerPresenter.getVideoImageTextInfo("", new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    mVideoImageTextInfoEntity = (VideoImageTextInfoEntity) msg.obj;
                    ArrayList pictures = mVideoImageTextInfoEntity.getPictures();
                    ArrayList articles = mVideoImageTextInfoEntity.getArticles();

                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(0, 10, 0, 10);

//                    MarginLayoutParams layoutParams = new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//                    layoutParams.setMargins(0, 10, 0, 10);
                    for (int i = 0; i < pictures.size(); i++) {
                        ImageCacheView imageView = new ImageCacheView(VideoPlayerActivity.this);
                        imageView.setLayoutParams(lp);
                        imageView.setImageSrc(pictures.get(i).toString());
                        mLayoutVideoDetailContent.addView(imageView);

                        TextView textView = new TextView(VideoPlayerActivity.this);
                        textView.setLayoutParams(lp);
                        textView.setText(articles.get(i).toString());
                        mLayoutVideoDetailContent.addView(textView);

                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示或隐藏图文分解详情
     */
    private void showOrHideImageTextDetail() {
        int visibility = mLayoutVideoDetailContentParent.getVisibility();
        // 显示图文分解
        if (View.GONE == visibility) {
            mLayoutVideoDetailContentParent.setVisibility(View.VISIBLE);
            RotateAnimation rotateAnimation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setDuration(400);
            rotateAnimation.setFillAfter(true);
            mIvMoreVideosIcon.startAnimation(rotateAnimation);

        } else {// 隐藏图文分解
            mLayoutVideoDetailContentParent.setVisibility(View.GONE);
            RotateAnimation rotateAnimation = new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setDuration(400);
            rotateAnimation.setFillAfter(true);
            mIvMoreVideosIcon.startAnimation(rotateAnimation);
        }


//        mIvMoreVideosIcon.setDrawingCacheEnabled(true);
//        Bitmap bitmap = Bitmap.createBitmap(mIvMoreVideosIcon.getDrawingCache());
//        mIvMoreVideosIcon.setDrawingCacheEnabled(false);
//        mIvMoreVideosIcon.setImageBitmap(toTurn(bitmap));
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
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(VDVideoViewController
                .getInstance(VideoPlayerActivity.this).mReciever);
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

    @Override
    public String getVideoId() {
        return mVideoId;
    }

    @Override
    public void playVideo(VideoInfoEntity videoInfoEntity) {
        showVideoInfo2Page(videoInfoEntity);
        mVDVideoListInfo = new VDVideoListInfo();
        VDVideoInfo info = new VDVideoInfo();
        mVideoEntity = videoInfoEntity.getVideos().get(0);
        info.mTitle = videoInfoEntity.getTitle();
        info.mPlayUrl = mVideoEntity.getOssUrl();
        mVDVideoListInfo.addVideoInfo(info);
        mVDVideoView.open(VideoPlayerActivity.this, mVDVideoListInfo);
        mVDVideoView.play(0);
    }

    @Override
    public void showImageTextDetail() {
        showOrHideImageTextDetail();
    }

    @Override
    public void hideImageTextDetail() {
        showOrHideImageTextDetail();
    }

    // 将视频信息显示在界面上
    private void showVideoInfo2Page(VideoInfoEntity videoInfoEntity) {
        mTvVideoName.setText(videoInfoEntity.getTitle());
        mTvVideoAuthorName.setText(videoInfoEntity.getAuthor());
        mTvVideoPlayingTimes.setText(String.valueOf(videoInfoEntity.getVideos().get(0).getPlayTimes()));
    }

    // 图片翻转180度
    public Bitmap toTurn(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate(180); /*翻转180度*/
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap img = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return img;
    }

    /**
     * 圆角处理
     * 实际上是在原图片上画了一个圆角遮罩。对于paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
     * 方法我刚看到也是一知半解Mode.SRC_IN参数是个画图模式，该类型是指只显 示两层图案的交集部分，且交集部位只显示上层图像。
     * 实际就是先画了一个圆角矩形的过滤框，于是形状有了，再将框中的内容填充为图片
     *
     * @param bitmap
     * @param roundPx
     * @return
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new android.graphics.PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}