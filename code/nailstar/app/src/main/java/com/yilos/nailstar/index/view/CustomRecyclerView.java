package com.yilos.nailstar.index.view;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;

/**
 * Created by yangdan on 15/11/4.
 */
public class CustomRecyclerView extends RecyclerView {

    public CustomRecyclerView(Context context) {
        this(context, null);
    }

    public CustomRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean reachTop() {
        GridLayoutManager gridLayoutManager = (GridLayoutManager) getLayoutManager();
        return gridLayoutManager.findFirstVisibleItemPosition() == 0;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();

        if(action == MotionEvent.ACTION_DOWN) {
            requestDisallowParentInterceptTouchEvent(true);
        }
        else if(action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            requestDisallowParentInterceptTouchEvent(false);
        }

        return super.onInterceptTouchEvent(ev);
    }

    public void requestDisallowParentInterceptTouchEvent(boolean disallow){
        ViewParent parent = getParent();
        while (null != parent) {
            parent.requestDisallowInterceptTouchEvent(disallow);
            parent = parent.getParent();
        }
    }
}
