package com.yilos.nailstar.aboutme.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.entity.SystemMessage;
import com.yilos.nailstar.util.DateUtil;

import java.util.List;

/**
 * Created by sisilai on 15/11/21.
 */
public class SystemMessageListAdapter extends RecyclerView.Adapter<SystemMessageListAdapter.ViewHolder>{
    private List<SystemMessage> systemMessageList;
    private Context context;
    public SystemMessageListAdapter(Context context, List<SystemMessage> systemMessageList) {
        this.context = context;
        this.systemMessageList = systemMessageList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleText;
        public TextView contentText;
        public TextView timeText;

        public ViewHolder(View view) {
            super(view);
            titleText = (TextView) view.findViewById(R.id.title);
            contentText = (TextView) view.findViewById(R.id.content);
            timeText = (TextView) view.findViewById(R.id.time);
        }
    }
    @Override
    public SystemMessageListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_system_message_item, parent, false);

        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(SystemMessageListAdapter.ViewHolder holder, int position) {
        final SystemMessage systemMessage = systemMessageList.get(position);
        holder.titleText.setText(systemMessage.getTitle());
        holder.contentText.setText(systemMessage.getContent());
        long time = systemMessage.getPublishDate();
        holder.timeText.setText(DateUtil.getYearAndMonth(time));
    }

    @Override
    public int getItemCount() {
        return systemMessageList.size();
    }
}
