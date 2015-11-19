package com.yilos.nailstar.category.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.yilos.nailstar.R;
import com.yilos.nailstar.category.model.CategoryListAPI;
import com.yilos.nailstar.category.presenter.CategoryListPresenter;
import com.yilos.nailstar.framework.view.BaseActivity;
import com.yilos.nailstar.index.entity.Topic;
import com.yilos.widget.titlebar.TitleBar;

import java.util.List;

public class CategoryListActivity extends BaseActivity implements ICategoryListView, RecyclerArrayAdapter.OnLoadMoreListener{
    private CategoryListPresenter presenter;

    private CategoryListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        presenter = CategoryListPresenter.getInstance(this, getIntent().getStringExtra(CategoryListAPI.getInstance().CATEGORY));

        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.initData();
        hideLoading();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideLoading();
    }

    private void initView() {
        TitleBar titleBar = (TitleBar)findViewById(R.id.titleBar);
        titleBar.getBackButton(this);
        titleBar.getTitleView(getIntent().getStringExtra(CategoryListAPI.getInstance().TITLE));

        EasyRecyclerView list = (EasyRecyclerView)findViewById(R.id.videoList);
        adapter = new CategoryListAdapter(this);
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(this));

        adapter.setMore(R.layout.view_more, this);
        adapter.setNoMore(R.layout.view_nomore);
    }

    @Override
    public void onLoadMore() {
        presenter.getNextPage();
    }

    @Override
    public void pauseMore() {
        adapter.pauseMore();
    }

    @Override
    public void stopMore() {
        adapter.stopMore();
    }

    @Override
    public void addData(List<Topic> topics) {
        adapter.addAll(topics);
    }

    @Override
    public void clearData() {
        adapter.clear();
    }
}
