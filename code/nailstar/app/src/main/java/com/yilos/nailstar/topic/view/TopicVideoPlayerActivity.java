package com.yilos.nailstar.topic.view;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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
import com.yilos.nailstar.topic.entity.TopicCommentAtInfo;
import com.yilos.nailstar.topic.entity.TopicCommentInfo;
import com.yilos.nailstar.topic.entity.TopicCommentReplyInfo;
import com.yilos.nailstar.topic.entity.TopicImageTextInfo;
import com.yilos.nailstar.topic.entity.TopicInfo;
import com.yilos.nailstar.topic.entity.TopicRelatedInfo;
import com.yilos.nailstar.topic.entity.TopicVideoInfo;
import com.yilos.nailstar.topic.presenter.TopicVideoPlayerPresenter;
import com.yilos.nailstar.util.CollectionUtil;
import com.yilos.nailstar.util.Constants;
import com.yilos.nailstar.util.LoggerFactory;
import com.yilos.nailstar.util.StringUtil;
import com.yilos.nailstar.util.UserInfo;
import com.yilos.nailstar.util.UserUtil;
import com.yilos.widget.circleimageview.CircleImageView;
import com.yilos.widget.photoview.PhotoView;
import com.yilos.widget.pullrefresh.PullToRefreshView;
import com.yilos.widget.view.ImageCacheView;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;

public class TopicVideoPlayerActivity extends BaseActivity implements
        ITopicVideoPlayerView,
        VDVideoExtListeners.OnVDVideoPlaylistListener,
        PullToRefreshView.OnHeaderRefreshListener,
        PullToRefreshView.OnFooterRefreshListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(TopicVideoPlayerActivity.class);

    private static final String TAG = "VideoPlayerActivity";

    private static final int TOPIC_COMMENT_REQUEST_CODE = 1;


    // 顶部返回、topic名称、分享
    private ImageView mBtnVideoPlayerBack;
    private ImageView mBtnVideoShare;
    private TextView mTvVideoName;

    private PullToRefreshView mTopicPullToRefreshView;
    private ScrollView mSvVideoPlayer;
    // 视频播放控件
    private VDVideoView mVDVideoView;
    private ImageView mIvVideoPlayIcon;

    // 作者信息
//    private LinearLayout mLayoutTopicsImageTextHead;
    private CircleImageView mIvVideoAuthorPhoto;
    private TextView mTvVideoAuthorName;
    private TextView mTvVideoPlayingTimes;
    private ImageView mIvVideoImageTextIcon;
    private TextView mTvTopicAuthorTag1;
    private TextView mTvTopicAuthorTag2;
    private TextView mTvTopicAuthorTag3;

    private ImageView mIvTopicAuthorTag1Icon;
    private ImageView mIvTopicAuthorTag2Icon;
    private ImageView mIvTopicAuthorTag3Icon;


    // 更多视频
    private LinearLayout mLayoutMoreVideosContent;


    // 图文分解
    private LinearLayout mLayoutTopicImageTextContent;
    private TextView mTvHideTopicImageTextContent;
    private TextView mTvDownloadTopicImageTextContent;

    private LinearLayout mLayoutShowTopicImageTextContent;
    private LinearLayout mLayoutTopicImageTextContentParent;


    private View mZoomInImageTextLayout;
    private TextView mTvZoomInImageTextIndex;
    private TextView mTvZoomInImageSave;
    private ViewPager mZoomInImageTextViewPager;

    private Dialog mZoomInImageTextDialog;
    private int mZoomInTextViewMarginTop = 0;

    // topic评论
    private LinearLayout mLayoutTopicComments;

    private FloatingActionButton mFabBackTop;

    // 底部下载、收藏、评论、交作业
    private RelativeLayout mLayoutBtnVideoDownload;
    private RelativeLayout mLayoutBtnTopicCollection;
    private RelativeLayout mLayoutBtnTopicComment;

    private TextView mTvTopicSubmittedHomework;

    private String mTopicId;
    private int mPage = 1;
    // 是否最后一页评论
    private boolean mIsTopicsCommentLastPage = false;


    private TopicVideoPlayerPresenter mTopicVideoPlayerPresenter;

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
        setContentView(R.layout.activity_topic_video_player);
        mTopicVideoPlayerPresenter = TopicVideoPlayerPresenter.getInstance(this);
        init();
        UserUtil.saveUserInfo(this, new UserInfo("001", "text", "http://pic.yilos.com/162b73dc3f69af2dc8a79a1b9da7591e"));
    }


    private void init() {
        // 获取topic id
        mTopicId = getIntent().getStringExtra(Constants.TOPIC_ID);
        mPage = 1;
        // 初始化控件
        initControl();
        // 初始化控件事件
        initControlEvent();

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


        mTopicVideoPlayerPresenter.initTopicVideo(mTopicId);
        mTopicVideoPlayerPresenter.initTopicRelatedInfo(mTopicId);
        mTopicVideoPlayerPresenter.initTopicImageTextInfo(mTopicId);
        mTopicVideoPlayerPresenter.initTopicComments(mTopicId, mPage);
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
        mIvVideoPlayIcon = (ImageView) findViewById(R.id.iv_video_play_icon);

        // 根据视频宽高比例，重新计算视频播放器高度
        mVDVideoView.getLayoutParams().height = (int) (getResources().getDisplayMetrics().widthPixels / Constants.VIDEO_ASPECT_RATIO);

        // 作者信息
//        mLayoutTopicsImageTextHead = (LinearLayout) findViewById(R.id.layout_topic_image_text_head);
        mIvVideoAuthorPhoto = (CircleImageView) findViewById(R.id.iv_video_author_photo);
        mTvVideoAuthorName = (TextView) findViewById(R.id.tv_video_author_name);
        mTvVideoPlayingTimes = (TextView) findViewById(R.id.tv_video_playing_times);
        mIvVideoImageTextIcon = (ImageView) findViewById(R.id.iv_video_image_text_icon);
        mTvTopicAuthorTag1 = (TextView) findViewById(R.id.tv_topic_author_tag_1);
        mTvTopicAuthorTag2 = (TextView) findViewById(R.id.tv_topic_author_tag_2);
        mTvTopicAuthorTag3 = (TextView) findViewById(R.id.tv_topic_author_tag_3);
        mIvTopicAuthorTag1Icon = (ImageView) findViewById(R.id.iv_topic_author_tag_1_icon);
        mIvTopicAuthorTag2Icon = (ImageView) findViewById(R.id.iv_topic_author_tag_2_icon);
        mIvTopicAuthorTag3Icon = (ImageView) findViewById(R.id.iv_topic_author_tag_3_icon);


        // 更多视频
        mLayoutMoreVideosContent = (LinearLayout) findViewById(R.id.layout_more_videos_content);

        // 图文分解
        mLayoutShowTopicImageTextContent = (LinearLayout) findViewById(R.id.layout_show_topic_image_text_content);

        mTvHideTopicImageTextContent = (TextView) findViewById(R.id.tv_hide_topic_image_text_content);
        mTvDownloadTopicImageTextContent = (TextView) findViewById(R.id.tv_download_topic_image_text_content);


        mLayoutTopicImageTextContent = (LinearLayout) findViewById(R.id.layout_topic_image_text_content);
        mLayoutTopicImageTextContentParent = (LinearLayout) findViewById(R.id.layout_topic_image_text_content_parent);

        mZoomInImageTextLayout = getLayoutInflater().inflate(R.layout.zoomin_topic_image_text_layout, null);
        mTvZoomInImageTextIndex = (TextView) mZoomInImageTextLayout.findViewById(R.id.tv_zoomIn_topic_image_text_index);
        mTvZoomInImageSave = (TextView) mZoomInImageTextLayout.findViewById(R.id.tv_zoomIn_topic_image_text_save);
        mZoomInImageTextViewPager = (ViewPager) mZoomInImageTextLayout.findViewById(R.id.vp_zoomIn_topic_image_text);

        mZoomInImageTextDialog = new Dialog(this, R.style.dialog_fullscreen);
        mZoomInImageTextDialog.setContentView(mZoomInImageTextLayout);
        mZoomInImageTextDialog.setCancelable(true);
        mZoomInImageTextDialog.setCanceledOnTouchOutside(true);


        mLayoutTopicComments = (LinearLayout) findViewById(R.id.layout_topic_comments);

        mFabBackTop = (FloatingActionButton) findViewById(R.id.fab_back_top);

        // 底部下载、收藏、评论、交作业
        mLayoutBtnVideoDownload = (RelativeLayout) findViewById(R.id.layout_btn_video_download);
        mLayoutBtnTopicCollection = (RelativeLayout) findViewById(R.id.layout_btn_topic_collection);
        mLayoutBtnTopicComment = (RelativeLayout) findViewById(R.id.layout_btn_topic_comment);

        mTvTopicSubmittedHomework = (TextView) findViewById(R.id.tv_submitted_homework);
    }

    private void initControlEvent() {
        // 设置播放结束监听
//        VDVideoViewController.getInstance(this).getExtListener().setOnVDVideoCompletionListener(new VDVideoExtListeners.OnVDVideoCompletionListener() {
//            @Override
//            public void onVDVideoCompletion(VDVideoInfo info, int status) {
//                Log.i(TAG, "视频播放结束了1");
//            }
//        });
        mVDVideoView.setCompletionListener(new VDVideoExtListeners.OnVDVideoCompletionListener() {
            @Override
            public void onVDVideoCompletion(VDVideoInfo info, int status) {
                Log.i(TAG, "视频播放结束了2");
            }
        });
        mVDVideoView.setPlayerChangeListener(new VDVideoExtListeners.OnVDVideoPlayerChangeListener() {
            @Override
            public void OnVDVideoPlayerChangeSwitch(int index, long position) {
                //Log.i(TAG, "视频播放结束了");
            }
        });

        // 界面顶部返回按钮
        mBtnVideoPlayerBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TopicVideoPlayerActivity.this, MainActivity.class);
                startActivity(intent);
                TopicVideoPlayerActivity.this.finish();
//                VideoPlayerActivity.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        // 界面顶部分享按钮
        mBtnVideoShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TopicVideoPlayerActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
            }
        });

        // 添加滚动监听
        mSvVideoPlayer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE://移动
//                        Log.i(TAG, "ScrollView的滚动事件" + (event.getAction() == MotionEvent.ACTION_DOWN));
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

        mIvVideoPlayIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIvVideoPlayIcon.setVisibility(View.GONE);
                mVDVideoView.play(0);
            }
        });

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

        mTvZoomInImageTextIndex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mZoomInImageTextDialog.isShowing()) {
                    mZoomInImageTextDialog.dismiss();
                }
            }
        });

        mTvZoomInImageSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TopicVideoPlayerActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                mTopicVideoPlayerPresenter.download();
                //TODO 保存单张图片
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
                Toast.makeText(TopicVideoPlayerActivity.this, "正在保存", Toast.LENGTH_SHORT).show();
            }
        });

        // 下载视频按钮
        mLayoutBtnVideoDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TopicVideoPlayerActivity.this, "正在下载视频", Toast.LENGTH_SHORT).show();
                mTopicVideoPlayerPresenter.download();
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
        mLayoutBtnTopicCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TopicVideoPlayerActivity.this, "收藏视频成功", Toast.LENGTH_SHORT).show();
            }
        });
        mLayoutBtnTopicCollection.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ImageView imageView = (ImageView) mLayoutBtnTopicCollection.getChildAt(0);
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    imageView.setImageResource(R.drawable.message);

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    imageView.setImageResource(R.drawable.collection);
                }
                return false;
            }
        });

        // 评论按钮
        mLayoutBtnTopicComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTopicComment();
            }
        });
        mLayoutBtnTopicComment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ImageView imageView = (ImageView) mLayoutBtnTopicComment.getChildAt(0);
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    imageView.setImageResource(R.drawable.collection);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    imageView.setImageResource(R.drawable.message);
                }
                return false;
            }
        });

        // 交作业按钮
        mTvTopicSubmittedHomework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TopicVideoPlayerActivity.this, "交作业成功", Toast.LENGTH_SHORT).show();
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
            // scrollView滚动到顶部
            mSvVideoPlayer.fullScroll(ScrollView.FOCUS_UP);
        }
    }

    /**
     * 评论
     */
    private void addTopicComment() {
        Intent intent = new Intent(this, TopicCommentActivity.class);
        intent.putExtra(Constants.TOPIC_ID, mTopicId);
        intent.putExtra(Constants.TYPE, Constants.TOPIC_COMMENT_TYPE_COMMENT);
        startActivityForResult(intent, TOPIC_COMMENT_REQUEST_CODE);
    }

    /**
     * 评论回复
     *
     * @param commentInfo
     * @param replyInfo
     */
    private void addTopicCommentReply(TopicCommentInfo commentInfo, TopicCommentReplyInfo replyInfo, int type) {
        Intent intent = new Intent(this, TopicCommentActivity.class);
        intent.putExtra(Constants.TOPIC_ID, mTopicId);
        if (null != commentInfo) {
            intent.putExtra(Constants.TOPIC_COMMENT_ID, commentInfo.getId());
            intent.putExtra(Constants.TOPIC_COMMENT_USER_ID, commentInfo.getUserId());
            intent.putExtra(Constants.TOPIC_COMMENT_AUTHOR, commentInfo.getAuthor());
        }
        if (null != replyInfo && type == Constants.TOPIC_COMMENT_TYPE_REPLY_AGAIN) {
            intent.putExtra(Constants.TOPIC_COMMENT_REPLY_ID, replyInfo.getId());
            intent.putExtra(Constants.TOPIC_COMMENT_REPLY_USER_ID, replyInfo.getUserId());
            intent.putExtra(Constants.TOPIC_COMMENT_REPLY_AUTHOR, replyInfo.getAuthor());
        }
        intent.putExtra(Constants.TYPE, type);
        startActivityForResult(intent, TOPIC_COMMENT_REQUEST_CODE);
    }


    @Override
    public void initTopicVideo(TopicInfo topicInfo) {
        //TODO 提示获取视频信息失败
        if (null == topicInfo) {
            LOGGER.error(TAG + " 获取topic信息为null，topicId:" + mTopicId);
            return;
        }
        showTopicInfo2Page(topicInfo);
        VDVideoListInfo mVDVideoListInfo = new VDVideoListInfo();
        VDVideoInfo info = new VDVideoInfo();
        TopicVideoInfo mVideoEntity = topicInfo.getVideos().get(0);
        info.mTitle = topicInfo.getTitle();
        info.mPlayUrl = mVideoEntity.getOssUrl();
        mVDVideoListInfo.addVideoInfo(info);
        mVDVideoView.open(TopicVideoPlayerActivity.this, mVDVideoListInfo);
    }

    /**
     * 将视频信息显示在界面上
     *
     * @param topicInfo
     */
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
            if (tags.size() > 0 && null != tags.get(0)) {
                mTvTopicAuthorTag1.setText(tags.get(0).toString());
                mTvTopicAuthorTag1.setVisibility(View.VISIBLE);
                mIvTopicAuthorTag1Icon.setVisibility(View.VISIBLE);
            }
            if (tags.size() > 1 && null != tags.get(1)) {
                mTvTopicAuthorTag2.setText(tags.get(1).toString());
                mTvTopicAuthorTag2.setVisibility(View.VISIBLE);
                mIvTopicAuthorTag2Icon.setVisibility(View.VISIBLE);
            }
            if (tags.size() > 2 && null != tags.get(2)) {
                mTvTopicAuthorTag3.setText(tags.get(2).toString());
                mTvTopicAuthorTag3.setVisibility(View.VISIBLE);
                mIvTopicAuthorTag3Icon.setVisibility(View.VISIBLE);
            }
        }
    }


    @Override
    public void initTopicImageTextInfo(final TopicImageTextInfo topicImageTextInfo) {
        //TODO 提示获取视频图文信息失败
        if (null == topicImageTextInfo) {
            LOGGER.error(TAG + " 获取topic图文信息为null，topicId:" + mTopicId);
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
            ImageCacheView imageView = new ImageCacheView(TopicVideoPlayerActivity.this);
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
                    zoomInTopicImageText((ImageCacheView) v, topicImageTextInfo);
                }
            });
            mLayoutTopicImageTextContent.addView(imageView);

            if (articles.get(i).toString().length() > 0) {
                TextView textView = new TextView(TopicVideoPlayerActivity.this);
                textView.setLayoutParams(lpMarginTop);
                textView.setText(articles.get(i).toString());
                mLayoutTopicImageTextContent.addView(textView);
            }
        }
    }

    private void zoomInTopicImageText(final ImageCacheView imageView, final TopicImageTextInfo topicImageTextInfo) {
        ArrayList<String> articles = topicImageTextInfo.getArticles();
        ArrayList<String> pictures = topicImageTextInfo.getPictures();
        final FrameLayout[] views = new FrameLayout[pictures.size()];
        final FrameLayout.LayoutParams textLp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int selectedIndex = -1;
        LayoutInflater inflater = getLayoutInflater();
        String picUrl = null;
        FrameLayout view = null;
        PhotoView pvImage = null;
        TextView tvText = null;
        for (int i = 0, length = pictures.size(); i < length; i++) {
            picUrl = pictures.get(i);
            if (StringUtil.isEmpty(picUrl)) {
                return;
            }
            if (selectedIndex == -1 && picUrl.equals(imageView.getImageSrc())) {
                selectedIndex = i;
            }
            view = (FrameLayout) inflater.inflate(R.layout.zoomin_topic_image_text_item, null);
            pvImage = (PhotoView) view.findViewById(R.id.pv_topic_image);
            tvText = (TextView) view.findViewById(R.id.tv_topic_text);

            pvImage.setBackgroundColor(getResources().getColor(R.color.black));
            pvImage.setImageSrc(picUrl);
            pvImage.enable();
            pvImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mZoomInImageTextDialog.isShowing()) {
                        mZoomInImageTextDialog.dismiss();
                    }
                }
            });
            tvText.setText(articles.get(i));
            pvImage.getMeasuredHeight();

            views[i] = view;
        }
        mZoomInImageTextViewPager.setPageMargin((int) (getResources().getDisplayMetrics().density * 15));
        mZoomInImageTextViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return views.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                if (position > getCount()) {
                    return null;
                }
                FrameLayout view = views[position];
                container.addView(view);
                //PhotoView photoView = (PhotoView) view.getChildAt(0);
                TextView textView = (TextView) view.getChildAt(1);

                textView.setLayoutParams(textLp);
//                Log.i(TAG, "photoView.getY():" + photoView.getY() + ",textView.getY():" + textView.getY());
//                Log.i(TAG, "photoView.getMeasuredHeight():" + photoView.getMeasuredHeight() + ",textView.getMeasuredHeight():" + textView.getMeasuredHeight());
//                Info info = photoView.getInfo();

//                Log.i(TAG, "photoView.getY():" + photoView.getY() + ",textView.getY():" + textView.getY());

                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public void finishUpdate(ViewGroup container) {
                super.finishUpdate(container);
                StringBuilder text = new StringBuilder();
                text.append(mZoomInImageTextViewPager.getCurrentItem() + 1)
                        .append("/")
                        .append(getCount());
                mTvZoomInImageTextIndex.setText(String.valueOf(text));
                if (null != container && container.getChildCount() > 0) {
                    // 获取第一页显示的图片，获取图片的底部Y轴位置，用于控制TextView的位置
                    PhotoView photoView = ((PhotoView) ((FrameLayout) container.getChildAt(0)).getChildAt(0));
                    mZoomInTextViewMarginTop = mImageTextMargin + (int) photoView.getInfo().getmRect().bottom;
                    textLp.setMargins(mImageTextMargin, mZoomInTextViewMarginTop, mImageTextMargin, 0);

//                    FrameLayout.LayoutParams textLp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                    textLp.setMargins(mImageTextMargin, mImageTextMargin + (int) rectF.bottom, mImageTextMargin, 0);
//                    TextView textView = ((TextView) ((FrameLayout) container.getChildAt(1)).getChildAt(1));
//                    textView.setLayoutParams(textLp);
//                    Log.i(TAG, "Text:" + textView.getText());
//                    Log.i(TAG, "bottom:" + rectF.bottom);
//                    Log.i(TAG, "photoView.getMeasuredHeight():" + photoView.getMeasuredHeight() + ",textView.getMeasuredHeight():" + textView.getMeasuredHeight());
                }
            }
        });
        if (selectedIndex != -1) {
            mZoomInImageTextViewPager.getAdapter().notifyDataSetChanged();
            final int index = selectedIndex;
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    mZoomInImageTextViewPager.setCurrentItem(index);
                    mZoomInImageTextDialog.show();
                }
            });
        } else {
            mZoomInImageTextDialog.show();
        }

    }

    @Override
    public void initTopicRelatedInfo(ArrayList<TopicRelatedInfo> topicRelatedList) {
        if (CollectionUtil.isEmpty(topicRelatedList)) {
            LOGGER.error(TAG + " topic没有关联其他的的topics，topicId:" + mTopicId);
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

            ImageCacheView topicRelateIv = new ImageCacheView(TopicVideoPlayerActivity.this);
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

    /**
     * 跳转到topic关联的topic
     *
     * @param topicRelatedInfo
     */
    private void showTopicRelated(TopicRelatedInfo topicRelatedInfo) {
        Intent intent = new Intent(this, TopicVideoPlayerActivity.class);
        intent.putExtra(Constants.TOPIC_ID, topicRelatedInfo.getTopicId());
        this.startActivity(intent);
        this.finish();
    }


    @Override
    public void initTopicCommentsInfo(ArrayList<TopicCommentInfo> topicComments, int orderBy) {
        if (CollectionUtil.isEmpty(topicComments)) {
            // 设置评论已经全部加载完毕
            mIsTopicsCommentLastPage = true;
            mTopicPullToRefreshView.setFooterLastUpdate(null);
            return;
        }
        mTopicPullToRefreshView.onFooterRefreshComplete();
        for (int i = 0; i < topicComments.size(); i++) {
            final TopicCommentInfo topicCommentInfo = topicComments.get(i);

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
                    addTopicCommentReply(commentInfo, null, Constants.TOPIC_COMMENT_TYPE_REPLY);
                }
            });


            // -----------------设置评论人头像-----------------
            LinearLayout.LayoutParams imageViewLp = new LinearLayout.LayoutParams(mAuthorPhotoSize, mAuthorPhotoSize);
            imageViewLp.setMargins(mAuthorPhotoMargin, mAuthorPhotoMargin, mAuthorPhotoMargin, 0);
            CircleImageView topicCommentAuthorIV = new CircleImageView(this);
            topicCommentAuthorIV.setLayoutParams(imageViewLp);
            if (!StringUtil.isEmpty(topicCommentInfo.getAuthorPhoto())) {//使用用户自定义头像
                topicCommentAuthorIV.setImageSrc(topicCommentInfo.getAuthorPhoto());
            } else {// 使用默认头像
                topicCommentAuthorIV.setImageResource(R.drawable.man);
            }
            topicCommentLayout.addView(topicCommentAuthorIV);


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

            StringBuilder contentText = new StringBuilder();
            if (topicCommentInfo.getIsHomework() == Constants.IS_HOME_WORK_VALUE) {
                contentText.append("<font color=\"#555657\">#")
                        .append(getResources().getString(R.string.submitted_homework))
                        .append("#    </font>");
            }
            contentText.append("<font color=\"#adafb0\">")
                    .append(getTopicCommentDateStr(topicCommentInfo.getCreateDate()))
                    .append("</font>");


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
                        Toast.makeText(TopicVideoPlayerActivity.this, "显示大图", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            // -----------------设置评论回复内容-----------------
            if (!CollectionUtil.isEmpty(topicCommentInfo.getReplies())) {
                for (TopicCommentReplyInfo topicCommentReplyInfo : topicCommentInfo.getReplies()) {
                    TextView topicCommentReplyTv = buildCommentReplyTextView(topicCommentInfo, topicCommentReplyInfo);
                    topicCommentContentLayout.addView(topicCommentReplyTv);
                }
            }

            topicCommentContentLayoutParent.addView(topicCommentContentLayout);
            topicCommentLayout.addView(topicCommentContentLayoutParent);

            // 将评论和回复内容添加的界面
            if (orderBy == Constants.TOPIC_COMMENTS_INIT_ORDER_BY_DESC) {
                mLayoutTopicComments.addView(topicCommentLayout, 0);
            } else {
                mLayoutTopicComments.addView(topicCommentLayout);
            }

        }
    }

    @NonNull
    private TextView buildCommentReplyTextView(TopicCommentInfo topicCommentInfo, TopicCommentReplyInfo topicCommentReplyInfo) {
        LinearLayout.LayoutParams topicCommentContentReplyLp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置主题评论回复之间的上下间隔
        topicCommentContentReplyLp.setMargins(0, 0, 0, mCommentReplyMarginBottom);

        TextView topicCommentReplyTv = new TextView(this);
        topicCommentReplyTv.setLayoutParams(topicCommentContentReplyLp);
        StringBuilder replyText = new StringBuilder();
        replyText.append("<font color=\"#693012\">")
                .append(topicCommentReplyInfo.getAuthor())
                .append("</font>");

        replyText.append("<font color=\"#555657\"> ")
                .append(getResources().getString(R.string.reply) + " " + topicCommentReplyInfo.getAt().getNickName())
                .append(": </font>");

        replyText.append("<font color=\"#000000\">")
                .append(topicCommentReplyInfo.getContent())
                .append("</font>");
        topicCommentReplyTv.setText(Html.fromHtml(replyText.toString()));
        // 设置行高
        topicCommentReplyTv.setLineSpacing(mCommentReplyLineHeight, 1);
        topicCommentReplyTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mCommentReplyFontSize);

        // 设置padding
        topicCommentReplyTv.setPadding(mCommentReplyPaddingLeft, mCommentReplyPaddingTop, mCommentReplyPaddingRight, mCommentReplyPaddingBottom);
        topicCommentReplyTv.setBackgroundColor(mCommentReplyBackgroundColor);
        topicCommentReplyTv.setTag(R.id.topic_comment_info, topicCommentInfo);
        topicCommentReplyTv.setTag(R.id.topic_comment_reply_info, topicCommentReplyInfo);
        topicCommentReplyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TopicCommentInfo commentInfo = (TopicCommentInfo) v.getTag(R.id.topic_comment_info);
                TopicCommentReplyInfo replyInfo = (TopicCommentReplyInfo) v.getTag(R.id.topic_comment_reply_info);
                addTopicCommentReply(commentInfo, replyInfo, Constants.TOPIC_COMMENT_TYPE_REPLY_AGAIN);
            }
        });
        return topicCommentReplyTv;
    }


    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        mTopicPullToRefreshView.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (mIsTopicsCommentLastPage) {
                    mTopicPullToRefreshView.setFooterLastUpdate(getResources().getString(R.string.loading_finish));
                } else {
                    mTopicVideoPlayerPresenter.initTopicComments(mTopicId, ++mPage);
                }
            }
        }, 1000);
    }


    /**
     * scrollView header刷新，不需要处理
     *
     * @param view
     */
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
                .getInstance(TopicVideoPlayerActivity.this).mReciever);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == TOPIC_COMMENT_REQUEST_CODE && resultCode == RESULT_OK) {
            UserInfo userInfo = UserUtil.getUserInfo(this);
            Bundle data = intent.getExtras();
            String topicId = data.getString(Constants.TOPIC_ID, Constants.EMPTY_STRING);
            int commentType = data.getInt(Constants.TYPE, Constants.TOPIC_COMMENT_TYPE_COMMENT);
            String commentId = data.getString(Constants.TOPIC_COMMENT_ID, Constants.EMPTY_STRING);
            String newCommentId = data.getString(Constants.TOPIC_NEW_COMMENT_ID, Constants.EMPTY_STRING);
            String commentUserId = data.getString(Constants.TOPIC_COMMENT_USER_ID, Constants.EMPTY_STRING);
            String commentAuthor = data.getString(Constants.TOPIC_COMMENT_AUTHOR, Constants.EMPTY_STRING);
            String content = data.getString(Constants.CONTENT, Constants.EMPTY_STRING);
            if (commentType == Constants.TOPIC_COMMENT_TYPE_COMMENT) {
                ArrayList<TopicCommentInfo> topicCommentInfoList = new ArrayList<TopicCommentInfo>();
                TopicCommentInfo topicCommentInfo = new TopicCommentInfo();
                topicCommentInfo.setId(newCommentId);

                // 获取登录用户userId
                topicCommentInfo.setUserId(userInfo.getUserId());
                // 获取登录用户昵称
                topicCommentInfo.setAuthor(userInfo.getNickName());
                // 获取登录用户头像url
                topicCommentInfo.setAuthorPhoto(userInfo.getPhoto());
                topicCommentInfo.setContent(content);
                // 获取登录用户头像url
                topicCommentInfo.setContentPic(Constants.EMPTY_STRING);
                // 获取登录用户头像url
                topicCommentInfo.setCreateDate(System.currentTimeMillis());
                topicCommentInfo.setIsHomework(Constants.NOT_HOME_WORK_VALUE);
                topicCommentInfo.setIsMine(0);
                topicCommentInfo.setStatus(0);
                topicCommentInfo.setReplies(new ArrayList<TopicCommentReplyInfo>());

                topicCommentInfoList.add(topicCommentInfo);
                initTopicCommentsInfo(topicCommentInfoList, Constants.TOPIC_COMMENTS_INIT_ORDER_BY_DESC);
            } else if (commentType == Constants.TOPIC_COMMENT_TYPE_REPLY) {
                TopicCommentReplyInfo topicCommentReplyInfo = new TopicCommentReplyInfo();
                TopicCommentAtInfo topicCommentAtInfo = new TopicCommentAtInfo();
                topicCommentAtInfo.setUserId(commentUserId);
                topicCommentAtInfo.setNickName(commentAuthor);
                topicCommentReplyInfo.setId(newCommentId);
                topicCommentReplyInfo.setUserId(userInfo.getUserId());
                topicCommentReplyInfo.setNickname(userInfo.getNickName());
                topicCommentReplyInfo.setAuthor(userInfo.getNickName());
                topicCommentReplyInfo.setContent(content);
                topicCommentReplyInfo.setContentPic(Constants.EMPTY_STRING);
                topicCommentReplyInfo.setIsHomework(Constants.NOT_HOME_WORK_VALUE);
                topicCommentReplyInfo.setStatus(0);
                topicCommentReplyInfo.setAt(topicCommentAtInfo);
                // 将评论回复添加到正确的显示位置
                addTopicCommentReplyToPage(commentId, topicCommentReplyInfo);
            } else {
                String commentReplyId = data.getString(Constants.TOPIC_COMMENT_REPLY_ID, Constants.EMPTY_STRING);
                String commentReplyUserId = data.getString(Constants.TOPIC_COMMENT_REPLY_USER_ID, Constants.EMPTY_STRING);
                String commentReplyAuthor = data.getString(Constants.TOPIC_COMMENT_REPLY_AUTHOR, Constants.EMPTY_STRING);

                TopicCommentReplyInfo topicCommentReplyInfo = new TopicCommentReplyInfo();
                TopicCommentAtInfo topicCommentAtInfo = new TopicCommentAtInfo();
                topicCommentAtInfo.setUserId(commentReplyUserId);
                topicCommentAtInfo.setNickName(commentReplyAuthor);
                topicCommentReplyInfo.setId(newCommentId);
                topicCommentReplyInfo.setUserId(userInfo.getUserId());
                topicCommentReplyInfo.setNickname(userInfo.getNickName());
                topicCommentReplyInfo.setAuthor(userInfo.getNickName());
                topicCommentReplyInfo.setContent(content);
                topicCommentReplyInfo.setContentPic(Constants.EMPTY_STRING);
                topicCommentReplyInfo.setIsHomework(Constants.NOT_HOME_WORK_VALUE);
                topicCommentReplyInfo.setStatus(0);
                topicCommentReplyInfo.setAt(topicCommentAtInfo);
                // 将评论回复添加到正确的显示位置
                addTopicCommentReplyToPage(commentId, topicCommentReplyInfo);
            }
        }
    }

    private void addTopicCommentReplyToPage(String topicCommentId, TopicCommentReplyInfo topicCommentReplyInfo) {
        // 从评论中找到回复评论的位置
        LinearLayout view = null;
        for (int i = mLayoutTopicComments.getChildCount() - 1; i > -1; i--) {
            view = (LinearLayout) mLayoutTopicComments.getChildAt(i);
            TopicCommentInfo topicCommentInfo = (TopicCommentInfo) view.getTag(R.id.topic_comment_info);
            if (null != topicCommentInfo && topicCommentInfo.getId().equals(topicCommentId)) {
                break;
            }
        }
        if (null != view) {
            LinearLayout topicCommentContentLayout = ((LinearLayout) ((LinearLayout) view.getChildAt(1)).getChildAt(0));
            TextView topicCommentReplyTv = buildCommentReplyTextView((TopicCommentInfo) view.getTag(R.id.topic_comment_info), topicCommentReplyInfo);
            topicCommentContentLayout.addView(topicCommentReplyTv);
        }
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