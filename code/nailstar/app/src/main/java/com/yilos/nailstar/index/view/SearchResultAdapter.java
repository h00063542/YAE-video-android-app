package com.yilos.nailstar.index.view;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.yilos.nailstar.index.entity.Topic;

import java.util.List;

/**
 * Created by yangdan on 15/11/17.
 */
public class SearchResultAdapter extends RecyclerArrayAdapter<Topic> {
    public SearchResultAdapter(Context context) {
        super(context);
    }

    public SearchResultAdapter(Context context, Topic[] objects) {
        super(context, objects);
    }

    public SearchResultAdapter(Context context, List<Topic> objects) {
        super(context, objects);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup viewGroup, int i) {
        return new SearchResultItemHolder(viewGroup);
    }
}
