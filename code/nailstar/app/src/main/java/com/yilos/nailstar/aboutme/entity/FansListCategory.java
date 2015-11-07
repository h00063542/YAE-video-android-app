package com.yilos.nailstar.aboutme.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sisilai on 15/11/6.
 */

public class FansListCategory {

    private String mCategoryName;
    private List<FansList> mCategoryItem = new ArrayList<>();

    public FansListCategory(String mCategroyName) {
        mCategoryName = mCategroyName;
    }

    public void addItem(FansList fansList) {
        mCategoryItem.add(fansList);
    }

    /**
     *  获取Item内容
     *
     * @param pPosition
     * @return
     */
    public FansList getItem(int pPosition) {
        return mCategoryItem.get(pPosition - 1);
    }

    public String getTitleItem() {
        return mCategoryName;
    }

    /**
     * 当前类别Item总数。Category也需要占用一个Item
     * @return
     */
    public int getItemCount() {
        return mCategoryItem.size() + 1;
    }

}