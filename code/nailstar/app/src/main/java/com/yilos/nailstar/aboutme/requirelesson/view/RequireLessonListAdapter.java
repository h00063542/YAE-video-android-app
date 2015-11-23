package com.yilos.nailstar.aboutme.requirelesson.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.requirelesson.entity.RequireLesson;
import com.yilos.widget.view.ImageCacheView;

import java.util.ArrayList;

/**
 * Created by sisilai on 15/11/23.
 */
public class RequireLessonListAdapter extends RecyclerView.Adapter<RequireLessonListAdapter.ViewHolder> {
    private ArrayList<RequireLesson> requireLessonArrayList;
    private Context context;

    public RequireLessonListAdapter(Context context, ArrayList<RequireLesson> requireLessonArrayList) {
        this.context = context;
        this.requireLessonArrayList = requireLessonArrayList;
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

    @Override
    public void onBindViewHolder(final RequireLessonListAdapter.ViewHolder holder, int position) {
        final RequireLesson requireLesson = requireLessonArrayList.get(position);
        holder.imageCacheView.setImageSrc(requireLesson.getThumbUrl());
        String showNo = "第" + requireLesson.getNo() + "期";
        holder.textView.setText(showNo);
    }

    @Override
    public int getItemCount() {
        return requireLessonArrayList.size();
    }
}
