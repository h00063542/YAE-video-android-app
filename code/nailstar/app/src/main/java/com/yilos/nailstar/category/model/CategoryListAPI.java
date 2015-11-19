package com.yilos.nailstar.category.model;

import android.app.Activity;
import android.content.Intent;

import com.yilos.nailstar.category.view.CategoryListActivity;

/**
 * Created by yangdan on 15/11/18.
 */
public class CategoryListAPI {
    public static final String TITLE = "title";
    public static final String CATEGORY = "category";
    private static CategoryListAPI instance = new CategoryListAPI();

    private CategoryListAPI(){}

    public static CategoryListAPI getInstance() {
        return instance;
    }

    public void gotoCategoryListView(Activity activity, String title, String category) {
        Intent intent = new Intent(activity, CategoryListActivity.class);
        intent.putExtra(TITLE, title);
        intent.putExtra(CATEGORY, category);

        activity.startActivityForResult(intent, 1);
    }
}
