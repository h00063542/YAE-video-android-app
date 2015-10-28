package com.yilos.widget.pullrefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by yangdan on 15/10/20.
 */
public class PullRefreshLayout extends PtrFrameLayout {
    private PullRefreshHeader pullRefreshHeader;
    private boolean disallowInterceptTouchEvent = false;

    public PullRefreshLayout(Context context) {
        super(context);
        this.initViews();
    }

    public PullRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initViews();
    }

    public PullRefreshLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.initViews();
    }

    private void initViews() {
        disableWhenHorizontalMove(true);
        this.pullRefreshHeader = new PullRefreshHeader(this.getContext());
        this.setHeaderView(this.pullRefreshHeader);
        this.addPtrUIHandler(this.pullRefreshHeader);
    }

    public PullRefreshHeader getHeader() {
        return this.pullRefreshHeader;
    }

    public void setLastUpdateTimeKey(String key) {
        if(this.pullRefreshHeader != null) {
            this.pullRefreshHeader.setLastUpdateTimeKey(key);
        }

    }

    public void setLastUpdateTimeRelateObject(Object object) {
        if(this.pullRefreshHeader != null) {
            this.pullRefreshHeader.setLastUpdateTimeRelateObject(object);
        }

    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        disallowInterceptTouchEvent = disallowIntercept;
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        if (disallowInterceptTouchEvent) {
            return dispatchTouchEventSupper(e);
        }
        return super.dispatchTouchEvent(e);
    }
}
