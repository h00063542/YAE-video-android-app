package com.yilos.nailstar.requirelession.view;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
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
    private RadioButton goVotingBtn;

    // 排行榜按钮
    private RadioButton goRankingBtn;

    // 在悬浮头中的投票列表按钮
    private RadioButton goVotingBtnFloat;

    // 在悬浮头中的排行榜按钮
    private RadioButton goRankingBtnFloat;

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
        bindControl();
        return view;
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

    }

    private void initData() {
        // 查询数据
        lessionPresenter.queryActivityTopic();
        lessionPresenter.queryAndRefreshVoteLession();
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

        // 投票列表、排行榜列表切换按钮

        // 页头的按钮
        switchLessionView = (RadioGroup)lessionViewHead1.findViewById(R.id.switchLessionView);
        goVotingBtn = (RadioButton) lessionViewHead1.findViewById(R.id.goVotingBtn);
        goRankingBtn = (RadioButton) lessionViewHead1.findViewById(R.id.goRankingBtn);
        requireLessionBtn = (Button)lessionViewHead1.findViewById(R.id.requireLessionBtn);

        // 悬浮页头的按钮
        switchLessionViewFloat = (RadioGroup)view.findViewById(R.id.switchLessionViewFloat);
        goVotingBtnFloat = (RadioButton) view.findViewById(R.id.goVotingBtnFloat);
        goRankingBtnFloat = (RadioButton) view.findViewById(R.id.goRankingBtnFloat);

        switchLessionView.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == goVotingBtn.getId()) {

                    lessionPresenter.goVoteLessionList();

                    // 切换的时候，保证页首显示的是返回的第一条数据
                    if (lessionVoteView.getFirstVisiblePosition() > 1){
                        lessionVoteView.setSelection(1);
                    }

                    // 更新悬浮页头的按钮状态
                    goVotingBtnFloat.setChecked(true);

                } else if (checkedId == goRankingBtn.getId()) {

                    lessionPresenter.goRankingLessionList();

                    // 切换的时候，保证页首显示的是返回的第一条数据
                    if (lessionVoteView.getFirstVisiblePosition() > 1){
                        lessionVoteView.setSelection(1);
                    }

                    // 更新悬浮页头的按钮状态
                    goRankingBtnFloat.setChecked(true);
                }
            }
        });

        switchLessionViewFloat.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == goVotingBtnFloat.getId()) {

                    if (!goVotingBtn.isChecked()) {
                        goVotingBtn.setChecked(true);
                    }

                } else if (checkedId == goRankingBtnFloat.getId()) {

                    if (!goRankingBtn.isChecked()) {
                        goRankingBtn.setChecked(true);
                    }

                }
            }
        });
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
