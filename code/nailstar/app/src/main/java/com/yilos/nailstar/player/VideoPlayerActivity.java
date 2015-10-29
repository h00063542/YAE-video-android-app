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
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
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
        IVideoPlayerView, VDVideoExtListeners.OnVDVideoPlaylistListener
        , PullToRefreshView.OnHeaderRefreshListener
        , PullToRefreshView.OnFooterRefreshListener {
    private static final String TAG = "VideoPlayerActivity";
    private int dp_2;
    private int dp_5;
    private int dp_10;
    private int dp_20;
    private int dp_40;

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
    private LinearLayout mLayoutMoreVideos;
    private ImageCacheView mIvTopicRelate1;
    private ImageCacheView mIvTopicRelate2;
    private ImageCacheView mIvTopicRelate3;
    private ImageCacheView mIvTopicRelate4;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        init();
        mVideoPlayerPresenter.playerVideo(mTopicId);
        mVideoPlayerPresenter.initTopicRelatedInfo(mTopicId);
        mVideoPlayerPresenter.initTopicImageTextInfo(mTopicId);
        mVideoPlayerPresenter.initTopicComments(mTopicId, mPage);
    }


    private void init() {
        dp_2 = getResources().getDimensionPixelSize(R.dimen.dp_2);
        dp_5 = getResources().getDimensionPixelSize(R.dimen.dp_5);
        dp_10 = getResources().getDimensionPixelSize(R.dimen.dp_10);
        dp_20 = getResources().getDimensionPixelSize(R.dimen.dp_20);
        dp_40 = getResources().getDimensionPixelSize(R.dimen.dp_40);

        // 获取topic id
        mTopicId = getIntent().getStringExtra(Constants.TOPIC_ID);
        mPage = 1;

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
        mVDVideoView.setVDVideoViewContainer((ViewGroup) mVDVideoView
                .getParent());
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
        mLayoutMoreVideos = (LinearLayout) findViewById(R.id.layout_more_videos);

        // 图文分解
        mLayoutShowTopicImageTextContent = (LinearLayout) findViewById(R.id.layout_show_topic_image_text_content);

        mTvHideTopicImageTextContent = (TextView) findViewById(R.id.tv_hide_topic_image_text_content);
        mTvDownloadTopicImageTextContent = (TextView) findViewById(R.id.tv_download_topic_image_text_content);


        mLayoutTopicImageTextContent = (LinearLayout) findViewById(R.id.layout_topic_image_text_content);
        mLayoutTopicImageTextContentParent = (LinearLayout) findViewById(R.id.layout_topic_image_text_content_parent);

        mLayoutTopicComments = (LinearLayout) findViewById(R.id.layout_topic_comments);

//        mFabBackTop = (FloatingActionButton) findViewById(R.id.fab_video_player);

        // 底部下载、收藏、评论、交作业
        mLayoutBtnVideoDownload = (RelativeLayout) findViewById(R.id.layout_btn_video_download);
        mLayoutBtnVideoCollection = (RelativeLayout) findViewById(R.id.layout_btn_video_collection);
        mLayoutBtnVideoComment = (RelativeLayout) findViewById(R.id.layout_btn_video_comment);

        mTvVideoSubmittedResult = (TextView) findViewById(R.id.tv_video_submitted_result);


//        Drawable commentDrawable = getResources().getDrawable(R.drawable.message);
//        commentDrawable.setBounds(0, 0, dip2px(this, 30), dip2px(this, 30));//第一0是距左边距离，第二0是距上边距离，30dp分别是长宽
//        mRbVideoComment.setCompoundDrawables(commentDrawable, null, null, null);//只放左边


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
                // TODO 分享功能
                Toast.makeText(VideoPlayerActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
            }
        });

        // 添加滚动监听
        mSvVideoPlayer.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {

                    //可以监听到ScrollView的滚动事件
                    //Toast.makeText(VideoPlayerActivity.this, "ScrollView的滚动事件", Toast.LENGTH_SHORT).show();

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

//        mFabBackTop.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                mSvVideoPlayer.fullScroll(ScrollView.FOCUS_UP);
//            }
//        });

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

        // 收藏按钮
        mLayoutBtnVideoCollection.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(VideoPlayerActivity.this, "收藏视频成功", Toast.LENGTH_SHORT).show();
            }
        });

        // 评论按钮
        mLayoutBtnVideoComment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(VideoPlayerActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
            }
        });

        // 交作业按钮
        mTvVideoSubmittedResult.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(VideoPlayerActivity.this, "交作业成功", Toast.LENGTH_SHORT).show();
            }
        });

        mVideoPlayerPresenter = TopicPresenter.getInstance(this);
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

        LinearLayout.LayoutParams lpMarginTopBottom = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lpMarginTopBottom.setMargins(0, dp_10, 0, dp_10);


        LinearLayout.LayoutParams lpMarginTop = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lpMarginTop.setMargins(0, dp_10, 0, 0);


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
    public void initTopicRelatedInfo(ArrayList<TopicRelatedInfo> topicRelateds) {
        if (CollectionUtil.isEmpty(topicRelateds)) {
            return;
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        lp.setMargins(0, 0, dp_5, 0);

        mIvTopicRelate1 = new ImageCacheView(VideoPlayerActivity.this);
        mIvTopicRelate1.setLayoutParams(lp);
        if (null != topicRelateds.get(0)) {
            mIvTopicRelate1.setImageSrc(topicRelateds.get(0).getThumbUrl());
            mIvTopicRelate1.setTag(R.id.topic_related_info, topicRelateds.get(0));
        }
        mIvTopicRelate1.setAdjustViewBounds(true);
        mIvTopicRelate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTopicRelated((TopicRelatedInfo) v.getTag(R.id.topic_related_info));
            }
        });


        mIvTopicRelate2 = new ImageCacheView(VideoPlayerActivity.this);
        mIvTopicRelate2.setLayoutParams(lp);
        if (topicRelateds.size() > 1 && null != topicRelateds.get(1)) {
            mIvTopicRelate2.setImageSrc(topicRelateds.get(1).getThumbUrl());
            mIvTopicRelate2.setTag(R.id.topic_related_info, topicRelateds.get(1));
        }
        mIvTopicRelate2.setAdjustViewBounds(true);
        mIvTopicRelate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTopicRelated((TopicRelatedInfo) v.getTag(R.id.topic_related_info));
            }
        });


        mIvTopicRelate3 = new ImageCacheView(VideoPlayerActivity.this);
        mIvTopicRelate3.setLayoutParams(lp);
        if (topicRelateds.size() > 2 && null != topicRelateds.get(2)) {
            mIvTopicRelate3.setImageSrc(topicRelateds.get(2).getThumbUrl());
            mIvTopicRelate3.setTag(R.id.topic_related_info, topicRelateds.get(2));
        }
        mIvTopicRelate3.setAdjustViewBounds(true);
        mIvTopicRelate3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTopicRelated((TopicRelatedInfo) v.getTag(R.id.topic_related_info));
            }
        });


        mIvTopicRelate4 = new ImageCacheView(VideoPlayerActivity.this);
        mIvTopicRelate4.setLayoutParams(lp);
        if (topicRelateds.size() > 3 && null != topicRelateds.get(3)) {
            mIvTopicRelate4.setImageSrc(topicRelateds.get(3).getThumbUrl());
            mIvTopicRelate4.setTag(R.id.topic_related_info, topicRelateds.get(3));
        }
        mIvTopicRelate4.setAdjustViewBounds(true);
        mIvTopicRelate4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTopicRelated((TopicRelatedInfo) v.getTag(R.id.topic_related_info));
            }
        });


        mLayoutMoreVideos.addView(mIvTopicRelate1);
        mLayoutMoreVideos.addView(mIvTopicRelate2);
        mLayoutMoreVideos.addView(mIvTopicRelate3);
        mLayoutMoreVideos.addView(mIvTopicRelate4);
    }

    private void showTopicRelated(TopicRelatedInfo topicRelatedInfo) {

    }


    @Override
    public void initTopicCommentsInfo(ArrayList<TopicCommentInfo> topicComments) {
        //TODO 提示获取主题评论信息失败
        if (CollectionUtil.isEmpty(topicComments)) {
            mIsTopicsCommentLastPage = true;
            mTopicPullToRefreshView.setFooterLastUpdate(null);
            return;
        }
        mTopicPullToRefreshView.onFooterRefreshComplete();
        for (int i = 0; i < topicComments.size(); i++) {
            TopicCommentInfo topicCommentInfo = topicComments.get(i);

            LinearLayout linearLayout = new LinearLayout(this);
            LinearLayout.LayoutParams layoutLp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutLp.setMargins(0, dp_10, 0, 0);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setLayoutParams(layoutLp);
            linearLayout.setTag(R.id.topic_comment_id, topicCommentInfo.getId());
            linearLayout.setTag(R.id.topic_comment_info, topicCommentInfo);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TopicCommentInfo commentInfo = (TopicCommentInfo) v.getTag(R.id.topic_comment_info);
                    Toast.makeText(VideoPlayerActivity.this, "回复" + commentInfo.getAuthor() + ":", Toast.LENGTH_SHORT).show();
                    mVideoPlayerPresenter.addTopicComment(commentInfo);
                }
            });

            // 设置评论人头像
            if (!StringUtil.isEmpty(topicCommentInfo.getAuthorPhoto())) {
                CircleImageView imageView = new CircleImageView(this);
                imageView.setImageSrc(topicCommentInfo.getAuthorPhoto());
                LinearLayout.LayoutParams imageViewLp = new LinearLayout.LayoutParams(dp_40, dp_40);
                imageViewLp.setMargins(dp_10, 0, dp_10, 0);
                imageView.setLayoutParams(imageViewLp);
                linearLayout.addView(imageView);
            } else {
                CircleImageView imageView = new CircleImageView(this);
                imageView.setImageResource(R.drawable.man);
                LinearLayout.LayoutParams imageViewLp = new LinearLayout.LayoutParams(dp_40, dp_40);
                imageViewLp.setMargins(dp_10, 0, dp_10, 0);
                imageView.setLayoutParams(imageViewLp);
                linearLayout.addView(imageView);
            }


            // 设置评论内容布局
            LinearLayout layoutTopicComments = new LinearLayout(this);
            LinearLayout.LayoutParams layoutTopicCommentsLp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutTopicComments.setOrientation(LinearLayout.VERTICAL);
            layoutTopicComments.setLayoutParams(layoutTopicCommentsLp);
//            if (i != topicComments.size() - 1) {
            layoutTopicComments.setBackgroundResource(R.drawable.bottom_border);
//            }
            layoutTopicComments.setPadding(0, 0, 0, dp_10);

            // 评论人、评论时间布局
            RelativeLayout relativeLayout = new RelativeLayout(this);
            RelativeLayout.LayoutParams relativeLayoutLp = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            relativeLayout.setLayoutParams(relativeLayoutLp);
            relativeLayout.setPadding(0, 0, dp_10, 0);

            // 评论人名称
            TextView tvTopicCommentAuthor = new TextView(this);
            RelativeLayout.LayoutParams tvTopicCommentAuthorLp = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            tvTopicCommentAuthorLp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            tvTopicCommentAuthor.setLayoutParams(tvTopicCommentAuthorLp);
            tvTopicCommentAuthor.setText(topicCommentInfo.getAuthor());
            relativeLayout.addView(tvTopicCommentAuthor);

            // 设置评论时间
            TextView tvTopicCommentCreateDate = new TextView(this);
            RelativeLayout.LayoutParams tvTopicCommentCreateDateLp = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            tvTopicCommentCreateDateLp.setMargins(dp_10, 0, 0, 0);
            tvTopicCommentCreateDateLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            tvTopicCommentCreateDate.setLayoutParams(tvTopicCommentCreateDateLp);
            StringBuffer text = new StringBuffer();
            if (topicCommentInfo.getIsHomework() == 1) {
                text.append("<font color=\"#555657\">#交作业#    </font>");
            }
            text.append("<font color=\"#adafb0\">" + getTopicCommentDateStr(topicCommentInfo.getCreateDate()) + "</font>");
            tvTopicCommentCreateDate.setText(Html.fromHtml(text.toString()));
            relativeLayout.addView(tvTopicCommentCreateDate);

            layoutTopicComments.addView(relativeLayout);

            // 设置评论内容
            TextView tvTopicCommentContent = new TextView(this);
            LinearLayout.LayoutParams tvTopicCommentContentLp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            tvTopicCommentContentLp.setMargins(0, dp_10, 0, dp_10);
            tvTopicCommentContent.setLayoutParams(tvTopicCommentContentLp);
            tvTopicCommentContent.setText(topicCommentInfo.getContent());
            layoutTopicComments.addView(tvTopicCommentContent);

            // 显示作业图片
            if (topicCommentInfo.getIsHomework() == 1 && !StringUtil.isEmpty(topicCommentInfo.getContentPic())) {
                ImageCacheView icvTopicCommentHomeWork = new ImageCacheView(this);
                icvTopicCommentHomeWork.setImageSrc(topicCommentInfo.getContentPic());
                LinearLayout.LayoutParams icvTopicCommentHomeWorkLp = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                icvTopicCommentHomeWorkLp.setMargins(0, 0, dp_10, dp_10);
                icvTopicCommentHomeWork.setLayoutParams(icvTopicCommentHomeWorkLp);
                icvTopicCommentHomeWork.setAdjustViewBounds(true);
                layoutTopicComments.addView(icvTopicCommentHomeWork);
                icvTopicCommentHomeWork.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO 显示大图
                        Toast.makeText(VideoPlayerActivity.this, "显示大图", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            // 设置评论回复内容
            for (TopicCommentReplyInfo topicCommentReplyInfo : topicCommentInfo.getReplies()) {
                LinearLayout.LayoutParams tvTopicCommentContentReplyLp = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                TextView tvTopicCommentContentReply = new TextView(this);
                tvTopicCommentContentReplyLp.setMargins(0, 0, dp_10, dp_5);


                tvTopicCommentContentReply.setLayoutParams(tvTopicCommentContentReplyLp);
                tvTopicCommentContentReply.setText(Html.fromHtml("<font color=\"#693012\">"
                        + topicCommentReplyInfo.getAuthor()
                        + "</font> <font color=\"#555657\">回复 "
                        + topicCommentReplyInfo.getAt().getNickName() + ": </font>"
                        + topicCommentReplyInfo.getContent()));
                tvTopicCommentContentReply.setLineSpacing(dp_5, 1);
                tvTopicCommentContentReply.setPadding(dp_5, dp_5, dp_5, dp_2);
                tvTopicCommentContentReply.setBackgroundColor(
                        getResources().getColor(R.color.topic_comment_reply_background_color));
                tvTopicCommentContentReply.setTag(R.id.topic_comment_id, topicCommentInfo.getId());
                tvTopicCommentContentReply.setTag(R.id.topic_comment_reply_id, topicCommentReplyInfo.getId());
                tvTopicCommentContentReply.setTag(R.id.topic_comment_reply_info, topicCommentReplyInfo);
                tvTopicCommentContentReply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String topicCommentId = (String) v.getTag(R.id.topic_comment_id);
                        TopicCommentReplyInfo replyInfo = (TopicCommentReplyInfo) v.getTag(R.id.topic_comment_reply_info);
                        Toast.makeText(VideoPlayerActivity.this, "回复" + replyInfo.getAuthor() + ":", Toast.LENGTH_SHORT).show();
                        mVideoPlayerPresenter.addTopicCommentReply(topicCommentId, replyInfo);
                    }
                });

                layoutTopicComments.addView(tvTopicCommentContentReply);
            }

            linearLayout.addView(layoutTopicComments);

            // 将评论和回复内容添加的界面
            mLayoutTopicComments.addView(linearLayout);
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

    private String getTopicCommentDateStr(long time) {
        Date date = new Date(time);
        Date today = new Date();
        long result = today.getTime() - time;
        if (result / 1000 <= 60) {
            return "刚刚";
        } else if (result / 1000 > 60 && result / 1000 <= 3600) {
            return Math.floor((result / 1000) / 60) + "分钟前";
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