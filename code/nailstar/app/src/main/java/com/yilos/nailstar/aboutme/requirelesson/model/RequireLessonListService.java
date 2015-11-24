package com.yilos.nailstar.aboutme.requirelesson.model;

import com.yilos.nailstar.aboutme.requirelesson.entity.RequireLesson;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by sisilai on 15/11/23.
 */
public interface RequireLessonListService {
    ArrayList<RequireLesson> getRequireLessonList(String uid) throws NetworkDisconnectException, JSONException;
}
