package com.yilos.nailstar.requirelession.view;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yilos.nailstar.R;
import com.yilos.nailstar.requirelession.Presenter.LessionPresenter;
import com.yilos.nailstar.requirelession.entity.CandidateLession;
import com.yilos.nailstar.social.model.SocialAPI;
import com.yilos.nailstar.util.Constants;
import com.yilos.widget.circleimageview.CircleImageView;
import com.yilos.widget.view.ImageCacheView;

import java.util.ArrayList;
import java.util.List;

/**
 * 求教程投票与排行榜ListView的Adapter
 */
public class VoteListViewAdapter extends BaseAdapter {

    private static final String shareUrl = "http://s.naildaka.com/site/share_candidate.html?candidateId=";

    private Activity context;
    private LayoutInflater layoutInflater;
    private LessionPresenter lessionPresenter;

    // 当前显示的页面（投票页或者排行榜）
    private ViewType viewType = ViewType.VOTE_LIST;

    // 投票页的数据
    private List<CandidateLession> voteLessionList;

    // 排行榜的数据
    private List<CandidateLession> rankingLessionList;

    // 当前活动阶段（求教程阶段或者视频制作阶段）
    private int stage;

    // 屏幕宽度
    private int screenWidth;

    private ViewGroup decorView;

    // 点击显示大图的view
    private View lessionImageView;

    // 显示大图时的ViewPager
    private ViewPager lessionImageViewPager;

    private LargeImagePagerAdapter pagerAdapter;

    // 显示大图时候的投票按钮
    private TextView lessionVoteBtn;

    // 显示大图时的拉票按钮
    private TextView lessionCanvassBtn;

    // 显示大图时的保存到相册按钮
    private TextView saveImageBtn;

    // 显示大图时的举报按钮
    private TextView reportIllegalBtn;

    // 显示大图时的取消按钮
    private TextView cancelBtn;

    // 当前大图对应的数据
    private CandidateLession currentImage;

    private Animation slideInBottom;

    private Animation slideOutBottom;

    // 弹出框正在消失
    private boolean isDismissing;

    public VoteListViewAdapter(Activity context, LayoutInflater layoutInflater, LessionPresenter lessionPresenter) {
        this.context = context;
        this.layoutInflater = layoutInflater;
        this.lessionPresenter = lessionPresenter;

        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        screenWidth = metric.widthPixels;

        isDismissing = false;

        // 点击图片的时候弹出大图
        initLessionImageView();
        // 绑定按钮
        bindLessionImageBtn();
    }

    private void initLessionImageView() {

        slideInBottom = AnimationUtils.loadAnimation(context, R.anim.slide_in_bottom);
        slideOutBottom = AnimationUtils.loadAnimation(context, R.anim.slide_out_bottom);

        lessionImageView = layoutInflater.inflate(R.layout.lession_image_action, null);
        lessionImageView.setBackgroundColor(0xa0000000);

        decorView = (ViewGroup) context.getWindow().getDecorView().findViewById(android.R.id.content);

        // 点击空白区域，弹框消失
        lessionImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissImageActionDialog();
            }
        });

        // ViewPager实现图片左右滑动
        lessionImageViewPager = (ViewPager) lessionImageView.findViewById(R.id.lessionImageViewPager);

        pagerAdapter = new LargeImagePagerAdapter(layoutInflater);

        lessionImageViewPager.setAdapter(pagerAdapter);

        lessionImageViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (viewType.equals(ViewType.VOTE_LIST)) {
                    currentImage = voteLessionList.get(position);
                } else if (viewType.equals(ViewType.RANKING_LIST)) {
                    currentImage = rankingLessionList.get(position);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // 显示大图时候显示的按钮
        lessionVoteBtn = (TextView) lessionImageView.findViewById(R.id.lessionVoteBtn);
        lessionCanvassBtn = (TextView) lessionImageView.findViewById(R.id.lessionCanvassBtn);
        saveImageBtn = (TextView) lessionImageView.findViewById(R.id.saveImageBtn);
        reportIllegalBtn = (TextView) lessionImageView.findViewById(R.id.reportIllegalBtn);
        cancelBtn = (TextView) lessionImageView.findViewById(R.id.cancelBtn);

    }

    // 绑定大图下方的按钮
    private void bindLessionImageBtn() {

        // 取消按钮
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissImageActionDialog();
            }
        });

        // 保存图片
        saveImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissImageActionDialog();
                lessionPresenter.saveImage(currentImage.getPicUrl(), Constants.YILOS_NAILSTAR_PICTURE_PATH, currentImage.getCandidateId() + ".jpg");
            }
        });

        // 举报按钮
        reportIllegalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissImageActionDialog();
                lessionPresenter.reportIllegal(currentImage);
            }
        });

        // 投票按钮
        lessionVoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissImageActionDialog();
                lessionPresenter.vote(currentImage);
            }
        });

        lessionCanvassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SocialAPI.getInstance().share(context,
                        context.getResources().getString(R.string.lession_share_title),
                        context.getResources().getString(R.string.lession_share_context),
                        shareUrl + currentImage.getCandidateId(),
                        currentImage.getPicUrl());
            }
        });
    }

    public void setViewType(ViewType viewType) {
        this.viewType = viewType;
    }

    public void setVoteLessionList(List<CandidateLession> voteLessionList) {

        this.voteLessionList = voteLessionList;

    }


    public void setRankingLessionList(List<CandidateLession> rankingLessionList) {

        this.rankingLessionList = rankingLessionList;

    }

    public void setStage(int stage) {

        this.stage = stage;

        if (stage == 2) {
            lessionVoteBtn.setVisibility(View.GONE);
            lessionCanvassBtn.setVisibility(View.GONE);
            reportIllegalBtn.setVisibility(View.GONE);
        } else if (stage == 1) {
            lessionVoteBtn.setVisibility(View.VISIBLE);
            lessionCanvassBtn.setVisibility(View.VISIBLE);
            reportIllegalBtn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean areAllItemsEnabled() {
        // 所有的item不可点击
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        // 所有的item不可点击
        return false;
    }

    @Override
    public int getCount() {

        int count = 0;
        if (viewType.equals(ViewType.VOTE_LIST) && voteLessionList != null) {
            // 投票列表每行显示3个，所以需要除以3
            count = (int) Math.ceil((double) voteLessionList.size() / 3);
        } else if (viewType.equals(ViewType.RANKING_LIST) && rankingLessionList != null) {
            count = rankingLessionList.size();
        }

        return count;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (position >= getCount()) {
            return null;
        }

        if (viewType.equals(ViewType.RANKING_LIST)) {

            convertView = handleRankingList(position, convertView);

        } else if (viewType.equals(ViewType.VOTE_LIST)) {

            convertView = handleVoteList(position, convertView);

        }

        return convertView;
    }

    private boolean canVote(int stage) {
        return stage == 1;
    }

    @NonNull
    private View handleRankingList(final int position, View convertView) {

        RankingViewHolder holder;

        if (convertView == null || convertView.getTag(R.id.lession_ranking_type) == null) {

            convertView = layoutInflater.inflate(R.layout.lession_ranking_item, null);

            holder = new RankingViewHolder();
            holder.lessionRankingNo = (TextView) convertView.findViewById(R.id.lessionRankingNo);
            holder.lessionRankingImg = (ImageCacheView) convertView.findViewById(R.id.lessionRankingImg);
            holder.lessionAuthorPhoto = (CircleImageView) convertView.findViewById(R.id.lessionAuthorPhoto);
            holder.lessionAuthorName = (TextView) convertView.findViewById(R.id.lessionAuthorName);
            holder.lessionVoteCount = (TextView) convertView.findViewById(R.id.lessionVoteCount);
            holder.lessionVotePic = (ImageView) convertView.findViewById(R.id.lessionVotePic);
            holder.lessionVote = (TextView) convertView.findViewById(R.id.lessionVote);
            holder.lessionCanvass = (Button) convertView.findViewById(R.id.lessionCanvass);

            // 图片高度为宽度的4/5，宽度在xml中设置为屏幕宽度的3/10。
            holder.lessionRankingImg.getLayoutParams().height = screenWidth * 3 / 10 * 4 / 5;

            // 设置头像大小
            holder.lessionAuthorPhoto.getLayoutParams().width = screenWidth / 18;
            holder.lessionAuthorPhoto.getLayoutParams().height = screenWidth / 18;

            convertView.setTag(R.id.lession_ranking_type, holder);

        } else {

            holder = (RankingViewHolder) convertView.getTag(R.id.lession_ranking_type);

        }

        final CandidateLession candidateLession = rankingLessionList.get(position);

        holder.lessionRankingNo.setText(String.valueOf(position + 1));
        holder.lessionAuthorName.setText(candidateLession.getAuthorName());
        holder.lessionVoteCount.setText(String.valueOf(candidateLession.getVoteCount()));

        if (candidateLession.getPicUrl() != null && !"".equals(candidateLession.getPicUrl())) {
            holder.lessionRankingImg.setImageSrc(candidateLession.getPicUrl());
        } else {
            holder.lessionRankingImg.setImageSrc("");
            holder.lessionRankingImg.setImageResource(R.mipmap.ic_default_image);
        }

        if (candidateLession.getAuthorPhoto() != null && !"".equals(candidateLession.getAuthorPhoto())) {
            holder.lessionAuthorPhoto.setImageSrc(candidateLession.getAuthorPhoto());
        } else {
            holder.lessionAuthorPhoto.setImageSrc("");
            holder.lessionAuthorPhoto.setImageResource(R.mipmap.ic_default_photo);
        }

        // 点击图片显示大图
        holder.lessionRankingImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击的时候记录当前打开的图，以便保存或者举报的时候用
                currentImage = candidateLession;
                // 显示大图
                pagerAdapter.setCandidateList(rankingLessionList);
                showImageActionDialog(position);
            }
        });

        // 拉票按钮
        holder.lessionCanvass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SocialAPI.getInstance().share(context,
                        context.getResources().getString(R.string.lession_share_title),
                        context.getResources().getString(R.string.lession_share_context),
                        shareUrl + candidateLession.getCandidateId(),
                        candidateLession.getPicUrl());
            }
        });

        // 投票按钮
        View.OnClickListener voteBtnListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (candidateLession.getVoted() == 0) {
                    lessionPresenter.vote(candidateLession);
                }
            }
        };

        holder.lessionVotePic.setOnClickListener(voteBtnListener);
        holder.lessionVote.setOnClickListener(voteBtnListener);

        // 是否已投票
        if (candidateLession.getVoted() > 0) {

            holder.lessionVotePic.setImageResource(R.mipmap.voted);
            holder.lessionVote.setText(R.string.voted);
            holder.lessionVote.setTextColor(context.getResources().getColor(R.color.orange));

        } else {

            holder.lessionVotePic.setImageResource(R.mipmap.vote_black);
            holder.lessionVote.setText(R.string.vote);
            holder.lessionVote.setTextColor(context.getResources().getColor(R.color.z3));
        }

        // 当前阶段是否能投票
        if (canVote(stage)) {
            holder.lessionVotePic.setVisibility(View.VISIBLE);
            holder.lessionVote.setVisibility(View.VISIBLE);
            holder.lessionCanvass.setVisibility(View.VISIBLE);
        } else {
            holder.lessionVotePic.setVisibility(View.GONE);
            holder.lessionVote.setVisibility(View.GONE);
            holder.lessionCanvass.setVisibility(View.GONE);
        }

        return convertView;
    }

    @NonNull
    private View handleVoteList(final int position, View convertView) {

        VoteItemViewHolder holder;

        Long now = System.currentTimeMillis();

        if (convertView == null || convertView.getTag(R.id.lession_vote_type) == null) {

            convertView = layoutInflater.inflate(R.layout.lession_vote_item, null);

            holder = new VoteItemViewHolder();
            VoteItem voteItem = new VoteItem();
            voteItem.voteItem = convertView.findViewById(R.id.voteItem0);
            voteItem.lessionVoteImg = (ImageCacheView) convertView.findViewById(R.id.lessionVoteImg0);
            voteItem.lessionVotePic = (ImageView) convertView.findViewById(R.id.lessionVotePic0);
            voteItem.lessionvoteCount = (TextView) convertView.findViewById(R.id.lessionvoteCount0);
            voteItem.lessionTime = (TextView) convertView.findViewById(R.id.lessionTime0);
            holder.voteItemList.add(voteItem);

            voteItem = new VoteItem();
            voteItem.voteItem = convertView.findViewById(R.id.voteItem1);
            voteItem.lessionVoteImg = (ImageCacheView) convertView.findViewById(R.id.lessionVoteImg1);
            voteItem.lessionVotePic = (ImageView) convertView.findViewById(R.id.lessionVotePic1);
            voteItem.lessionvoteCount = (TextView) convertView.findViewById(R.id.lessionvoteCount1);
            voteItem.lessionTime = (TextView) convertView.findViewById(R.id.lessionTime1);
            holder.voteItemList.add(voteItem);

            voteItem = new VoteItem();
            voteItem.voteItem = convertView.findViewById(R.id.voteItem2);
            voteItem.lessionVoteImg = (ImageCacheView) convertView.findViewById(R.id.lessionVoteImg2);
            voteItem.lessionVotePic = (ImageView) convertView.findViewById(R.id.lessionVotePic2);
            voteItem.lessionvoteCount = (TextView) convertView.findViewById(R.id.lessionvoteCount2);
            voteItem.lessionTime = (TextView) convertView.findViewById(R.id.lessionTime2);
            holder.voteItemList.add(voteItem);

            // 设置高度为宽度的 4/5
            for (int i = 0; i < 3; i++) {
                LinearLayout.LayoutParams laParams = new LinearLayout.LayoutParams(holder.voteItemList.get(i).lessionVoteImg.getLayoutParams());
                laParams.height = screenWidth / 3 * 4 / 5;
                holder.voteItemList.get(i).lessionVoteImg.setLayoutParams(laParams);
            }

            convertView.setTag(R.id.lession_vote_type, holder);

        } else {

            holder = (VoteItemViewHolder) convertView.getTag(R.id.lession_vote_type);

        }

        for (int i = 0; i < 3; i++) {

            VoteItem voteItem = holder.voteItemList.get(i);

            final int realPosition = position * 3 + i;
            if (realPosition < voteLessionList.size()) {

                voteItem.voteItem.setVisibility(View.VISIBLE);

                CandidateLession candidateLession = voteLessionList.get(realPosition);

                voteItem.lessionvoteCount.setText(String.valueOf(candidateLession.getVoteCount()));
                if (candidateLession.getPicUrl() != null && !"".equals(candidateLession.getPicUrl())) {
                    voteItem.lessionVoteImg.setImageSrc(candidateLession.getPicUrl());
                } else {
                    voteItem.lessionVoteImg.setImageSrc("");
                    voteItem.lessionVoteImg.setImageResource(R.mipmap.ic_default_image);
                }

                View.OnClickListener onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 点击的时候记录当前打开的图，以便保存或者举报的时候用
                        currentImage = voteLessionList.get(realPosition);
                        // 显示大图
                        pagerAdapter.setCandidateList(voteLessionList);
                        showImageActionDialog(realPosition);
                    }
                };
                // 点击图片显示大图
                voteItem.lessionVoteImg.setOnClickListener(onClickListener);
                // 点击投票按钮也显示大图
                voteItem.lessionVotePic.setOnClickListener(onClickListener);

                String lessionTime = "";
                if ((now - candidateLession.getCreateDate()) / 1000 < 60) {
                    lessionTime = context.getResources().getString(R.string.just_now);
                } else if ((now - candidateLession.getCreateDate()) / 1000 < 60 * 60) {
                    lessionTime = String.valueOf((int) Math.floor((now - candidateLession.getCreateDate()) / (60 * 1000)));
                    lessionTime += context.getResources().getString(R.string.minute);
                    lessionTime += context.getResources().getString(R.string.before);
                } else if ((now - candidateLession.getCreateDate()) / 1000 < 60 * 60 * 24) {
                    lessionTime = String.valueOf((int) Math.floor((now - candidateLession.getCreateDate()) / (60 * 60 * 1000)));
                    lessionTime += context.getResources().getString(R.string.hour);
                    lessionTime += context.getResources().getString(R.string.before);
                } else {
                    lessionTime = String.valueOf((int) Math.floor((now - candidateLession.getCreateDate()) / (24 * 60 * 60 * 1000)));
                    lessionTime += context.getResources().getString(R.string.day);
                    lessionTime += context.getResources().getString(R.string.before);
                }
                voteItem.lessionTime.setText(lessionTime);

                // 是否已投票
                if (candidateLession.getVoted() > 0) {

                    voteItem.lessionVotePic.setImageResource(R.mipmap.voted);

                } else {

                    voteItem.lessionVotePic.setImageResource(R.mipmap.vote_black);

                }

            } else {

                voteItem.voteItem.setVisibility(View.INVISIBLE);

            }
        }

        return convertView;
    }

    private void showImageActionDialog(int position) {

        if (isShowingImageActionDialog()) {
            return;
        }

        pagerAdapter.notifyDataSetChanged();
        lessionImageViewPager.setCurrentItem(position);
        decorView.addView(lessionImageView);

        lessionImageView.startAnimation(slideInBottom);

    }

    public void dismissImageActionDialog() {

        if (isDismissing) {
            return;
        }

        //消失动画
        slideOutBottom.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                decorView.post(new Runnable() {
                    @Override
                    public void run() {
                        decorView.removeView(lessionImageView);
                        isDismissing = false;
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        isDismissing = true;
        lessionImageView.startAnimation(slideOutBottom);

    }

    public boolean isShowingImageActionDialog() {
        return isShowing(R.id.lessionImageContainer);
    }

    private boolean isShowing(@IdRes int id) {
        View view = decorView.findViewById(id);
        return view != null;
    }

    class VoteItemViewHolder {

        public List<VoteItem> voteItemList;

        public VoteItemViewHolder() {

            this.voteItemList = new ArrayList<>(3);
        }
    }

    class RankingViewHolder {

        // 排行榜的页面组件

        public TextView lessionRankingNo;

        public ImageCacheView lessionRankingImg;

        public CircleImageView lessionAuthorPhoto;

        public TextView lessionAuthorName;

        public TextView lessionVoteCount;

        public ImageView lessionVotePic;

        public TextView lessionVote;

        public Button lessionCanvass;
    }

    class VoteItem {

        // 投票页的页面组件

        public View voteItem;

        public ImageCacheView lessionVoteImg;

        public ImageView lessionVotePic;

        public TextView lessionvoteCount;

        public TextView lessionTime;

    }

    public enum ViewType {

        VOTE_LIST(1),
        RANKING_LIST(2);

        private int value;

        ViewType(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(this.value);
        }
    }
}
