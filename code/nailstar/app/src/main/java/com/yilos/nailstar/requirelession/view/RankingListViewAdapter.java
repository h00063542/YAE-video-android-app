package com.yilos.nailstar.requirelession.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yilos.nailstar.requirelession.entity.CandidateLession;

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
        return rankingLessionList.size();
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
        return null;
    }

    public void setRankingLessionList(List<CandidateLession> rankingLessionList) {
        this.rankingLessionList = rankingLessionList;
    }

    class ViewHolder {

    }
}
