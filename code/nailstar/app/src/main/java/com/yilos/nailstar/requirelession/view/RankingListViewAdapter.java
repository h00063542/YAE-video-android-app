package com.yilos.nailstar.requirelession.view;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yilos.nailstar.R;
import com.yilos.nailstar.requirelession.entity.CandidateLession;
import com.yilos.widget.view.ImageCacheView;

import java.util.List;

/**
 * 求教程排行榜ListView的Adapter
 */
public class RankingListViewAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;

    private List<CandidateLession> rankingLessionList;

    public RankingListViewAdapter(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (rankingLessionList != null) {
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

        ViewHolder holder;

        CandidateLession candidateLession = rankingLessionList.get(position);

        if (convertView == null) {

            convertView = layoutInflater.inflate(R.layout.lession_ranking_item, null);

            holder = new ViewHolder();
            holder.lessionRankingNo = (TextView) convertView.findViewById(R.id.lessionRankingNo);
            holder.lessionRankingImg = (ImageCacheView) convertView.findViewById(R.id.lessionRankingImg);
            holder.lessionAuthorPhoto = (ImageView) convertView.findViewById(R.id.lessionAuthorPhoto);
            holder.lessionAuthorName = (TextView) convertView.findViewById(R.id.lessionAuthorName);
            holder.lessionvoteCount = (TextView) convertView.findViewById(R.id.lessionvoteCount);
            holder.lessionVotePic = (ImageView) convertView.findViewById(R.id.lessionVotePic);
            holder.lessionVote = (TextView) convertView.findViewById(R.id.lessionVote);
            holder.lessionCanvass = (Button) convertView.findViewById(R.id.lessionCanvass);

            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();

        }

        holder.lessionRankingNo.setText(String.valueOf(position + 1));
        holder.lessionRankingImg.setImageSrc(candidateLession.getPicUrl());
        holder.lessionAuthorName.setText(candidateLession.getAuthorName());
        holder.lessionvoteCount.setText(String.valueOf(candidateLession.getVoteCount()));
        if (candidateLession.getVoted() == 1) {

        }

        return convertView;
    }

    public void setRankingLessionList(List<CandidateLession> rankingLessionList) {

        this.rankingLessionList = rankingLessionList;

    }

    class ViewHolder {

        public TextView lessionRankingNo;

        public ImageCacheView lessionRankingImg;

        public ImageView lessionAuthorPhoto;

        public TextView lessionAuthorName;

        public TextView lessionvoteCount;

        public ImageView lessionVotePic;

        public TextView lessionVote;

        public Button lessionCanvass;

    }
}
