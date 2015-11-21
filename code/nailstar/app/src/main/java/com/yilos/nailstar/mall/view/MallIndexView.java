package com.yilos.nailstar.mall.view;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by ganyue on 15/11/20.
 */
public interface MallIndexView {
    public MallIndexCommodityListAdapter getMallIndexCommodityListAdapter();
    public LinearLayout getIndexCommodityHot1();

    public LinearLayout getIndexCommodityHot2();

    public LinearLayout getIndexCommodityHot3() ;

    public View getRootView();

    public LayoutInflater getInflater();

    public int getScreenWidth();
}
