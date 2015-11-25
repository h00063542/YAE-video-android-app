package com.yilos.nailstar.aboutme.view;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.yilos.nailstar.R;
import com.yilos.nailstar.index.view.CustomRecyclerView;

/**
 * Created by sisilai on 15/11/18.
 */
public class MessageListAdapter extends PagerAdapter {
    private Activity activity;

    // 回复我的、系统消息显示控件
    private View userMessageListView;
    private View systemMessageListView;

    public MessageListAdapter(Activity activity) {
        this.activity = activity;
        // 初始化回复我的、系统消息Tab页
        userMessageListView = initMessageRecycleView();
        systemMessageListView = initMessageRecycleView();
    }

    public void showEmptySystemMessageView() {
        systemMessageListView.findViewById(R.id.message_list_empty).setVisibility(View.VISIBLE);
        systemMessageListView.findViewById(R.id.message_list).setVisibility(View.GONE);
    }

    public void showEmptyUserMessageView() {
        userMessageListView.findViewById(R.id.message_list_empty).setVisibility(View.VISIBLE);
        userMessageListView.findViewById(R.id.message_list).setVisibility(View.GONE);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View newView;
        switch (position) {
            case 0:
                newView = userMessageListView;
                break;
            case 1:
                newView = systemMessageListView;
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
                return activity.getResources().getString(R.string.reply_me);
            case 1:
                return activity.getResources().getString(R.string.system_message);
            default:
                return "";
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }

    public CustomRecyclerView getUserMessageListView() {
        return (CustomRecyclerView) userMessageListView.findViewById(R.id.message_list);
    }

    public CustomRecyclerView getSystemMessageListView() {
        return (CustomRecyclerView) systemMessageListView.findViewById(R.id.message_list);
    }

    private View initMessageRecycleView() {
        return activity.getLayoutInflater().inflate(R.layout.activity_message_list, null);
    }
}

