package com.yilos.nailstar.aboutme.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.entity.UserMessage;

import java.util.List;

/**
 * Created by sisilai on 15/11/18.
 */
public class UserMessageListAdapter extends RecyclerView.Adapter<UserMessageListAdapter.ViewHolder>{
    private List<UserMessage> userMessageList;
    private Context context;
    public UserMessageListAdapter(Context context, List<UserMessage> userMessageList) {
                 this.context = context;
                 this.userMessageList = userMessageList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.textView1);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_user_message_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(userMessageList.get(position).getId());
    }


    @Override
    public int getItemCount() {
        return userMessageList.size();
    }

}