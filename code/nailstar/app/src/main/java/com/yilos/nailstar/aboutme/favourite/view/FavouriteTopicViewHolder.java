package com.yilos.nailstar.aboutme.favourite.view;

import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.favourite.entity.FavouriteTopic;
import com.yilos.nailstar.util.DateUtil;
import com.yilos.widget.view.ImageCacheView;

import java.util.Date;

/**
 * Created by yangdan on 15/11/23.
 */
public class FavouriteTopicViewHolder extends BaseViewHolder<FavouriteTopic> {
    private ImageCacheView topicImageView;
    private TextView titleNameView;
    private TextView teacherNameView;
    private TextView updateDateView;

    public FavouriteTopicViewHolder(ViewGroup parent) {
        super(parent, R.layout.my_favourite_item);

        topicImageView = $(R.id.topicImage);
        titleNameView = $(R.id.titleView);
        teacherNameView = $(R.id.teacherNameView);
        updateDateView = $(R.id.updateDateView);
    }

    @Override
    public void setData(FavouriteTopic data) {
        topicImageView.setImageSrc(data.getPhotoUrl());
        titleNameView.setText(data.getTitle());
        teacherNameView.setText(data.getAuthor());
        updateDateView.setText(DateUtil.getUpdateDateString(data.getCreateDate(), new Date()));
    }
}
