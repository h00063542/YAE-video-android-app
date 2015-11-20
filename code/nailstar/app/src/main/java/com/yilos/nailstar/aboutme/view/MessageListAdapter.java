package com.yilos.nailstar.aboutme.view;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.yilos.nailstar.index.view.CustomRecyclerView;

/**
 * Created by sisilai on 15/11/18.
 */
public class MessageListAdapter extends PagerAdapter {
    private Activity activity;

    // 回复我的、系统消息显示控件
    private CustomRecyclerView userMessageListView;
    private CustomRecyclerView SystemMessageListView;

    public MessageListAdapter(Activity activity) {
        this.activity = activity;

        // 初始化回复我的、系统消息Tab页
        userMessageListView = initMessageRecycleView();
        SystemMessageListView = initMessageRecycleView();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        RecyclerView newView = null;
        switch (position) {
            case 0:
                newView = userMessageListView;
                break;
            case 1:
                newView = SystemMessageListView;
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
        return 2;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "回复我的";
            case 1:
                return "系统消息";
            default:
                return "";
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }

    public CustomRecyclerView getUserMessageListView() {
        return userMessageListView;
    }

    public CustomRecyclerView getSystemMessageListView() {
        return SystemMessageListView;
    }

    private CustomRecyclerView initMessageRecycleView() {
        final CustomRecyclerView view = new CustomRecyclerView(activity);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        view.setLayoutManager(linearLayoutManager);
        return view;
    }
}

