package com.yilos.nailstar.aboutme.favourite.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.favourite.entity.FavouriteTopic;
import com.yilos.nailstar.aboutme.favourite.presenter.FavouritePresenter;
import com.yilos.nailstar.framework.view.BaseActivity;
import com.yilos.widget.titlebar.TitleBar;

import java.util.List;

public class MyFavouriteActivity extends BaseActivity implements IFavouriteView {
    private FavouritePresenter presenter;

    private View noFavouritePanel;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favourite);
        presenter = FavouritePresenter.getInstance(this);

        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.initFavouriteList();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        presenter.initFavouriteList();
    }

    private void initView() {
        TitleBar titleBar = (TitleBar)findViewById(R.id.titleBar);
        titleBar.getBackButton(this);
        titleBar.getTitleView(R.string.title_activity_my_favourite);

        recyclerView = (RecyclerView)findViewById(R.id.myFavouriteList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        noFavouritePanel = findViewById(R.id.noFavouritePanel);
    }

    @Override
    public void setFavouriteList(List<FavouriteTopic> favouriteTopicList) {
        if(null == favouriteTopicList || favouriteTopicList.size() <= 0) {
            noFavouritePanel.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            return;
        }

        FavouriteListAdapter adapter = new FavouriteListAdapter(this, favouriteTopicList);
        recyclerView.setAdapter(adapter);

        noFavouritePanel.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }
}
