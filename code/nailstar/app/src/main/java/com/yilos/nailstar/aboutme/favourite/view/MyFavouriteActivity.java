package com.yilos.nailstar.aboutme.favourite.view;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.favourite.entity.FavouriteTopic;
import com.yilos.nailstar.aboutme.favourite.presenter.FavouritePresenter;
import com.yilos.nailstar.framework.view.BaseActivity;

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
    protected void onResume() {
        super.onResume();
        presenter.initFavouriteList();
    }

    private void initView() {
        recyclerView = (RecyclerView)findViewById(R.id.myFavouriteList);
        noFavouritePanel = findViewById(R.id.noFavouritePanel);
    }

    @Override
    public void initFavouriteList(List<FavouriteTopic> favouriteTopicList) {
        if(null == favouriteTopicList || favouriteTopicList.size() <= 0) {
            noFavouritePanel.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

        noFavouritePanel.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setAdapter(new FavouriteListAdapter(this, favouriteTopicList));
    }
}
