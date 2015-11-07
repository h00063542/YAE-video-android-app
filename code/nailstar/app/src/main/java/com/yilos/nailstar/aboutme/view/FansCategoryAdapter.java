package com.yilos.nailstar.aboutme.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.entity.FansList;
import com.yilos.nailstar.aboutme.entity.FansListCategory;

import java.util.ArrayList;

/**
 * Created by sisilai on 15/11/6.
 */

public class FansCategoryAdapter extends BaseAdapter {

    private static final int TYPE_CATEGORY_ITEM = 0;
    private static final int TYPE_ITEM = 1;

    private ArrayList<FansListCategory> mListData;
    private LayoutInflater mInflater;


    public FansCategoryAdapter(Context context, ArrayList<FansListCategory> pData) {
        mListData = pData;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        int count = 0;

        if (null != mListData) {
            //  所有分类中item的总和是ListVIew  Item的总个数
            for (FansListCategory fansListCategory : mListData) {
                count += fansListCategory.getItemCount();
            }
        }

        return count;
    }

    @Override
    public FansList getItem(int position) {

        // 异常情况处理
        if (null == mListData || position <  0|| position > getCount()) {
            return null;
        }

        // 同一分类内，第一个元素的索引值
        int categroyFirstIndex = 0;

        for (FansListCategory fansListCategory : mListData) {
            int size = fansListCategory.getItemCount();
            // 在当前分类中的索引值
            int categoryIndex = position - categroyFirstIndex;
            // item在当前分类内
            if (categoryIndex < size) {
                return  fansListCategory.getItem( categoryIndex );
            }

            // 索引移动到当前分类结尾，即下一个分类第一个元素索引
            categroyFirstIndex += size;
        }

        return null;
    }
    public String getTitleItem(int position) {

        // 异常情况处理
        if (null == mListData || position <  0|| position > getCount()) {
            return null;
        }

        // 同一分类内，第一个元素的索引值
        int categroyFirstIndex = 0;

        for (FansListCategory fansListCategory : mListData) {
            int size = fansListCategory.getItemCount();
            // 在当前分类中的索引值
            int categoryIndex = position - categroyFirstIndex;
            // item在当前分类内
            if (categoryIndex < size) {
                if (categoryIndex == 0) {
                    return  fansListCategory.getTitleItem();
                }
            }

            // 索引移动到当前分类结尾，即下一个分类第一个元素索引
            categroyFirstIndex += size;
        }

        return null;
    }

    @Override
    public int getItemViewType(int position) {
        // 异常情况处理
        if (null == mListData || position <  0|| position > getCount()) {
            return TYPE_ITEM;
        }


        int categroyFirstIndex = 0;

        for (FansListCategory fansListCategory : mListData) {
            int size = fansListCategory.getItemCount();
            // 在当前分类中的索引值
            int categoryIndex = position - categroyFirstIndex;
            if (categoryIndex == 0) {
                return TYPE_CATEGORY_ITEM;
            }

            categroyFirstIndex += size;
        }

        return TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case TYPE_CATEGORY_ITEM:
                if (null == convertView) {
                    convertView = mInflater.inflate(R.layout.activity_fans_list_item_title, null);
                }

                TextView textView = (TextView) convertView.findViewById(R.id.head_title);
                String  itemValue = getTitleItem(position);
                textView.setText(itemValue);
                break;

            case TYPE_ITEM:
                FansList fansList = getItem(position);
                ViewHolder viewHolder = null;
                if (null == convertView) {

                    convertView = mInflater.inflate(R.layout.activity_fans_list_item, null);
                    viewHolder = new ViewHolder();
                    viewHolder.nickName = (TextView) convertView.findViewById(R.id.fans_list_name);
                    viewHolder.photo = (ImageView) convertView.findViewById(R.id.fans_list_photo);
                    viewHolder.introduction = (TextView) convertView.findViewById(R.id.fans_list_introduction);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                // 绑定数据
                String nickName = fansList.getNickname();
                viewHolder.nickName.setText(nickName);
                viewHolder.introduction.setText(fansList.getProfile());
                viewHolder.photo.setImageBitmap(fansList.getImageBitmap());
                convertView.setTag(viewHolder);
                break;
        }

        return convertView;
    }


    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return getItemViewType(position) != TYPE_CATEGORY_ITEM;
    }


    private class ViewHolder {
        TextView nickName;
        ImageView photo;
        TextView introduction;
    }
}