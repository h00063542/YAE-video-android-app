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

    private int currentPlayTimes = 0;

    public Banner(Context context) {
        super(context);
    }

    public Banner(Context context, AttributeSet attrs) {
        super(context, attrs);

        setClipChildren(false);
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

//    public Banner<T> setViews(final ViewCreator creator, final List<T> data){
//        viewCount = data == null ? 0 : data.size();
//        setOffscreenPageLimit((viewCount-1)/2);
//        setAdapter(new MPagerAdapter() {
//            @Override
//            public Object createItem(ViewGroup container, final int position) {
//                View view = creator.createView(getContext(), position, data.get(position));
//                if (view.getParent() != null) {
//                    container.removeView(view);
//                }
//                //view.setClickable(true);
//                container.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//                return view;
//            }
//
//            @Override
//            public boolean isLoop() {
//                return true;
//            }
//
//            @Override
//            public int getActualCount() {
//                return viewCount;
//            }
//
//            @Override
//            public boolean isViewFromObject(View view, Object object) {
//                return view == object;
//            }
//
//            @Override
//            public void destroyItem(ViewGroup container, int position, Object object) {
//            }
//        });
//
//        return this;
//    }

    public Banner<T> play() {
        if(playing) {
            return this;
        }

        playing = true;
        currentPlayTimes = (currentPlayTimes + 1) % 10000;
        delayPlay(currentPlayTimes);
        return this;
    }

    public Banner<T> stop() {
        playing = false;

        return this;
    }

    public Banner<T> setInterval(int interval){
        this.interval = interval;
        return this;
    }



    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        final int action = ev.getAction();

        switch (action & MotionEventCompat.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                stop();
                return super.dispatchTouchEvent(ev);
            }
            case MotionEvent.ACTION_MOVE:
                stop();
                return super.dispatchTouchEvent(ev);
            case MotionEvent.ACTION_UP:
                play();
                return super.dispatchTouchEvent(ev);
            default:
                play();
                break;
        }

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev){
        final int action = ev.getAction();

        switch (action & MotionEventCompat.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                stop();
                break;
            }
            case MotionEvent.ACTION_MOVE:
                stop();
                break;
            case MotionEvent.ACTION_UP:
                play();
                break;
            default:
                play();
                break;
        }

        return super.onTouchEvent(ev);
    }

    public interface ViewCreator<T> {
        //View createView(Context context, int position, T data);

        List<View> createViews();
    }

    private void delayPlay(final long playTimes){
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if(playing) {
                    if(playTimes == currentPlayTimes) {
                        playNext();
                        delayPlay(currentPlayTimes);
                    }
                }
            }
        }, interval);
    }

    private void playNext() {
        setCurrentItem(getCurrentItem() + 1);
    }
}
