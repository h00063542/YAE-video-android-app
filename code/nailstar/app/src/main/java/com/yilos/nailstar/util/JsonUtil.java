package com.yilos.nailstar.util;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by yilos on 2015-11-02.
 */
public class JsonUtil {
    /**
     * 解决JsonObject optString bug
     * 当key对应的值为null时({name:null})，JsonObject.optString返回的为"null"字符串
     *
     * @param jsonObject
     * @param key
     * @return
     */
    public static String optString(JSONObject jsonObject, String key) {
        return (null == jsonObject || jsonObject.isNull(key))
                ? Constants.EMPTY_STRING : jsonObject.optString(key, Constants.EMPTY_STRING);
    }

    /**
     * 同上
     *
     * @param jsonArray
     * @param index
     * @return
     */
    public static String optString(JSONArray jsonArray, int index) {
        return (null == jsonArray || jsonArray.isNull(index))
                ? Constants.EMPTY_STRING : jsonArray.optString(index, Constants.EMPTY_STRING);
    }
}
