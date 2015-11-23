package com.yilos.nailstar.aboutme.favourite.view;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.yilos.nailstar.aboutme.favourite.entity.FavouriteTopic;

import java.util.List;

/**
 * Created by yangdan on 15/11/23.
 */
public class FavouriteListAdapter extends RecyclerArrayAdapter<FavouriteTopic>{
    public FavouriteListAdapter(Context context, List<FavouriteTopic> favouriteTopicList) {
        super(context, favouriteTopicList);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup viewGroup, int i) {
        return new FavouriteTopicViewHolder(viewGroup);
    }
}