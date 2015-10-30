package com.yilos.nailstar.requirelession.view;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.yilos.nailstar.R;
import com.yilos.nailstar.requirelession.Presenter.LessionPresenter;
import com.yilos.nailstar.requirelession.entity.CandidateLession;
import com.yilos.nailstar.requirelession.entity.LessionActivity;


import java.util.List;

/**
 * 求教程Fragment
 */
public class RequireLessionFragment extends Fragment implements LessionView {

    // 求教程页面
    private View view;

    // 页面头部（求教程榜首）
    private View lessionViewHead0;

    // 页面头部（求教程按钮）
    private View lessionViewHead1;

    // 页面悬浮部分（求教程按钮）
    private View lessionViewHeadFloat;

    // 候选款式列表
    private ListView lessionVoteView;

    // 投票候选列表Adapter
    private VoteListViewAdapter voteListViewAdapter;

    // 单选按钮group
    private RadioGroup switchLessionView;

    // 在悬浮头中的单选按钮group
    private RadioGroup switchLessionViewFloat;

    // 求教程按钮
    private Button requireLessionBtn;

    // 投票列表按钮
    private Button goVotingBtn;

    // 排行榜按钮
    private Button goRankingBtn;


    private LessionPresenter lessionPresenter = new LessionPresenter(this);

//    private static final String YILOS_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/yilos";
//
//    private TakeImage takeImage;

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
        initData();
        return view;
    }

    private void bindControl() {
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

        // 初始化ListView
        lessionVoteView = (ListView)view.findViewById(R.id.lessionVoteView);

        lessionVoteView.addHeaderView(lessionViewHead0, null, false);
        lessionVoteView.addHeaderView(lessionViewHead1, null, false);

        voteListViewAdapter = new VoteListViewAdapter(inflater);
        lessionVoteView.setAdapter(voteListViewAdapter);

        // 页面头部悬浮效果
        lessionViewHeadFloat = view.findViewById(R.id.lessionListViewHeadFloat);

        // 悬浮头部截住点击事件，防止传到下面的listview
        lessionViewHeadFloat.setOnClickListener(null);

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

        // 切换到投票列表的按钮
        RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d(RequireLessionFragment.class.getName(), String.valueOf(checkedId));
            }
        };
        switchLessionView = (RadioGroup)lessionViewHead1.findViewById(R.id.switchLessionView);
        switchLessionView.setOnCheckedChangeListener(onCheckedChangeListener);
    }

    private void initData() {
        // 查询数据
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

        voteListViewAdapter.setVoteLessionList(voteLessionList);
        voteListViewAdapter.setViewType(VoteListViewAdapter.ViewType.VOTE_LIST);
        voteListViewAdapter.notifyDataSetChanged();

    }

    @Override
    public void refreshRankingLession(List<CandidateLession> voteLessionList) {

        voteListViewAdapter.setVoteLessionList(voteLessionList);
        voteListViewAdapter.setViewType(VoteListViewAdapter.ViewType.RANKING_LIST);
        voteListViewAdapter.notifyDataSetChanged();

    }

}
