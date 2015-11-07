package com.yilos.widget.pageindicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yilos.widget.R;

/**
 * Created by yangdan on 15/11/3.
 */
public class TabPageIndicator extends HorizontalScrollView implements ViewPager.OnPageChangeListener{
    /** Title text used when no title is provided by the adapter. */
    private static final CharSequence EMPTY_TITLE = "";

    /**
     * Interface for a callback when the selected tab has been reselected.
     */
    public interface OnTabReselectedListener {
        /**
         * Callback when the selected tab has been reselected.
         *
         * @param position Position of the current center item.
         */
        void onTabReselected(int position);
    }

    private Runnable mTabSelector;

    private final OnClickListener mTabClickListener = new OnClickListener() {
        public void onClick(View view) {
            TabView tabView = (TabView)view;
            final int oldSelected = mViewPager.getCurrentItem();
            final int newSelected = tabView.getIndex();
            mViewPager.setCurrentItem(newSelected);
            if (oldSelected == newSelected && mTabReselectedListener != null) {
                mTabReselectedListener.onTabReselected(newSelected);
            }
        }
    };

    private final IcsLinearLayout mTabLayout;

    private ViewPager mViewPager;
    private ViewPager.OnPageChangeListener mListener;

    private int mMaxTabWidth;
    private int mSelectedTabIndex;

    private OnTabReselectedListener mTabReselectedListener;

    public TabPageIndicator(Context context) {
        this(context, null);
    }

    public TabPageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        setHorizontalScrollBarEnabled(false);

        mTabLayout = new IcsLinearLayout(context, R.attr.vpiTabPageIndicatorStyle);
        addView(mTabLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void setOnTabReselectedListener(OnTabReselectedListener listener) {
        mTabReselectedListener = listener;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final boolean lockedExpanded = widthMode == MeasureSpec.EXACTLY;
        setFillViewport(lockedExpanded);

        final int childCount = mTabLayout.getChildCount();
        if (childCount > 1 && (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST)) {
            if (childCount > 2) {
                mMaxTabWidth = (int)(MeasureSpec.getSize(widthMeasureSpec) * 0.4f);
            } else {
                mMaxTabWidth = MeasureSpec.getSize(widthMeasureSpec) / 2;
            }
        } else {
            mMaxTabWidth = -1;
        }

        final int oldWidth = getMeasuredWidth();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int newWidth = getMeasuredWidth();

        if (lockedExpanded && oldWidth != newWidth) {
            // Recenter the tab display if we're at a new (scrollable) size.
            setCurrentItem(mSelectedTabIndex);
        }
    }

    private void animateToTab(final int position) {
        final View tabView = mTabLayout.getChildAt(position);
        if (mTabSelector != null) {
            removeCallbacks(mTabSelector);
        }
        mTabSelector = new Runnable() {
            public void run() {
                final int scrollPos = tabView.getLeft() - (getWidth() - tabView.getWidth()) / 2;
                smoothScrollTo(scrollPos, 0);
                mTabSelector = null;
            }
        };
        post(mTabSelector);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mTabSelector != null) {
            // Re-post the selector we saved
            post(mTabSelector);
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mTabSelector != null) {
            removeCallbacks(mTabSelector);
        }
    }

    private void addTab(int index, CharSequence text, int iconResId) {
        final TabView tabView = new TabView(getContext());
        tabView.mIndex = index;
        tabView.setFocusable(true);
        tabView.setOnClickListener(mTabClickListener);
        tabView.setText(text);

        if (iconResId != 0) {
            tabView.setCompoundDrawablesWithIntrinsicBounds(iconResId, 0, 0, 0);
        }

        mTabLayout.addView(tabView, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1));
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        if (mListener != null) {
            mListener.onPageScrollStateChanged(arg0);
        }
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        if (mListener != null) {
            mListener.onPageScrolled(arg0, arg1, arg2);
        }
    }

    @Override
    public void onPageSelected(int arg0) {
        setCurrentItem(arg0);
        if (mListener != null) {
            mListener.onPageSelected(arg0);
        }
    }

    //@Override
    public void setViewPager(ViewPager view) {
        if (mViewPager == view) {
            return;
        }
        if (mViewPager != null) {
            mViewPager.setOnPageChangeListener(null);
        }
        final PagerAdapter adapter = view.getAdapter();
        if (adapter == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        mViewPager = view;
        view.setOnPageChangeListener(this);
        notifyDataSetChanged();
    }

    public void notifyDataSetChanged() {
        mTabLayout.removeAllViews();
        PagerAdapter adapter = mViewPager.getAdapter();
//        IconPagerAdapter iconAdapter = null;
//        if (adapter instanceof IconPagerAdapter) {
//            iconAdapter = (IconPagerAdapter)adapter;
//        }
        final int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            CharSequence title = adapter.getPageTitle(i);
            if (title == null) {
                title = EMPTY_TITLE;
            }
            int iconResId = 0;
//            if (iconAdapter != null) {
//                iconResId = iconAdapter.getIconResId(i);
//            }
            addTab(i, title, iconResId);
        }
        if (mSelectedTabIndex > count) {
            mSelectedTabIndex = count - 1;
        }
        setCurrentItem(mSelectedTabIndex);
        requestLayout();
    }

    //@Override
    public void setViewPager(ViewPager view, int initialPosition) {
        setViewPager(view);
        setCurrentItem(initialPosition);
    }

    //@Override
    public void setCurrentItem(int item) {
        if (mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }
        mSelectedTabIndex = item;
        mViewPager.setCurrentItem(item);

        final int tabCount = mTabLayout.getChildCount();
        for (int i = 0; i < tabCount; i++) {
            final View child = mTabLayout.getChildAt(i);
            final boolean isSelected = (i == item);
            child.setSelected(isSelected);
            if (isSelected) {
                animateToTab(item);
            }
        }
    }

    //@Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mListener = listener;
    }

    private class TabView extends TextView {
        private int mIndex;

        public TabView(Context context) {
            super(context, null, R.attr.vpiTabPageIndicatorStyle);
        }

        @Override
        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

            // Re-measure if we went beyond our maximum size.
            if (mMaxTabWidth > 0 && getMeasuredWidth() > mMaxTabWidth) {
                super.onMeasure(MeasureSpec.makeMeasureSpec(mMaxTabWidth, MeasureSpec.EXACTLY),
                        heightMeasureSpec);
            }
        }

        public int getIndex() {
            return mIndex;
        }
    }

    class IcsLinearLayout extends LinearLayout {
        private final int[] LL = new int[] {
        /* 0 */ android.R.attr.divider,
        /* 1 */ android.R.attr.showDividers,
        /* 2 */ android.R.attr.dividerPadding,
        };
        private static final int LL_DIVIDER = 0;
        private static final int LL_SHOW_DIVIDER = 1;
        private static final int LL_DIVIDER_PADDING = 2;

        private Drawable mDivider;
        private int mDividerWidth;
        private int mDividerHeight;
        private int mShowDividers;
        private int mDividerPadding;


        public IcsLinearLayout(Context context, int themeAttr) {
            super(context);

            TypedArray a = context.obtainStyledAttributes(null, LL, themeAttr, 0);
            setDividerDrawable(a.getDrawable(IcsLinearLayout.LL_DIVIDER));
            mDividerPadding = a.getDimensionPixelSize(LL_DIVIDER_PADDING, 0);
            mShowDividers = a.getInteger(LL_SHOW_DIVIDER, SHOW_DIVIDER_NONE);
            a.recycle();
        }

        public void setDividerDrawable(Drawable divider) {
            if (divider == mDivider) {
                return;
            }
            mDivider = divider;
            if (divider != null) {
                mDividerWidth = divider.getIntrinsicWidth();
                mDividerHeight = divider.getIntrinsicHeight();
            } else {
                mDividerWidth = 0;
                mDividerHeight = 0;
            }
            setWillNotDraw(divider == null);
            requestLayout();
        }

        @Override
        protected void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
            final int index = indexOfChild(child);
            final int orientation = getOrientation();
            final LayoutParams params = (LayoutParams) child.getLayoutParams();
            if (hasDividerBeforeChildAt(index)) {
                if (orientation == VERTICAL) {
                    //Account for the divider by pushing everything up
                    params.topMargin = mDividerHeight;
                } else {
                    //Account for the divider by pushing everything left
                    params.leftMargin = mDividerWidth;
                }
            }

            final int count = getChildCount();
            if (index == count - 1) {
                if (hasDividerBeforeChildAt(count)) {
                    if (orientation == VERTICAL) {
                        params.bottomMargin = mDividerHeight;
                    } else {
                        params.rightMargin = mDividerWidth;
                    }
                }
            }
            super.measureChildWithMargins(child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            if (mDivider != null) {
                if (getOrientation() == VERTICAL) {
                    drawDividersVertical(canvas);
                } else {
                    drawDividersHorizontal(canvas);
                }
            }
            super.onDraw(canvas);
        }

        private void drawDividersVertical(Canvas canvas) {
            final int count = getChildCount();
            for (int i = 0; i < count; i++) {
                final View child = getChildAt(i);

                if (child != null && child.getVisibility() != GONE) {
                    if (hasDividerBeforeChildAt(i)) {
                        final android.widget.LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) child.getLayoutParams();
                        final int top = child.getTop() - lp.topMargin/* - mDividerHeight*/;
                        drawHorizontalDivider(canvas, top);
                    }
                }
            }

            if (hasDividerBeforeChildAt(count)) {
                final View child = getChildAt(count - 1);
                int bottom = 0;
                if (child == null) {
                    bottom = getHeight() - getPaddingBottom() - mDividerHeight;
                } else {
                    //final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                    bottom = child.getBottom()/* + lp.bottomMargin*/;
                }
                drawHorizontalDivider(canvas, bottom);
            }
        }

        private void drawDividersHorizontal(Canvas canvas) {
            final int count = getChildCount();
            for (int i = 0; i < count; i++) {
                final View child = getChildAt(i);

                if (child != null && child.getVisibility() != GONE) {
                    if (hasDividerBeforeChildAt(i)) {
                        final android.widget.LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) child.getLayoutParams();
                        final int left = child.getLeft() - lp.leftMargin/* - mDividerWidth*/;
                        drawVerticalDivider(canvas, left);
                    }
                }
            }

            if (hasDividerBeforeChildAt(count)) {
                final View child = getChildAt(count - 1);
                int right = 0;
                if (child == null) {
                    right = getWidth() - getPaddingRight() - mDividerWidth;
                } else {
                    //final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                    right = child.getRight()/* + lp.rightMargin*/;
                }
                drawVerticalDivider(canvas, right);
            }
        }

        private void drawHorizontalDivider(Canvas canvas, int top) {
            mDivider.setBounds(getPaddingLeft() + mDividerPadding, top,
                    getWidth() - getPaddingRight() - mDividerPadding, top + mDividerHeight);
            mDivider.draw(canvas);
        }

        private void drawVerticalDivider(Canvas canvas, int left) {
            mDivider.setBounds(left, getPaddingTop() + mDividerPadding,
                    left + mDividerWidth, getHeight() - getPaddingBottom() - mDividerPadding);
            mDivider.draw(canvas);
        }

        private boolean hasDividerBeforeChildAt(int childIndex) {
            if (childIndex == 0 || childIndex == getChildCount()) {
                return false;
            }
            if ((mShowDividers & SHOW_DIVIDER_MIDDLE) != 0) {
                boolean hasVisibleViewBefore = false;
                for (int i = childIndex - 1; i >= 0; i--) {
                    if (getChildAt(i).getVisibility() != GONE) {
                        hasVisibleViewBefore = true;
                        break;
                    }
                }
                return hasVisibleViewBefore;
            }
            return false;
        }
    }
}
