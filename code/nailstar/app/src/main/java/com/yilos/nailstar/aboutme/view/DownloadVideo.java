package com.yilos.nailstar.aboutme.view;

import android.os.Bundle;
import android.widget.ListView;

import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.presenter.DownloadPresenter;
import com.yilos.nailstar.download.DownLoadInfo;
import com.yilos.nailstar.framework.view.BaseActivity;

import java.util.List;

public class DownloadVideo extends BaseActivity implements IDownloadVideo{

    private DownloadPresenter downloadPresenter = new DownloadPresenter(this);

    private ListView downloadListView;
    private DownLoadVideoAdapter downLoadVideoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_video);
        initDownLoadList();
    }

    private void initDownLoadList() {
        downloadListView = (ListView) findViewById(R.id.downloadVideoList);
        downLoadVideoAdapter = new DownLoadVideoAdapter(this, downloadPresenter);
        downloadListView.setAdapter(downLoadVideoAdapter);
    }

    @Override
    public void refreshDownLoadInfoList(List<DownLoadInfo> downLoadInfoList) {
        if (downLoadVideoAdapter != null) {
            downLoadVideoAdapter.notifyDataSetInvalidated();
            downLoadVideoAdapter.setDownLoadInfoList(downLoadInfoList);
            downLoadVideoAdapter.notifyDataSetChanged();
        }
    }
}
