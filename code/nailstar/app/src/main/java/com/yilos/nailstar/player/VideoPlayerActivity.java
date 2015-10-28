package com.yilos.nailstar.player;

import android.content.Context;
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
import com.yilos.nailstar.player.entity.TopicCommentInfo;
import com.yilos.nailstar.player.entity.TopicCommentReplyInfo;
import com.yilos.nailstar.player.entity.TopicImageTextInfo;
import com.yilos.nailstar.player.entity.TopicInfo;
import com.yilos.nailstar.player.entity.VideoInfo;
import com.yilos.nailstar.player.presenter.TopicPresenter;
import com.yilos.nailstar.player.view.IVideoPlayerView;
import com.yilos.widget.view.ImageCacheView;

import java.util.ArrayList;
import java.util.Date;

public class VideoPlayerActivity extends BaseActivity implements
        IVideoPlayerView, VDVideoExtListeners.OnVDVideoPlaylistListener {
    private static final String TAG = "VideoPlayerActivity";
    private int dp_2;
    private int dp_5;
    private int dp_10;
    private int dp_20;
    private int dp_40;


    private ImageView mBtnVideoPlayerBack;
    private ImageView mBtnVideoShare;
    private TextView mTvVideoName;

    private ScrollView mSvVideoPlayer;
    // 视频播放控件
    private VDVideoView mVDVideoView;
    private FloatingActionButton mFabVideoPlayer;

    private ImageView mIvVideoAuthorPhoto;
    private TextView mTvVideoAuthorName;
    private TextView mTvVideoPlayingTimes;
    private ImageView mIvVideoImageTextIcon;
    private TextView mTvVideoAuthorTag1;
    private TextView mTvVideoAuthorTag2;
    private TextView mTvVideoAuthorTag3;
    // 图文分解
    private LinearLayout mLayoutVideoDetailContent;
    private TextView mTvHideVideoImageTextDetail;
    private TextView mTvDownloadVideoImageTextDetail;

    private LinearLayout mLayoutShowImageTextContent;
    private ViewGroup mLayoutVideoDetailContentParent;

    private LinearLayout mLayoutVideoComments;

    private FloatingActionButton mFabBackTop;


    private RelativeLayout mLayoutBtnVideoDownload;
    private RelativeLayout mLayoutBtnVideoCollection;
    private RelativeLayout mLayoutBtnVideoComment;

    private ImageView mIvVideoDownloadIcon;
    private ImageView mIvVideoCollectionIcon;
    //    private ImageView mIvVideoCommentIcon;
//
    private TextView mTvVideoDownloadText;
    //    private TextView mTvVideoCollectionText;
//    private TextView mTvVideoCommentText;
    private TextView mTvVideoSubmittedResult;

    private String mTopicId;


    private TopicPresenter mVideoPlayerPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        init();
        mVideoPlayerPresenter.playerVideo(mTopicId);
        mVideoPlayerPresenter.initTopicImageTextInfo(mTopicId);
        mVideoPlayerPresenter.initTopicComments(mTopicId, 1);
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    private void init() {
        dp_2 = getResources().getDimensionPixelSize(R.dimen.dp_2);
        dp_5 = getResources().getDimensionPixelSize(R.dimen.dp_5);
        dp_10 = getResources().getDimensionPixelSize(R.dimen.dp_10);
        dp_20 = getResources().getDimensionPixelSize(R.dimen.dp_20);
        dp_40 = getResources().getDimensionPixelSize(R.dimen.dp_40);

        mBtnVideoPlayerBack = (ImageView) findViewById(R.id.btn_video_player_back);
        mBtnVideoShare = (ImageView) findViewById(R.id.btn_video_share);
        mTvVideoName = (TextView) findViewById(R.id.tv_video_name);
        mSvVideoPlayer = (ScrollView) findViewById(R.id.sv_video_player);
        mVDVideoView = (VDVideoView) findViewById(R.id.video_player);
        // 手动这是播放窗口父类，横屏的时候，会用这个做为容器使用，如果不设置，那么默认直接跳转到DecorView
        mVDVideoView.setVDVideoViewContainer((ViewGroup) mVDVideoView
                .getParent());
        mFabVideoPlayer = (FloatingActionButton) findViewById(R.id.fab_video_player);
        mIvVideoAuthorPhoto = (ImageView) findViewById(R.id.iv_video_author_photo);
        mTvVideoAuthorName = (TextView) findViewById(R.id.tv_video_author_name);
        mTvVideoPlayingTimes = (TextView) findViewById(R.id.tv_video_playing_times);
        mIvVideoImageTextIcon = (ImageView) findViewById(R.id.iv_video_image_text_icon);
        mTvVideoAuthorTag1 = (TextView) findViewById(R.id.tv_video_author_tag_1);
        mTvVideoAuthorTag2 = (TextView) findViewById(R.id.tv_video_author_tag_2);
        mTvVideoAuthorTag3 = (TextView) findViewById(R.id.tv_video_author_tag_3);

        mLayoutShowImageTextContent = (LinearLayout) findViewById(R.id.layout_show_image_text_content);

        mTvHideVideoImageTextDetail = (TextView) findViewById(R.id.tv_hide_video_image_text_detail);
        mTvDownloadVideoImageTextDetail = (TextView) findViewById(R.id.tv_download_video_image_text_detail);
        // 获取视频Id，名称，url地址
        mTopicId = getIntent().getStringExtra("");

        mLayoutVideoDetailContent = (LinearLayout) findViewById(R.id.layout_video_detail_content);
        mLayoutVideoDetailContentParent = (ViewGroup) mLayoutVideoDetailContent.getParent();

        mLayoutVideoComments = (LinearLayout) findViewById(R.id.layout_video_comments);

        mFabBackTop = (FloatingActionButton) findViewById(R.id.fab_video_player);

        mLayoutBtnVideoDownload = (RelativeLayout) findViewById(R.id.layout_btn_video_download);
        mLayoutBtnVideoCollection = (RelativeLayout) findViewById(R.id.layout_btn_video_collection);
        mLayoutBtnVideoComment = (RelativeLayout) findViewById(R.id.layout_btn_video_comment);


        mIvVideoDownloadIcon = (ImageView) findViewById(R.id.iv_video_download_icon);
        mTvVideoDownloadText = (TextView) findViewById(R.id.tv_video_download_text);
        mIvVideoCollectionIcon = (ImageView) findViewById(R.id.iv_video_collection_icon);
//        mTvVideoCollectionText = (TextView) findViewById(R.id.tv_video_collection_text);
//        mIvVideoCommentIcon = (ImageView) findViewById(R.id.iv_video_comment_icon);
//        mTvVideoCommentText = (TextView) findViewById(R.id.tv_video_comment_text);
        mTvVideoSubmittedResult = (TextView) findViewById(R.id.tv_video_submitted_result);


//        //控制登录用户名图标大小
//        Drawable downloadDrawable = getResources().getDrawable(R.drawable.collection);
//        downloadDrawable.setBounds(0, 0, dip2px(this, 30), dip2px(this, 30));//第一0是距左边距离，第二0是距上边距离，30dp分别是长宽
//        mRbVideoDownload.setCompoundDrawables(downloadDrawable, null, null, null);//只放左边
//
//
//        Drawable collectionDrawable = getResources().getDrawable(R.drawable.collection);
//        collectionDrawable.setBounds(0, 0, dip2px(this, 30), dip2px(this, 30));//第一0是距左边距离，第二0是距上边距离，30dp分别是长宽
//        mRbVideoCollection.setCompoundDrawables(collectionDrawable, null, null, null);//只放左边
//
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
                VideoPlayerActivity.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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

        mFabVideoPlayer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mVDVideoView.play(0);
                mFabVideoPlayer.setVisibility(View.GONE);
            }
        });

        // 展开收起图文按钮
        mLayoutShowImageTextContent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showOrHideImageTextDetail();
            }
        });

        // 收起图文按钮
        mTvHideVideoImageTextDetail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showOrHideImageTextDetail();
            }
        });

        mFabBackTop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mSvVideoPlayer.fullScroll(ScrollView.FOCUS_UP);
            }
        });

        // 下载图文按钮
        mTvDownloadVideoImageTextDetail.setOnClickListener(new View.OnClickListener() {

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


//        // 下载视频按钮
//        mIvVideoDownloadIcon.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(VideoPlayerActivity.this, "正在下载视频", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        mIvVideoDownloadIcon.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(VideoPlayerActivity.this, "正在下载视频", Toast.LENGTH_SHORT).show();
//            }
//        });


//        // 收藏按钮
//        mIvVideoCollectionIcon.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(VideoPlayerActivity.this, "收藏视频成功", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        // 收藏按钮
//        mTvVideoCollectionText.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(VideoPlayerActivity.this, "收藏视频成功", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//        // 评论按钮
//        mIvVideoCommentIcon.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(VideoPlayerActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        // 评论按钮
//        mTvVideoCommentText.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(VideoPlayerActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
//            }
//        });

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
        int visibility = mLayoutVideoDetailContentParent.getVisibility();

        // 显示图文分解
        if (View.GONE == visibility) {
            mLayoutVideoDetailContentParent.setVisibility(View.VISIBLE);
            // 展开图文分解动画
            TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF
                    , 0.0f, Animation.RELATIVE_TO_SELF, 0.0f
                    , Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
            mShowAction.setDuration(100);
            mLayoutVideoDetailContent.startAnimation(mShowAction);

            // 展开图文分解图片动画，顺时针方向旋转180度
            RotateAnimation rotateAnimation = new RotateAnimation(0, 180
                    , Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setDuration(400);
            rotateAnimation.setFillAfter(true);
            mIvVideoImageTextIcon.startAnimation(rotateAnimation);

        } else {// 隐藏图文分解
            mLayoutVideoDetailContentParent.setVisibility(View.GONE);
            // 隐藏图文分解动画
            TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                    0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f);

            mHiddenAction.setDuration(800);
            mLayoutVideoDetailContent.startAnimation(mHiddenAction);

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

        showVideoInfo2Page(topicInfo);
        VDVideoListInfo mVDVideoListInfo = new VDVideoListInfo();
        VDVideoInfo info = new VDVideoInfo();
        VideoInfo mVideoEntity = topicInfo.getVideos().get(0);
        info.mTitle = topicInfo.getTitle();
        info.mPlayUrl = mVideoEntity.getOssUrl();
        mVDVideoListInfo.addVideoInfo(info);
        mVDVideoView.open(VideoPlayerActivity.this, mVDVideoListInfo);
    }

    @Override
    public void initVideoImageTextInfo(TopicImageTextInfo topicImageTextInfo) {
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
        lpMarginTop.setMargins(0, dp_20, 0, 0);


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

    @Override
    public void initTopicCommentsInfo(ArrayList<TopicCommentInfo> topicComments) {
        //TODO 提示获取主题失败
        if (null == topicComments) {
            return;
        }
        for (int i = 0; i < topicComments.size(); i++) {
            TopicCommentInfo commentInfo = topicComments.get(i);

            LinearLayout linearLayout = new LinearLayout(this);
            LinearLayout.LayoutParams layoutLp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutLp.setMargins(0, dp_10, 0, 0);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setLayoutParams(layoutLp);

            // 设置评论人头像
//            if (null != commentInfo.getAuthorPhoto() && commentInfo.getAuthorPhoto().length() > 0) {
//                ImageCacheView imageView = new ImageCacheView(this);
//                imageView.setImageSrc(commentInfo.getAuthorPhoto());
//                LinearLayout.LayoutParams imageViewLp = new LinearLayout.LayoutParams(40, 40);
//                imageViewLp.setMargins(10, 10, 10, 0);
//                imageView.setLayoutParams(imageViewLp);
//
//                linearLayout.addView(imageView);
//            } else {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(R.drawable.videos_1);
            LinearLayout.LayoutParams imageViewLp = new LinearLayout.LayoutParams(dp_40, dp_40);
            imageViewLp.setMargins(dp_10, 0, dp_10, 0);
            imageView.setLayoutParams(imageViewLp);

            linearLayout.addView(imageView);
//            }


            // 设置评论内容布局
            LinearLayout layoutTopicComments = new LinearLayout(this);
            LinearLayout.LayoutParams layoutTopicCommentsLp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutTopicComments.setOrientation(LinearLayout.VERTICAL);
            layoutTopicComments.setLayoutParams(layoutTopicCommentsLp);
            if (i != topicComments.size() - 1) {
                layoutTopicComments.setBackgroundResource(R.drawable.bottom_border);
            }
            layoutTopicComments.setPadding(0, 0, 0, dp_20);

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
            tvTopicCommentAuthor.setText(commentInfo.getAuthor());
            relativeLayout.addView(tvTopicCommentAuthor);


            // 设置是否显示#交作业#
//            RelativeLayout tempLayout = new RelativeLayout(this);
//            RelativeLayout.LayoutParams tempLayoutLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            tempLayoutLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//            tempLayout.setLayoutParams(tempLayoutLp);
            if (commentInfo.getIsHomework() == 1) {
                TextView tvTopicCommentHomeWork = new TextView(this);
                RelativeLayout.LayoutParams tvTopicCommentHomeWorkLp = new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                tvTopicCommentHomeWorkLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                tvTopicCommentHomeWork.setLayoutParams(tvTopicCommentHomeWorkLp);
                tvTopicCommentHomeWork.setText("#交作业#");
                relativeLayout.addView(tvTopicCommentHomeWork);
            }

            // 设置评论时间
            TextView tvTopicCommentCreateDate = new TextView(this);
            RelativeLayout.LayoutParams tvTopicCommentCreateDateLp = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            tvTopicCommentCreateDateLp.setMargins(dp_10, 0, 0, 0);
            tvTopicCommentCreateDateLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            tvTopicCommentCreateDate.setLayoutParams(tvTopicCommentCreateDateLp);
            Date createDate = new Date(commentInfo.getCreateDate());
            tvTopicCommentCreateDate.setText(createDate.getHours() + "点" + createDate.getMinutes() + "分");
            relativeLayout.addView(tvTopicCommentCreateDate);
//            relativeLayout.addView(tempLayout);


            layoutTopicComments.addView(relativeLayout);


            // 设置评论内容
            TextView tvTopicCommentContent = new TextView(this);
            LinearLayout.LayoutParams tvTopicCommentContentLp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            tvTopicCommentContent.setLayoutParams(tvTopicCommentContentLp);
            tvTopicCommentContent.setText(commentInfo.getContent());
            layoutTopicComments.addView(tvTopicCommentContent);

            // 设置评论回复内容
            for (TopicCommentReplyInfo topicCommentReplyInfo : commentInfo.getReplies()) {
                LinearLayout.LayoutParams layoutTopicCommentReplyLp = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutTopicCommentReplyLp.setMargins(0, 0, dp_10, dp_5);
                LinearLayout layoutTopicCommentContentReply = new LinearLayout(this);
                layoutTopicCommentContentReply.setTag(R.id.topic_comments_id, commentInfo.getId());
                layoutTopicCommentContentReply.setTag(R.id.topic_comments_reply_id, topicCommentReplyInfo.getId());
                layoutTopicCommentContentReply.setTag(R.id.topic_comments_reply_info, topicCommentReplyInfo);
                layoutTopicCommentContentReply.setLayoutParams(layoutTopicCommentReplyLp);
                layoutTopicCommentContentReply.setBackgroundColor(
                        getResources().getColor(R.color.topic_comment_reply_background_color));


                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                TextView tvTopicCommentContentReply = new TextView(this);
                lp.setMargins(0, 0, dp_10, 0);

                tvTopicCommentContentReply.setText(Html.fromHtml("<font color=\"#693012\">"
                        + topicCommentReplyInfo.getAuthor() + "</font> 回复 "
                        + topicCommentReplyInfo.getAt().getNickName() + ": " + topicCommentReplyInfo.getContent()));
                tvTopicCommentContentReply.setLineSpacing(dp_2, 1);
                tvTopicCommentContentReply.setPadding(dp_2, dp_2, dp_2, dp_2);

                layoutTopicCommentContentReply.addView(tvTopicCommentContentReply);

                layoutTopicCommentContentReply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                layoutTopicComments.addView(layoutTopicCommentContentReply);
            }

            linearLayout.addView(layoutTopicComments);

            // 将评论和回复内容添加的界面
            mLayoutVideoComments.addView(linearLayout);
        }
    }

    // 将视频信息显示在界面上

    private void showVideoInfo2Page(TopicInfo videoInfoEntity) {
        // 视频名称
        mTvVideoName.setText(videoInfoEntity.getTitle());
        // 作者照片
        mIvVideoAuthorPhoto.setImageBitmap(ImageLoader.getInstance().loadImageSync(videoInfoEntity.getAuthorPhoto()));
        // 作者名称
        mTvVideoAuthorName.setText(videoInfoEntity.getAuthor());
        // 视频播放次数
        mTvVideoPlayingTimes.setText(String.valueOf(videoInfoEntity.getVideos().get(0).getPlayTimes()));
        ArrayList tags = videoInfoEntity.getTags();
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