package com.yilos.nailstar.mall.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yilos.nailstar.R;
import com.yilos.nailstar.mall.entity.Commodity;
import com.yilos.widget.banner.Banner;
import com.yilos.widget.view.ImageCacheView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by ganyue on 15/11/20.
 */
public class MallIndexCommodityListAdapter extends ArrayAdapter<Commodity> {

    static class ViewHolder {
        TextView titleView;
        TextView priceView;
        ImageCacheView imageView;
    }

    private final LayoutInflater mLayoutInflater;
    private final Random mRandom;
    private final ArrayList<Integer> mBackgroundColors;
    public MallIndexCommodityListAdapter(Context context) {
        super(context, 0);
        mLayoutInflater = LayoutInflater.from(context);
        mRandom = new Random();
        mBackgroundColors = new ArrayList<Integer>();
        mBackgroundColors.add(R.color.orange);
        mBackgroundColors.add(R.color.green);
        mBackgroundColors.add(R.color.grey);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Commodity item = (Commodity)getItem(position);
        //获取商品对象
        ViewHolder vh;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.mall_commodity_index_item, parent, false);
            vh = new ViewHolder();
            vh.titleView = (TextView) convertView.findViewById(R.id.index_commodity_name);
            vh.priceView = (TextView) convertView.findViewById(R.id.index_commodity_price);
            vh.imageView = (ImageCacheView) convertView.findViewById(R.id.index_commodity_item_image);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.titleView.setText(item.getName());
        vh.priceView.setText(String.valueOf(item.getPrice()));
        vh.imageView.setImageSrc(item.getImageUrl());
        vh.imageView.setAdjustViewBounds(true);
        vh.imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        return convertView;
    }



}
