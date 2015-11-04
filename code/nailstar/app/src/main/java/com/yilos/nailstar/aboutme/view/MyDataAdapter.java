package com.yilos.nailstar.aboutme.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.entity.MyData;

import java.util.List;

/**
 * Created by sisilai on 15/11/4.
 */
public class MyDataAdapter extends ArrayAdapter<MyData> {

    private int resourceId;

    public MyDataAdapter(Context context, int textViewResourceId, List<MyData> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        MyData data = getItem(position);
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        } else {
            view = convertView;
        }
        ImageView dataImageView = (ImageView) view.findViewById(R.id.myDataImage);
        TextView dataStringTextView = (TextView) view.findViewById(R.id.myDataString);
        dataImageView.setImageResource(data.getImageId());
        dataStringTextView.setText(data.getDataString());
        return view;
    }

}