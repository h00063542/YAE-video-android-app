package com.yilos.widget.view;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by yangdan on 15/11/3.
 */
public class MaxHeightGridLayoutManager extends GridLayoutManager {

    private int maxHeight = 0;

    private boolean disableScroll = false;

    public MaxHeightGridLayoutManager(Context context, int spanCount, int maxHeight) {
        this(context, spanCount);
        this.maxHeight = maxHeight;
    }

    public MaxHeightGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public MaxHeightGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public MaxHeightGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    public void setDisableScroll(boolean disableScroll) {
        this.disableScroll = disableScroll;
    }

    public void setMaxHeight(int maxHeight) {
        if(maxHeight != this.maxHeight) {
            this.maxHeight = maxHeight;
            requestLayout();
        }
    }

    @Override
    public boolean canScrollVertically() {
        return getOrientation() == VERTICAL && !disableScroll;
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        int measuredWidth = View.MeasureSpec.getSize(widthSpec);
        setMeasuredDimension(measuredWidth, maxHeight);

//        if(getItemCount() <= 0) {
//            super.onMeasure(recycler, state, widthSpec, heightSpec);
//            return;
//        }
//
//        View view = recycler.getViewForPosition(0);
//
//        if(view != null) {
//            measureChild(view, widthSpec, heightSpec);
//            int measuredWidth = View.MeasureSpec.getSize(widthSpec);
//            int measuredHeight = view.getMeasuredHeight() * (getChildCount() / getSpanCount() + 1);
//            if(maxHeight > 0) {
//                if(measuredHeight > maxHeight) {
//                    measuredHeight = maxHeight;
//                }
//            }
//
//            setMeasuredDimension(measuredWidth, measuredHeight);
//        }
    }
}
