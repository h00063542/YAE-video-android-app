package com.yilos.nailstar.player;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.sina.sinavideo.sdk.VDVideoExtListeners;
import com.sina.sinavideo.sdk.VDVideoView;
import com.sina.sinavideo.sdk.VDVideoViewController;
import com.sina.sinavideo.sdk.data.VDVideoInfo;
import com.sina.sinavideo.sdk.data.VDVideoListInfo;
import com.sina.sinavideo.sdk.utils.VDVideoFullModeController;
import com.yilos.nailstar.R;
import com.yilos.nailstar.framework.view.BaseActivity;
import com.yilos.nailstar.main.MainActivity;
import com.yilos.nailstar.player.entity.TopicCommentInfo;
import com.yilos.nailstar.player.entity.TopicCommentReplyInfo;
import com.yilos.nailstar.player.entity.TopicImageTextInfo;
import com.yilos.nailstar.player.entity.TopicInfo;
import com.yilos.nailstar.player.entity.TopicRelatedInfo;
import com.yilos.nailstar.player.entity.VideoInfo;
import com.yilos.nailstar.player.presenter.TopicPresenter;
import com.yilos.nailstar.player.view.IVideoPlayerView;
import com.yilos.nailstar.util.CollectionUtil;
import com.yilos.nailstar.util.Constants;
import com.yilos.nailstar.util.StringUtil;
import com.yilos.widget.circleimageview.CircleImageView;
import com.yilos.widget.pullrefresh.PullToRefreshView;
import com.yilos.widget.view.ImageCacheView;

import java.util.ArrayList;
import java.util.Date;

public class VideoPlayerActivity extends BaseActivity implements
        IVideoPlayerView,
        VDVideoExtListeners.OnVDVideoPlaylistListener,
        PullToRefreshView.OnHeaderRefreshListener,
        PullToRefreshView.OnFooterRefreshListener {
    private static final String TAG = "VideoPlayerActivity";

    // 顶部返回、topic名称、分享
    private ImageView mBtnVideoPlayerBack;
    private ImageView mBtnVideoShare;
    private TextView mTvVideoName;

    private PullToRefreshView mTopicPullToRefreshView;
    private ScrollView mSvVideoPlayer;
    // 视频播放控件
    private VDVideoView mVDVideoView;
    private FloatingActionButton mFabVideoPlayer;

    // 作者信息
//    private LinearLayout mLayoutTopicsImageTextHead;
    private CircleImageView mIvVideoAuthorPhoto;
    private TextView mTvVideoAuthorName;
    private TextView mTvVideoPlayingTimes;
    private ImageView mIvVideoImageTextIcon;
    private TextView mTvVideoAuthorTag1;
    private TextView mTvVideoAuthorTag2;
    private TextView mTvVideoAuthorTag3;

    // 更多视频
    private LinearLayout mLayoutMoreVideosContent;


    // 图文分解
    private LinearLayout mLayoutTopicImageTextContent;
    private TextView mTvHideTopicImageTextContent;
    private TextView mTvDownloadTopicImageTextContent;

    private LinearLayout mLayoutShowTopicImageTextContent;
    private LinearLayout mLayoutTopicImageTextContentParent;

    // topic评论
    private LinearLayout mLayoutTopicComments;

    private FloatingActionButton mFabBackTop;

    // 底部下载、收藏、评论、交作业
    private RelativeLayout mLayoutBtnVideoDownload;
    private RelativeLayout mLayoutBtnVideoCollection;
    private RelativeLayout mLayoutBtnVideoComment;

    private TextView mTvVideoSubmittedResult;

    private String mTopicId;
    private int mPage = 1;
    // 是否最后一页评论
    private boolean mIsTopicsCommentLastPage = false;


    private TopicPresenter mVideoPlayerPresenter;

//    private float mTopicAuthorFontSize;
//    private float mTopicVideoPlayingTimeFontSize;
//    private float mTopicImageTextFontSize;

    private float mCommentAuthorFontSize;
    private float mCommentIsHomeWorkFontSize;
    private float mCommentCreateDateFontSize;
    private float mCommentContentFontSize;
    private float mCommentReplyFontSize;

    private int mAuthorPhotoSize;
    private int mAuthorPhotoMargin;

    private int mTopicRelatedMarginRight;
    private int mTopicRelatedPlayImageSize;

    private int mCommentMarginTop;
    private int mCommentMarginRight;
    private int mCommentMarginBottom;
    private int mCommentContentMarginTop;
    private int mCommentContentMarginBottom;
    private int mCommentContentPicMarginBottom;

    private int mImageTextMargin;


    private int mCommentReplyMarginBottom;
    private int mCommentReplyLineHeight;

    private int mCommentReplyPaddingLeft;
    private int mCommentReplyPaddingTop;
    private int mCommentReplyPaddingRight;
    private int mCommentReplyPaddingBottom;

    private int mCommentReplyBackgroundColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        mVideoPlayerPresenter = TopicPresenter.getInstance(this);
        init();
    }


    private void init() {
        // 获取topic id
        mTopicId = getIntent().getStringExtra(Constants.TOPIC_ID);
        mPage = 1;
        // 初始化控件
        initControl();
        // 绑定控件事件
        bindControlEvent();

        mCommentAuthorFontSize = getResources().getDimensionPixelSize(R.dimen.topic_comment_author_font_size);
        mCommentIsHomeWorkFontSize = getResources().getDimensionPixelSize(R.dimen.topic_comment_is_home_work_font_size);
        mCommentCreateDateFontSize = getResources().getDimensionPixelSize(R.dimen.topic_comment_create_date_font_size);
        mCommentContentFontSize = getResources().getDimensionPixelSize(R.dimen.topic_comment_content_font_size);
        mCommentReplyFontSize = getResources().getDimensionPixelSize(R.dimen.topic_comment_reply_font_size);

        mAuthorPhotoSize = getResources().getDimensionPixelSize(R.dimen.topic_comment_author_photo_size);
        mAuthorPhotoMargin = getResources().getDimensionPixelSize(R.dimen.topic_comment_author_photo_margin);

        mTopicRelatedMarginRight = getResources().getDimensionPixelSize(R.dimen.topic_related_margin_right);
        mTopicRelatedPlayImageSize = getResources().getDimensionPixelSize(R.dimen.topic_related_play_image_size);


        mCommentMarginTop = getResources().getDimensionPixelSize(R.dimen.topic_comment_margin_top);
        mCommentMarginRight = getResources().getDimensionPixelSize(R.dimen.topic_comment_margin_right);
        mCommentMarginBottom = getResources().getDimensionPixelSize(R.dimen.topic_comment_margin_bottom);

        mCommentContentMarginTop = getResources().getDimensionPixelSize(R.dimen.topic_comment_content_margin_top);
        mCommentContentMarginBottom = getResources().getDimensionPixelSize(R.dimen.topic_comment_content_margin_bottom);
        mCommentContentPicMarginBottom = getResources().getDimensionPixelSize(R.dimen.topic_comment_content_pic_margin_bottom);

        mImageTextMargin = getResources().getDimensionPixelSize(R.dimen.topic_image_text_margin);

        mCommentReplyMarginBottom = getResources().getDimensionPixelSize(R.dimen.topic_comment_reply_margin_bottom);
        mCommentReplyLineHeight = getResources().getDimensionPixelSize(R.dimen.topic_comment_reply_line_height);

        mCommentReplyPaddingLeft = getResources().getDimensionPixelSize(R.dimen.topic_comment_reply_padding_left);
        mCommentReplyPaddingTop = getResources().getDimensionPixelSize(R.dimen.topic_comment_reply_padding_top);
        mCommentReplyPaddingRight = getResources().getDimensionPixelSize(R.dimen.topic_comment_reply_padding_right);
        mCommentReplyPaddingBottom = getResources().getDimensionPixelSize(R.dimen.topic_comment_reply_padding_bottom);

        mCommentReplyBackgroundColor = getResources().getColor(R.color.topic_comment_reply_background_color);


        mVideoPlayerPresenter.playerVideo(mTopicId);
        mVideoPlayerPresenter.initTopicRelatedInfo(mTopicId);
        mVideoPlayerPresenter.initTopicImageTextInfo(mTopicId);
        mVideoPlayerPresenter.initTopicComments(mTopicId, mPage);
    }

    private void initControl() {
        // 顶部返回、topic名称、分享
        mBtnVideoPlayerBack = (ImageView) findViewById(R.id.btn_video_player_back);
        mBtnVideoShare = (ImageView) findViewById(R.id.btn_video_share);
        mTvVideoName = (TextView) findViewById(R.id.tv_video_name);

        mTopicPullToRefreshView = (PullToRefreshView) findViewById(R.id.topic_pull_refresh_view);
//        mTopicPullToRefreshView.setOnHeaderRefreshListener(this);
        mTopicPullToRefreshView.setOnFooterRefreshListener(this);
        // 视频播放控件
        mSvVideoPlayer = (ScrollView) findViewById(R.id.sv_video_player);
        mVDVideoView = (VDVideoView) findViewById(R.id.video_player);
        // 手动这是播放窗口父类，横屏的时候，会用这个做为容器使用，如果不设置，那么默认直接跳转到DecorView
        mVDVideoView.setVDVideoViewContainer((ViewGroup) mVDVideoView.getParent());
//        mFabVideoPlayer = (FloatingActionButton) findViewById(R.id.fab_video_player);

        // 作者信息
//        mLayoutTopicsImageTextHead = (LinearLayout) findViewById(R.id.layout_topic_image_text_head);
        mIvVideoAuthorPhoto = (CircleImageView) findViewById(R.id.iv_video_author_photo);
        mTvVideoAuthorName = (TextView) findViewById(R.id.tv_video_author_name);
        mTvVideoPlayingTimes = (TextView) findViewById(R.id.tv_video_playing_times);
        mIvVideoImageTextIcon = (ImageView) findViewById(R.id.iv_video_image_text_icon);
        mTvVideoAuthorTag1 = (TextView) findViewById(R.id.tv_video_author_tag_1);
        mTvVideoAuthorTag2 = (TextView) findViewById(R.id.tv_video_author_tag_2);
        mTvVideoAuthorTag3 = (TextView) findViewById(R.id.tv_video_author_tag_3);

        // 更多视频
        mLayoutMoreVideosContent = (LinearLayout) findViewById(R.id.layout_more_videos_content);

        // 图文分解
        mLayoutShowTopicImageTextContent = (LinearLayout) findViewById(R.id.layout_show_topic_image_text_content);

        mTvHideTopicImageTextContent = (TextView) findViewById(R.id.tv_hide_topic_image_text_content);
        mTvDownloadTopicImageTextContent = (TextView) findViewById(R.id.tv_download_topic_image_text_content);


        mLayoutTopicImageTextContent = (LinearLayout) findViewById(R.id.layout_topic_image_text_content);
        mLayoutTopicImageTextContentParent = (LinearLayout) findViewById(R.id.layout_topic_image_text_content_parent);

        mLayoutTopicComments = (LinearLayout) findViewById(R.id.layout_topic_comments);

        mFabBackTop = (FloatingActionButton) findViewById(R.id.fab_back_top);

        // 底部下载、收藏、评论、交作业
        mLayoutBtnVideoDownload = (RelativeLayout) findViewById(R.id.layout_btn_video_download);
        mLayoutBtnVideoCollection = (RelativeLayout) findViewById(R.id.layout_btn_video_collection);
        mLayoutBtnVideoComment = (RelativeLayout) findViewById(R.id.layout_btn_video_comment);

        mTvVideoSubmittedResult = (TextView) findViewById(R.id.tv_video_submitted_result);
    }

    private void bindControlEvent() {
        // 界面顶部返回按钮
        mBtnVideoPlayerBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoPlayerActivity.this, MainActivity.class);
                startActivity(intent);
                VideoPlayerActivity.this.finish();
//                VideoPlayerActivity.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        // 界面顶部分享按钮
        mBtnVideoShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(VideoPlayerActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
            }
        });

        // 添加滚动监听
        mSvVideoPlayer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE://移动
                        Log.i(TAG, "ScrollView的滚动事件" + (event.getAction() == MotionEvent.ACTION_DOWN));
                        break;
                    case MotionEvent.ACTION_UP://向上
                        View childView = mSvVideoPlayer.getChildAt(0);
                        Log.i(TAG, "=======================================childView.getMeasuredHeight():" + childView.getMeasuredHeight());
                        Log.i(TAG, "=======================================mSvVideoPlayer.getScrollY():" + mSvVideoPlayer.getScrollY());
                        Log.i(TAG, "=======================================mSvVideoPlayer.getHeight():" + mSvVideoPlayer.getHeight());
                        Log.i(TAG, "=======================================minus:" + (childView.getMeasuredHeight() <= mSvVideoPlayer.getScrollY() + mSvVideoPlayer.getHeight()));
                        if (childView != null && childView.getMeasuredHeight() <= mSvVideoPlayer.getScrollY() + mSvVideoPlayer.getHeight()) {
                            mFabBackTop.setVisibility(View.VISIBLE);
                            Log.i(TAG, "=======================================底部1");
                        } else {
                            mFabBackTop.setVisibility(View.GONE);
                        }
                        break;
                    case MotionEvent.ACTION_DOWN://向下
                        mFabBackTop.setVisibility(View.GONE);
                        break;
                }
                return false;
            }
        });

//        mFabVideoPlayer.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                mVDVideoView.play(0);
//                mFabVideoPlayer.setVisibility(View.GONE);
//            }
//        });

        // 展开收起图文按钮
        mLayoutShowTopicImageTextContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOrHideImageTextDetail();
            }
        });

        // 收起图文按钮
        mTvHideTopicImageTextContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOrHideImageTextDetail();
            }
        });

        mFabBackTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFabBackTop.setVisibility(View.GONE);
                Log.i(TAG, "==========================================Back top");
                mSvVideoPlayer.fullScroll(ScrollView.FOCUS_UP);
            }
        });

        // 下载图文按钮
        mTvDownloadTopicImageTextContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 下载视频图片
                Toast.makeText(VideoPlayerActivity.this, "正在保存", Toast.LENGTH_SHORT).show();
            }
        });

        // 下载视频按钮
        mLayoutBtnVideoDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(VideoPlayerActivity.this, "正在下载视频", Toast.LENGTH_SHORT).show();
            }
        });
        mLayoutBtnVideoDownload.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ImageView imageView = (ImageView) mLayoutBtnVideoDownload.getChildAt(0);
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    imageView.setImageResource(R.drawable.collection);

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    imageView.setImageResource(R.drawable.message);
                }
                return false;
            }
        });

        // 收藏按钮
        mLayoutBtnVideoCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(VideoPlayerActivity.this, "收藏视频成功", Toast.LENGTH_SHORT).show();
            }
        });
        mLayoutBtnVideoCollection.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ImageView imageView = (ImageView) mLayoutBtnVideoCollection.getChildAt(0);
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    imageView.setImageResource(R.drawable.message);

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    imageView.setImageResource(R.drawable.collection);
                }
                return false;
            }
        });

        // 评论按钮
        mLayoutBtnVideoComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(VideoPlayerActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
            }
        });
        mLayoutBtnVideoComment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ImageView imageView = (ImageView) mLayoutBtnVideoComment.getChildAt(0);
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    imageView.setImageResource(R.drawable.collection);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    imageView.setImageResource(R.drawable.message);
                }
                return false;
            }
        });

        // 交作业按钮
        mTvVideoSubmittedResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(VideoPlayerActivity.this, "交作业成功", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * 显示或隐藏图文分解详情
     */
    public void showOrHideImageTextDetail() {
        int visibility = mLayoutTopicImageTextContentParent.getVisibility();

        // 显示图文分解
        if (View.GONE == visibility) {
            // 如果没有图文信息则返回
            if (mLayoutTopicImageTextContent.getChildCount() == 0) {
                Toast.makeText(this, "暂无图文信息", Toast.LENGTH_SHORT).show();
                return;
            }

            mLayoutTopicImageTextContentParent.setVisibility(View.VISIBLE);
            // 展开图文分解动画
            TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF
                    , 0.0f, Animation.RELATIVE_TO_SELF, 0.0f
                    , Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
            mShowAction.setDuration(100);
            mLayoutTopicImageTextContent.startAnimation(mShowAction);

            // 展开图文分解图片动画，顺时针方向旋转180度
            RotateAnimation rotateAnimation = new RotateAnimation(0, 180
                    , Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setDuration(400);
            rotateAnimation.setFillAfter(true);
            mIvVideoImageTextIcon.startAnimation(rotateAnimation);
        } else {// 隐藏图文分解
            mLayoutTopicImageTextContentParent.setVisibility(View.GONE);
            // 隐藏图文分解动画
            TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                    0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f);

            mHiddenAction.setDuration(800);
            mLayoutTopicImageTextContent.startAnimation(mHiddenAction);

            // 展开图文分解图片动画，顺时针方向旋转180度
            RotateAnimation rotateAnimation = new RotateAnimation(180, 360
                    , Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setDuration(400);
            rotateAnimation.setFillAfter(true);
            mIvVideoImageTextIcon.startAnimation(rotateAnimation);
        }
    }


    @Override
    public void playVideo(TopicInfo topicInfo) {
        //TODO 提示获取视频信息失败
        if (null == topicInfo) {
            return;
        }
        showTopicInfo2Page(topicInfo);
        VDVideoListInfo mVDVideoListInfo = new VDVideoListInfo();
        VDVideoInfo info = new VDVideoInfo();
        VideoInfo mVideoEntity = topicInfo.getVideos().get(0);
        info.mTitle = topicInfo.getTitle();
        info.mPlayUrl = mVideoEntity.getOssUrl();
        mVDVideoListInfo.addVideoInfo(info);
        mVDVideoView.open(VideoPlayerActivity.this, mVDVideoListInfo);
        mVDVideoView.play(0);
    }

    // 将视频信息显示在界面上
    private void showTopicInfo2Page(TopicInfo topicInfo) {
        // 视频名称
        mTvVideoName.setText(topicInfo.getTitle());
        // 作者照片
        mIvVideoAuthorPhoto.setImageSrc(topicInfo.getAuthorPhoto());
        // 作者名称
        mTvVideoAuthorName.setText(topicInfo.getAuthor());
        // 视频播放次数
        mTvVideoPlayingTimes.setText(String.valueOf(topicInfo.getVideos().get(0).getPlayTimes()));

        ArrayList tags = topicInfo.getTags();
        if (null != tags && !tags.isEmpty()) {
            if (tags.size() > 1 && null != tags.get(0)) {
                mTvVideoAuthorTag1.setText(tags.get(0).toString());
                mTvVideoAuthorTag2.setVisibility(View.VISIBLE);
            }
            if (tags.size() > 2 && null != tags.get(1)) {
                mTvVideoAuthorTag2.setText(tags.get(1).toString());
                mTvVideoAuthorTag2.setVisibility(View.VISIBLE);

            }
            if (tags.size() > 3 && null != tags.get(2)) {
                mTvVideoAuthorTag3.setText(tags.get(2).toString());
                mTvVideoAuthorTag3.setVisibility(View.VISIBLE);
            }
        }
    }


    @Override
    public void initTopicImageTextInfo(TopicImageTextInfo topicImageTextInfo) {
        //TODO 提示获取视频图文信息失败
        if (null == topicImageTextInfo) {
            return;
        }
        ArrayList pictures = topicImageTextInfo.getPictures();
        ArrayList articles = topicImageTextInfo.getArticles();

//        LinearLayout.LayoutParams lpMarginTopBottom = new LinearLayout.LayoutParams
//                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        lpMarginTopBottom.setMargins(0, dp_10, 0, dp_10);

        LinearLayout.LayoutParams lpMarginTop = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lpMarginTop.setMargins(0, mImageTextMargin, 0, 0);


        for (int i = 0; i < pictures.size(); i++) {
            ImageCacheView imageView = new ImageCacheView(VideoPlayerActivity.this);
            // 如果没有配图没有文字，并且不是第一张图片时，不需要设置Margin Top
//            imageView.setLayoutParams(((null == articles.get(i) || articles.get(i).toString().length() == 0))
//                    ? lpMarginTop : lpMarginTopBottom);
            imageView.setLayoutParams(lpMarginTop);
            imageView.setImageSrc(pictures.get(i).toString());
            imageView.setAdjustViewBounds(true);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO 显示大图
                }
            });
            mLayoutTopicImageTextContent.addView(imageView);

            if (articles.get(i).toString().length() > 0) {
                TextView textView = new TextView(VideoPlayerActivity.this);
                textView.setLayoutParams(lpMarginTop);
                textView.setText(articles.get(i).toString());
                mLayoutTopicImageTextContent.addView(textView);
            }
        }
    }

    @Override
    public void initTopicRelatedInfo(ArrayList<TopicRelatedInfo> topicRelatedList) {
        if (CollectionUtil.isEmpty(topicRelatedList)) {
            return;
        }
        LinearLayout.LayoutParams layoutLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        layoutLp.setMargins(0, 0, mTopicRelatedMarginRight, 0);

        FrameLayout.LayoutParams topicRelateIvLp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        FrameLayout.LayoutParams playTopicRelateIvLp = new FrameLayout.LayoutParams(mTopicRelatedPlayImageSize, mTopicRelatedPlayImageSize);
        playTopicRelateIvLp.gravity = Gravity.RIGHT | Gravity.BOTTOM;
        playTopicRelateIvLp.setMargins(0, 0, mAuthorPhotoMargin, mAuthorPhotoMargin);


        for (int i = 0; i < 4; i++) {
            FrameLayout topicRelateLayout = new FrameLayout(this);
            topicRelateLayout.setLayoutParams(layoutLp);

            ImageCacheView topicRelateIv = new ImageCacheView(VideoPlayerActivity.this);
            topicRelateIv.setLayoutParams(topicRelateIvLp);
            topicRelateIv.setAdjustViewBounds(true);
            if (topicRelatedList.size() > i && null != topicRelatedList.get(i)) {
                topicRelateIv.setImageSrc(topicRelatedList.get(i).getThumbUrl());
                topicRelateIv.setTag(R.id.topic_related_info, topicRelatedList.get(i));
                topicRelateIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showTopicRelated((TopicRelatedInfo) v.getTag(R.id.topic_related_info));
                    }
                });
                topicRelateLayout.addView(topicRelateIv);

                ImageView playTopicRelateIv = new ImageView(this);
                playTopicRelateIv.setImageResource(R.drawable.play);
                playTopicRelateIv.setLayoutParams(playTopicRelateIvLp);
                topicRelateLayout.addView(playTopicRelateIv);
            } else {
                topicRelateIv.setBackgroundResource(R.color.white);
                topicRelateLayout.addView(topicRelateIv);
            }


            mLayoutMoreVideosContent.addView(topicRelateLayout);
        }
    }

    private void showTopicRelated(TopicRelatedInfo topicRelatedInfo) {
        Intent intent = new Intent(this, VideoPlayerActivity.class);
        intent.putExtra(Constants.TOPIC_ID, topicRelatedInfo.getTopicId());
        this.startActivity(intent);
        this.finish();
    }


    @Override
    public void initTopicCommentsInfo(ArrayList<TopicCommentInfo> topicComments) {
        if (CollectionUtil.isEmpty(topicComments)) {
            mIsTopicsCommentLastPage = true;
            mTopicPullToRefreshView.setFooterLastUpdate(null);
            return;
        }
        mTopicPullToRefreshView.onFooterRefreshComplete();
        for (int i = 0; i < topicComments.size(); i++) {
            TopicCommentInfo topicCommentInfo = topicComments.get(i);

            LinearLayout topicCommentLayout = new LinearLayout(this);
            LinearLayout.LayoutParams layoutLp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            topicCommentLayout.setOrientation(LinearLayout.HORIZONTAL);
            topicCommentLayout.setLayoutParams(layoutLp);
            topicCommentLayout.setTag(R.id.topic_comment_info, topicCommentInfo);
            topicCommentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TopicCommentInfo commentInfo = (TopicCommentInfo) v.getTag(R.id.topic_comment_info);
                    Toast.makeText(VideoPlayerActivity.this, "回复" + commentInfo.getAuthor() + ":", Toast.LENGTH_SHORT).show();
                    mVideoPlayerPresenter.addTopicComment(commentInfo);
                }
            });


            // -----------------设置评论人头像-----------------
            if (!StringUtil.isEmpty(topicCommentInfo.getAuthorPhoto())) {
                CircleImageView topicCommentAuthorIV = new CircleImageView(this);
                topicCommentAuthorIV.setImageSrc(topicCommentInfo.getAuthorPhoto());
                LinearLayout.LayoutParams imageViewLp = new LinearLayout.LayoutParams(mAuthorPhotoSize, mAuthorPhotoSize);
                imageViewLp.setMargins(mAuthorPhotoMargin, mAuthorPhotoMargin, mAuthorPhotoMargin, 0);
                topicCommentAuthorIV.setLayoutParams(imageViewLp);
                topicCommentLayout.addView(topicCommentAuthorIV);
            } else {
                CircleImageView topicCommentAuthorIV = new CircleImageView(this);
                topicCommentAuthorIV.setImageResource(R.drawable.man);
                LinearLayout.LayoutParams imageViewLp = new LinearLayout.LayoutParams(mAuthorPhotoSize, mAuthorPhotoSize);
                imageViewLp.setMargins(mAuthorPhotoMargin, mAuthorPhotoMargin, mAuthorPhotoMargin, 0);
                topicCommentAuthorIV.setLayoutParams(imageViewLp);
                topicCommentLayout.addView(topicCommentAuthorIV);
            }


            // -----------------主题评论内容父布局，用于显示每条评论底部的线-----------------
            LinearLayout topicCommentContentLayoutParent = new LinearLayout(this);
            LinearLayout.LayoutParams topicCommentContentLayoutParentLp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            topicCommentContentLayoutParent.setLayoutParams(topicCommentContentLayoutParentLp);
            topicCommentContentLayoutParent.setBackgroundResource(R.drawable.bottom_border);


            // -----------------设置评论内容布局，用于控制评论内容的上、右、下边距-----------------
            LinearLayout topicCommentContentLayout = new LinearLayout(this);
            LinearLayout.LayoutParams layoutTopicCommentsLp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutTopicCommentsLp.setMargins(0, mCommentMarginTop, mCommentMarginRight, mCommentMarginBottom);
            topicCommentContentLayout.setOrientation(LinearLayout.VERTICAL);
            topicCommentContentLayout.setLayoutParams(layoutTopicCommentsLp);


            // -----------------评论人、评论时间布局-----------------
            RelativeLayout relativeLayout = new RelativeLayout(this);
            RelativeLayout.LayoutParams relativeLayoutLp = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            relativeLayout.setLayoutParams(relativeLayoutLp);

            // 评论人名称
            TextView topicCommentAuthorTv = new TextView(this);
            RelativeLayout.LayoutParams tvTopicCommentAuthorLp = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            tvTopicCommentAuthorLp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            topicCommentAuthorTv.setLayoutParams(tvTopicCommentAuthorLp);
            topicCommentAuthorTv.setText(topicCommentInfo.getAuthor());
            topicCommentAuthorTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mCommentAuthorFontSize);
            relativeLayout.addView(topicCommentAuthorTv);

            // 设置评论时间
            TextView topicCommentCreateDateTv = new TextView(this);
            RelativeLayout.LayoutParams tvTopicCommentCreateDateLp = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            tvTopicCommentCreateDateLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            topicCommentCreateDateTv.setLayoutParams(tvTopicCommentCreateDateLp);
            StringBuffer contentText = new StringBuffer();
            if (topicCommentInfo.getIsHomework() == Constants.IS_HOME_WORK_VALUE) {
                contentText.append("<font color=\"#555657\">#交作业#    </font>");
            }
            contentText.append("<font color=\"#adafb0\">" + getTopicCommentDateStr(topicCommentInfo.getCreateDate()) + "</font>");
            topicCommentCreateDateTv.setText(Html.fromHtml(contentText.toString()));
            topicCommentAuthorTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mCommentCreateDateFontSize);
            relativeLayout.addView(topicCommentCreateDateTv);

            topicCommentContentLayout.addView(relativeLayout);


            // -----------------设置评论内容-----------------
            TextView topicCommentContentTv = new TextView(this);
            LinearLayout.LayoutParams tvTopicCommentContentLp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            tvTopicCommentContentLp.setMargins(0, mCommentContentMarginTop, 0, mCommentContentMarginBottom);
            topicCommentContentTv.setLayoutParams(tvTopicCommentContentLp);
            topicCommentContentTv.setText(topicCommentInfo.getContent());
            topicCommentContentTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mCommentContentFontSize);
            topicCommentContentTv.setTextColor(getResources().getColor(R.color.black));

            topicCommentContentLayout.addView(topicCommentContentTv);


            // -----------------显示作业图片-----------------
            if (topicCommentInfo.getIsHomework() == 1 && !StringUtil.isEmpty(topicCommentInfo.getContentPic())) {
                ImageCacheView topicCommentHomeWorkIv = new ImageCacheView(this);
                topicCommentHomeWorkIv.setImageSrc(topicCommentInfo.getContentPic());
                LinearLayout.LayoutParams icvTopicCommentHomeWorkLp = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                icvTopicCommentHomeWorkLp.setMargins(0, 0, 0, mCommentContentPicMarginBottom);
                topicCommentHomeWorkIv.setLayoutParams(icvTopicCommentHomeWorkLp);
                topicCommentHomeWorkIv.setAdjustViewBounds(true);
                topicCommentContentLayout.addView(topicCommentHomeWorkIv);
                topicCommentHomeWorkIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO 显示大图
                        Toast.makeText(VideoPlayerActivity.this, "显示大图", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            // -----------------设置评论回复内容-----------------
            for (TopicCommentReplyInfo topicCommentReplyInfo : topicCommentInfo.getReplies()) {
                LinearLayout.LayoutParams topicCommentContentReplyLp = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                // 设置主题评论回复之间的上下间隔
                topicCommentContentReplyLp.setMargins(0, 0, 0, mCommentReplyMarginBottom);

                TextView topicCommentReplyTv = new TextView(this);
                topicCommentReplyTv.setLayoutParams(topicCommentContentReplyLp);
                StringBuffer replyText = new StringBuffer();
                replyText.append("<font color=\"#693012\">" + topicCommentReplyInfo.getAuthor() + "</font>");
                replyText.append("<font color=\"#555657\"> 回复 " + topicCommentReplyInfo.getAt().getNickName() + ": </font>");
                replyText.append("<font color=\"#000000\">" + topicCommentReplyInfo.getContent() + "</font>");
                topicCommentReplyTv.setText(Html.fromHtml(replyText.toString()));
                // 设置行高
                topicCommentReplyTv.setLineSpacing(mCommentReplyLineHeight, 1);
                topicCommentReplyTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mCommentReplyFontSize);

                // 设置padding
                topicCommentReplyTv.setPadding(mCommentReplyPaddingLeft, mCommentReplyPaddingTop, mCommentReplyPaddingRight, mCommentReplyPaddingBottom);
                topicCommentReplyTv.setBackgroundColor(mCommentReplyBackgroundColor);
                topicCommentReplyTv.setTag(R.id.topic_comment_id, topicCommentInfo.getId());
                topicCommentReplyTv.setTag(R.id.topic_comment_reply_info, topicCommentReplyInfo);
                topicCommentReplyTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String topicCommentId = (String) v.getTag(R.id.topic_comment_id);
                        TopicCommentReplyInfo replyInfo = (TopicCommentReplyInfo) v.getTag(R.id.topic_comment_reply_info);
                        Toast.makeText(VideoPlayerActivity.this, "回复" + replyInfo.getAuthor() + ":", Toast.LENGTH_SHORT).show();
                        mVideoPlayerPresenter.addTopicCommentReply(topicCommentId, replyInfo);
                    }
                });

                topicCommentContentLayout.addView(topicCommentReplyTv);
            }

            topicCommentContentLayoutParent.addView(topicCommentContentLayout);
            topicCommentLayout.addView(topicCommentContentLayoutParent);

            // 将评论和回复内容添加的界面
            mLayoutTopicComments.addView(topicCommentLayout);
        }
    }


    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        mTopicPullToRefreshView.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (mIsTopicsCommentLastPage) {
                    mTopicPullToRefreshView.setFooterLastUpdate(getResources().getString(R.string.loading_finish));
                } else {
                    mVideoPlayerPresenter.initTopicComments(mTopicId, ++mPage);
                }
            }
        }, 1000);
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        mTopicPullToRefreshView.postDelayed(new Runnable() {

            @Override
            public void run() {
                mTopicPullToRefreshView.onHeaderRefreshComplete();
            }
        }, 1000);

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

    private String getTopicCommentDateStr(long time) {
        Date date = new Date(time);
        Date today = new Date();
        long result = today.getTime() - time;
        if (result / 1000 <= 60) {
            return "刚刚";
        } else if (result / 1000 > 60 && result / 1000 <= 3600) {
            return (int) Math.floor((result / 1000) / 60) + "分钟前";
        } else if (result / 1000 > 3600 && result / 1000 <= 86400) {
            return date.getHours() + "点" + date.getMinutes() + "分";
        }
//        else if (result / 1000 > 3600 && result / 1000 <= 86400) {
//            return Math.floor((result / 1000) / 3600) + "小时前";
//        }
//        else if (result / 1000 > 86400 && result / 1000 <= 86400 * 30) {
//            return Math.floor((result / 1000) / 86400) + "天前";
//        }
        else {
            return (date.getMonth() + 1) + "月" + date.getDate() + "日";
        }

    }

}