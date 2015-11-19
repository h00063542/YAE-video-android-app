package com.yilos.nailstar.aboutme.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
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

import java.util.List;

import static android.text.Spanned.SPAN_EXCLUSIVE_INCLUSIVE;
import static com.yilos.nailstar.R.dimen.small_text_size;

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
        public TextView messageText;

        public ViewHolder(View view) {
            super(view);
            messageText = (TextView) view.findViewById(R.id.message_text);
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
        UserMessage userMessage = userMessageList.get(position);
        UserMessage.CommentEntity commentEntity = userMessage.getComment();
        String commentContent = commentEntity.getContent();
        String atName = commentEntity.getAtName();
        String commentCreateDate = String.valueOf(commentEntity.getCreateDate());
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

        holder.messageText.setText(spannableStringBuilder);
        //comment : {"content":"这尽职尽责尽职尽责","atName":"在","createDate":1446709317872,"isHomework":1}

//        * reply : {"accountId":"d77348c0-60d7-11e5-ade9-e3d220e2c964","accountName":"勿忘我",
// "accountPhoto":"http://pic.yilos.com/ec9a2bbc1abb13166af6da31495bea0b",
//             "content":"嗯嗯","createDate":1446718910072,
// "replyTo":"9f59f430-8390-11e5-a74c-839a83b22973",
// "lastReplyTo":"b1438670-8390-11e5-a74c-839a83b22973"}

        UserMessage.ReplyEntity replyEntity = userMessage.getReply();
        String accountId = replyEntity.getAccountId();
        String accountName = replyEntity.getAccountName();
        String accountPhoto = replyEntity.getAccountPhoto();
        String content = replyEntity.getContent();
        String createDate = String.valueOf(replyEntity.getCreateDate());
        String replyTo = replyEntity.getReplyTo();
    }


    @Override
    public int getItemCount() {
        return userMessageList.size();
    }

}