package com.yilos.nailstar.index.view;

import android.content.Context;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * Created by yangdan on 15/11/4.
 */
public class NestedScrollingScrollView extends NestedScrollView {
    private int maxScrollY = 0;
    private int mTouchSlop;
    private NestedScrollingParentHelper nestedScrollingParentHelper;
    private NestedScrollingChildHelper nestedScrollingChildHelper;
    private float downY;

    private ScrollViewListener scrollViewListener = null;

    public NestedScrollingScrollView(Context context) {
        this(context, null);
    }

    public NestedScrollingScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NestedScrollingScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        nestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        nestedScrollingChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    public void setMaxScrollY(int maxScrollY) {
        this.maxScrollY = maxScrollY;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy, computeHorizontalScrollRange(), computeVerticalScrollRange());
        }
    }

    @Override
    public int getNestedScrollAxes() {
        return 0;
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        if(dy > 0) {
            if(getScrollY() + getHeight() < computeVerticalScrollRange()) {
                int y = dy;
//                if(getScrollY() + dy > maxScrollY) {
//                    y = maxScrollY - getScrollY();
//                }
                if(getScrollY() + getHeight() + dy > computeVerticalScrollRange()) {
                    y = computeVerticalScrollRange() - getHeight() - getScrollY();
                }
                if(y < 0) {
                    y = 0;
                }
                scrollBy(0, y);
                consumed[1] = y;
            }
        }
        else if(dy < 0) {
            if(target instanceof CustomRecyclerView) {
                if(((CustomRecyclerView)target).getChildCount() <= 0) {
                    scrollBy(0, dy);
                }
            }
        }
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        //向上滚
        if(velocityY < 0 && getScrollY() > 0) {
            // 子视图已经滚到最上面了
            if(target instanceof CustomRecyclerView && ((CustomRecyclerView)target).reachTop()) {
                fling((int)velocityY);
                return true;
            }
        }
        // 向下滚
        else if(velocityY > 0) {
            // 滚动条尚未到底
            if(getScrollY() + getHeight() < computeVerticalScrollRange()) {
                fling((int)velocityY);
                if(getScrollY() + getHeight() < computeVerticalScrollRange()) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        if(velocityY < 0 && getScrollY() > 0) {
            if(!consumed) {
                fling((int)velocityY);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();

        if(action == MotionEvent.ACTION_DOWN) {
            downY = ev.getY();
            super.onInterceptTouchEvent(ev);
            return false;
        } else if(action == MotionEvent.ACTION_MOVE) {
            if(Math.abs(ev.getY() - downY) > 100) {
                return super.onInterceptTouchEvent(ev);
            } else {
                return false;
            }
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        //空实现
    }

    public interface ScrollViewListener {

        void onScrollChanged(NestedScrollingScrollView scrollView, int x, int y, int oldx, int oldy, int xRange, int yRange);

    }
}
