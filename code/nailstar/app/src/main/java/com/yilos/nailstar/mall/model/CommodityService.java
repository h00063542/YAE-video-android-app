package com.yilos.nailstar.mall.model;

import com.yilos.nailstar.mall.entity.Commodity;

import java.util.List;

/**
 * Created by ganyue on 15/11/20.
 */
public interface CommodityService {
    public List<Commodity> getRecommendCommodityList(int pageNo, int prePageNo);
    public List<Commodity> getHotCommodityList();
}
