package com.yilos.nailstar.requirelession.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.StringRes;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.model.LoginAPI;
import com.yilos.nailstar.category.model.CategoryListAPI;
import com.yilos.nailstar.framework.entity.NailStarApplicationContext;
import com.yilos.nailstar.requirelession.Presenter.LessionPresenter;
import com.yilos.nailstar.requirelession.entity.CandidateLession;
import com.yilos.nailstar.requirelession.entity.LessionActivity;
import com.yilos.nailstar.takeImage.TakeImage;
import com.yilos.nailstar.takeImage.TakeImageCallback;
import com.yilos.nailstar.topic.view.TopicVideoPlayerActivity;
import com.yilos.nailstar.util.Constants;
import com.yilos.nailstar.util.FileUtils;
import com.yilos.widget.circleimageview.CircleImageView;
import com.yilos.widget.titlebar.TitleBar;
import com.yilos.widget.view.ImageCacheView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * 求教程Fragment
 */
public class RequireLessionFragment extends Fragment implements LessionView {

    private static final String QJC = "qjc";
    // 屏幕宽度
    private int screenWidth;

    // 下拉刷新
    private PtrClassicFrameLayout lessionPullRefresh;

    // title bar
    private TitleBar lessionTitleBar;

    // 求教程页面
    private View view;

    // 候选款式列表
    private ListView lessionVoteView;

    // 投票候选列表Adapter
    private VoteListViewAdapter voteListViewAdapter;

    // 求教程榜首（第一阶段：求教程阶段）
    private View lessionBackground;

    // 求教程榜首（第二阶段：视频制作阶段）
    private View candidateBackground;

    // 倒计时描述
    private TextView lessionCountDownText;

    // 剩余时间
    private TextView lessionCountDownValue;

    // 页面悬浮部分（求教程按钮）
    private View lessionViewHeadFloat;

    // 求教程按钮
    private Button requireLessionBtn;
    private Button requireLessionGrayBtn;

    // 悬浮页头的求教程按钮
    private Button requireLessionBtnFloat;
    private Button requireLessionGrayBtnFloat;

    // 投票列表按钮
    private TextView goVotingBtn;

    // 排行榜按钮
    private TextView goRankingBtn;

    // 在悬浮头中的投票列表按钮
    private TextView goVotingBtnFloat;

    // 在悬浮头中的排行榜按钮
    private TextView goRankingBtnFloat;

    // 页签指示
    private View pageCursor;
    private View pageCursorFloat;

    private LessionPresenter lessionPresenter = new LessionPresenter(this);

    private TakeImage takeImage;

    public RequireLessionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        screenWidth = metric.widthPixels;

        view = inflater.inflate(R.layout.fragment_require_lession, container, false);

        initView(inflater);
        initData();
        bindControl();
        return view;
    }

    private void initView(LayoutInflater inflater) {

        // 初始化ListView
        lessionVoteView = (ListView) view.findViewById(R.id.lessionVoteView);

        View lessionViewHead0 = inflater.inflate(R.layout.lession_listview_head0, null);
        View lessionViewHead1 = inflater.inflate(R.layout.lession_listview_head1, null);

        lessionVoteView.addHeaderView(lessionViewHead0, null, false);
        lessionVoteView.addHeaderView(lessionViewHead1, null, false);

        voteListViewAdapter = new VoteListViewAdapter(this.getActivity(), inflater, lessionPresenter);
        lessionVoteView.setAdapter(voteListViewAdapter);

        // 页面头部悬浮效果
        lessionViewHeadFloat = view.findViewById(R.id.lessionListViewHeadFloat);

        // 求教程榜首，因为是放在listview的header中，必须放在listview初始化之后
        lessionBackground = view.findViewById(R.id.lessionBackground);
        candidateBackground = view.findViewById(R.id.candidateBackground);

        // 页头的按钮
        goVotingBtn = (TextView) lessionViewHead1.findViewById(R.id.goVotingBtn);
        goRankingBtn = (TextView) lessionViewHead1.findViewById(R.id.goRankingBtn);
        pageCursor = lessionViewHead1.findViewById(R.id.pageCursor);
        pageCursor.getLayoutParams().width = screenWidth / 2;


        // 悬浮页头的按钮
        goVotingBtnFloat = (TextView) lessionViewHeadFloat.findViewById(R.id.goVotingBtn);
        goRankingBtnFloat = (TextView) lessionViewHeadFloat.findViewById(R.id.goRankingBtn);
        pageCursorFloat = lessionViewHeadFloat.findViewById(R.id.pageCursor);
        pageCursorFloat.getLayoutParams().width = screenWidth / 2;

        // 倒计时
        lessionCountDownText = (TextView) view.findViewById(R.id.lessionCountDownText);
        lessionCountDownValue = (TextView) view.findViewById(R.id.lessionCountDownValue);

        // 求教程按钮
        requireLessionBtn = (Button) lessionViewHead1.findViewById(R.id.requireLessionBtn);
        requireLessionBtnFloat = (Button) lessionViewHeadFloat.findViewById(R.id.requireLessionBtn);
        requireLessionGrayBtn = (Button) lessionViewHead1.findViewById(R.id.requireLessionGrayBtn);
        requireLessionGrayBtnFloat = (Button) lessionViewHeadFloat.findViewById(R.id.requireLessionGrayBtn);

        // 下拉刷新
        lessionPullRefresh = (PtrClassicFrameLayout) view.findViewById(R.id.lessionPullRefresh);

        // title bar
        lessionTitleBar = (TitleBar) view.findViewById(R.id.lessionTitleBar);
        lessionTitleBar.getTitleView().setText(getResources().getString(R.string.require_lession));
        lessionTitleBar.getRightTextButton().setText(R.string.lession_collection);

    }

    private void initData() {

        lessionPresenter.setCacheDir(NailStarApplicationContext.getInstance().getFileDir());
        // 查询数据
        lessionPresenter.queryAndRefreshActivityTopic();
        lessionPresenter.initCandidateList();

    }

    private void bindControl() {

        // 悬浮头部截住点击事件，防止传到下面的listview
        lessionViewHeadFloat.setOnClickListener(null);

        // ListView头部悬浮效果
        lessionVoteView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // ListView头部悬浮效果：隐藏一个与头部一样的页面片段，当头部滚动消失的时候，设置该页面片段可见
                if (firstVisibleItem >= 1) {
                    lessionViewHeadFloat.setVisibility(View.VISIBLE);
                } else {
                    lessionViewHeadFloat.setVisibility(View.GONE);
                }
            }
        });

        lessionTitleBar.getRightTextButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryListAPI.getInstance().gotoCategoryListView(RequireLessionFragment.this.getActivity(), getResources().getString(R.string.lession_collection_title), QJC);
            }
        });

        // 点击页头的列表切换
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.goVotingBtn:
                        goVoting();
                        break;
                    case R.id.goRankingBtn:
                        goRanking();
                        break;
                    default:
                        break;
                }
            }

        };

        goVotingBtn.setSelected(true);
        goVotingBtnFloat.setSelected(true);

        goVotingBtn.setOnClickListener(onClickListener);
        goVotingBtnFloat.setOnClickListener(onClickListener);
        goRankingBtn.setOnClickListener(onClickListener);
        goRankingBtnFloat.setOnClickListener(onClickListener);

        takeImage = new TakeImage.Builder()
                .context(this)
                .aspectX(500)
                .aspectY(400)
                .outputX(500)
                .outputY(400)
                .uri(Constants.YILOS_NAILSTAR_PICTURE_PATH)
                .callback(new TakeImageCallback() {
                    @Override
                    public void callback(Uri uri) {
                        if (uri == null) {
                            showMessage(R.string.upload_image_failed);
                            return;
                        }
                        File uploadFile = new File(uri.getPath());
                        if (!uploadFile.exists()) {
                            showMessage(R.string.upload_image_failed);
                            return;
                        }
                        // 上传图片
                        lessionPresenter.postCandidate(uploadFile);
                    }
                }).build();

        View.OnClickListener requireLessionBtnListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 未登录不能求教程
                if (!LoginAPI.getInstance().isLogin()) {
                    gotoLoginPage();
                    return;
                }

                // 网络不可用的时候提示用户
                if (!NailStarApplicationContext.getInstance().isNetworkConnected()) {
                    showMessage(R.string.network_disconnected);
                }

                takeImage.initTakeImage();
            }
        };

        requireLessionBtn.setOnClickListener(requireLessionBtnListener);
        requireLessionBtnFloat.setOnClickListener(requireLessionBtnListener);

        // 下拉刷新绑定
        lessionPullRefresh.setLastUpdateTimeRelateObject(this);
        lessionPullRefresh.setPtrHandler(new PtrHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {

                // 刷新页头
                lessionPresenter.queryAndRefreshActivityTopic();

                // 刷新列表
                lessionPresenter.refreshCandidateList();

            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                // 滑动到顶部才允许刷新
                boolean canScrollUp = lessionVoteView.getChildCount() > 0 && (lessionVoteView.getFirstVisiblePosition() > 0
                        || lessionVoteView.getChildAt(0).getTop() < lessionVoteView.getPaddingTop());
                return !canScrollUp;
            }
        });
    }

    private void goVoting() {

        if (goVotingBtn.isSelected()) {
            return;
        }
        goVotingBtn.setSelected(true);
        goVotingBtnFloat.setSelected(true);
        goRankingBtn.setSelected(false);
        goRankingBtnFloat.setSelected(false);

        TranslateAnimation animation = new TranslateAnimation(screenWidth / 2, 0 , 0, 0);
        animation.setFillAfter(true);
        animation.setDuration(300);
        pageCursor.startAnimation(animation);
        pageCursorFloat.startAnimation(animation);

        // 切换的时候先用notifyDataSetInvalidated通知Adapter数据暂时不可用，数据准备好之后再调用notifyDataSetChanged通知数据可用
        voteListViewAdapter.notifyDataSetInvalidated();
        voteListViewAdapter.setViewType(VoteListViewAdapter.ViewType.VOTE_LIST);

        lessionPresenter.goVoteLessionList();

        // 切换的时候，保证页首显示的是返回的第一条数据
        if (lessionVoteView.getFirstVisiblePosition() > 1) {
            lessionVoteView.setSelection(1);
        }
    }

    private void goRanking() {

        if (goRankingBtn.isSelected()) {
            return;
        }
        goVotingBtn.setSelected(false);
        goVotingBtnFloat.setSelected(false);
        goRankingBtn.setSelected(true);
        goRankingBtnFloat.setSelected(true);

        TranslateAnimation animation = new TranslateAnimation(0, screenWidth / 2, 0, 0);
        animation.setFillAfter(true);
        animation.setDuration(300);
        pageCursor.startAnimation(animation);
        pageCursorFloat.startAnimation(animation);

        // 切换的时候先用notifyDataSetInvalidated通知Adapter数据暂时不可用，数据准备好之后再调用notifyDataSetChanged通知数据可用
        voteListViewAdapter.notifyDataSetInvalidated();
        voteListViewAdapter.setViewType(VoteListViewAdapter.ViewType.RANKING_LIST);

        lessionPresenter.goRankingLessionList();

        // 切换的时候，保证页首显示的是返回的第一条数据
        if (lessionVoteView.getFirstVisiblePosition() > 1) {
            lessionVoteView.setSelection(1);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        takeImage.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void refreshFailed() {
        // TODO
    }

    @Override
    public void refreshActivityTopic(LessionActivity lessionActivity) {

        if (lessionActivity.getStage() == 1) {
            handleLessionTopic(lessionActivity);
        } else if (lessionActivity.getStage() == 2) {
            handleCandidateTopic(lessionActivity);
        }
        // 列表需要获取当前阶段以决定是否显示投票按钮
        voteListViewAdapter.setStage(lessionActivity.getStage());

        // 通知下拉组件更新刷新时间
        lessionPullRefresh.refreshComplete();
    }


    // 求教程第一阶段（求教程阶段）
    private void handleLessionTopic(final LessionActivity lessionActivity) {

        requireLessionBtn.setVisibility(View.VISIBLE);
        requireLessionBtnFloat.setVisibility(View.VISIBLE);
        requireLessionGrayBtn.setVisibility(View.GONE);
        requireLessionGrayBtnFloat.setVisibility(View.GONE);

        // 设置图片
        ImageCacheView lessionPhoto = (ImageCacheView) view.findViewById(R.id.lessionPhoto);
        CircleImageView lessionAuthorPhoto = (CircleImageView) view.findViewById(R.id.lessionAuthorPhoto);

        lessionPhoto.setImageSrc(lessionActivity.getPrevious().getPicUrl());
        lessionAuthorPhoto.setImageSrc(lessionActivity.getPrevious().getAuthorPhoto());

        // 设置图片大小（按照屏幕大小的百分比计算）
        lessionAuthorPhoto.getLayoutParams().width = screenWidth / 18;
        lessionAuthorPhoto.getLayoutParams().height = screenWidth / 18;

        ImageView newVideoIcon = (ImageView) view.findViewById(R.id.newVideoIcon);
        newVideoIcon.getLayoutParams().width = screenWidth / 12;
        newVideoIcon.getLayoutParams().height = screenWidth / 12;
        ImageView playVideoIcon = (ImageView) view.findViewById(R.id.playVideoIcon);
        playVideoIcon.getLayoutParams().width = screenWidth / 18;
        playVideoIcon.getLayoutParams().height = screenWidth / 18;
        FrameLayout.LayoutParams playVideoIconLayoutLp = new FrameLayout.LayoutParams(playVideoIcon.getLayoutParams());
        playVideoIconLayoutLp.setMargins(0, 0, screenWidth / 50, screenWidth / 50);
        playVideoIconLayoutLp.gravity = Gravity.RIGHT | Gravity.BOTTOM;
        playVideoIcon.setLayoutParams(playVideoIconLayoutLp);

        // 设置文字
        TextView lessionTopic = (TextView) view.findViewById(R.id.lessionTopic);
        TextView lessionAuthorName = (TextView) view.findViewById(R.id.lessionAuthorName);
        lessionTopic.setText(lessionActivity.getPrevious().getTitle());
        lessionAuthorName.setText(lessionActivity.getPrevious().getAuthorName());
        lessionCountDownText.setText(getResources().getString(R.string.stage1_count_down));

        // 绑定播放按钮
        lessionPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TopicVideoPlayerActivity.class);
                intent.putExtra(Constants.TOPIC_ID, lessionActivity.getPrevious().getTopicId());
                startActivity(intent);
            }
        });

        // 显示现阶段的页头，隐藏其它阶段页头
        candidateBackground.setVisibility(View.GONE);
        lessionBackground.setVisibility(View.VISIBLE);

    }

    // 求教程第二阶段（视频制作阶段）
    private void handleCandidateTopic(LessionActivity lessionActivity) {

        requireLessionBtn.setVisibility(View.GONE);
        requireLessionBtnFloat.setVisibility(View.GONE);
        requireLessionGrayBtn.setVisibility(View.VISIBLE);
        requireLessionGrayBtnFloat.setVisibility(View.VISIBLE);

        ImageCacheView lessionCandidatePic = (ImageCacheView) view.findViewById(R.id.lessionCandidatePic);
        CircleImageView lessionUserPhoto = (CircleImageView) view.findViewById(R.id.lessionUserPhoto);

        TextView lessionUserName = (TextView) view.findViewById(R.id.lessionUserName);
        TextView lessionVoteCount = (TextView) view.findViewById(R.id.lessionVoteCount);

        lessionUserName.setText(lessionActivity.getCurrent().getAuthorName());
        lessionVoteCount.setText(String.valueOf(lessionActivity.getCurrent().getVoteCount()));

        lessionCandidatePic.setImageSrc(lessionActivity.getCurrent().getPicUrl());
        lessionUserPhoto.setImageSrc(lessionActivity.getCurrent().getAuthorPhoto());
        lessionCountDownText.setText(getResources().getString(R.string.stage2_count_down));

        //根据背景图片的比例设置头部高度
        candidateBackground.getLayoutParams().height = screenWidth / 3;

        ImageView crownImage = (ImageView) view.findViewById(R.id.crownImage);

        LinearLayout.LayoutParams crownImageLayoutLp = new LinearLayout.LayoutParams(screenWidth / 18, screenWidth / 18, 1);
        crownImageLayoutLp.setMargins(0, 0, 0, 0);
        crownImage.setLayoutParams(crownImageLayoutLp);

        LinearLayout.LayoutParams userPhotoLayoutLp = new LinearLayout.LayoutParams(screenWidth / 18, screenWidth / 18, 1);
        userPhotoLayoutLp.setMargins(0, -(screenWidth / 54), 0, 0);
        lessionUserPhoto.setLayoutParams(userPhotoLayoutLp);

        // 显示现阶段的页头，隐藏其它阶段页头
        lessionBackground.setVisibility(View.GONE);
        candidateBackground.setVisibility(View.VISIBLE);

    }

    @Override
    public void notifyRefreshListView() {

        voteListViewAdapter.notifyDataSetChanged();

    }

    @Override
    public void setVoteLession(List<CandidateLession> voteLessionList) {

        if (voteLessionList != null) {
            voteListViewAdapter.setVoteLessionList(new ArrayList<CandidateLession>(voteLessionList));
        }

    }

    @Override
    public void setRankingLession(List<CandidateLession> rankingLessionList) {

        if (rankingLessionList != null) {
            voteListViewAdapter.setRankingLessionList(new ArrayList<CandidateLession>(rankingLessionList));
        }

    }

    @Override
    public void refreshCountDown(String time) {
        lessionCountDownValue.setText(time);
    }

    @Override
    public void mediaRefresh(String filePath) {
        FileUtils.mediaRefresh(this.getActivity(), filePath);
    }

    @Override
    public void showMessage(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showMessage(@StringRes int resId) {
        Toast.makeText(getActivity(), resId, Toast.LENGTH_LONG).show();
    }

    @Override
    public void gotoLoginPage() {
        LoginAPI.getInstance().gotoLoginPage(RequireLessionFragment.this.getActivity());
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (voteListViewAdapter.isShowingImageActionDialog()) {
                voteListViewAdapter.dismissImageActionDialog();
                return true;
            }
        }
        return false;
    }
}
