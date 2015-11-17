package com.yilos.nailstar.index.view;

import com.yilos.nailstar.framework.view.IView;

/**
 * Created by yangdan on 15/11/16.
 */
public interface ISearchView extends IView {
    String getSearchContent();

    void showSearchResultView();

    void hideSearchResultView();

    void showNoResultView();

    void hideNoResultView();

    void closeSearchView();

    void notifyItemRangeInserted(int positionStart, int itemCount);

    void notifyDataSetChanged();
}
