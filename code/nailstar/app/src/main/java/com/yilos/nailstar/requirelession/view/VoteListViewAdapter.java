package com.yilos.nailstar.requirelession.view;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yilos.nailstar.R;
import com.yilos.nailstar.requirelession.Presenter.LessionPresenter;
import com.yilos.nailstar.requirelession.entity.CandidateLession;
import com.yilos.widget.circleimageview.CircleImageView;
import com.yilos.widget.view.ImageCacheView;

import java.util.ArrayList;
import java.util.List;

/**
 * 求教程投票与排行榜ListView的Adapter
 */
public class VoteListViewAdapter extends BaseAdapter {

    private Activity context;
    private LayoutInflater layoutInflater;
    private LessionPresenter lessionPresenter;

    private ViewType viewType = ViewType.VOTE_LIST;

    private List<CandidateLession> voteLessionList;

    private int stage;

    private int screenWidth;

    public VoteListViewAdapter(Activity context, LayoutInflater layoutInflater, LessionPresenter lessionPresenter) {
        this.context = context;
        this.layoutInflater = layoutInflater;
        this.lessionPresenter = lessionPresenter;

        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        screenWidth = metric.widthPixels;
    }

    public void setViewType(ViewType viewType) {
        this.viewType = viewType;
    }

    public void setVoteLessionList(List<CandidateLession> voteLessionList) {

        this.voteLessionList = voteLessionList;

    }

    public void setStage(int stage) {
        this.stage = stage;
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
        if (voteLessionList != null) {
            count = voteLessionList.size();
        }

        // 投票列表每行显示3个，所以需要除以3
        if (viewType.equals(ViewType.VOTE_LIST)) {
            count = (int) Math.ceil((double) count / 3);
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
    private View handleRankingList(int position, View convertView) {

        ViewHolder holder;

        if (convertView == null || !((ViewHolder) convertView.getTag()).viewType.equals(ViewType.RANKING_LIST)) {

            convertView = layoutInflater.inflate(R.layout.lession_ranking_item, null);

            holder = new ViewHolder();
            holder.viewType = ViewType.RANKING_LIST;
            holder.rankingItem.lessionRankingNo = (TextView) convertView.findViewById(R.id.lessionRankingNo);
            holder.rankingItem.lessionRankingImg = (ImageCacheView) convertView.findViewById(R.id.lessionRankingImg);
            holder.rankingItem.lessionAuthorPhoto = (CircleImageView) convertView.findViewById(R.id.lessionAuthorPhoto);
            holder.rankingItem.lessionAuthorName = (TextView) convertView.findViewById(R.id.lessionAuthorName);
            holder.rankingItem.lessionVoteCount = (TextView) convertView.findViewById(R.id.lessionVoteCount);
            holder.rankingItem.lessionVotePic = (ImageView) convertView.findViewById(R.id.lessionVotePic);
            holder.rankingItem.lessionVote = (TextView) convertView.findViewById(R.id.lessionVote);
            holder.rankingItem.lessionCanvass = (Button) convertView.findViewById(R.id.lessionCanvass);

            // 设置头像大小
            holder.rankingItem.lessionAuthorPhoto.getLayoutParams().width = screenWidth / 18;
            holder.rankingItem.lessionAuthorPhoto.getLayoutParams().height = screenWidth / 18;

            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();

        }

        final CandidateLession candidateLession = voteLessionList.get(position);

        holder.rankingItem.lessionRankingNo.setText(String.valueOf(position + 1));
        holder.rankingItem.lessionAuthorName.setText(candidateLession.getAuthorName());
        holder.rankingItem.lessionVoteCount.setText(String.valueOf(candidateLession.getVoteCount()));
        holder.rankingItem.lessionRankingImg.setImageSrc(candidateLession.getPicUrl());
        holder.rankingItem.lessionAuthorPhoto.setImageSrc(candidateLession.getAuthorPhoto());

        // 是否已投票
        if (candidateLession.getVoted() > 0) {

            holder.rankingItem.lessionVotePic.setImageResource(R.mipmap.voted);
            holder.rankingItem.lessionVote.setText(R.string.voted);

        } else {

            View.OnClickListener voteBtnListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (candidateLession.getVoted() == 0) {
                        lessionPresenter.vote(candidateLession);
                    }
                }
            };

            holder.rankingItem.lessionVotePic.setOnClickListener(voteBtnListener);
            holder.rankingItem.lessionVote.setOnClickListener(voteBtnListener);

            holder.rankingItem.lessionVotePic.setImageResource(R.mipmap.vote_black);
            holder.rankingItem.lessionVote.setText(R.string.vote);
        }

        // 当前阶段是否能投票
        if (canVote(stage)) {
            holder.rankingItem.lessionVotePic.setVisibility(View.VISIBLE);
            holder.rankingItem.lessionVote.setVisibility(View.VISIBLE);
            holder.rankingItem.lessionCanvass.setVisibility(View.VISIBLE);
        } else {
            holder.rankingItem.lessionVotePic.setVisibility(View.GONE);
            holder.rankingItem.lessionVote.setVisibility(View.GONE);
            holder.rankingItem.lessionCanvass.setVisibility(View.GONE);
        }

        return convertView;
    }

    @NonNull
    private View handleVoteList(int position, View convertView) {

        ViewHolder holder;

        Long now = System.currentTimeMillis();

        if (convertView == null || !((ViewHolder) convertView.getTag()).viewType.equals(ViewType.VOTE_LIST)) {

            convertView = layoutInflater.inflate(R.layout.lession_vote_item, null);

            holder = new ViewHolder();
            holder.viewType = ViewType.VOTE_LIST;
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

            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();

        }

        for (int i = 0; i < 3; i++) {

            VoteItem voteItem = holder.voteItemList.get(i);

            if (position * 3 + i < voteLessionList.size()) {

                voteItem.voteItem.setVisibility(View.VISIBLE);

                CandidateLession candidateLession = voteLessionList.get(position * 3 + i);

                voteItem.lessionvoteCount.setText(String.valueOf(candidateLession.getVoteCount()));
                voteItem.lessionVoteImg.setImageSrc(candidateLession.getPicUrl());

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

    class ViewHolder {

        public ViewType viewType;

        public RankingItem rankingItem;

        public List<VoteItem> voteItemList;

        public ViewHolder() {
            this.rankingItem = new RankingItem();
            this.voteItemList = new ArrayList<>(3);
        }
    }

    class RankingItem {

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
