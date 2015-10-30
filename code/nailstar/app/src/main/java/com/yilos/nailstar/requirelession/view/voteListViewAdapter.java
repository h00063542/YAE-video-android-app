package com.yilos.nailstar.requirelession.view;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yilos.nailstar.R;
import com.yilos.nailstar.requirelession.entity.CandidateLession;

import java.util.ArrayList;
import java.util.List;

/**
 * 求教程投票与排行榜ListView的Adapter
 */
public class VoteListViewAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;

    private ViewType viewType = ViewType.VOTE_LIST;

    private List<CandidateLession> voteLessionList;

    public VoteListViewAdapter(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
    }

    public void setViewType(ViewType viewType) {
        this.viewType = viewType;
    }

    public void setVoteLessionList(List<CandidateLession> voteLessionList) {

        this.voteLessionList = voteLessionList;

    }

    @Override
    public int getCount() {

        int count = 0;
        if (voteLessionList != null) {
            count = voteLessionList.size();
        }

        // 投票列表每行显示3个，所以需要除以3
        if (viewType.equals(ViewType.VOTE_LIST)) {
            count = (int) Math.ceil(count / 3);
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
        }
        if (viewType.equals(ViewType.VOTE_LIST)) {
            convertView = handleVoteList(position, convertView);
        }

        return convertView;
    }

    @NonNull
    private View handleRankingList(int position, View convertView) {

        ViewHolder holder;

        if (convertView == null) {

            convertView = layoutInflater.inflate(R.layout.lession_ranking_item, null);

            holder = new ViewHolder();
            holder.rankingItem.lessionRankingNo = (TextView) convertView.findViewById(R.id.lessionRankingNo);
            holder.rankingItem.lessionRankingImg = (ImageView) convertView.findViewById(R.id.lessionRankingImg);
            holder.rankingItem.lessionAuthorPhoto = (ImageView) convertView.findViewById(R.id.lessionAuthorPhoto);
            holder.rankingItem.lessionAuthorName = (TextView) convertView.findViewById(R.id.lessionAuthorName);
            holder.rankingItem.lessionVoteCount = (TextView) convertView.findViewById(R.id.lessionVoteCount);
            holder.rankingItem.lessionVotePic = (ImageView) convertView.findViewById(R.id.lessionVotePic);
            holder.rankingItem.lessionVote = (TextView) convertView.findViewById(R.id.lessionVote);
            holder.rankingItem.lessionCanvass = (Button) convertView.findViewById(R.id.lessionCanvass);

            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();

        }

        CandidateLession candidateLession = voteLessionList.get(position);

        holder.rankingItem.lessionRankingNo.setText(String.valueOf(position + 1));
        holder.rankingItem.lessionAuthorName.setText(candidateLession.getAuthorName());
        holder.rankingItem.lessionVoteCount.setText(String.valueOf(candidateLession.getVoteCount()));
        return convertView;
    }

    @NonNull
    private View handleVoteList(int position, View convertView) {

        ViewHolder holder;

        if (convertView == null) {

            convertView = layoutInflater.inflate(R.layout.lession_ranking_item, null);

            holder = new ViewHolder();

            VoteItem voteItem = new VoteItem();
            voteItem.voteItem = convertView.findViewById(R.id.voteItem0);
            voteItem.lessionVoteImg = (ImageView) convertView.findViewById(R.id.lessionVoteImg0);
            voteItem.lessionVotePic = (ImageView) convertView.findViewById(R.id.lessionVotePic0);
            voteItem.lessionvoteCount = (TextView) convertView.findViewById(R.id.lessionvoteCount0);
            voteItem.lessionTime = (TextView) convertView.findViewById(R.id.lessionTime0);
            holder.voteItemList.add(voteItem);

            voteItem = new VoteItem();
            voteItem.voteItem = convertView.findViewById(R.id.voteItem1);
            voteItem.lessionVoteImg = (ImageView) convertView.findViewById(R.id.lessionVoteImg1);
            voteItem.lessionVotePic = (ImageView) convertView.findViewById(R.id.lessionVotePic1);
            voteItem.lessionvoteCount = (TextView) convertView.findViewById(R.id.lessionvoteCount1);
            voteItem.lessionTime = (TextView) convertView.findViewById(R.id.lessionTime1);
            holder.voteItemList.add(voteItem);

            voteItem = new VoteItem();
            voteItem.voteItem = convertView.findViewById(R.id.voteItem2);
            voteItem.lessionVoteImg = (ImageView) convertView.findViewById(R.id.lessionVoteImg2);
            voteItem.lessionVotePic = (ImageView) convertView.findViewById(R.id.lessionVotePic2);
            voteItem.lessionvoteCount = (TextView) convertView.findViewById(R.id.lessionvoteCount2);
            voteItem.lessionTime = (TextView) convertView.findViewById(R.id.lessionTime2);
            holder.voteItemList.add(voteItem);


        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        for (int i = 0; i < 3; i++) {

        }

        return convertView;
    }

    class ViewHolder {

        public RankingItem rankingItem;

        public List<VoteItem> voteItemList;

        public ViewHolder () {
            this.rankingItem = new RankingItem();
            this.voteItemList = new ArrayList<>(3);
        }
    }

    class RankingItem {

        // 排行榜的页面组件

        public TextView lessionRankingNo;

        public ImageView lessionRankingImg;

        public ImageView lessionAuthorPhoto;

        public TextView lessionAuthorName;

        public TextView lessionVoteCount;

        public ImageView lessionVotePic;

        public TextView lessionVote;

        public Button lessionCanvass;
    }

    class VoteItem {

        // 投票页的页面组件

        public View voteItem;

        public ImageView lessionVoteImg;

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
