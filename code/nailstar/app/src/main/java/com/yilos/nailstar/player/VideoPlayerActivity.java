package com.yilos.nailstar.player;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
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

import java.util.ArrayList;

public class VideoPlayerActivity extends BaseActivity implements
        IVideoPlayerView,
        VDVideoExtListeners.OnVDVideoPlaylistListener {
    private static final String TAG = "VideoPlayerActivity";

    // 视频播放控件
    private VDVideoView mVDVideoView;

    private ImageButton mBtnVideoPlayerBack;
    private ImageButton mBtnVideoShare;
    private TextView mTvVideoName;
    private ImageView mIvVideoAuthorPhoto;
    private TextView mTvVideoAuthorName;
    private TextView mTvVideoPlayingTimes;
    private ImageView mIvVideoImageTextIcon;
    private TextView mTvVideoAuthorTag1;
    private TextView mTvVideoAuthorTag2;
    private TextView mTvVideoAuthorTag3;

    private LinearLayout mLayoutShowImageTextContent;

    private ViewGroup mLayoutVideoDetailContentParent;

    private String mTopicsId;

    // 图文分解
    private LinearLayout mLayoutVideoDetailContent;


    private VideoPlayerPresenter mVideoPlayerPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        init();
        mVideoPlayerPresenter.playerVideo(mTopicsId);
        mVideoPlayerPresenter.initVideoImageTextInfo(mTopicsId);
    }


    private void init() {
        mBtnVideoPlayerBack = (ImageButton) findViewById(R.id.btn_video_player_back);
        mBtnVideoShare = (ImageButton) findViewById(R.id.btn_video_share);
        mTvVideoName = (TextView) findViewById(R.id.tv_video_name);
        mVDVideoView = (VDVideoView) findViewById(R.id.video_player);
        // 手动这是播放窗口父类，横屏的时候，会用这个做为容器使用，如果不设置，那么默认直接跳转到DecorView
        mVDVideoView.setVDVideoViewContainer((ViewGroup) mVDVideoView
                .getParent());
        mIvVideoAuthorPhoto = (ImageView) findViewById(R.id.iv_video_author_photo);
        mTvVideoAuthorName = (TextView) findViewById(R.id.tv_video_author_name);
        mTvVideoPlayingTimes = (TextView) findViewById(R.id.tv_video_playing_times);
        mIvVideoImageTextIcon = (ImageView) findViewById(R.id.iv_video_image_text_icon);
        mTvVideoAuthorTag1 = (TextView) findViewById(R.id.tv_video_author_tag_1);
        mTvVideoAuthorTag2 = (TextView) findViewById(R.id.tv_video_author_tag_2);
        mTvVideoAuthorTag3 = (TextView) findViewById(R.id.tv_video_author_tag_3);

        mLayoutShowImageTextContent = (LinearLayout) findViewById(R.id.layout_show_image_text_content);
        // 获取视频Id，名称，url地址
        mTopicsId = getIntent().getStringExtra("");

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

        mVideoPlayerPresenter = VideoPlayerPresenter.getInstance(this);
    }


    /**
     * 显示或隐藏图文分解详情
     */
    public void showOrHideImageTextDetail() {
        int visibility = mLayoutVideoDetailContentParent.getVisibility();
        // 显示图文分解
        if (View.GONE == visibility) {
            mLayoutVideoDetailContentParent.setVisibility(View.VISIBLE);
            RotateAnimation rotateAnimation = new RotateAnimation(0, 180
                    , Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setDuration(400);
            rotateAnimation.setFillAfter(true);
            mIvVideoImageTextIcon.startAnimation(rotateAnimation);

        } else {// 隐藏图文分解
            mLayoutVideoDetailContentParent.setVisibility(View.GONE);
            RotateAnimation rotateAnimation = new RotateAnimation(181, 359
                    , Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setDuration(400);
            rotateAnimation.setFillAfter(true);
            mIvVideoImageTextIcon.startAnimation(rotateAnimation);
        }
    }


    @Override
    public void playVideo(VideoInfoEntity videoInfoEntity) {
        //TODO 提示获取视频信息失败
        if (null == videoInfoEntity) {
            return;
        }

        showVideoInfo2Page(videoInfoEntity);
        VDVideoListInfo mVDVideoListInfo = new VDVideoListInfo();
        VDVideoInfo info = new VDVideoInfo();
        VideoEntity mVideoEntity = videoInfoEntity.getVideos().get(0);
        info.mTitle = videoInfoEntity.getTitle();
        info.mPlayUrl = mVideoEntity.getOssUrl();
        mVDVideoListInfo.addVideoInfo(info);
        mVDVideoView.open(VideoPlayerActivity.this, mVDVideoListInfo);
        mVDVideoView.play(0);
    }

    @Override
    public void initVideoImageTextInfo(VideoImageTextInfoEntity videoImageTextInfoEntity) {
        //TODO 提示获取视频图文信息失败
        if (null == videoImageTextInfoEntity) {
            return;
        }
        ArrayList pictures = videoImageTextInfoEntity.getPictures();
        ArrayList articles = videoImageTextInfoEntity.getArticles();

        LinearLayout.LayoutParams lpMarginTopBottom = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lpMarginTopBottom.setMargins(0, 10, 0, 10);


        LinearLayout.LayoutParams lpMarginTop = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lpMarginTop.setMargins(0, 20, 0, 0);


        for (int i = 0; i < pictures.size(); i++) {
            ImageCacheView imageView = new ImageCacheView(VideoPlayerActivity.this);
            // 如果没有配图没有文字，并且不是第一张图片时，不需要设置Margin Top
//            imageView.setLayoutParams(((null == articles.get(i) || articles.get(i).toString().length() == 0))
//                    ? lpMarginTop : lpMarginTopBottom);
            imageView.setLayoutParams(lpMarginTop);
            imageView.setImageSrc(pictures.get(i).toString());
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO 显示大图
                }
            });
            mLayoutVideoDetailContent.addView(imageView);

            if (articles.get(i).toString().length() > 0) {
                TextView textView = new TextView(VideoPlayerActivity.this);
                textView.setLayoutParams(lpMarginTop);
                textView.setText(articles.get(i).toString());
                mLayoutVideoDetailContent.addView(textView);
            }
        }
    }

    // 将视频信息显示在界面上
    private void showVideoInfo2Page(VideoInfoEntity videoInfoEntity) {
        mTvVideoName.setText(videoInfoEntity.getTitle());
        mIvVideoAuthorPhoto.setImageBitmap(ImageLoader.getInstance().loadImageSync(videoInfoEntity.getAuthorPhoto()));
        mTvVideoAuthorName.setText(videoInfoEntity.getAuthor());
        mTvVideoPlayingTimes.setText(String.valueOf(videoInfoEntity.getVideos().get(0).getPlayTimes()));
        ArrayList tags = videoInfoEntity.getTags();
        if (!tags.isEmpty()) {
            if (null != tags.get(0)) {
                mTvVideoAuthorTag1.setText(tags.get(0).toString());
            }
            if (null != tags.get(1)) {
                mTvVideoAuthorTag2.setText(tags.get(1).toString());
            }
            if (null != tags.get(2)) {
                mTvVideoAuthorTag3.setText(tags.get(2).toString());
            }
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!mVDVideoView.onVDKeyDown(keyCode, event)) {
            return super.onKeyDown(keyCode, event);
        }
        return true;
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

    @Override
    protected void onStop() {
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
        if (info == null) {
            Log.e(TAG, "info is null");
        }
        mVDVideoView.play(p);
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
    public Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {

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