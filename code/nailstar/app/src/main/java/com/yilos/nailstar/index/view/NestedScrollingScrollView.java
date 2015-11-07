package com.yilos.nailstar.index.view;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
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

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        if(Build.VERSION.SDK_INT < 21) {
//            if(ev.getAction() == MotionEvent.ACTION_DOWN) {
//                if(!super.onInterceptTouchEvent(ev)) {
//                    super.onTouchEvent(ev);
//                    return false;
//                }
//            }
//        }
//
//        if(ev.getY() > 1100)
//        return false;
//        else return super.onInterceptTouchEvent(ev);
//    }

    /**
     * NestedScrollingParent接口
     * @param child
     * @param target
     * @param nestedScrollAxes
     * @return
     */
    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }
//
//    @Override
//    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
//        nestedScrollingParentHelper.onNestedScrollAccepted(child, target, nestedScrollAxes);
//        startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
//    }
//
//    @Override
//    public void onStopNestedScroll(View target) {
//        stopNestedScroll();
//    }
//
//    @Override
//    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed,
//                               int dyUnconsumed) {
//        final int oldScrollY = getScrollY();
//        scrollBy(0, dyUnconsumed);
//        final int myConsumed = getScrollY() - oldScrollY;
//        final int myUnconsumed = dyUnconsumed - myConsumed;
//        dispatchNestedScroll(0, myConsumed, 0, myUnconsumed, null);
//    }
//
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


    /**
     * NestedScrollChild接口
     * @param enabled
     */
//    @Override
//    public void setNestedScrollingEnabled(boolean enabled) {
//        nestedScrollingChildHelper.setNestedScrollingEnabled(enabled);
//    }
//
//    @Override
//    public boolean isNestedScrollingEnabled() {
//        return nestedScrollingChildHelper.isNestedScrollingEnabled();
//    }
//
//    @Override
//    public boolean startNestedScroll(int axes) {
//        return nestedScrollingChildHelper.startNestedScroll(axes);
//    }
//
//    @Override
//    public void stopNestedScroll() {
//        nestedScrollingChildHelper.stopNestedScroll();
//    }
//
//    @Override
//    public boolean hasNestedScrollingParent() {
//        return nestedScrollingChildHelper.hasNestedScrollingParent();
//    }
//
//    @Override
//    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed,
//                                        int dyUnconsumed, int[] offsetInWindow) {
//        return nestedScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed,
//                offsetInWindow);
//    }
//
//    @Override
//    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
//        return nestedScrollingChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
//    }
//
//    @Override
//    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
//        return nestedScrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
//    }
//
//    @Override
//    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
//        return nestedScrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY);
//    }

    public interface ScrollViewListener {

        void onScrollChanged(NestedScrollingScrollView scrollView, int x, int y, int oldx, int oldy, int xRange, int yRange);

    }
}
