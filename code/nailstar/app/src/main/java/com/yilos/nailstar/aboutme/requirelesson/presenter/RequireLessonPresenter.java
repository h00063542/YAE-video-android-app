package com.yilos.nailstar.aboutme.requirelesson.presenter;

import com.yilos.nailstar.aboutme.requirelesson.entity.RequireLesson;
import com.yilos.nailstar.aboutme.requirelesson.model.RequireLessonListService;
import com.yilos.nailstar.aboutme.requirelesson.model.RequireLessonListServiceImpl;
import com.yilos.nailstar.aboutme.requirelesson.view.RequireLessonListActivity;
import com.yilos.nailstar.framework.exception.NetworkDisconnectException;
import com.yilos.nailstar.util.TaskManager;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by sisilai on 15/11/23.
 */
public class RequireLessonPresenter {

    private RequireLessonListService requireLessonListService = RequireLessonListServiceImpl.getInstance();
    private static RequireLessonPresenter requireLessonPresenter = new RequireLessonPresenter();
    private RequireLessonListActivity requireLessonListActivity;

    public static RequireLessonPresenter getInstance(RequireLessonListActivity requireLessonListActivity) {
        requireLessonPresenter.requireLessonListActivity = requireLessonListActivity;
        return requireLessonPresenter;
    }

    //获取求教程列表
    public void getRequireLessonList(final String uid) {
        TaskManager.Task loadRequireLessonList = new TaskManager.BackgroundTask() {
            @Override
            public Object doWork(Object data) {
                try {
                    return requireLessonListService.getRequireLessonList(uid);
                } catch (NetworkDisconnectException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        TaskManager.UITask<ArrayList<RequireLesson>> RequireLessonListUITask = new TaskManager.UITask<ArrayList<RequireLesson>>() {
            @Override
            public ArrayList<RequireLesson> doWork(ArrayList<RequireLesson> data) {
                requireLessonListActivity.getRequireLessonList(data);
                return null;
            }
        };

        new TaskManager().next(loadRequireLessonList).next(RequireLessonListUITask).start();
    }

}
