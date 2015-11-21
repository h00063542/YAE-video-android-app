package com.yilos.nailstar.mall.presenter;

/**
 * Created by ganyue on 15/11/20.
 */
public interface MallIndexPresenter {
    public void loadBannerData();
    public void loadCommodityCateData();
    public void loadHotViewData();
    public void loadRecommendPageData(int pageNo, int prePageNo);
}
