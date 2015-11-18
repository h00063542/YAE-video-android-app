package com.yilos.nailstar.index.view;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.yilos.nailstar.R;
import com.yilos.nailstar.framework.view.BaseActivity;
import com.yilos.nailstar.index.entity.Topic;
import com.yilos.nailstar.topic.view.TopicVideoPlayerActivity;
import com.yilos.nailstar.util.Constants;
import com.yilos.widget.view.ImageCacheView;

/**
 * Created by yangdan on 15/11/17.
 */
public class VideoListItemHolder extends BaseViewHolder<Topic> {
    private ViewGroup parent;

    private ImageCacheView videoImage;

    private TextView titleView;

    public VideoListItemHolder(ViewGroup parent) {
        this(parent, R.layout.fragment_index_topic_item);
    }

    public VideoListItemHolder(ViewGroup parent, int res) {
        super(parent, res);
        this.parent = parent;

        videoImage = $(R.id.videoImage);
        titleView = $(R.id.titleView);
    }

    @Override
    public void setData(final Topic data) {
        videoImage.setImageSrc(data.getSmallThumbUrl());
        videoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (parent.getContext() instanceof BaseActivity) {
                    ((BaseActivity) parent.getContext()).showLoading(null);
                }
                Intent intent = new Intent(parent.getContext(), TopicVideoPlayerActivity.class);
                intent.putExtra(Constants.TOPIC_ID, data.getTopicId());
                if (parent.getContext() instanceof Activity) {
                    ((Activity) parent.getContext()).startActivityForResult(intent, 1);
                } else {
                    parent.getContext().startActivity(intent);
                }
            }
        });
        titleView.setText(data.getTitle());
    }
}
