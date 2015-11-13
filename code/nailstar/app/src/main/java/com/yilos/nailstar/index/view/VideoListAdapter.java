package com.yilos.nailstar.index.view;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.yilos.nailstar.R;
import com.yilos.nailstar.framework.application.NailStarApplication;
import com.yilos.widget.view.MaxHeightGridLayoutManager;

/**
 * Created by yangdan on 15/11/13.
 */
public class VideoListAdapter extends PagerAdapter {
    private Activity activity;

    // 最新、最热、关注的视频显示控件
    private CustomRecyclerView latestListView;
    private CustomRecyclerView hotestListView;
    private CustomRecyclerView watchListView;

    public VideoListAdapter(Activity activity) {
        this.activity = activity;

        // 初始化最热、最新、关注视频Tab页
        latestListView = initVideoRecycleView();
        hotestListView = initVideoRecycleView();
        watchListView = initVideoRecycleView();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        RecyclerView newView = null;
        switch (position) {
            case 0:
                newView = latestListView;
                break;
            case 1:
                newView = hotestListView;
                break;
            case 2:
                newView = watchListView;
                break;
            default:
                newView = new RecyclerView(activity);
                break;
        }

        if (newView.getParent() != null) {
            container.removeView(newView);
        }

        container.addView(newView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return newView;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "最新";
            case 1:
                return "热播";
            case 2:
                return "关注";
            default:
                return "";
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }

    public CustomRecyclerView getLatestListView() {
        return latestListView;
    }

    public CustomRecyclerView getHotestListView() {
        return hotestListView;
    }

    public CustomRecyclerView getWatchListView() {
        return watchListView;
    }

    private CustomRecyclerView initVideoRecycleView() {
        final CustomRecyclerView view = new CustomRecyclerView(activity);

        MaxHeightGridLayoutManager maxHeightGridLayoutManager = new MaxHeightGridLayoutManager(activity, 3, 1500);
        maxHeightGridLayoutManager.setOrientation(MaxHeightGridLayoutManager.VERTICAL);
        maxHeightGridLayoutManager.setSmoothScrollbarEnabled(true);

        int result = 0;
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = activity.getResources().getDimensionPixelSize(resourceId);
        }

        maxHeightGridLayoutManager.setMaxHeight(NailStarApplication.getApplication().getScreenHeight(activity)
                - activity.getResources().getDimensionPixelSize(R.dimen.index_tab_height)
                - activity.getResources().getDimensionPixelSize(R.dimen.common_title_bar_height)
                - activity.getResources().getDimensionPixelSize(R.dimen.common_main_tab_height) - result);
        view.setLayoutManager(maxHeightGridLayoutManager);

        return view;
    }
}
