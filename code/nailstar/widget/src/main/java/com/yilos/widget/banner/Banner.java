package com.yilos.widget.banner;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.yilos.widget.view.MViewPager;

import java.util.List;

/**
 * Created by yangdan on 15/10/21.
 */
public class Banner<T> extends MViewPager {
    /**
     * Banner的view创建器
     */
    private ViewCreator viewCreator;

    private List<View> views;

    private int viewCount = 0;

    private int interval = 5000; //默认播放间隔为5000毫秒

    private boolean playing = false;

    private boolean pause = false;

    private int currentPlayTimes = 0;

    private long lastPlayTime = 0;

    private long pauseTime = 0;

    private long pausedTime = 0;

    private long touchDownTime = 0;

    public Banner(Context context) {
        super(context);
    }

    public Banner(Context context, AttributeSet attrs) {
        super(context, attrs);

        setClipChildren(false);
    }

    public Banner<T> play() {
        if(playing) {
            return this;
        }

        playing = true;
        lastPlayTime = System.currentTimeMillis();
        currentPlayTimes = (currentPlayTimes + 1) % 10000;
        delayPlay(currentPlayTimes);
        return this;
    }

    public void resume() {
        pause = false;
        pauseTime += (System.currentTimeMillis() - pausedTime);
        currentPlayTimes = (currentPlayTimes + 1) % 10000;

        delayPlay(currentPlayTimes);
    }

    public Banner<T> stop() {
        playing = false;

        return this;
    }

    public void pause() {
        pause = true;
        pausedTime = System.currentTimeMillis();
    }

    public void setViewCreator(ViewCreator viewCreator) {
        this.viewCreator = viewCreator;
        views = viewCreator.createViews();

        viewCount = views == null ? 0 : views.size();
        setOffscreenPageLimit((viewCount-1)/2);

        if(viewCount == 1) {
            views.addAll(viewCreator.createViews());
            views.addAll(viewCreator.createViews());
        } else if(viewCount == 2) {
            views.addAll(viewCreator.createViews());
        }

        setAdapter(new BannerAdapter() {
            @Override
            public Object createItem(ViewGroup container, final int position) {
                View view = views.get(position);

                if (view.getParent() != null) {
                    container.removeView(view);
                }
                //view.setClickable(true);
                container.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                return view;
            }

            @Override
            public boolean isLoop() {
                return true;
            }

            @Override
            public int getActualCount() {
                return viewCount;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
            }
        });
    }

    public Banner<T> setInterval(int interval){
        this.interval = interval;
        return this;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        final int action = ev.getAction();

        switch (action & MotionEventCompat.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                pause();
                touchDownTime = System.currentTimeMillis();
                return super.dispatchTouchEvent(ev);
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                resume();
                if(System.currentTimeMillis() - touchDownTime > 500) {
                    return true;
                }
                return super.dispatchTouchEvent(ev);
            default:
                resume();
                break;
        }

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev){
        final int action = ev.getAction();

        switch (action & MotionEventCompat.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                pause();
                break;
            }
            case MotionEvent.ACTION_MOVE:
                //pause();
                break;
            case MotionEvent.ACTION_UP:
                resume();
                break;
            default:
                resume();
                break;
        }

        return super.onTouchEvent(ev);
    }

    public interface ViewCreator<T> {
        List<View> createViews();
    }

    private void delayPlay(final long playTimes){
        if(pauseTime > 0) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    playAndNext(playTimes);
                }
            }, interval - pauseTime);
        } else {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    playAndNext(playTimes);
                }
            }, interval);
        }
    }

    private void playAndNext(final long playTimes) {
        if(!playing) {
            return;
        }

        if(playTimes == currentPlayTimes && !pause) {
            playNext();
            delayPlay(currentPlayTimes);
        }
    }

    private void playNext() {
        setCurrentItem(getCurrentItem() + 1, true);

        lastPlayTime = System.currentTimeMillis();
        pauseTime = 0;
    }
}
