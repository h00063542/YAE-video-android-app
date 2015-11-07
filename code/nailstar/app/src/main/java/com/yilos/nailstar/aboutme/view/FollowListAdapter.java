package com.yilos.nailstar.aboutme.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.entity.FollowList;
import java.util.List;

/**
 * Created by sisilai on 15/11/4.
 */
public class FollowListAdapter extends ArrayAdapter<FollowList> {

    private int resourceId;
    public FollowListAdapter(Context context, int textViewResourceId, List<FollowList> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        FollowList followList = getItem(position);
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        } else {
            view = convertView;
        }
        ImageView followListPhoto = (ImageView) view.findViewById(R.id.follow_list_photo);
        TextView followListName = (TextView) view.findViewById(R.id.follow_list_name);
        TextView followListIntroduction = (TextView) view.findViewById(R.id.follow_list_introduction);
        if (followList.getImageBitmap() == null) {
            followListPhoto.setImageResource(R.mipmap.ic_default_photo);
        } else {
            followListPhoto.setImageBitmap(followList.getImageBitmap());
        }
        followListName.setText(followList.getNickname());
        followListIntroduction.setText(followList.getProfile());
        return view;
    }
}