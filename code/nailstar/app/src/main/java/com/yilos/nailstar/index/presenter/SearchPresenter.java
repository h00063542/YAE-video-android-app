package com.yilos.nailstar.index.presenter;

import com.yilos.nailstar.framework.entity.CommonResult;
import com.yilos.nailstar.framework.exception.JSONParseException;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.index.entity.Topic;
import com.yilos.nailstar.index.model.SearchServiceImpl;
import com.yilos.nailstar.index.view.ISearchView;
import com.yilos.nailstar.index.view.SearchResultAdapter;
import com.yilos.nailstar.util.TaskManager;

import java.util.List;

/**
 * Created by yangdan on 15/11/16.
 */
public class SearchPresenter {
    private static SearchPresenter instance = new SearchPresenter();

    private SearchServiceImpl service = SearchServiceImpl.getInstance();

    private ISearchView view;

    // 表示当前是第几次搜索，避免上次搜索结果回复慢时数据错乱
    private int searchTime;

    private SearchResultAdapter adapter;

    private int searchedPage = 0;

    private boolean lastPage;

    private SearchPresenter() {}

    public static SearchPresenter getInstance(ISearchView searchView) {
        instance.view = searchView;
        return instance;
    }

    public void closeSearchView() {
        view.closeSearchView();
    }

    public void search(boolean newSearch) {
        final String keyWord = view.getSearchContent();
        if(keyWord == null || keyWord.trim().equals("")) {
            view.showShortToast("请输入要搜索的内容");
            return;
        }

        if(newSearch) {
            adapter.clear();
            searchedPage = 0;
            lastPage = false;
            view.hideNoResultView();
            view.hideSearchResultView();
            view.showLoading("");
        }
        if(lastPage) {
            return;
        }

        searchTime = (searchTime + 1) % 10000;
        final int currentPage = searchedPage + 1;
        final int currentSearchTime = searchTime;

        TaskManager.BackgroundTask<List<Topic>> backgroundTask = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                CommonResult<List<Topic>> commonResult = new CommonResult<>();
                try {
                    commonResult.setResult(service.searchByKeyWord(keyWord, currentPage));
                    return commonResult;
                } catch (NetworkDisconnectException e) {
                    commonResult.setError(true);
                    commonResult.setErrorMsg(e.getMessage());
                    return commonResult;
                } catch (JSONParseException e) {
                    commonResult.setError(true);
                    commonResult.setErrorMsg(e.getMessage());
                    return commonResult;
                }
            }
        };

        TaskManager.UITask<CommonResult<List<Topic>>> uiTask = new TaskManager.UITask<CommonResult<List<Topic>>>() {
            @Override
            public Object doWork(CommonResult<List<Topic>> data) {
                if(currentSearchTime != searchTime) {
                    return null;
                }
                view.hideLoading();
                if(data.isError()) {
                    view.showLongToast(data.getErrorMsg());
                    return null;
                }
                if(data.getResult() == null || data.getResult().size() == 0) {
                    if(currentPage == 1) {
                        view.hideSearchResultView();
                        view.showNoResultView();
                    }
                    lastPage = true;
                    adapter.stopMore();
                    return null;
                }

                if(currentPage == 1) {
                    view.hideNoResultView();
                    view.showSearchResultView();
                    adapter.clear();
                    adapter.addAll(data.getResult());
                } else {
                    adapter.addAll(data.getResult());
                }
                searchedPage++;
                return null;
            }
        };

        new TaskManager()
                .next(backgroundTask)
                .next(uiTask)
                .start();
    }

    public SearchResultAdapter getAdapter() {
        if(null == adapter) {
            adapter = new SearchResultAdapter(view.getViewContext());
        }

        return adapter;
    }
}
