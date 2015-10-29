package com.yilos.nailstar.requirelession.view;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.yilos.nailstar.R;
import com.yilos.nailstar.requirelession.Presenter.LessionPresenter;
import com.yilos.nailstar.requirelession.entity.CandidateLession;
import com.yilos.nailstar.requirelession.entity.LessionActivity;


import java.util.List;

/**
 * 求教程Fragment
 */
public class RequireLessionFragment extends Fragment implements LessionView {

    View view;
    View lessionViewHead0;
    View lessionViewHead1;
    View lessionViewHeadFloat;

    private LessionPresenter lessionPresenter = new LessionPresenter(this);

//    private static final String YILOS_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/yilos";
//
//    private TakeImage takeImage;

    private ListView lessionRankingView;

    private RankingListViewAdapter rankingListViewAdapter;

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
        view = inflater.inflate(R.layout.fragment_require_lession, container, false);
        lessionViewHead0 = inflater.inflate(R.layout.lession_listview_head0, null);
        lessionViewHead1 = inflater.inflate(R.layout.lession_listview_head1, null);
        initView(inflater);
        return view;
    }

    private void bindControl(View view) {
//        takeImage = new TakeImage.Builder().context(this).uri(YILOS_PATH).callback(new TakeImageCallback() {
//            @Override
//            public void callback(Uri uri) {
//                Log.d(RequireLessionFragment.class.getName(), "callback " + uri);
//            }
//        }).build();
//        Button button = (Button) view.findViewById(R.id.hand_in_homework_btn);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                takeImage.initTakeImage();
//            }
//        });
    }

    private void initView(LayoutInflater inflater) {

        lessionRankingView = (ListView)view.findViewById(R.id.lessionRankingView);
        lessionViewHeadFloat = view.findViewById(R.id.lessionListViewHead1);

        lessionRankingView.addHeaderView(lessionViewHead0, null, false);
        lessionRankingView.addHeaderView(lessionViewHead1, null, false);

        rankingListViewAdapter = new RankingListViewAdapter(inflater);
        lessionRankingView.setAdapter(rankingListViewAdapter);

        lessionRankingView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // ListView头部浮动效果：隐藏一个与头部一样的页面片段，当头部滚动消失的时候，设置该页面片段可见
                if (firstVisibleItem >= 1) {
                    lessionViewHeadFloat.setVisibility(View.VISIBLE);
                } else {
                    lessionViewHeadFloat.setVisibility(View.GONE);
                }
            }
        });

        lessionPresenter.queryActivityTopic();
        lessionPresenter.queryRankingLession();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        takeImage.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void refreshFailed() {
    }

    @Override
    public void refreshActivityTopic(LessionActivity lessionActivity) {
    }

    @Override
    public void refreshVoteLession(List<CandidateLession> voteLessionList) {

    }

    @Override
    public void refreshRankingLession(List<CandidateLession> rankingLessionList) {
        rankingListViewAdapter.setRankingLessionList(rankingLessionList);
        rankingListViewAdapter.notifyDataSetChanged();
    }

}
