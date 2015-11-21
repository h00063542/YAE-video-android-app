package com.yilos.nailstar.aboutme.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.presenter.DownloadPresenter;
import com.yilos.nailstar.download.DownLoadInfo;
import com.yilos.nailstar.framework.view.BaseActivity;
import com.yilos.widget.titlebar.TitleBar;

import java.util.List;

public class DownloadVideo extends BaseActivity implements IDownloadVideo{

    private DownloadPresenter downloadPresenter = new DownloadPresenter(this);

    private TitleBar downloadVideoTitleBar;
    private ListView downloadListView;
    private DownLoadVideoAdapter downLoadVideoAdapter;
    private View noDownloadVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_video);
        noDownloadVideo = findViewById(R.id.noDownloadVideo);
        initDownLoadList();
    }

    private void initDownLoadList() {
        downloadVideoTitleBar = (TitleBar) findViewById(R.id.downloadVideoTitleBar);
        downloadListView = (ListView) findViewById(R.id.downloadVideoList);
        downLoadVideoAdapter = new DownLoadVideoAdapter(this, downloadPresenter);
        downloadListView.setAdapter(downLoadVideoAdapter);

        downloadVideoTitleBar.getTitleView().setText(R.string.about_me_downloaded);
        downloadVideoTitleBar.getBackButton(this);

        if (downloadPresenter.getDownLoadInfoList().isEmpty()) {
            noDownloadVideo.setVisibility(View.VISIBLE);
        } else {
            noDownloadVideo.setVisibility(View.GONE);
        }
    }

    @Override
    public void refreshDownLoadInfoList(List<DownLoadInfo> downLoadInfoList) {

        if (downloadPresenter.getDownLoadInfoList().isEmpty()) {
            noDownloadVideo.setVisibility(View.VISIBLE);
        } else {
            noDownloadVideo.setVisibility(View.GONE);
        }

        if (downLoadVideoAdapter != null) {
            downLoadVideoAdapter.notifyDataSetInvalidated();
            downLoadVideoAdapter.setDownLoadInfoList(downLoadInfoList);
            downLoadVideoAdapter.notifyDataSetChanged();
        }
    }
}
