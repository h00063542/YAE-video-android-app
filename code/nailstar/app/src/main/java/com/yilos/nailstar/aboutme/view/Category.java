package com.yilos.nailstar.aboutme.view;

import com.yilos.nailstar.aboutme.entity.FansList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sisilai on 15/11/6.
 */

public class Category {

    private String mCategoryName;
    private List<FansList> mCategoryItem = new ArrayList<>();

    public Category(String mCategroyName) {
        mCategoryName = mCategroyName;
    }

    public String getmCategoryName() {
        return mCategoryName;
    }

//    public void addItem(String pItemName) {
//        mCategoryItem.add(pItemName);
//    }

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
        // Category排在第一位
//        if (pPosition == 0) {
//            return mCategoryItem.get(pPosition);
//        } else {
        return mCategoryItem.get(pPosition - 1);
    }

    public String getTitleItem(int pPosition) {
        // Category排在第一位
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