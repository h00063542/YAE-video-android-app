package com.yilos.nailstar.aboutme.entity;

import android.widget.Adapter;

/**
 * Created by sisilai on 15/11/5.
 */
public class FansListCategory {
    private String mTitle;

    private Adapter mAdapter;
    public FansListCategory(String title, Adapter adapter) {
        mTitle = title;
        mAdapter = adapter;
    }

    public void setTile(String title) {
        mTitle = title;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setAdapter(Adapter adapter) {
        mAdapter = adapter;
    }

    public Adapter getAdapter() {
        return mAdapter;
    }

}