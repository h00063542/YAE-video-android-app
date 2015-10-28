package com.yilos.nailstar.requirelession.view;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.yilos.nailstar.R;
import com.yilos.nailstar.requirelession.Presenter.LessionPresenter;
import com.yilos.nailstar.requirelession.entity.CandidateLession;
import com.yilos.nailstar.requirelession.entity.LessionActivity;
import com.yilos.nailstar.takeImage.TakeImage;
import com.yilos.nailstar.takeImage.TakeImageCallback;


import java.util.List;

/**
 * 求教程Fragment
 */
public class RequireLessionFragment extends Fragment implements LessionView {

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
        View view = inflater.inflate(R.layout.fragment_require_lession, container, false);
        initView(view, inflater);
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

    private void initView(View view, LayoutInflater inflater) {

        rankingListViewAdapter = new RankingListViewAdapter(inflater);
        lessionRankingView = (ListView)view.findViewById(R.id.lessionRankingView);
        lessionRankingView.setAdapter(rankingListViewAdapter);

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
