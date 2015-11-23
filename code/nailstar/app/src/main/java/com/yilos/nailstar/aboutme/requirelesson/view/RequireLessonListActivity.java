package com.yilos.nailstar.aboutme.requirelesson.view;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.model.LoginAPI;
import com.yilos.nailstar.aboutme.requirelesson.entity.RequireLesson;
import com.yilos.nailstar.aboutme.requirelesson.presenter.RequireLessonPresenter;
import com.yilos.nailstar.framework.view.BaseActivity;
import com.yilos.widget.titlebar.TitleBar;
import com.yilos.widget.view.MaxHeightGridLayoutManager;

import java.util.ArrayList;

/**
 * Created by sisilai on 15/11/23.
 */
public class RequireLessonListActivity extends BaseActivity implements IRequireLessonView {
    private RecyclerView recyclerView;
    private TitleBar titleBar;
    private RequireLessonListAdapter requireLessonListAdapter;
    private ArrayList<RequireLesson> requireLessonArrayList = new ArrayList<>();

    @Override
    public void getRequireLessonList(ArrayList<RequireLesson> requireLessonArrayList) {
        this.requireLessonArrayList = requireLessonArrayList;
        requireLessonListAdapter = new RequireLessonListAdapter(this,requireLessonArrayList);
        recyclerView.setAdapter(requireLessonListAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_require_lesson_list);
        titleBar = (TitleBar) findViewById(R.id.require_lesson_list_title_bar);
        titleBar.getTitleView(R.string.about_me_my_require_lesson);
        titleBar.getBackButton(this);
        recyclerView = (RecyclerView) findViewById(R.id.require_lesson_list);
        MaxHeightGridLayoutManager maxHeightGridLayoutManager = new MaxHeightGridLayoutManager(this, 2, 1500);
        maxHeightGridLayoutManager.setOrientation(MaxHeightGridLayoutManager.VERTICAL);
        maxHeightGridLayoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(maxHeightGridLayoutManager);
        RequireLessonPresenter requireLessonPresenter = RequireLessonPresenter.getInstance(this);
        requireLessonPresenter.getRequireLessonList(LoginAPI.getInstance().getLoginUserId());
    }
}
