package com.yilos.nailstar.mall.model;

import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.mall.entity.Commodity;
import com.yilos.nailstar.mall.entity.CommodityCategory;
import com.yilos.nailstar.mall.entity.MallIndexBanner;

import java.util.List;

/**
 * Created by ganyue on 15/11/20.
 */
public interface CommodityService {
    public List<Commodity> getRecommendCommodityList(int pageNo, int prePageNo) throws NetworkDisconnectException;
    public List<Commodity> getHotCommodityList() throws NetworkDisconnectException;
    public List<CommodityCategory> getCommodityCategory() throws NetworkDisconnectException;
    public List<MallIndexBanner> getMallIndexBanner() throws NetworkDisconnectException;
}
