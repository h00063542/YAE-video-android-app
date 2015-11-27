package com.yilos.nailstar.topic.view;

/**
 * Created by yilos on 2015-11-21.
 */

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.trade.ItemService;
import com.alibaba.sdk.android.trade.TradeConstants;
import com.alibaba.sdk.android.trade.callback.TradeProcessCallback;
import com.alibaba.sdk.android.trade.item.ItemType;
import com.alibaba.sdk.android.trade.model.TradeResult;
import com.alibaba.sdk.android.webview.UiSettings;
import com.sina.sinavideo.sdk.VDVideoExtListeners;
import com.sina.sinavideo.sdk.VDVideoView;
import com.sina.sinavideo.sdk.data.VDVideoInfo;
import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.model.LoginAPI;
import com.yilos.nailstar.framework.entity.NailStarApplicationContext;
import com.yilos.nailstar.framework.view.BaseActivity;
import com.yilos.nailstar.framework.view.YLSWebViewActivity;
import com.yilos.nailstar.main.MainActivity;
import com.yilos.nailstar.social.model.SocialAPI;
import com.yilos.nailstar.takeImage.TakeImage;
import com.yilos.nailstar.takeImage.TakeImageCallback;
import com.yilos.nailstar.topic.entity.TopicCommentAtInfo;
import com.yilos.nailstar.topic.entity.TopicCommentInfo;
import com.yilos.nailstar.topic.entity.TopicCommentReplyInfo;
import com.yilos.nailstar.topic.entity.TopicImageTextInfo;
import com.yilos.nailstar.topic.entity.TopicInfo;
import com.yilos.nailstar.topic.entity.TopicRelatedInfo;
import com.yilos.nailstar.topic.entity.TopicRelatedProduct;
import com.yilos.nailstar.topic.entity.TopicStatusInfo;
import com.yilos.nailstar.topic.entity.TopicVideoInfo;
import com.yilos.nailstar.topic.presenter.TopicVideoPlayerPresenter;
import com.yilos.nailstar.util.CollectionUtil;
import com.yilos.nailstar.util.Constants;
import com.yilos.nailstar.util.StringUtil;
import com.yilos.widget.circleimageview.CircleImageView;
import com.yilos.widget.photoview.PhotoView;
import com.yilos.widget.pullrefresh.PullToRefreshView;
import com.yilos.widget.titlebar.TitleBar;
import com.yilos.widget.view.ImageCacheView;
import com.yilos.widget.view.RoundProgressBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class TopicVideoPlayerActivity extends BaseActivity implements
        ITopicVideoPlayerView,
        VDVideoExtListeners.OnVDVideoPlaylistListener,
        PullToRefreshView.OnFooterRefreshListener {

    private final String TAG = "TopicVideoPlayerActivity";

    private int widthPixels;
    private int heightPixels;
    private float density;

    private ViewGroup mDecorView;
    private TopicInfo mTopicInfo;

    // 顶部返回、topic名称、分享
    private TitleBar mTbVideoPlayerHead;
    private ImageView mIvVideoPlayerBack;
    private ImageView mIvVideoDownload;
    private ImageView mIvTopicShare;
    private TextView mTvTopicName;

    private PullToRefreshView mTopicPullToRefreshView;
    private ListView mLvTopicDetail;
    private TopicCommentAdapter mTopicCommentAdapter;

    // 视频播放控件
    private VDVideoView mVDVideoView;
    private LinearLayout mPlayIconParent;
    private ImageView mIvVideoPlayIcon;
    private RelativeLayout mLayoutVideoPlayNotWifi;
    private TextView mTvVideoPlayNotWifi;

    // 作者信息
    private CircleImageView mIvVideoAuthorPhoto;
    private TextView mTvVideoAuthorPlayTimes;
    private ImageView mIvVideoImageTextIcon;
    private TextView mTvTopicAuthorTag1;
    private TextView mTvTopicAuthorTag2;
    private TextView mTvTopicAuthorTag3;

    private LinearLayout mLayoutTopicAuthorTags;
    private ImageView mIvTopicAuthorTag1Icon;
    private ImageView mIvTopicAuthorTag2Icon;
    private ImageView mIvTopicAuthorTag3Icon;

    // 更多视频
    private LinearLayout mLayoutMoreVideosContent;


    // 使用产品
    private LinearLayout mLayoutVideoUsedProductContent;

    // 图文分解
    private LinearLayout mLayoutTopicImageTextContent;
    private TextView mTvHideTopicImageTextContent;
    private TextView mTvDownloadTopicImageTextContent;
    private Dialog mDownloadTopicImageTextDialog;
    private RoundProgressBar mRpbDownloadTopicImageText;
    private TextView mTvDownloadTopicImageText;
    private int mDownloadTopicImageTextIndex;
    private TopicImageTextInfo mTopicImageTextInfo;

    private LinearLayout mLayoutShowTopicImageTextContent;
    private LinearLayout mLayoutTopicImageTextContentParent;

    private View mZoomInImageTextLayout;
    private TextView mTvZoomInImageTextIndex;
    private TextView mTvZoomInImageSave;
    private ViewPager mZoomInImageTextViewPager;

    private int mZoomInTextViewMarginTop = 0;

    private TextView mTvTopicCommentCount;

    private LinearLayout mTopicEmptyCommentLayout;


    // 底部下载、收藏、评论、交作业
    private CheckBox mCbTopicTabLike;
    private CheckBox mCbTopicTabCollection;
    private TextView mTvTopicTabComment;

    private TextView mTvTopicSubmittedHomework;
    private TakeImage mTakeImage;

    private String mTopicId;
    private String mVideoRemoteUrl;
    private int mPage = 1;
    // 是否最后一页评论
    private boolean mIsTopicsCommentLastPage = false;

    private TopicVideoPlayerPresenter mTopicVideoPlayerPresenter;

    private int mImageTextMargin;

    // 主题关联
    private int mTopicRelatedMarginRight;
    private int mTopicRelatedPlayImageSize;
    private int mMoresVideoMargin;

    //关联商品的布局尺寸信息
    private int mTopicRelateProductLineWidth;
    private int mTopicRelateProductLineHeight;


    // topic信息是否初始化完成
    private boolean initTopicInfoFinish = false;
    // topic关联信息是否初始化完成
    private boolean initTopicRelatedInfoFinish = false;
    // topic关联商品信息是否初始化完成
    private boolean initTopicRelatedUsedProductsFinish = false;
    // topic图文信息是否初始化完成
    private boolean initTopicImageTextInfoFinish = false;
    // topic当前页评论信息是否初始化完成
    private boolean initTopicCommentsFinish = false;
    // topic状态信息（赞，收藏）是否初始化完成
    private boolean initUserTopicStatusFinish = false;
    // 是否自动设置的topic赞状态信息
    private boolean autoSetTopicLikeStatus = false;
    // 是否自动设置的topic收藏状态信息
    private boolean autoSetTopicCollectionStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_detail);
        init();
    }

    private void init() {
        // 获取topic id
        mTopicId = getIntent().getStringExtra(Constants.TOPIC_ID);
        mPage = 1;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        widthPixels = displayMetrics.widthPixels;
        heightPixels = displayMetrics.heightPixels;
        density = displayMetrics.density;

        mTopicVideoPlayerPresenter = new TopicVideoPlayerPresenter(this);

        // 初始化控件
        initControl();
        // 初始化控件事件
        initControlEvent();
        // 初始化控件布局参数
        initControlLayoutParams();
        // 初始化数据
        initData();
    }

    private void initControl() {
        mDecorView = (ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content);
        // 顶部返回、topic名称、分享
        mTbVideoPlayerHead = (TitleBar) findViewById(R.id.tb_video_player_head);
        mIvVideoPlayerBack = mTbVideoPlayerHead.getBackButton();
        mIvVideoDownload = mTbVideoPlayerHead.getRightImageButtonTwo();
        mIvTopicShare = mTbVideoPlayerHead.getRightImageButtonOne();
        mTvTopicName = mTbVideoPlayerHead.getTitleView();
        mIvVideoPlayerBack.setImageResource(R.mipmap.icon_back_white);
        mIvVideoDownload.setImageResource(R.mipmap.icon_download_white);
        mIvTopicShare.setImageResource(R.mipmap.icon_share_white);

        mTopicPullToRefreshView = (PullToRefreshView) findViewById(R.id.topic_pull_refresh_view);
        mTopicPullToRefreshView.setOnFooterRefreshListener(this);

        mLvTopicDetail = (ListView) findViewById(R.id.lv_topic_detail);

        RelativeLayout topicDetailHeadLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.topic_detail_head, null);
        mLvTopicDetail.addHeaderView(topicDetailHeadLayout, null, false);
        mTopicCommentAdapter = new TopicCommentAdapter(this, mTopicId);
        mLvTopicDetail.setAdapter(mTopicCommentAdapter);


        // 视频播放控件
        mVDVideoView = (VDVideoView) topicDetailHeadLayout.findViewById(R.id.video_player);
        // 手动这是播放窗口父类，横屏的时候，会用这个做为容器使用，如果不设置，那么默认直接跳转到DecorView
        mVDVideoView.setVDVideoViewContainer((ViewGroup) mVDVideoView.getParent());
        mIvVideoPlayIcon = (ImageView) topicDetailHeadLayout.findViewById(R.id.iv_video_play_icon);
        mLayoutVideoPlayNotWifi = (RelativeLayout) topicDetailHeadLayout.findViewById(R.id.layout_video_play_not_wifi);
        mTvVideoPlayNotWifi = (TextView) topicDetailHeadLayout.findViewById(R.id.tv_video_play_not_wifi);
        mPlayIconParent = (LinearLayout) mIvVideoPlayIcon.getParent();

        // 当前是非WIFI环境，并且允许在非WIFI环境下播放视频，需要提示用户
        //&& SharedPreferencesUtil.getAllowNoWifi(this)

        // WIFI环境下显示播放按钮
        initVideoPlayerIcon();

        // 根据视频宽高比例，重新计算视频播放器高度
        mVDVideoView.getLayoutParams().height = (int) (widthPixels / Constants.VIDEO_ASPECT_RATIO);
        topicDetailHeadLayout.findViewById(R.id.video_player_icon_tips_layout).getLayoutParams().height = mVDVideoView.getLayoutParams().height;

        // 作者信息
        mIvVideoAuthorPhoto = (CircleImageView) topicDetailHeadLayout.findViewById(R.id.iv_video_author_photo);
        mTvVideoAuthorPlayTimes = (TextView) topicDetailHeadLayout.findViewById(R.id.tv_video_author_play_times);
        mIvVideoImageTextIcon = (ImageView) topicDetailHeadLayout.findViewById(R.id.iv_video_image_text_icon);
        mLayoutTopicAuthorTags = (LinearLayout) topicDetailHeadLayout.findViewById(R.id.layout_topic_author_tags);
        mTvTopicAuthorTag1 = (TextView) topicDetailHeadLayout.findViewById(R.id.tv_topic_author_tag_1);
        mTvTopicAuthorTag2 = (TextView) topicDetailHeadLayout.findViewById(R.id.tv_topic_author_tag_2);
        mTvTopicAuthorTag3 = (TextView) topicDetailHeadLayout.findViewById(R.id.tv_topic_author_tag_3);
        mIvTopicAuthorTag1Icon = (ImageView) topicDetailHeadLayout.findViewById(R.id.iv_topic_author_tag_1_icon);
        mIvTopicAuthorTag2Icon = (ImageView) topicDetailHeadLayout.findViewById(R.id.iv_topic_author_tag_2_icon);
        mIvTopicAuthorTag3Icon = (ImageView) topicDetailHeadLayout.findViewById(R.id.iv_topic_author_tag_3_icon);

        // 更多视频
        mLayoutMoreVideosContent = (LinearLayout) topicDetailHeadLayout.findViewById(R.id.layout_more_videos_content);

        //使用产品
        mLayoutVideoUsedProductContent = (LinearLayout) topicDetailHeadLayout.findViewById(R.id.layout_used_product_content);

        // 图文分解
        mLayoutShowTopicImageTextContent = (LinearLayout) topicDetailHeadLayout.findViewById(R.id.layout_show_topic_image_text_content);

        mTvHideTopicImageTextContent = (TextView) topicDetailHeadLayout.findViewById(R.id.tv_hide_topic_image_text_content);
        mTvDownloadTopicImageTextContent = (TextView) topicDetailHeadLayout.findViewById(R.id.tv_download_topic_image_text_content);
        mDownloadTopicImageTextDialog = new Dialog(this);
        mDownloadTopicImageTextDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LinearLayout downloadTopicImageTextLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.download_topic_image_text_layout, null);
        mDownloadTopicImageTextDialog.setContentView(downloadTopicImageTextLayout);
        mDownloadTopicImageTextDialog.setCancelable(false);
        mDownloadTopicImageTextDialog.setCanceledOnTouchOutside(false);
        mRpbDownloadTopicImageText = (RoundProgressBar) downloadTopicImageTextLayout.findViewById(R.id.rpb_download_topic_image_text);
        mTvDownloadTopicImageText = (TextView) downloadTopicImageTextLayout.findViewById(R.id.tv_download_topic_image_text);
        mRpbDownloadTopicImageText.setProgress(0);


        mLayoutTopicImageTextContent = (LinearLayout) topicDetailHeadLayout.findViewById(R.id.layout_topic_image_text_content);
        mLayoutTopicImageTextContentParent = (LinearLayout) topicDetailHeadLayout.findViewById(R.id.layout_topic_image_text_content_parent);

        mZoomInImageTextLayout = getLayoutInflater().inflate(R.layout.zoomin_topic_image_text_layout, null);
        mTvZoomInImageTextIndex = (TextView) mZoomInImageTextLayout.findViewById(R.id.tv_zoomIn_topic_image_text_index);
        mTvZoomInImageSave = (TextView) mZoomInImageTextLayout.findViewById(R.id.tv_zoomIn_topic_image_text_save);
        mZoomInImageTextViewPager = (ViewPager) mZoomInImageTextLayout.findViewById(R.id.vp_zoomIn_topic_image_text);
        mZoomInImageTextLayout.setBackgroundColor(getResources().getColor(R.color.black));
        mZoomInImageTextLayout.setTag(R.layout.zoomin_topic_image_text_layout, true);

        mTvTopicCommentCount = (TextView) topicDetailHeadLayout.findViewById(R.id.tv_topic_comment_count);
        // 设置默认显示的评论数
        mTvTopicCommentCount.setText(R.string.topic_comment_count_0);

        mTopicEmptyCommentLayout = (LinearLayout) topicDetailHeadLayout.findViewById(R.id.topic_empty_comment_layout);


        // 底部下载、收藏、评论、交作业
        mCbTopicTabLike = (CheckBox) findViewById(R.id.cb_topic_tab_like);
        mCbTopicTabCollection = (CheckBox) findViewById(R.id.cb_topic_tab_collection);
        mTvTopicTabComment = (TextView) findViewById(R.id.tv_topic_tab_comment);
        mTvTopicSubmittedHomework = (TextView) findViewById(R.id.tv_submitted_homework);
        // 交作业截图
        mTakeImage = new TakeImage.Builder().context(this)
                .uri(Constants.YILOS_NAILSTAR_PATH)
                .aspectX(Constants.HOMEWORK_PIC_ASPECT_RATIO)
                .aspectY(Constants.HOMEWORK_PIC_ASPECT_RATIO)
                .outputX(Constants.HOMEWORK_PIC_PIXEL)
                .outputY(Constants.HOMEWORK_PIC_PIXEL)
                .callback(new TakeImageCallback() {
                    @Override
                    public void callback(Uri uri) {
                        Intent intent = new Intent(TopicVideoPlayerActivity.this, TopicHomeworkActivity.class);
                        intent.putExtra(Constants.TOPIC_ID, mTopicId);
                        // 交作业是atuserId为视频作者
                        intent.putExtra(Constants.TOPIC_COMMENT_USER_ID, mTopicInfo.getAuthorId());
                        intent.putExtra(Constants.CONTENT_PIC, uri.getPath());
                        startActivityForResult(intent, Constants.TOPIC_HOMEWORK_REQUEST_CODE);
                    }
                }).build();

        //  计算图文详情放大时文字的marginTop
        int zooInLayout = heightPixels - getResources().getDimensionPixelSize(R.dimen.zoomIn_layout_margin_bottom);
        int zoomInImageHeight = (int) (widthPixels / Constants.IMAGE_TEXT_ASPECT_RATIO);
        mZoomInTextViewMarginTop = (zooInLayout / 2) + (zoomInImageHeight / 2) + mImageTextMargin;
    }

    private void initControlEvent() {
        // 界面顶部返回按钮
        mIvVideoPlayerBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TopicVideoPlayerActivity.this, MainActivity.class);
                setResult(RESULT_OK, intent);
                TopicVideoPlayerActivity.this.finish();
            }
        });

        mIvVideoDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAndShowNetworkStatus()) {
                    return;
                }
                // 视频已经下载或正在下载
                if (mTopicVideoPlayerPresenter.isDownloadVideo(mTopicInfo)) {
                    showShortToast(String.format(getString(R.string.video_has_been_cached), mTopicInfo.getTitle()));
                    return;
                }

                // 非wifi网络提示是否继续下载
                if (!NailStarApplicationContext.getInstance().isWifi()) {
                    showMessageDialogWithEvent(null,
                            getString(R.string.video_play_not_wifi_tips1) + ", " + getString(R.string.continue_to_download),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //下载视频
                                    showShortToast(getString(R.string.add_video_download));
                                    mTopicVideoPlayerPresenter.downloadVideo(mTopicInfo);
                                }
                            },
                            null);
                } else {
                    //下载视频
                    showShortToast(getString(R.string.add_video_download));
                    mTopicVideoPlayerPresenter.downloadVideo(mTopicInfo);
                }

            }
        });

        // 界面顶部分享按钮
        mIvTopicShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAndShowNetworkStatus()) {
                    return;
                }
                SocialAPI.getInstance().share(TopicVideoPlayerActivity.this,
                        getString(R.string.topic_share_title),
                        String.format(getString(R.string.topic_share_content), mTopicInfo.getTitle()),
                        String.format(Constants.TOPIC_SHARE_URL, mTopicId),
                        mTopicInfo.getThumbUrl());
            }
        });

        // 设置视频播放监听
        mVDVideoView.setCompletionListener(new VDVideoExtListeners.OnVDVideoCompletionListener() {
            @Override
            public void onVDVideoCompletion(VDVideoInfo info, int status) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        showLongToast("视频播放结束");
                        mPlayIconParent.setVisibility(View.VISIBLE);
                        initVideoPlayerIcon();
                    }
                });
            }
        });


        mPlayIconParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 屏蔽视频播放器的点击事件
            }
        });

        mIvVideoPlayIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAndShowNetworkStatus()) {
                    return;
                }
                mPlayIconParent.setVisibility(View.GONE);
                mIvVideoPlayIcon.setVisibility(View.GONE);
                mLayoutVideoPlayNotWifi.setVisibility(View.GONE);
                mVDVideoView.play(0);
                mTopicVideoPlayerPresenter.addVideoPlayCount(mTopicId);
            }
        });

        mTvVideoPlayNotWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAndShowNetworkStatus()) {
                    return;
                }
                mPlayIconParent.setVisibility(View.GONE);
                mLayoutVideoPlayNotWifi.setVisibility(View.GONE);
                mVDVideoView.play(0);
                mTopicVideoPlayerPresenter.addVideoPlayCount(mTopicId);
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

        mZoomInImageTextLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDecorView.removeView(mZoomInImageTextLayout);
            }
        });
        mTvZoomInImageTextIndex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDecorView.removeView(mZoomInImageTextLayout);
            }
        });

        mTvZoomInImageSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAndShowNetworkStatus()) {
                    return;
                }
                // 保存单张图片
                int index = mZoomInImageTextViewPager.getCurrentItem();
                if (mTopicImageTextInfo.getPictures().size() > index) {
                    mTopicVideoPlayerPresenter.downLoadTopicImage(mTopicId, mTopicInfo.getTitle(), index + 1, mTopicImageTextInfo.getPictures().get(index));
                } else {
                    showShortToast(getString(R.string.operation_fail));
                }
            }
        });


        // 下载图文按钮
        mTvDownloadTopicImageTextContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAndShowNetworkStatus()) {
                    return;
                }
                // 下载图文信息到本地
                if (isEmptyTopicImageTextInfo()) {
                    showShortToast(R.string.no_topic_image_text_info);
                    return;
                }
                mDownloadTopicImageTextDialog.show();
                mDownloadTopicImageTextDialog.getWindow().setLayout((int) (widthPixels * 0.6), ViewGroup.LayoutParams.WRAP_CONTENT);
                mTvDownloadTopicImageText.setText(R.string.saving_photos);
                mRpbDownloadTopicImageText.setMax(mTopicImageTextInfo.getPictures().size());
                mRpbDownloadTopicImageText.setVisibility(View.VISIBLE);
                mDownloadTopicImageTextIndex = 0;
                mTopicVideoPlayerPresenter.downloadTopicImageText(mTopicId, mTopicInfo.getTitle(), mTopicImageTextInfo.getPictures());
            }
        });

        // 喜欢
        mCbTopicTabLike.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkAndShowNetworkStatus()) {
                    autoSetTopicLikeStatus = true;
                    mCbTopicTabLike.setChecked(!isChecked);
                    return;
                }
                if (!LoginAPI.getInstance().isLogin()) {
                    autoSetTopicLikeStatus = true;
                    mCbTopicTabLike.setChecked(false);
                    LoginAPI.getInstance().gotoLoginPage(TopicVideoPlayerActivity.this);
                    return;
                }
                if (autoSetTopicLikeStatus) {
                    autoSetTopicLikeStatus = false;
                    return;
                }
                mTopicVideoPlayerPresenter.setTopicLikeStatus(mTopicId, isChecked);
            }
        });
        //收藏
        mCbTopicTabCollection.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkAndShowNetworkStatus()) {
                    autoSetTopicCollectionStatus = true;
                    mCbTopicTabCollection.setChecked(!isChecked);
                    return;
                }
                if (!LoginAPI.getInstance().isLogin()) {
                    autoSetTopicCollectionStatus = true;
                    mCbTopicTabCollection.setChecked(false);
                    LoginAPI.getInstance().gotoLoginPage(TopicVideoPlayerActivity.this);
                    return;
                }
                if (autoSetTopicCollectionStatus) {
                    autoSetTopicCollectionStatus = false;
                    return;
                }
                mTopicVideoPlayerPresenter.setTopicCollectionStatus(mTopicId, isChecked);
            }
        });

        // 评论按钮
        mTvTopicTabComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAndShowNetworkStatus()) {
                    return;
                }
                if (!LoginAPI.getInstance().isLogin()) {
                    LoginAPI.getInstance().gotoLoginPage(TopicVideoPlayerActivity.this);
                    return;
                }
                addTopicComment();
            }
        });

        // 交作业按钮
        mTvTopicSubmittedHomework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAndShowNetworkStatus()) {
                    return;
                }
                if (!LoginAPI.getInstance().isLogin()) {
                    LoginAPI.getInstance().gotoLoginPage(TopicVideoPlayerActivity.this);
                    return;
                }
                mTakeImage.initTakeImage();
            }
        });
    }

    private void initControlLayoutParams() {
        mMoresVideoMargin = getResources().getDimensionPixelSize(R.dimen.topic_mores_video_margin_right);

        mTopicRelatedMarginRight = getResources().getDimensionPixelSize(R.dimen.topic_related_margin_right);
        mTopicRelatedPlayImageSize = getResources().getDimensionPixelSize(R.dimen.topic_related_play_image_size);

        mImageTextMargin = getResources().getDimensionPixelSize(R.dimen.topic_image_text_margin);

        mTopicRelateProductLineWidth = getResources().getDimensionPixelSize(R.dimen.topic_related_used_product_width);
        mTopicRelateProductLineHeight = getResources().getDimensionPixelSize(R.dimen.topic_related_used_product_height);
    }

    private void initData() {
        if (NailStarApplicationContext.getInstance().isNetworkConnected()) {
            showLoading("");
        }
        // 调用后台接口初始化界面数据
        mTopicVideoPlayerPresenter.initTopicInfo(mTopicId);
        mTopicVideoPlayerPresenter.initTopicRelatedInfo(mTopicId);
        mTopicVideoPlayerPresenter.initTopicRelatedUsedProductList(mTopicId);

        mTopicVideoPlayerPresenter.initTopicImageTextInfo(mTopicId);
        mTopicVideoPlayerPresenter.initTopicComments(mTopicId, mPage);
        // 如果没有登录的话，则不需要查询用户的topic状态信息
        if (LoginAPI.getInstance().isLogin()) {
            mTopicVideoPlayerPresenter.initUserTopicStatus(mTopicId);
        } else {
            initUserTopicStatusFinish = true;
        }
        checkInitFinish();
    }

    private void initVideoPlayerIcon() {
        //WIFI环境下显示播放按钮
        if (NailStarApplicationContext.getInstance().isWifi()) {
            mIvVideoPlayIcon.setVisibility(View.VISIBLE);
            mLayoutVideoPlayNotWifi.setVisibility(View.GONE);
        } else {//否则给用户选择提示
            mIvVideoPlayIcon.setVisibility(View.GONE);
            mLayoutVideoPlayNotWifi.setVisibility(View.VISIBLE);
        }
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
                if (isEmptyTopicImageTextInfo()) {
                    showShortToast(R.string.no_topic_image_text_info);
                    return;
                }
                addTopicImageTextInfo2Page();
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

    /**
     * 评论
     */
    private void addTopicComment() {
        if (!LoginAPI.getInstance().isLogin()) {
            LoginAPI.getInstance().gotoLoginPage(this);
            return;
        }
        Intent intent = new Intent(this, TopicCommentActivity.class);
        intent.putExtra(Constants.TOPIC_ID, mTopicId);
        // 评论的atuserid为视频作者
        intent.putExtra(Constants.TOPIC_COMMENT_USER_ID, mTopicInfo.getAuthorId());
        intent.putExtra(Constants.TYPE, Constants.TOPIC_COMMENT_TYPE_COMMENT);
        startActivityForResult(intent, Constants.TOPIC_COMMENT_REQUEST_CODE);
    }

    @Override
    public void initTopicInfo(TopicInfo topicInfo) {
        initTopicInfoFinish = true;
        checkInitFinish();
        //TODO 提示获取视频信息失败
        if (null == topicInfo) {
//            LOGGER.error(TAG + " 获取topic信息为null，topicId:" + mTopicId);
            return;
        }
        mTopicInfo = topicInfo;
        showTopicInfo2Page(topicInfo);
        VDVideoInfo info = new VDVideoInfo();
        if (!CollectionUtil.isEmpty(topicInfo.getVideos())) {
            TopicVideoInfo topicVideoInfo = topicInfo.getVideos().get(0);
            mVideoRemoteUrl = mTopicVideoPlayerPresenter.buildVideoRemoteUrl(topicVideoInfo);
            info.mTitle = topicInfo.getTitle();
            info.mPlayUrl = mVideoRemoteUrl;
            info.setNetUrl(mVideoRemoteUrl);
            // 获取视频缩略图
            Bitmap bitmap = createVideoThumbnail(mVideoRemoteUrl, 700, (int) (700 / Constants.VIDEO_ASPECT_RATIO));
            mPlayIconParent.setBackgroundDrawable(new BitmapDrawable(bitmap));
        }
        mVDVideoView.open(this, info);
    }

    /**
     * 将视频信息显示在界面上
     *
     * @param topicInfo
     */
    private void showTopicInfo2Page(TopicInfo topicInfo) {
        // 视频名称
        mTvTopicName.setText(topicInfo.getTitle());
        // 作者照片
        mIvVideoAuthorPhoto.setImageSrc(topicInfo.getAuthorPhoto());
        // 作者和播放次数名称
        StringBuilder stringBuilder = new StringBuilder()
                .append(buildTextFont(R.color.z2, topicInfo.getAuthor()));
        if (!CollectionUtil.isEmpty(topicInfo.getVideos())) {
            stringBuilder.append("  ")
                    .append(buildTextFont(R.color.z3, String.format(getString(R.string.video_play_times), topicInfo.getVideos().get(0).getPlayTimes())));
        }
        mTvVideoAuthorPlayTimes.setText(Html.fromHtml(stringBuilder.toString()));

        // 设置作者的tags
        ArrayList tags = topicInfo.getTags();
        if (!CollectionUtil.isEmpty(tags)) {
            mLayoutTopicAuthorTags.setVisibility(View.VISIBLE);
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
        } else {
            mLayoutTopicAuthorTags.setVisibility(View.GONE);
        }

        // 设置评论数量
        mTvTopicCommentCount.setText(String.format(getString(R.string.topic_comment_count), topicInfo.getCommentCount()));
    }


    @Override
    public void initTopicImageTextInfo(final TopicImageTextInfo topicImageTextInfo) {
        initTopicImageTextInfoFinish = true;
        checkInitFinish();
        //TODO 提示获取视频图文信息失败
        if (null == topicImageTextInfo) {
//            LOGGER.warn(TAG + " 获取topic图文信息为null，topicId:" + mTopicId);
            return;
        }
        mTopicImageTextInfo = topicImageTextInfo;
    }

    private void addTopicImageTextInfo2Page() {
        if (null == mTopicImageTextInfo) {
            return;
        }
        ArrayList<String> pictures = mTopicImageTextInfo.getPictures();
        ArrayList<String> articles = mTopicImageTextInfo.getArticles();

        LinearLayout.LayoutParams lpMarginTop = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lpMarginTop.setMargins(0, mImageTextMargin, 0, 0);


        for (int i = 0, size = pictures.size(); i < size; i++) {
            ImageCacheView imageView = new ImageCacheView(TopicVideoPlayerActivity.this);
            imageView.setLayoutParams(lpMarginTop);
            imageView.setImageSrc(pictures.get(i));
            imageView.setAdjustViewBounds(true);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 显示大图
                    zoomInTopicImageText((ImageCacheView) v, mTopicImageTextInfo);
                }
            });
            mLayoutTopicImageTextContent.addView(imageView);

            if (!StringUtil.isEmpty(articles.get(i))) {
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
        textLp.setMargins(mImageTextMargin, mZoomInTextViewMarginTop, mImageTextMargin, 0);
        // 查看图文分解时，选择放大查看的图片是哪张
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
            // 获取查看图文分解时，选择放大查看的图片是哪张
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
                    mDecorView.removeView(mZoomInImageTextLayout);
                }
            });
            tvText.setText(articles.get(i));
            // 设置textview的margintop，让它正好处于图片的下方
            tvText.setLayoutParams(textLp);
            views[i] = view;
        }
        mZoomInImageTextViewPager.setPageMargin((int) (density * 15));
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
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public void finishUpdate(ViewGroup container) {
                super.finishUpdate(container);
                StringBuilder text = new StringBuilder()
                        .append(mZoomInImageTextViewPager.getCurrentItem() + 1)
                        .append("/")
                        .append(getCount());
                mTvZoomInImageTextIndex.setText(text);
            }
        });
        if (selectedIndex != -1) {
            mZoomInImageTextViewPager.getAdapter().notifyDataSetChanged();
            final int index = selectedIndex;
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    mZoomInImageTextViewPager.setCurrentItem(index);
                    mDecorView.removeView(mZoomInImageTextLayout);
                    mDecorView.addView(mZoomInImageTextLayout);
                }
            });
        } else {
            mDecorView.removeView(mZoomInImageTextLayout);
            mDecorView.addView(mZoomInImageTextLayout);
        }
    }

    @Override
    public void initTopicRelatedInfo(ArrayList<TopicRelatedInfo> topicRelatedList) {
        initTopicRelatedInfoFinish = true;
        checkInitFinish();
        if (CollectionUtil.isEmpty(topicRelatedList)) {
//            LOGGER.warn(TAG + " topic没有关联其他的的topics，topicId:" + mTopicId);
            return;
        }
        LinearLayout.LayoutParams layoutLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        layoutLp.setMargins(0, 0, mTopicRelatedMarginRight, 0);

        FrameLayout.LayoutParams topicRelateIvLp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        FrameLayout.LayoutParams playTopicRelateIvLp = new FrameLayout.LayoutParams(mTopicRelatedPlayImageSize, mTopicRelatedPlayImageSize);
        playTopicRelateIvLp.gravity = Gravity.RIGHT | Gravity.BOTTOM;
        playTopicRelateIvLp.setMargins(0, 0, mMoresVideoMargin, mMoresVideoMargin);

        for (int i = 0; i < Constants.MORE_VIDEOS_COUNT; i++) {
            FrameLayout topicRelateLayout = new FrameLayout(this);
            topicRelateLayout.setLayoutParams(layoutLp);

            ImageCacheView topicRelateIv = new ImageCacheView(this);
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
                playTopicRelateIv.setImageResource(R.mipmap.icon_video_play);
                playTopicRelateIv.setLayoutParams(playTopicRelateIvLp);
                topicRelateLayout.addView(playTopicRelateIv);
            } else {
                topicRelateIv.setBackgroundResource(R.color.white);
                topicRelateLayout.addView(topicRelateIv);
            }

            mLayoutMoreVideosContent.addView(topicRelateLayout);
        }
    }

    //初始化关联的产品列表
    @Override
    public void initTopicRelatedUsedProductList(ArrayList<TopicRelatedProduct> topicRelatedProductList) {
        initTopicRelatedUsedProductsFinish = true;
        checkInitFinish();
        if (CollectionUtil.isEmpty(topicRelatedProductList)) {
//            LOGGER.warn(TAG + " topic没有关联商品信息，topicId:" + mTopicId);
            //使用产品区隐藏
            ((LinearLayout) findViewById(R.id.layout_used_products)).setVisibility(View.GONE);
            ((LinearLayout) findViewById(R.id.layout_used_product_line)).setVisibility(View.GONE);
            return;
        }
        LinearLayout.LayoutParams topicRelateIvLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mTopicRelateProductLineHeight);
        topicRelateIvLp.setMargins(10 * getResources().getDimensionPixelSize(R.dimen.common_1_dp), 0, 0, 0);
        LinearLayout.LayoutParams topicRelateProduxtTextIvLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        topicRelateProduxtTextIvLp.weight = 1;
        LinearLayout.LayoutParams arrowsLp = new LinearLayout.LayoutParams(15 * getResources().getDimensionPixelSize(R.dimen.common_1_dp),
                15 * getResources().getDimensionPixelSize(R.dimen.common_1_dp));
        arrowsLp.setMargins(10 * getResources().getDimensionPixelSize(R.dimen.common_1_dp), 0, 10 * getResources().getDimensionPixelSize(R.dimen.common_1_dp), 0);

        for (int i = 0; i < topicRelatedProductList.size(); i++) {
            LinearLayout topicRelateLayout = new LinearLayout(this);
            topicRelateLayout.setLayoutParams(topicRelateIvLp);
            if (i != topicRelatedProductList.size() - 1) {
                topicRelateLayout.setBackgroundResource(R.drawable.bottom_border);
            }
            topicRelateLayout.setGravity(Gravity.CENTER_VERTICAL);
            TextView topicRelateUseProductv = new TextView(TopicVideoPlayerActivity.this);
            topicRelateUseProductv.setLayoutParams(topicRelateProduxtTextIvLp);
            topicRelateUseProductv.setText(topicRelatedProductList.get(i).getProductName());
            topicRelateUseProductv.setTextColor(getResources().getColor(R.color.z1));
            topicRelateUseProductv.setTextSize(15);
            topicRelateUseProductv.setTag(R.id.topic_related_used_product, topicRelatedProductList.get(i));
            topicRelateUseProductv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showTopicRelatedUsedProductDetail((TopicRelatedProduct) v.getTag(R.id.topic_related_used_product));
                }
            });
            topicRelateLayout.addView(topicRelateUseProductv);
            ImageView arrows = new ImageView(TopicVideoPlayerActivity.this);
            arrows.setImageResource(R.mipmap.ic_right_button);
            arrows.setLayoutParams(arrowsLp);
            topicRelateLayout.addView(arrows);
            mLayoutVideoUsedProductContent.addView(topicRelateLayout);

            //设置购物帮助事件

            ImageView helper = (ImageView) findViewById(R.id.video_used_product_helper);
            helper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openHelper();
                }
            });

        }
    }


    @Override
    public void initUserTopicStatus(TopicStatusInfo topicStatusInfo) {
        initUserTopicStatusFinish = true;
        checkInitFinish();
        if (null != topicStatusInfo) {
            if (topicStatusInfo.isLike()) {
                autoSetTopicLikeStatus = true;
                mCbTopicTabLike.setChecked(topicStatusInfo.isLike());
            }
            if (topicStatusInfo.isCollect()) {
                autoSetTopicCollectionStatus = true;
                mCbTopicTabCollection.setChecked(topicStatusInfo.isCollect());
            }
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
        startActivity(intent);
        finish();
    }


    /**
     * 跳转到topic关联的商品详情界面
     *
     * @param topicRelatedProduct
     */
    private void showTopicRelatedUsedProductDetail(TopicRelatedProduct topicRelatedProduct) {
        //跳转淘宝商品详情界面
        Map<String, String> exParams = new HashMap<String, String>();
        exParams.put(TradeConstants.ITEM_DETAIL_VIEW_TYPE, TradeConstants.BAICHUAN_H5_VIEW);
        exParams.put(TradeConstants.ISV_CODE, Constants.ISV_CODE);
        ItemService itemService = AlibabaSDK.getService(ItemService.class);
        UiSettings uiSetting = new UiSettings();
        itemService.showItemDetailByItemId(this, new TradeProcessCallback() {
            @Override
            public void onPaySuccess(TradeResult tradeResult) {
                //弹出框，提示购买成功，去淘宝查看订单信息
                final OrderFinishDialog successDialog = new OrderFinishDialog(TopicVideoPlayerActivity.this);
                successDialog.show();
                final Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        successDialog.dismiss();
                        timer.cancel();
                    }
                };
                //3秒后消失
                timer.schedule(task, 3000);
            }

            @Override
            public void onFailure(int code, String msg) {

            }
        }, uiSetting, Long.valueOf(topicRelatedProduct.getReal_id()), ItemType.TAOBAO, exParams);

    }

    private void openHelper() {
        //打开webview，展示帮助信息
        Intent intent = new Intent(this, YLSWebViewActivity.class);
        intent.putExtra(Constants.WEBVIEW_TITLE, getString(R.string.topic_shopping_helper));
        intent.putExtra(Constants.WEBVIEW_URL, Constants.TOPIC_PRODUCT_HELPER_URL);
        startActivity(intent);
    }

    @Override
    public void initTopicCommentInfo(ArrayList<TopicCommentInfo> topicComments, int orderBy) {
        initTopicCommentsFinish = true;
        checkInitFinish();
        if (CollectionUtil.isEmpty(topicComments)) {
            // 设置评论已经全部加载完毕
            mIsTopicsCommentLastPage = true;
            mTopicPullToRefreshView.setFooterLastUpdate(null);
            if (mPage == 1) {//没有评论
                mTopicEmptyCommentLayout.setVisibility(View.VISIBLE);
            }
            return;
        } else {
            if (mPage == 1) {
                mTopicEmptyCommentLayout.setVisibility(View.GONE);
            }
        }
        if (Constants.TOPIC_COMMENTS_INIT_ORDER_BY_DESC == orderBy) {
            mTopicCommentAdapter.addTopicCommentInfoList(topicComments, 0);
        } else {
            mTopicCommentAdapter.addTopicCommentInfoList(topicComments);
        }
        mTopicCommentAdapter.notifyDataSetChanged();
        mTopicPullToRefreshView.onFooterRefreshComplete();
    }


    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        mTopicPullToRefreshView.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (mIsTopicsCommentLastPage) {
                    mTopicPullToRefreshView.setFooterLastUpdate(getString(R.string.loading_finish));
                } else {
                    mTopicVideoPlayerPresenter.initTopicComments(mTopicId, ++mPage);
                }
            }
        }, 1000);
    }

    @Override
    public void setTopicLikeStatus(boolean isLike, boolean isSuccess) {
        if (!isSuccess) {
            autoSetTopicLikeStatus = true;
            mCbTopicTabLike.setChecked(!isLike);
            showShortToast(getString(R.string.operation_fail));
        }
    }

    @Override
    public void setTopicCollectionStatus(boolean isCollection, boolean isSuccess) {
        if (isCollection && isSuccess) {
            showShortToast(getString(R.string.add_topic_collection));
        }
        if (!isSuccess) {
            autoSetTopicCollectionStatus = true;
            mCbTopicTabCollection.setChecked(!isCollection);
            showShortToast(getString(R.string.operation_fail));
        }
    }

    @Override
    public void setDownloadTopicImageStatus(boolean isSuccess, String filePath) {
        if (isSuccess) {
            refreshMedia(filePath);
            showShortToast(R.string.save_success);
            return;
        }
        showShortToast(R.string.save_fail);
    }

    @Override
    public void setDownloadTopicImageTextStatus(boolean isSuccess, final String filePath) {
        if (!isSuccess) {
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                refreshMedia(filePath);
                mRpbDownloadTopicImageText.setProgress(++mDownloadTopicImageTextIndex);
                mTvDownloadTopicImageText.setText(String.format(getString(R.string.download_topic_image_text_index), mDownloadTopicImageTextIndex));
                if (mRpbDownloadTopicImageText.getMax() == mRpbDownloadTopicImageText.getProgress()) {
                    // 显示下载完成
                    mTvDownloadTopicImageText.setText(R.string.save_topic_image_text_success);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mDownloadTopicImageTextDialog.dismiss();
                            mRpbDownloadTopicImageText.setProgress(0);
                        }
                    }, 1500);
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            boolean hasZoomInLayout = false;
            View view = null;
            for (int i = 0, count = mDecorView.getChildCount(); i < count; i++) {
                view = mDecorView.getChildAt(i);
                if (null != view.getTag(R.layout.zoomin_topic_image_text_layout)
                        || null != view.getTag(R.layout.zoomin_topic_comment_image_layout)) {
                    hasZoomInLayout = true;
                    break;
                }
            }
            mDecorView.removeView(view);
            if (hasZoomInLayout) {
                return false;
            }
        }
        if (!mVDVideoView.onVDKeyDown(keyCode, event)) {
            return super.onKeyDown(keyCode, event);
        }
        return true;
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode != RESULT_OK) {
            if (requestCode == Constants.TOPIC_COMMENT_REQUEST_CODE && resultCode == RESULT_FIRST_USER) {
                showShortToast(R.string.submitted_comment_fail);
            } else if (requestCode == Constants.TOPIC_HOMEWORK_REQUEST_CODE && resultCode == RESULT_FIRST_USER) {
                showShortToast(R.string.submitted_homework_fail);
            }
            return;
        }
        String userId = LoginAPI.getInstance().getLoginUserId();
        String userNickname = LoginAPI.getInstance().getLoginUserNickname();
        String userPhotoUrl = LoginAPI.getInstance().getLoginUserPhotourl();
        // 评论或回复
        if (requestCode == Constants.TOPIC_COMMENT_REQUEST_CODE) {
            Bundle data = intent.getExtras();
//            String topicId = data.getString(Constants.TOPIC_ID, Constants.EMPTY_STRING);
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
                topicCommentInfo.setUserId(userId);
                topicCommentInfo.setAuthor(userNickname);
                topicCommentInfo.setAuthorPhoto(userPhotoUrl);
                topicCommentInfo.setContent(content);
                topicCommentInfo.setContentPic(Constants.EMPTY_STRING);
                topicCommentInfo.setCreateDate(System.currentTimeMillis());
                topicCommentInfo.setIsHomework(Constants.NOT_HOME_WORK_VALUE);
                topicCommentInfo.setIsMine(0);
                topicCommentInfo.setStatus(0);
                topicCommentInfo.setReplies(new ArrayList<TopicCommentReplyInfo>());

                topicCommentInfoList.add(topicCommentInfo);
                initTopicCommentInfo(topicCommentInfoList, Constants.TOPIC_COMMENTS_INIT_ORDER_BY_DESC);
            } else if (commentType == Constants.TOPIC_COMMENT_TYPE_REPLY) {
                TopicCommentReplyInfo topicCommentReplyInfo = new TopicCommentReplyInfo();
                TopicCommentAtInfo topicCommentAtInfo = new TopicCommentAtInfo();
                topicCommentAtInfo.setUserId(commentUserId);
                topicCommentAtInfo.setNickName(commentAuthor);
                topicCommentReplyInfo.setId(newCommentId);
                topicCommentReplyInfo.setUserId(userId);
                topicCommentReplyInfo.setNickname(userNickname);
                topicCommentReplyInfo.setAuthor(userNickname);
                topicCommentReplyInfo.setContent(content);
                topicCommentReplyInfo.setContentPic(Constants.EMPTY_STRING);
                topicCommentReplyInfo.setIsHomework(Constants.NOT_HOME_WORK_VALUE);
                topicCommentReplyInfo.setStatus(0);
                topicCommentReplyInfo.setAt(topicCommentAtInfo);
                // 将评论回复添加到正确的显示位置
                mTopicCommentAdapter.addTopicCommentReply(commentId, topicCommentReplyInfo);
            } else {
//                String commentReplyId = data.getString(Constants.TOPIC_COMMENT_REPLY_ID, Constants.EMPTY_STRING);
                String commentReplyUserId = data.getString(Constants.TOPIC_COMMENT_REPLY_USER_ID, Constants.EMPTY_STRING);
                String commentReplyAuthor = data.getString(Constants.TOPIC_COMMENT_REPLY_AUTHOR, Constants.EMPTY_STRING);

                TopicCommentReplyInfo topicCommentReplyInfo = new TopicCommentReplyInfo();
                TopicCommentAtInfo topicCommentAtInfo = new TopicCommentAtInfo();
                topicCommentAtInfo.setUserId(commentReplyUserId);
                topicCommentAtInfo.setNickName(commentReplyAuthor);
                topicCommentReplyInfo.setId(newCommentId);
                topicCommentReplyInfo.setUserId(userId);
                topicCommentReplyInfo.setNickname(userNickname);
                topicCommentReplyInfo.setAuthor(userNickname);
                topicCommentReplyInfo.setContent(content);
                topicCommentReplyInfo.setContentPic(Constants.EMPTY_STRING);
                topicCommentReplyInfo.setIsHomework(Constants.NOT_HOME_WORK_VALUE);
                topicCommentReplyInfo.setStatus(0);
                topicCommentReplyInfo.setAt(topicCommentAtInfo);
                // 将评论回复添加到正确的显示位置
                mTopicCommentAdapter.addTopicCommentReply(commentId, topicCommentReplyInfo);
            }
            return;
        }
        // 交作业
        if (requestCode == Constants.TOPIC_HOMEWORK_REQUEST_CODE) {
            Bundle data = intent.getExtras();
            String content = data.getString(Constants.CONTENT, Constants.EMPTY_STRING);
            String contentPic = data.getString(Constants.CONTENT_PIC, Constants.EMPTY_STRING);
            String newCommentId = data.getString(Constants.TOPIC_NEW_COMMENT_ID, Constants.EMPTY_STRING);

            ArrayList<TopicCommentInfo> topicCommentInfoList = new ArrayList<TopicCommentInfo>();
            TopicCommentInfo topicCommentInfo = new TopicCommentInfo();
            topicCommentInfo.setId(newCommentId);
            topicCommentInfo.setUserId(userId);
            topicCommentInfo.setAuthor(userNickname);
            topicCommentInfo.setAuthorPhoto(userPhotoUrl);
            topicCommentInfo.setContent(content);
            topicCommentInfo.setContentPic(contentPic);
            topicCommentInfo.setCreateDate(System.currentTimeMillis());
            topicCommentInfo.setIsHomework(Constants.IS_HOME_WORK_VALUE);
            topicCommentInfo.setIsMine(0);
            topicCommentInfo.setStatus(0);
            topicCommentInfo.setReplies(new ArrayList<TopicCommentReplyInfo>());

            topicCommentInfoList.add(topicCommentInfo);
            initTopicCommentInfo(topicCommentInfoList, Constants.TOPIC_COMMENTS_INIT_ORDER_BY_DESC);
            return;
        }
        mTakeImage.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mVDVideoView.setIsFullScreen(true);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            mVDVideoView.setIsFullScreen(false);
        }
    }

    /**
     * 播放列表里面点击了某个视频，触发外部事件
     */
    @Override
    public void onPlaylistClick(VDVideoInfo info, int p) {
        if (info == null) {
//            LOGGER.error("视频信息为null");
        }
        mVDVideoView.play(p);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mVDVideoView.onStart();
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
//        mVDVideoView.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setContentView(R.layout.layout_null);
        mVDVideoView.onStop();
        mVDVideoView.destroyDrawingCache();
        mVDVideoView.unRegisterSensorManager();
        mVDVideoView.release(false);
    }

    private void refreshMedia(String filePath) {
        if (StringUtil.isEmpty(filePath)) {
            return;
        }
        MediaScannerConnection.scanFile(this, new String[]{filePath}, null, null);
    }

    private void checkInitFinish() {
        if (initTopicInfoFinish//初始化topic信息
//                && initTopicRelatedInfoFinish//初始化topic关联信息
//                && initTopicRelatedUsedProductsFinish//初始化topic产品信息
//                && initTopicImageTextInfoFinish//初始化topic图文信息
//                && initTopicCommentsFinish//初始化topic评论信息
                && initUserTopicStatusFinish) {//初始化登录用户topic状态信息
            hideLoading();
        }
    }

    private boolean isEmptyTopicImageTextInfo() {
        return null == mTopicImageTextInfo || CollectionUtil.isEmpty(mTopicImageTextInfo.getPictures());
    }
}