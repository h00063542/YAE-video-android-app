package com.yilos.nailstar.aboutme.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.entity.UserMessage;
import com.yilos.nailstar.util.Constants;
import com.yilos.widget.circleimageview.CircleImageView;
import com.yilos.widget.view.ImageCacheView;

import java.text.SimpleDateFormat;
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
        public TextView commentText;
        public CircleImageView accountPhoto;
        public TextView accountName;
        public TextView replyCreateDate;
        public TextView replyContent;
        public ImageCacheView thumbUrl;
        public TextView commentCreateDate;
        public TextView title;
        public TextView teacher;
        public TextView hasBeenReply;
        public TextView replyButton;

        public ViewHolder(View view) {
            super(view);
            commentText = (TextView) view.findViewById(R.id.commentText);
            accountPhoto = (CircleImageView) view.findViewById(R.id.accountPhoto);
            accountName = (TextView) view.findViewById(R.id.accountName);
            replyCreateDate = (TextView) view.findViewById(R.id.replyCreateDate);
            replyContent = (TextView) view.findViewById(R.id.replyContent);
            thumbUrl = (ImageCacheView) view.findViewById(R.id.thumbUrl);
            commentCreateDate = (TextView) view.findViewById(R.id.commentCreateDate);
            title = (TextView) view.findViewById(R.id.title);
            teacher = (TextView) view.findViewById(R.id.teacher);
            hasBeenReply = (TextView) view.findViewById(R.id.has_been_reply);
            replyButton = (TextView) view.findViewById(R.id.reply_button);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_user_message_item, parent, false);

        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final UserMessage userMessage = userMessageList.get(position);
        UserMessage.CommentEntity commentEntity = userMessage.getComment();
        String commentContent = commentEntity.getContent();
        String atName = commentEntity.getAtName();

        String commentTime = new SimpleDateFormat("MM月dd日").format(commentEntity.getCreateDate());
        String commentCreateDate = String.valueOf(commentTime);
        String isHomework = String.valueOf(commentEntity.getIsHomework());

        String wo = "我 ";
        String huiFu = "回复 ";
        String commentString = wo + huiFu + atName + " :" + commentContent;
        int woIndex = commentString.indexOf(wo);
        int huiFuIndex = commentString.indexOf(huiFu);
        int atNameIndex = commentString.indexOf(atName);
        int commentContentIndex = commentString.indexOf(commentContent);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(commentString);


        //我 样式
        AbsoluteSizeSpan woAbsoluteSizeSpan = new AbsoluteSizeSpan(12,true);
        ForegroundColorSpan woForegroundColorSpan = new ForegroundColorSpan(context.getResources().getColor(R.color.z1));
        spannableStringBuilder.setSpan(woForegroundColorSpan,woIndex,woIndex + wo.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        spannableStringBuilder.setSpan(woAbsoluteSizeSpan,woIndex,woIndex + wo.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        //回复 样式
        AbsoluteSizeSpan huiFuAbsoluteSizeSpan = new AbsoluteSizeSpan(12,true);
        ForegroundColorSpan huiFuForegroundColorSpan = new ForegroundColorSpan(context.getResources().getColor(R.color.z2));
        spannableStringBuilder.setSpan(huiFuForegroundColorSpan,huiFuIndex,huiFuIndex + huiFu.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        spannableStringBuilder.setSpan(huiFuAbsoluteSizeSpan,huiFuIndex,huiFuIndex + huiFu.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        //atName样式
        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(12,true);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(context.getResources().getColor(R.color.z1));
        spannableStringBuilder.setSpan(foregroundColorSpan,atNameIndex,atNameIndex + atName.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        spannableStringBuilder.setSpan(absoluteSizeSpan,atNameIndex,atNameIndex + atName.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        //commentContent 样式
        AbsoluteSizeSpan commentContentAbsoluteSizeSpan = new AbsoluteSizeSpan(12,true);
        ForegroundColorSpan commentContentForegroundColorSpan = new ForegroundColorSpan(context.getResources().getColor(R.color.z1));
        spannableStringBuilder.setSpan(commentContentForegroundColorSpan,commentContentIndex,commentContentIndex + commentContent.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        spannableStringBuilder.setSpan(commentContentAbsoluteSizeSpan,commentContentIndex,commentContentIndex + commentContent.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        holder.commentText.setText(spannableStringBuilder);
        holder.commentCreateDate.setText(commentCreateDate);

        UserMessage.ReplyEntity replyEntity = userMessage.getReply();
        String accountId = replyEntity.getAccountId();
        String accountName = replyEntity.getAccountName();
        String accountPhoto = replyEntity.getAccountPhoto();
        String replyContent = replyEntity.getContent();
        String replyTime = new SimpleDateFormat("MM月dd日").format(replyEntity.getCreateDate());
        String replyCreateDate = String.valueOf(replyTime);
        String replyTo = replyEntity.getReplyTo();
        boolean hasBeenReply = userMessage.getHasBeenReply();

        if (hasBeenReply) {
            holder.hasBeenReply.setVisibility(View.VISIBLE);
            holder.replyButton.setVisibility(View.GONE);
        } else {
            holder.replyButton.setVisibility(View.VISIBLE);
            holder.hasBeenReply.setVisibility(View.GONE);
        }

        holder.accountPhoto.setImageSrc(accountPhoto);
        holder.accountName.setText(accountName);
        holder.replyCreateDate.setText(replyCreateDate);
        holder.replyContent.setText(replyContent);
        holder.thumbUrl.setImageSrc(userMessage.getThumbUrl());
        holder.title.setText(userMessage.getTitle());
        holder.teacher.setText(userMessage.getTeacher());

        holder.replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,UserMessageReplyActivity.class);
                Bundle bundle =new Bundle();
                bundle.putSerializable(Constants.USERMESSAGE, userMessage);
                intent.putExtras(bundle);
                ((MessageActivity) context).startActivityForResult(intent, 1);

            }
        });
    }


    @Override
    public int getItemCount() {
        return userMessageList.size();
    }

}