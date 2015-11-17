package com.yilos.nailstar.index.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.yilos.nailstar.R;
import com.yilos.nailstar.framework.view.BaseActivity;
import com.yilos.nailstar.index.presenter.SearchPresenter;
import com.yilos.widget.view.ImageCacheView;

public class SearchActivity extends BaseActivity implements ISearchView{
    private SearchPresenter presenter;

    private LayoutInflater layoutInflater;

    private EditText searchText;

    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        presenter = SearchPresenter.getInstance(this);
        layoutInflater = getLayoutInflater();

        initViews();
    }

    private void initViews() {
        // 初始化后退按钮
        ImageButton backButton = (ImageButton)findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.closeSearchView();
            }
        });

        // 搜索框键盘监听
        searchText = (EditText)findViewById(R.id.searctText);
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)){
                    presenter.search(true);
                    return true;
                }

                return false;
            }
        });

        // 搜索按钮事件
        TextView searchButton = (TextView)findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.search(true);
            }
        });

        // 视频列表
        final EasyRecyclerView videoListView = (EasyRecyclerView)findViewById(R.id.videoList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        videoListView.setLayoutManager(gridLayoutManager);
        adapter = new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = layoutInflater.inflate(R.layout.search_item, null);

                return new RecyclerView.ViewHolder(view) {
                    @Override
                    public String toString() {
                        return super.toString();
                    }
                };
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                View view = holder.itemView;
                ImageCacheView videoImage = (ImageCacheView) view.findViewById(R.id.videoImage);
                videoImage.setImageSrc(presenter.getSearchResultItem(position).getThumbUrl());
                ((TextView) view.findViewById(R.id.titleView)).setText(presenter.getSearchResultItem(position).getTitle());
            }

            @Override
            public int getItemCount() {
                return presenter.getSearchResultCount();
            }
        };
        videoListView.setAdapter(adapter);

//        videoListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            private int latestVisibleItem;
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                latestVisibleItem = ((GridLayoutManager) videoListView.getLayoutManager()).findLastVisibleItemPosition();
//            }
//
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if(newState == RecyclerView.SCROLL_STATE_IDLE && latestVisibleItem + 1 == adapter.getItemCount()) {
//
//                }
//            }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        searchText.requestFocus();
        InputMethodManager inputManager = (InputMethodManager)searchText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(searchText, 2);
    }

    @Override
    public String getSearchContent() {
        return searchText.getText().toString().trim();
    }

    @Override
    public void showSearchResultView() {
        findViewById(R.id.videoList).setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSearchResultView() {
        findViewById(R.id.videoList).setVisibility(View.GONE);
    }

    @Override
    public void showNoResultView() {
        findViewById(R.id.noRelativeVideoView).setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNoResultView() {
        findViewById(R.id.noRelativeVideoView).setVisibility(View.GONE);
    }

    @Override
    public void closeSearchView() {
        this.finish();
        this.overridePendingTransition(0, R.anim.fade_out_center);
    }

    @Override
    public void notifyItemRangeInserted(int positionStart, int itemCount){
        adapter.notifyItemRangeInserted(positionStart, itemCount);
    }

    @Override
    public void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
    }
}
