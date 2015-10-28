package com.yilos.nailstar.requirelession.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.yilos.nailstar.R;
import com.yilos.nailstar.requirelession.Presenter.LessionPresenter;
import com.yilos.nailstar.requirelession.entity.CandidateLession;
import com.yilos.nailstar.requirelession.entity.LessionActivity;
import com.yilos.nailstar.requirelession.model.LessionServiceImpl;
import com.yilos.nailstar.takeImage.TakeImage;
import com.yilos.nailstar.takeImage.TakeImageCallback;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 求教程Fragment
 */
public class RequireLessionFragment extends Fragment implements LessionView {

//    private static final String YILOS_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/yilos";
//
//    private TakeImage takeImage;

    private LessionPresenter lessionPresenter = new LessionPresenter(this);

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
        bindControl(view);
        initView();
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

    private void initView() {
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

    }

}
