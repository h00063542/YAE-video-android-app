package com.yilos.nailstar.aboutme.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.entity.SystemMessage;
import com.yilos.nailstar.aboutme.entity.UserMessage;
import com.yilos.nailstar.topic.view.TopicVideoPlayerActivity;
import com.yilos.nailstar.util.Constants;
import com.yilos.nailstar.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sisilai on 15/11/21.
 */
public class SystemMessageListAdapter extends RecyclerView.Adapter<SystemMessageListAdapter.ViewHolder>{
    private ArrayList<SystemMessage> systemMessageList;
    private Context context;
    public SystemMessageListAdapter(Context context, ArrayList<SystemMessage> systemMessageList) {
        this.context = context;
        this.systemMessageList = systemMessageList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleText;
        public TextView contentText;
        public TextView timeText;
        public LinearLayout systemMessageItem;

        public ViewHolder(View view) {
            super(view);
            titleText = (TextView) view.findViewById(R.id.title);
            contentText = (TextView) view.findViewById(R.id.content);
            timeText = (TextView) view.findViewById(R.id.time);
            systemMessageItem = (LinearLayout) view.findViewById(R.id.system_message_item);
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
    public void onBindViewHolder(final SystemMessageListAdapter.ViewHolder holder, int position) {
        final SystemMessage systemMessage = systemMessageList.get(position);
        holder.titleText.setText(systemMessage.getTitle());
        holder.contentText.setText(systemMessage.getContent());
        long time = systemMessage.getPublishDate();
        holder.timeText.setText(DateUtil.getYearAndMonth(time));
        int colorZ2 = context.getResources().getColor(R.color.z2);
        int colorZ3 = context.getResources().getColor(R.color.z3);
        if (systemMessage.getHasBeenRead()) {
            holder.titleText.setTextColor(colorZ3);
            holder.contentText.setTextColor(colorZ3);
        } else {
            holder.titleText.setTextColor(colorZ2);
            holder.contentText.setTextColor(colorZ2);
        }

        holder.systemMessageItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (SystemMessage systemMessage1 : systemMessageList) {
                    if (systemMessage.getId() == systemMessage1.getId()) {
                        systemMessage1.setHasBeenRead(true);
                        break;
                    }
                }
                ((MessageActivity) context).setLocalSystemMessage(systemMessageList,Constants.COVER);
                ((MessageActivity) context).initSystemMessageList(systemMessageList);
                Intent intent = new Intent(context, TopicVideoPlayerActivity.class);
                intent.putExtra(Constants.TOPIC_ID, systemMessage.getTopicId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return systemMessageList.size();
    }
}
