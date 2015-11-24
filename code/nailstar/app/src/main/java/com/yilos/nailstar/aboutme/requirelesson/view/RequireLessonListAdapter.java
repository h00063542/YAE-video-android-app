package com.yilos.nailstar.aboutme.requirelesson.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.requirelesson.entity.RequireLesson;
import com.yilos.nailstar.framework.application.NailStarApplication;
import com.yilos.nailstar.util.Constants;
import com.yilos.widget.view.ImageCacheView;

import java.util.ArrayList;

/**
 * Created by sisilai on 15/11/23.
 */
public class RequireLessonListAdapter extends RecyclerView.Adapter<RequireLessonListAdapter.ViewHolder> {
    private ArrayList<RequireLesson> requireLessonArrayList;
    private Context context;
    private int imageWidth;

    public RequireLessonListAdapter(Context context, ArrayList<RequireLesson> requireLessonArrayList) {
        this.context = context;
        this.requireLessonArrayList = requireLessonArrayList;
        imageWidth = (NailStarApplication.getApplication().getScreenWidth((RequireLessonListActivity) context) - 3 * context.getResources().getDimensionPixelSize(R.dimen.common_10_dp)) / 2;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageCacheView imageCacheView;
        private TextView textView;

        public ViewHolder(View view) {
            super(view);
            imageCacheView = (ImageCacheView) view.findViewById(R.id.require_lesson_list_image);
            textView = (TextView) view.findViewById(R.id.require_lesson_list_no);
        }
    }

    @Override
    public RequireLessonListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_require_lesson_list_item, parent, false);

        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(final RequireLessonListAdapter.ViewHolder holder, int position) {
        final RequireLesson requireLesson = requireLessonArrayList.get(position);
        holder.imageCacheView.setImageSrc(requireLesson.getThumbUrl());
        int backColor = context.getResources().getColor(R.color.white,null);
        holder.imageCacheView.setBackgroundColor(backColor);
        holder.imageCacheView.getLayoutParams().height = (int) (imageWidth * 0.75);
        String di = context.getResources().getString(R.string.di);
        String qi = context.getResources().getString(R.string.qi);
        String showNo = di + requireLesson.getNo() + qi;
        holder.textView.setText(showNo);
    }

    @Override
    public int getItemCount() {
        return requireLessonArrayList.size();
    }
}
