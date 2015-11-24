package com.yilos.nailstar.aboutme.favourite.view;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.favourite.entity.FavouriteTopic;
import com.yilos.nailstar.topic.view.TopicVideoPlayerActivity;
import com.yilos.nailstar.util.Constants;
import com.yilos.nailstar.util.DateUtil;
import com.yilos.widget.view.ImageCacheView;

import java.util.Date;

/**
 * Created by yangdan on 15/11/23.
 */
public class FavouriteTopicViewHolder extends BaseViewHolder<FavouriteTopic> {
    private ViewGroup parent;

    private ImageCacheView topicImageView;
    private TextView titleNameView;
    private TextView teacherNameView;
    private TextView updateDateView;

    public FavouriteTopicViewHolder(ViewGroup parent) {
        super(parent, R.layout.my_favourite_item);
        this.parent = parent;

        topicImageView = $(R.id.topicImage);
        titleNameView = $(R.id.titleView);
        teacherNameView = $(R.id.teacherNameView);
        updateDateView = $(R.id.updateDateView);
    }

    @Override
    public void setData(final FavouriteTopic data) {
        topicImageView.setImageSrc(data.getThumbUrl());
        titleNameView.setText(data.getTitle());
        teacherNameView.setText(data.getAuthor());
        updateDateView.setText(DateUtil.getUpdateDateString(data.getCreateDate(), new Date()));

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent.getContext(), TopicVideoPlayerActivity.class);
                intent.putExtra(Constants.TOPIC_ID, data.getTopicId());
                parent.getContext().startActivity(intent);
            }
        };
        itemView.setOnClickListener(clickListener);
        topicImageView.setOnClickListener(clickListener);
    }
}
