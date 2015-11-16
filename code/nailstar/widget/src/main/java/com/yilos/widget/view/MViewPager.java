package com.yilos.widget.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;

import com.yilos.widget.banner.BannerAdapter;

/**
 * Created by yangdan on 15/10/29.
 * 优化系统ViewPager的一些处理
 * 1、ViewPager设置WRAP_CONTENT后不生效的问题
 * 2、配合MPagerAdapter实现循环播放
 */
public class MViewPager extends ViewPager{
    public MViewPager(Context context) {
        super(context);
    }

    public MViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 返回子view的高度，否则WRAP_CONTENT不生效
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        //下面遍历所有child的高度
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec,
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if (h > height) //采用最大的view的高度。
                height = h;
        }

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
                MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();

        if(action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
            requestDisallowParentInterceptTouchEvent(true);
        } else {
            requestDisallowParentInterceptTouchEvent(false);
        }

        return super.onTouchEvent(ev);
    }

    public void setAdapter(BannerAdapter adapter) {
        super.setAdapter(adapter);

        if(adapter.isLoop()) {
            setCurrentItem(adapter.getCount() / 2, false);
        }
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        if(adapter instanceof BannerAdapter) {
            setAdapter((BannerAdapter)adapter);
        } else {
            super.setAdapter(adapter);
        }
    }

    public void requestDisallowParentInterceptTouchEvent(boolean disallow){
        ViewParent parent = getParent();
        while (null != parent) {
            parent.requestDisallowInterceptTouchEvent(disallow);
            parent = parent.getParent();
        }
    }
}
