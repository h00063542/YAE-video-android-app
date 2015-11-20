package com.yilos.nailstar.mall.presenter;

import com.yilos.nailstar.mall.entity.Commodity;
import com.yilos.nailstar.mall.model.CommodityService;
import com.yilos.nailstar.mall.model.CommodityServiceImpl;
import com.yilos.nailstar.mall.view.MallIndexCommodityListAdapter;
import com.yilos.nailstar.mall.view.MallIndexView;

import java.util.List;

/**
 * Created by ganyue on 15/11/20.
 */
public class MallIndexPresenterImpl implements MallIndexPresenter{
    MallIndexView view;
    CommodityService service;
    public MallIndexPresenterImpl(MallIndexView view){
        this.view = view;
        service = new CommodityServiceImpl();
    }
    @Override
    public void loadPageData(int pageNo, int prePageNo) {
        List<Commodity> list =  service.getRecommendCommodityList(pageNo, prePageNo);
        MallIndexCommodityListAdapter adapter = view.getMallIndexCommodityListAdapter();
        for(Commodity one:list){
            adapter.add(one);
        }
    }
}
