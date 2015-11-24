package com.yilos.nailstar.aboutme.requirelesson.model;

import com.yilos.nailstar.aboutme.entity.UserMessage;
import com.yilos.nailstar.aboutme.requirelesson.entity.RequireLesson;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.util.Constants;
import com.yilos.nailstar.util.HttpClient;
import com.yilos.nailstar.util.JsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
        String jsonObject = null;
        JSONObject requireLessonJSONObject = null;
        JSONArray requireLessonArray;
        String url = "/vapi/nailstar/qjc/candidate?uid=" + uid;
        try {
            jsonObject = HttpClient.getJson(url);
            requireLessonJSONObject = new JSONObject(jsonObject);
            if (requireLessonJSONObject.getInt(Constants.CODE) != 0) {
                return requireLessonArrayList;
            }
            requireLessonArray = requireLessonJSONObject.getJSONObject(Constants.RESULT).getJSONArray(Constants.CANDIDATES);
            for (int i = 0; i < requireLessonArray.length(); i++) {
                JSONObject requireLessonObject = requireLessonArray.getJSONObject(i);
                int no = requireLessonObject.getInt(Constants.NO);
                long createDate = requireLessonObject.getLong(Constants.CREATE_DATE);
                String thumbUrl = JsonUtil.optString(requireLessonObject, Constants.THUMB_URL);

                RequireLesson requireLesson = new RequireLesson(createDate, no, thumbUrl);
                requireLessonArrayList.add(requireLesson);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return requireLessonArrayList;
    }
}
