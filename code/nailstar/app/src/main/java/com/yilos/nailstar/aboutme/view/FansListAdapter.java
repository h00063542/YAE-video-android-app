package com.yilos.nailstar.aboutme.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.entity.FansList;

import java.util.List;

/**
 * Created by sisilai on 15/11/5.
 */

public class FansListAdapter extends ArrayAdapter<FansList> {

    private int resourceId;
    public FansListAdapter(Context context, int textViewResourceId, List<FansList> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        FansList fansList = getItem(position);
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        } else {
            view = convertView;
        }
        ImageView followListPhoto = (ImageView) view.findViewById(R.id.fans_list_photo);
        TextView followListName = (TextView) view.findViewById(R.id.fans_list_name);
        TextView followListIntroduction = (TextView) view.findViewById(R.id.fans_list_introduction);
        if (fansList.getImageBitmap() == null) {
            followListPhoto.setImageResource(R.mipmap.ic_default_photo);
        } else {
            followListPhoto.setImageBitmap(fansList.getImageBitmap());
        }
        followListName.setText(fansList.getNickname());
        followListIntroduction.setText(fansList.getProfile());
        return view;
    }
}