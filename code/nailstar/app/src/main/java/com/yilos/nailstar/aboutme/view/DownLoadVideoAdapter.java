package com.yilos.nailstar.aboutme.view;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.presenter.DownloadPresenter;
import com.yilos.nailstar.download.DownLoadInfo;
import com.yilos.widget.circleimageview.CircleImageView;
import com.yilos.widget.view.ImageCacheView;

import java.util.List;

/**
 * Created by yilos on 15/11/16.
 */
public class DownLoadVideoAdapter extends BaseAdapter{

    private List<DownLoadInfo> downLoadInfoList;

    private int screenWidth;

    private Activity context;
    private DownloadPresenter downloadPresenter;

    public DownLoadVideoAdapter (Activity context, DownloadPresenter downloadPresenter) {

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
            holder.downloadTopic = (TextView) convertView.findViewById(R.id.downloadTopic);

            holder.downloadImage.getLayoutParams().width = screenWidth * 25 / 100;
            holder.downloadImage.getLayoutParams().height = screenWidth * 25 / 100;

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        DownLoadInfo downLoadInfo = downLoadInfoList.get(position);
        holder.downloadAuthorName.setText(downLoadInfo.getName());
        if (downLoadInfo.getPhoto() != null) {
            holder.downloadAuthorPhoto.setImageSrc("file://" + downLoadInfo.getPhoto());
        } else {
            holder.downloadAuthorPhoto.setImageResource(R.mipmap.ic_default_photo);
        }
        if (downLoadInfo.getIamge() != null) {
            holder.downloadImage.setImageSrc("file://" + downLoadInfo.getIamge());
        } else {
            holder.downloadImage.setImageResource(R.mipmap.ic_default_image);
        }

        holder.downloadTopic.setText(downLoadInfo.getTitle());

        return convertView;
    }

    class ViewHolder {
        public ImageCacheView downloadImage;
        public TextView downloadTopic;
        public CircleImageView downloadAuthorPhoto;
        public TextView downloadAuthorName;
    }
}
