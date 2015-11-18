package com.yilos.nailstar.index.view;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.yilos.nailstar.R;
import com.yilos.nailstar.framework.view.BaseActivity;
import com.yilos.nailstar.index.presenter.SearchPresenter;

public class SearchActivity extends BaseActivity implements ISearchView, RecyclerArrayAdapter.OnLoadMoreListener{
    private SearchPresenter presenter;

    private EditText searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        presenter = SearchPresenter.getInstance(this);

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
        final BaseActivity activity = this;
        final EasyRecyclerView videoListView = (EasyRecyclerView)findViewById(R.id.videoList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        videoListView.setLayoutManager(gridLayoutManager);
        final SearchResultAdapter adapter = presenter.getAdapter();
        videoListView.setAdapterWithProgress(adapter);
        adapter.setMore(R.layout.view_more, this);
        adapter.setNoMore(R.layout.view_nomore);
        adapter.setError(R.layout.view_error);
//        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(int i) {
//                activity.showLoading(null);
//                Intent intent = new Intent(activity, TopicVideoPlayerActivity.class);
//                intent.putExtra(Constants.TOPIC_ID, adapter.getItem(i).getTopicId());
//                startActivityForResult(intent, 1);
//            }
//        });
        videoListView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                if(parent.getChildPosition(view) != 0)
                    outRect.top = 5;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        hideLoading();

        searchText.requestFocus();
        InputMethodManager inputManager = (InputMethodManager)searchText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(searchText, 2);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        hideLoading();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideLoading();
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
    public void onLoadMore() {
        presenter.search(false);
    }
}
