package com.yilos.nailstar.mall.model;

import com.yilos.nailstar.mall.entity.Commodity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ganyue on 15/11/20.
 */
public class CommodityServiceImpl implements CommodityService {
    @Override
    public List<Commodity> getRecommendCommodityList(int pageNo, int prePageNo) {
        List<Commodity> list = new ArrayList<Commodity>();
        list.add(new Commodity("马卡龙甲油胶",10,"正品保障","https://gd2.alicdn.com/bao/uploaded/i2/TB1E8k7KpXXXXaCXFXXXXXXXXXX_!!0-item_pic.jpg_400x400.jpg"));
        list.add(new Commodity("马卡龙甲油胶",10,"正品保障","https://gd2.alicdn.com/bao/uploaded/i2/TB1E8k7KpXXXXaCXFXXXXXXXXXX_!!0-item_pic.jpg_400x400.jpg"));
        list.add(new Commodity("马卡龙甲油胶",10,"正品保障","https://gd2.alicdn.com/bao/uploaded/i2/TB1E8k7KpXXXXaCXFXXXXXXXXXX_!!0-item_pic.jpg_400x400.jpg"));
        list.add(new Commodity("马卡龙甲油胶",10,"正品保障","https://gd2.alicdn.com/bao/uploaded/i2/TB1E8k7KpXXXXaCXFXXXXXXXXXX_!!0-item_pic.jpg_400x400.jpg"));
        list.add(new Commodity("马卡龙甲油胶",10,"正品保障","https://gd2.alicdn.com/bao/uploaded/i2/TB1E8k7KpXXXXaCXFXXXXXXXXXX_!!0-item_pic.jpg_400x400.jpg"));
        list.add(new Commodity("马卡龙甲油胶",10,"正品保障","https://gd2.alicdn.com/bao/uploaded/i2/TB1E8k7KpXXXXaCXFXXXXXXXXXX_!!0-item_pic.jpg_400x400.jpg"));
        list.add(new Commodity("马卡龙甲油胶",10,"正品保障","https://gd2.alicdn.com/bao/uploaded/i2/TB1E8k7KpXXXXaCXFXXXXXXXXXX_!!0-item_pic.jpg_400x400.jpg"));
        list.add(new Commodity("马卡龙甲油胶",10,"正品保障","https://gd2.alicdn.com/bao/uploaded/i2/TB1E8k7KpXXXXaCXFXXXXXXXXXX_!!0-item_pic.jpg_400x400.jpg"));
        list.add(new Commodity("马卡龙甲油胶",10,"正品保障","https://gd2.alicdn.com/bao/uploaded/i2/TB1E8k7KpXXXXaCXFXXXXXXXXXX_!!0-item_pic.jpg_400x400.jpg"));
        list.add(new Commodity("马卡龙甲油胶",10,"正品保障","https://gd2.alicdn.com/bao/uploaded/i2/TB1E8k7KpXXXXaCXFXXXXXXXXXX_!!0-item_pic.jpg_400x400.jpg"));
        return list;
    }

    @Override
    public List<Commodity> getHotCommodityList() {
        return null;
    }
}
