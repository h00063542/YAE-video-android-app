package com.yilos.nailstar.aboutme.view;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.presenter.DownloadPresenter;
import com.yilos.nailstar.download.DownLoadInfo;
import com.yilos.nailstar.download.DownloadConstants;
import com.yilos.widget.circleimageview.CircleImageView;
import com.yilos.widget.view.ImageCacheView;

import java.util.List;

/**
 * Created by yilos on 15/11/16.
 */
public class DownLoadVideoAdapter extends BaseAdapter {

    private List<DownLoadInfo> downLoadInfoList;

    private int screenWidth;

    private Activity context;
    private DownloadPresenter downloadPresenter;

    public DownLoadVideoAdapter(Activity context, DownloadPresenter downloadPresenter) {

        this.context = context;
        this.downloadPresenter = downloadPresenter;

        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        this.screenWidth = metric.widthPixels;
    }

    public void setDownLoadInfoList(List<DownLoadInfo> downLoadInfoList) {
        this.downLoadInfoList = downLoadInfoList;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (downLoadInfoList != null) {
            count = downLoadInfoList.size();
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

        if (position >= getCount()) {
            return null;
        }
        final ViewHolder holder;
        if (convertView == null) {
            convertView = context.getLayoutInflater().inflate(R.layout.download_video_item, null);
            holder = new ViewHolder();
            holder.downloadAuthorName = (TextView) convertView.findViewById(R.id.downloadAuthorName);
            holder.downloadAuthorPhoto = (CircleImageView) convertView.findViewById(R.id.downloadAuthorPhoto);
            holder.downloadImage = (ImageCacheView) convertView.findViewById(R.id.downloadImage);
            holder.downloadOperation = (ImageView) convertView.findViewById(R.id.downloadOperation);
            holder.downloadTopic = (TextView) convertView.findViewById(R.id.downloadTopic);
            holder.downloadProgressBar = (ProgressBar) convertView.findViewById(R.id.downloadProgressBar);
            holder.downloadMessage = convertView.findViewById(R.id.downloadMessage);
            holder.downloadBytes = (TextView) convertView.findViewById(R.id.downloadBytes);
            holder.deleteVideo = (Button) convertView.findViewById(R.id.deleteVideo);

            holder.downloadImage.getLayoutParams().width = screenWidth * 25 / 100;
            holder.downloadImage.getLayoutParams().height = screenWidth * 25 / 100;

            holder.downloadOperation.getLayoutParams().width = screenWidth * 10 / 100;
            holder.downloadOperation.getLayoutParams().height = screenWidth * 10 / 100;

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final DownLoadInfo downLoadInfo = downLoadInfoList.get(position);
        holder.downloadAuthorName.setText(downLoadInfo.getName());
        if (downLoadInfo.getPhoto() != null) {
            holder.downloadAuthorPhoto.setImageSrc("file://" + downLoadInfo.getPhoto());
        } else {
            holder.downloadAuthorPhoto.setImageSrc(R.mipmap.ic_default_photo);
        }
        if (downLoadInfo.getIamge() != null) {
            holder.downloadImage.setImageSrc("file://" + downLoadInfo.getIamge());
        } else {
            holder.downloadImage.setImageSrc(R.mipmap.ic_default_image);
        }

        if (downLoadInfo.getStatus() == DownloadConstants.DOWNLOADING) {
            holder.downloadOperation.setImageResource(R.mipmap.suspend_video);
            holder.downloadImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    downloadPresenter.pauseDownLoadTask(downLoadInfo);
                }
            });
        } else if (downLoadInfo.getStatus() == DownloadConstants.DOWNLOAD_FINISH) {
            holder.downloadOperation.setImageResource(R.mipmap.play_video);
            holder.downloadImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent downloadVideoPlayer = new Intent(context, DownloadVideoPlayer.class);
                    downloadVideoPlayer.putExtra(DownloadVideoPlayer.TITLE, downLoadInfo.getTitle());
                    downloadVideoPlayer.putExtra(DownloadVideoPlayer.URL, downLoadInfo.getPath());
                    context.startActivity(downloadVideoPlayer);
                }
            });
        } else if (downLoadInfo.getStatus() == DownloadConstants.DOWNLOAD_STOP) {
            holder.downloadOperation.setImageResource(R.mipmap.icon_download_white);
            holder.downloadImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    downloadPresenter.resumeDownLoadTask(downLoadInfo);
                }
            });
        } else {
            holder.downloadImage.setVisibility(View.GONE);
        }

        holder.downloadTopic.setText(downLoadInfo.getTitle());

        holder.deleteVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadPresenter.deleteVideoConfirm(downLoadInfo);
            }
        });

        handleDownloadProcess(downLoadInfo, holder);

        return convertView;
    }

    private void handleDownloadProcess(DownLoadInfo downLoadInfo, ViewHolder holder) {
        if (downLoadInfo.getStatus() == DownloadConstants.DOWNLOAD_FINISH || downLoadInfo.getFileSize() <= 0) {
            holder.downloadProgressBar.setVisibility(View.GONE);
            holder.downloadMessage.setVisibility(View.GONE);
            return;
        }

        holder.downloadProgressBar.setMax((int) (downLoadInfo.getFileSize() / 1024));
        holder.downloadProgressBar.setProgress((int) (downLoadInfo.getBytesRead() / 1024));

        double totalMb = (double) downLoadInfo.getFileSize() / (1024 * 1024);
        double readMb = (double) downLoadInfo.getBytesRead() / (1024 * 1024);
        holder.downloadBytes.setText(String.format("%.1fM / %.1fM", readMb, totalMb));

        holder.downloadProgressBar.setVisibility(View.VISIBLE);
        holder.downloadMessage.setVisibility(View.VISIBLE);
    }

    class ViewHolder {
        public ImageCacheView downloadImage;
        public ImageView downloadOperation;
        public TextView downloadTopic;
        public CircleImageView downloadAuthorPhoto;
        public TextView downloadAuthorName;
        public ProgressBar downloadProgressBar;
        public View downloadMessage;
        public TextView downloadBytes;
        public Button deleteVideo;
    }
}
