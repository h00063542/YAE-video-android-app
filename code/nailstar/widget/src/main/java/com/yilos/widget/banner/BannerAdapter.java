package com.yilos.widget.banner;

import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

/**
 * Created by yangdan on 15/10/29.
 * 用来兼容处理可以循环播放的问题
 */
public abstract class BannerAdapter extends PagerAdapter {
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        return createItem(container, getRealPosition(position));
    }

    @Override
    public int getCount() {
        if(isLoop()) {
            return 2 * 100 * getActualCount();
        }
        else {
            return getActualCount();
        }
    }

    /**
     * 是否可以循环播放
     * @return
     */
    public abstract boolean isLoop();

    /**
     * 获取实际的Pager个数
     * @return
     */
    public abstract int getActualCount();

    /**
     * 创建项目
     * @param container
     * @param position
     * @return
     */
    public abstract Object createItem(ViewGroup container, final int position);

    /**
     * 获取真实的位置
     * @param position
     * @return
     */
    private int getRealPosition(int position) {
        if(getActualCount() <= 0){
            return position;
        }

        if(getActualCount() == 1) {
            //如果真实的个数为1，则必须提供3个view，因为同一个view不能同时被加到多个父容器中，所以应该返回原始值
            position = position % 3;
        } else if (getActualCount() == 2) {
            //如果真实的个数为1，则必须提供4个view，因为同一个view不能同时被加到多个父容器中，所以应该返回原始值
            position = position % 4;
        } else {
            position = position % getActualCount();
        }

        if (position < 0) {
            position += getActualCount();
        }

        return position;
    }
}
