package com.yilos.nailstar.category.view;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.yilos.nailstar.index.entity.Topic;

/**
 * Created by yangdan on 15/11/18.
 */
public class CategoryListAdapter extends RecyclerArrayAdapter<Topic> {
    public CategoryListAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup viewGroup, int i) {
        return new CategoryListItemViewHolder(viewGroup);
    }
}
