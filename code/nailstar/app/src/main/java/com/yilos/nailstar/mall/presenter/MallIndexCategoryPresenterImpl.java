package com.yilos.nailstar.mall.presenter;

import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.mall.entity.Commodity;
import com.yilos.nailstar.mall.model.CommodityService;
import com.yilos.nailstar.mall.model.CommodityServiceImpl;
import com.yilos.nailstar.mall.view.IMallIndexCategoryView;
import com.yilos.nailstar.mall.view.MallIndexCategoryListAdapter;
import com.yilos.nailstar.mall.view.MallIndexCommodityListAdapter;
import com.yilos.nailstar.util.TaskManager;

import java.util.ArrayList;

/**
 * Created by ganyue on 15/11/23.
 */
public class MallIndexCategoryPresenterImpl implements MallIndexCategoryPresenter {

    IMallIndexCategoryView view;

    CommodityService service;
    public MallIndexCategoryPresenterImpl(IMallIndexCategoryView view) {
        this.view = view;
        service = new CommodityServiceImpl();
    }

    @Override
    public void loadCategoryPageData(final String categoryId,final int pageNo, final int prePageNo) {
        TaskManager.Task loadTopicRelatedInfo = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                try {
                    return service.getCategoryCommodityList(categoryId,pageNo, prePageNo);
                } catch (NetworkDisconnectException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        TaskManager.UITask<ArrayList<Commodity>> updateUi = new TaskManager.UITask<ArrayList<Commodity>>() {
            @Override
            public Object doWork(ArrayList<Commodity> commodityList) {
                MallIndexCategoryListAdapter adapter = view.getMallIndexCommodityListAdapter();
                for(Commodity one:commodityList){
                    adapter.add(one);
                }
                return null;
            }
        };

        new TaskManager()
                .next(loadTopicRelatedInfo)
                .next(updateUi)
                .start();
    }
}
