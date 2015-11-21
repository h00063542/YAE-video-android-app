package com.yilos.nailstar.mall.model;

import com.yilos.nailstar.framework.entity.NailStarApplicationContext;
import com.yilos.nailstar.mall.entity.Commodity;
import com.yilos.nailstar.mall.entity.CommodityCategory;
import com.yilos.nailstar.mall.entity.MallIndexBanner;
import com.yilos.nailstar.topic.entity.TopicInfo;
import com.yilos.nailstar.util.Constants;
import com.yilos.nailstar.util.HttpClient;
import com.yilos.nailstar.util.JsonUtil;
import com.yilos.nailstar.util.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ganyue on 15/11/20.
 */
public class CommodityServiceImpl implements CommodityService {
    private static final String URL_PREFIX = "/vapi/nailstar/";
    @Override
    public List<Commodity> getRecommendCommodityList(int pageNo, int prePageNo) {

        List<Commodity> result = new ArrayList<>();

        if (!NailStarApplicationContext.getInstance().isNetworkConnected()) {
            return new ArrayList<>();
        }
        String url = URL_PREFIX + "mall/getAllCommodities?page=" + pageNo;
        try {
//            String strResult = HttpClient.getJson(url);
//            JSONObject jsonObject = buildJSONObject(strResult);
//            JSONObject jsonResult = jsonObject.optJSONObject(Constants.RESULT);
//            JSONArray jsonVCommoditys = jsonResult.optJSONArray("commodities");
//            for (int i = 0; i < jsonVCommoditys.length(); i++) {
//                JSONObject jsonOne = jsonVCommoditys.optJSONObject(i);
//                Commodity one = new Commodity();
//                one.setName(JsonUtil.optString(jsonOne, "name"));
//                one.setImageUrl(JsonUtil.optString(jsonOne, "pic_url"));
//                one.setDesc(JsonUtil.optString(jsonOne, "description"));
//                one.setGoodsId(JsonUtil.optString(jsonOne, "real_id"));
//            }


            result.add(new Commodity("马卡龙甲油胶", 10, "正品保障", "https://gd2.alicdn.com/bao/uploaded/i2/TB1E8k7KpXXXXaCXFXXXXXXXXXX_!!0-item_pic.jpg_400x400.jpg"));
            result.add(new Commodity("Laparry功能胶", 10, "正品保障", "https://gd2.alicdn.com/bao/uploaded/i2/TB1J6NhKFXXXXcgXXXXXXXXXXXX_!!0-item_pic.jpg_400x400.jpg"));
            result.add(new Commodity("马卡龙甲油胶", 10, "正品保障", "https://gd2.alicdn.com/bao/uploaded/i2/TB1E8k7KpXXXXaCXFXXXXXXXXXX_!!0-item_pic.jpg_400x400.jpg"));
            result.add(new Commodity("Laparry功能胶", 10, "正品保障", "https://gd2.alicdn.com/bao/uploaded/i2/TB1J6NhKFXXXXcgXXXXXXXXXXXX_!!0-item_pic.jpg_400x400.jpg"));
            result.add(new Commodity("马卡龙甲油胶", 10, "正品保障", "https://gd2.alicdn.com/bao/uploaded/i2/TB1E8k7KpXXXXaCXFXXXXXXXXXX_!!0-item_pic.jpg_400x400.jpg"));
            result.add(new Commodity("Laparry功能胶", 10, "正品保障", "https://gd2.alicdn.com/bao/uploaded/i2/TB1J6NhKFXXXXcgXXXXXXXXXXXX_!!0-item_pic.jpg_400x400.jpg"));
            result.add(new Commodity("马卡龙甲油胶", 10, "正品保障", "https://gd2.alicdn.com/bao/uploaded/i2/TB1E8k7KpXXXXaCXFXXXXXXXXXX_!!0-item_pic.jpg_400x400.jpg"));
            result.add(new Commodity("Laparry功能胶", 10, "正品保障", "https://gd2.alicdn.com/bao/uploaded/i2/TB1J6NhKFXXXXcgXXXXXXXXXXXX_!!0-item_pic.jpg_400x400.jpg"));
            result.add(new Commodity("马卡龙甲油胶", 10, "正品保障", "https://gd2.alicdn.com/bao/uploaded/i2/TB1E8k7KpXXXXaCXFXXXXXXXXXX_!!0-item_pic.jpg_400x400.jpg"));
            result.add(new Commodity("Laparry功能胶", 10, "正品保障", "https://gd2.alicdn.com/bao/uploaded/i2/TB1J6NhKFXXXXcgXXXXXXXXXXXX_!!0-item_pic.jpg_400x400.jpg"));

            return result;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Commodity> getHotCommodityList() {
        List<Commodity> result = new ArrayList<>();

        if (!NailStarApplicationContext.getInstance().isNetworkConnected()) {
            return new ArrayList<>();
        }
        String url = URL_PREFIX + "mall/getHotSales";
        try {
//            String strResult = HttpClient.getJson(url);
//            JSONObject jsonObject = buildJSONObject(strResult);
//            JSONObject jsonResult = jsonObject.optJSONObject(Constants.RESULT);
//            JSONArray jsonVCommoditys = jsonResult.optJSONArray("commodities");
//            for (int i = 0; i < jsonVCommoditys.length(); i++) {
//                JSONObject jsonOne = jsonVCommoditys.optJSONObject(i);
//                Commodity one = new Commodity();
//                one.setName(JsonUtil.optString(jsonOne, "name"));
//                one.setImageUrl(JsonUtil.optString(jsonOne, "pic_url"));
//                one.setDesc(JsonUtil.optString(jsonOne, "description"));
//                one.setGoodsId(JsonUtil.optString(jsonOne, "real_id"));
//            }

            result.add(new Commodity("马卡龙甲油胶", 10, "正品保障", "https://gd2.alicdn.com/bao/uploaded/i2/TB1E8k7KpXXXXaCXFXXXXXXXXXX_!!0-item_pic.jpg_400x400.jpg"));
            result.add(new Commodity("Laparry功能胶", 10, "正品保障", "https://gd2.alicdn.com/bao/uploaded/i2/TB1J6NhKFXXXXcgXXXXXXXXXXXX_!!0-item_pic.jpg_400x400.jpg"));
            result.add(new Commodity("马卡龙甲油胶", 10, "正品保障", "https://gd2.alicdn.com/bao/uploaded/i2/TB1E8k7KpXXXXaCXFXXXXXXXXXX_!!0-item_pic.jpg_400x400.jpg"));
            return result;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<CommodityCategory> getCommodityCategory() {
        List<CommodityCategory> result = new ArrayList<>();

        if (!NailStarApplicationContext.getInstance().isNetworkConnected()) {
            return new ArrayList<>();
        }
        String url = URL_PREFIX + "mall/getCommodityCategories";
        try {
//            String strResult = HttpClient.getJson(url);
//            JSONObject jsonObject = buildJSONObject(strResult);
//            JSONObject jsonResult = jsonObject.optJSONObject(Constants.RESULT);
//            JSONArray jsonVCommoditys = jsonResult.optJSONArray("commodities");
//            for (int i = 0; i < jsonVCommoditys.length(); i++) {
//                JSONObject jsonOne = jsonVCommoditys.optJSONObject(i);
//                Commodity one = new Commodity();
//                one.setName(JsonUtil.optString(jsonOne, "name"));
//                one.setImageUrl(JsonUtil.optString(jsonOne, "pic_url"));
//                one.setDesc(JsonUtil.optString(jsonOne, "description"));
//                one.setGoodsId(JsonUtil.optString(jsonOne, "real_id"));
//            }

            result.add(new CommodityCategory("甲油胶", "1"));
            result.add(new CommodityCategory("笔刷","2"));
            result.add(new CommodityCategory("工具","3"));
            result.add(new CommodityCategory("甲片", "4"));
            result.add(new CommodityCategory("饰品/贴纸", "5"));
            return result;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<MallIndexBanner> getMallIndexBanner() {
        List<MallIndexBanner> result = new ArrayList<>();

        if (!NailStarApplicationContext.getInstance().isNetworkConnected()) {
            return new ArrayList<>();
        }
        String url = URL_PREFIX + "mall/activities";
        try {
//            String strResult = HttpClient.getJson(url);
//            JSONObject jsonObject = buildJSONObject(strResult);
//            JSONObject jsonResult = jsonObject.optJSONObject(Constants.RESULT);
//            JSONArray jsonVCommoditys = jsonResult.optJSONArray("commodities");
//            for (int i = 0; i < jsonVCommoditys.length(); i++) {
//                JSONObject jsonOne = jsonVCommoditys.optJSONObject(i);
//                Commodity one = new Commodity();
//                one.setName(JsonUtil.optString(jsonOne, "name"));
//                one.setImageUrl(JsonUtil.optString(jsonOne, "pic_url"));
//                one.setDesc(JsonUtil.optString(jsonOne, "description"));
//                one.setGoodsId(JsonUtil.optString(jsonOne, "real_id"));
//            }

            result.add(new MallIndexBanner("http://www.baidu.com", 200, 10));
            result.add(new MallIndexBanner("http://www.yilos.com", 200, 10));
            return result;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private JSONObject buildJSONObject(String jsonResultStr) throws JSONException {
        if (StringUtil.isEmpty(jsonResultStr)) {
            return null;
        }
        JSONObject jsonResult = new JSONObject(jsonResultStr);
        if (null == jsonResult || Constants.CODE_VALUE_SUCCESS != jsonResult.optInt(Constants.CODE, Constants.CODE_VALUE_FAIL)) {
            return null;
        }
        return jsonResult;
    }
}
