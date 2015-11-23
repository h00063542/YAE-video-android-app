package com.yilos.nailstar.aboutme.requirelesson.model;

import com.yilos.nailstar.aboutme.entity.UserMessage;
import com.yilos.nailstar.aboutme.requirelesson.entity.RequireLesson;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.util.Constants;
import com.yilos.nailstar.util.JsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sisilai on 15/11/23.
 */
public class RequireLessonListServiceImpl implements RequireLessonListService {


    private static RequireLessonListService requireLessonListService = new RequireLessonListServiceImpl();

    public static RequireLessonListService getInstance() {
        return requireLessonListService;
    }

    @Override
    public ArrayList<RequireLesson> getRequireLessonList(String uid) throws NetworkDisconnectException, JSONException {
        ArrayList<RequireLesson> requireLessonArrayList = new ArrayList<>();
        String jsonObject;
        JSONObject requireLessonJSONObject;
        JSONArray requireLessonArray;
        uid = "a8affd60-efe6-11e4-a908-3132fc2abe39";
        String url = "/vapi2/nailstar/qjc/candidate?uid=" + uid;
        jsonObject = //HttpClient.getJson(url);
                "{\"code\":0,\"result\":{\"candidates\":[{\"no\":21,\"thumbUrl\":\"http://pic.yilos.com/f5a60719425c62c4c7c494fc96d2e787\",\"createDate\":1437989431228},\n" +
                        "                {\"no\":6,\"thumbUrl\":\"http://pic.yilos.com/b730af57b5e411742c39d6d1a0fefe4f\",\"createDate\":1433815483239},\n" +
                        "                {\"no\":5,\"thumbUrl\":\"http://pic.yilos.com/ef8df27c0da69f48a3b67f92e7e3ba03\",\"createDate\":1433557425609},\n" +
                        "                {\"no\":4,\"thumbUrl\":\"http://pic.yilos.com/1789251b09caebec0e9f90ae21ebe3bc\",\"createDate\":1433263024460}]}}";
        requireLessonJSONObject = new JSONObject(jsonObject);
        if (requireLessonJSONObject.getInt(Constants.CODE) != 0) {
            return requireLessonArrayList;
        }
        requireLessonArray = requireLessonJSONObject.getJSONObject(Constants.RESULT).getJSONArray(Constants.CANDIDATES);
        for(int i=0;i<requireLessonArray.length();i++){
            JSONObject requireLessonObject = requireLessonArray.getJSONObject(i);
            int no = requireLessonObject.getInt(Constants.NO);
            long createDate = requireLessonObject.getLong(Constants.CREATE_DATE);
            String thumbUrl = JsonUtil.optString(requireLessonObject, Constants.THUMB_URL);

            RequireLesson requireLesson = new RequireLesson(createDate, no, thumbUrl);
            requireLessonArrayList.add(requireLesson);
        }
        return requireLessonArrayList;
    }
}
