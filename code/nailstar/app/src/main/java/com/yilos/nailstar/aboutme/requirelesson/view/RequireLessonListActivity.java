package com.yilos.nailstar.aboutme.requirelesson.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.model.LoginAPI;
import com.yilos.nailstar.aboutme.requirelesson.entity.RequireLesson;
import com.yilos.nailstar.aboutme.requirelesson.presenter.RequireLessonPresenter;
import com.yilos.nailstar.framework.view.BaseActivity;
import com.yilos.widget.titlebar.TitleBar;

import java.util.ArrayList;

/**
 * Created by sisilai on 15/11/23.
 */
public class RequireLessonListActivity extends BaseActivity implements IRequireLessonView {
    private RecyclerView recyclerView;
    private TitleBar titleBar;
    private LinearLayout requireLessonEmpty;
    private RequireLessonListAdapter requireLessonListAdapter;
    private ArrayList<RequireLesson> requireLessonArrayList;

    @Override
    public void getRequireLessonList(ArrayList<RequireLesson> requireLessonArrayList) {
        this.requireLessonArrayList = requireLessonArrayList;
        requireLessonListAdapter = new RequireLessonListAdapter(this,requireLessonArrayList);
        recyclerView.setAdapter(requireLessonListAdapter);
        if (this.requireLessonArrayList.size() == 0) {
            requireLessonEmpty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_require_lesson_list);
        titleBar = (TitleBar) findViewById(R.id.require_lesson_list_title_bar);
        titleBar.getTitleView(R.string.about_me_my_require_lesson);
        titleBar.getBackButton(this);
        requireLessonEmpty = (LinearLayout) findViewById(R.id.require_lesson_empty);
        recyclerView = (RecyclerView) findViewById(R.id.require_lesson_list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);
        RequireLessonPresenter requireLessonPresenter = RequireLessonPresenter.getInstance(this);
        requireLessonPresenter.getRequireLessonList(LoginAPI.getInstance().getLoginUserId());
    }
}
