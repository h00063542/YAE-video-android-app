package com.yilos.nailstar.index.view;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.yilos.nailstar.framework.application.NailStarApplication;
import com.yilos.nailstar.framework.view.BaseActivity;
import com.yilos.nailstar.index.entity.Poster;
import com.yilos.nailstar.topic.view.TopicVideoPlayerActivity;
import com.yilos.nailstar.util.CollectionUtil;
import com.yilos.nailstar.util.Constants;
import com.yilos.widget.banner.Banner;
import com.yilos.widget.view.ImageCacheView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangdan on 15/11/17.
 */
public class PosterViewCreator implements Banner.ViewCreator<Poster> {
    private BaseActivity baseActivity;

    private List<Poster> posters;

    public PosterViewCreator(BaseActivity baseActivity, List<Poster> posters) {
        this.baseActivity = baseActivity;
        this.posters = posters;
    }

    @Override
    public List<View> createViews() {
        final int height = NailStarApplication.getApplication().getHeightByScreenWidth(baseActivity, 7f/3f);

        final List<View> views = new ArrayList<>(8);
        if (!CollectionUtil.isEmpty(posters)) {
            for (final Poster poster : posters) {
                ImageCacheView imageCacheView = new ImageCacheView(baseActivity);

                Banner.LayoutParams layoutParams = new Banner.LayoutParams();
                layoutParams.width = Banner.LayoutParams.MATCH_PARENT;
                layoutParams.height = height;
                imageCacheView.setLayoutParams(layoutParams);
                imageCacheView.setAdjustViewBounds(true);
                imageCacheView.setScaleType(ImageView.ScaleType.FIT_CENTER);

                imageCacheView.setImageSrc(poster.getPicUrl());
                imageCacheView.setClickable(true);
                imageCacheView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(baseActivity, TopicVideoPlayerActivity.class);
                        intent.putExtra(Constants.TOPIC_ID, poster.getTopicId());
                        baseActivity.startActivityForResult(intent, 1);
                    }
                });
                views.add(imageCacheView);
            }
        }

        return views;
    }
}
