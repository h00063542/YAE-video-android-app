package com.yilos.widget.banner;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.yilos.widget.view.CViewPager;

import java.util.List;

/**
 * Created by yangdan on 15/10/21.
 */
public class Banner<T> extends CViewPager{
    private int viewCount = 0;

    private int interval = 5000; //默认播放间隔为5000毫秒

    private boolean playing = false;

    public Banner(Context context) {
        super(context);
    }

    public Banner(Context context, AttributeSet attrs) {
        super(context, attrs);

        setClipChildren(false);
        setOffscreenPageLimit(2);
    }

    public Banner<T> setViews(final ViewCreator creator, final List<T> data){
        viewCount = data.size();
        setAdapter(new PagerAdapter() {
            @Override
            public Object instantiateItem(ViewGroup container, final int position) {
                View view = creator.createView(getContext(), getRealPosition(position), data.get(getRealPosition(position)));
                if (view.getParent() != null) {
                    container.removeView(view);
                }
                container.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                return view;
            }

            @Override
            public int getCount() {
                return 2 * 1314000 * viewCount;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
            }

            private int getRealPosition(int position) {
                position = position % data.size();
                if (position < 0) {
                    position += data.size();
                }

                return position;
            }
        });

        setCurrentItem(1314000 * viewCount);
        startPlay();

        return this;
    }

    public Banner<T> startPlay() {
        playing = true;
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (playing) {
                    setCurrentItem(getCurrentItem() + 1);
                    startPlay();
                }
            }
        }, interval);

        return this;
    }

    public Banner<T> stopPlay() {
        playing = false;

        return this;
    }

    public Banner<T> setInterval(int interval){
        this.interval = interval;
        return this;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev){
        final int action = ev.getAction();

        switch (action & MotionEventCompat.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                stopPlay();
                break;
            }
            case MotionEvent.ACTION_UP:
                startPlay();
                break;
            default:
                break;
        }

        return super.onTouchEvent(ev);
    }

    public interface ViewCreator<T> {
        View createView(Context context, int position, T data);
    }
}
