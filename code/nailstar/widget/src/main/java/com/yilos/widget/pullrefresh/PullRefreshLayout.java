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
    private float downY = 0;

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
    public boolean dispatchTouchEvent(MotionEvent e) {
        int action = e.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downY = e.getY();
                return super.dispatchTouchEvent(e);
            case MotionEvent.ACTION_MOVE:
                // 下拉的距离较大时，才执行下拉刷新，以保证容器内的横向滚动获得更高的优先级
                if(e.getY() - downY > 100) {
                    return super.dispatchTouchEvent(e);
                } else {
                    return dispatchTouchEventSupper(e);
                }
            default:
                break;
        }
        return super.dispatchTouchEvent(e);
    }
}
