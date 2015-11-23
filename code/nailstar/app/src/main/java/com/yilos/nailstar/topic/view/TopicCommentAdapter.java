package com.yilos.nailstar.topic.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.webkit.URLUtil;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.model.LoginAPI;
import com.yilos.nailstar.framework.view.BaseActivity;
import com.yilos.nailstar.topic.entity.TopicCommentInfo;
import com.yilos.nailstar.topic.entity.TopicCommentReplyInfo;
import com.yilos.nailstar.util.CollectionUtil;
import com.yilos.nailstar.util.Constants;
import com.yilos.nailstar.util.StringUtil;
import com.yilos.widget.circleimageview.CircleImageView;
import com.yilos.widget.view.ImageCacheView;

import java.util.ArrayList;

/**
 * Created by yilos on 2015-11-21.
 */
public class TopicCommentAdapter extends BaseAdapter {
    private final int HOMEWORK_IMAGE_ZOOM_ANIMATION_TIME = 200;

    private int widthPixels;
    private int heightPixels;
    private int mCommentContentMarginTop;
    private int mCommentContentMarginBottom;
    private int mCommentReplyPaddingLeft;
    private int mCommentReplyPaddingTop;
    private int mCommentReplyPaddingRight;
    private int mCommentReplyPaddingBottom;

    private ViewGroup mDecorView;
    private BaseActivity mBaseActivity = null;
    private LayoutInflater mInflater = null;


    // topic评论
    private View mZoomInImageLayout;
    private ImageCacheView mIcvTopicCommentImage;
    private ScaleAnimation homeworkZoomInScaleAnimation;
    private ScaleAnimation homeworkZoomOutScaleAnimation;

    private String mTopicId;

    private ArrayList<TopicCommentInfo> topicCommentInfoList = new ArrayList<>();


    public TopicCommentAdapter(BaseActivity activity, String topicId) {
        mBaseActivity = activity;
        mInflater = activity.getLayoutInflater();
        mTopicId = topicId;
        mDecorView = (ViewGroup) activity.getWindow().getDecorView().findViewById(android.R.id.content);
        mZoomInImageLayout = mInflater.inflate(R.layout.zoomin_topic_comment_image_layout, null);
        mIcvTopicCommentImage = (ImageCacheView) mZoomInImageLayout.findViewById(R.id.icv_topic_comment_image);
        mZoomInImageLayout.setBackgroundColor(0x80000000);
        mZoomInImageLayout.setTag(R.layout.zoomin_topic_comment_image_layout, true);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        widthPixels = displayMetrics.widthPixels;
        heightPixels = displayMetrics.heightPixels;
        mCommentReplyPaddingLeft = mBaseActivity.getResources().getDimensionPixelSize(R.dimen.topic_comment_reply_padding_left);
        mCommentReplyPaddingTop = mBaseActivity.getResources().getDimensionPixelSize(R.dimen.topic_comment_reply_padding_top);
        mCommentReplyPaddingRight = mBaseActivity.getResources().getDimensionPixelSize(R.dimen.topic_comment_reply_padding_right);
        mCommentReplyPaddingBottom = mBaseActivity.getResources().getDimensionPixelSize(R.dimen.topic_comment_reply_padding_bottom);
        mCommentContentMarginTop = mBaseActivity.getResources().getDimensionPixelSize(R.dimen.topic_comment_content_margin_top);
        mCommentContentMarginBottom = mBaseActivity.getResources().getDimensionPixelSize(R.dimen.topic_comment_content_margin_bottom);
        // 取消评论区域图片放大查看
        mIcvTopicCommentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIcvTopicCommentImage.startAnimation(homeworkZoomOutScaleAnimation);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mDecorView.removeView(mZoomInImageLayout);
                    }
                }, HOMEWORK_IMAGE_ZOOM_ANIMATION_TIME);
            }
        });
        mZoomInImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 防止点到底层控件
            }
        });
    }

    @Override
    public int getCount() {
        return topicCommentInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return topicCommentInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {
        public CircleImageView authorPhoto;
        public LinearLayout commentLayoutParent;
        public LinearLayout commentLayout;
        public LinearLayout commentReplayLayout;
        public TextView authorName;
        public TextView commentTime;
        public TextView commentContent;
        public ImageCacheView commentContentPic;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final TopicCommentInfo topicCommentInfo = topicCommentInfoList.get(position);
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.topic_comment_item, null);
            ViewHolder holder = new ViewHolder();
            holder.authorPhoto = (CircleImageView) convertView.findViewById(R.id.topic_comment_author_photo);
            holder.commentLayoutParent = (LinearLayout) convertView.findViewById(R.id.topic_comment_content_layout_parent);
            holder.commentLayout = (LinearLayout) convertView.findViewById(R.id.topic_comment_content_layout);
            holder.authorName = (TextView) convertView.findViewById(R.id.topic_comment_author_name);
            holder.commentTime = (TextView) convertView.findViewById(R.id.topic_comment_time);
            holder.commentContent = (TextView) convertView.findViewById(R.id.topic_comment_content);
            holder.commentContentPic = (ImageCacheView) convertView.findViewById(R.id.topic_comment_content_pic);
            holder.commentReplayLayout = (LinearLayout) convertView.findViewById(R.id.topic_comment_replay_layout);
            convertView.setTag(holder);
        }
        final ViewHolder holder = (ViewHolder) convertView.getTag();
        // -----------------设置评论人头像-----------------
        if (!StringUtil.isEmpty(topicCommentInfo.getAuthorPhoto())) {//使用用户自定义头像
            holder.authorPhoto.setImageSrc(topicCommentInfo.getAuthorPhoto());
        } else {// 使用默认头像
            holder.authorPhoto.setImageResource(R.drawable.man);
        }

        // -----------------评论人名称-----------------
        holder.authorName.setText(topicCommentInfo.getAuthor());

        boolean isHomework = topicCommentInfo.getIsHomework() == Constants.IS_HOME_WORK_VALUE
                && !StringUtil.isEmpty(topicCommentInfo.getContentPic());

        // -----------------评论人时间-----------------
        StringBuilder contentText = new StringBuilder();
        if (isHomework) {
            contentText.append("#")
                    .append(mBaseActivity.getString(R.string.submitted_homework))
                    .append("#    ");
        }
        contentText.append(StringUtil.getTopicCommentDateStr(topicCommentInfo.getCreateDate()));
        holder.commentTime.setText(contentText);

        // -----------------设置评论内容-----------------
        holder.commentContent.setText(Html.fromHtml(StringUtil.buildTelNumberHtmlText(topicCommentInfo.getContent())));
        ((LinearLayout.LayoutParams) holder.commentContent.getLayoutParams()).setMargins(0, mCommentContentMarginTop, 0,
                isHomework || !CollectionUtil.isEmpty(topicCommentInfo.getReplies()) ? mCommentContentMarginBottom : 0);

        // -----------------设置评论图片-----------------
        if (isHomework) {
            // 交作业时，显示的是本地图片
            if (!URLUtil.isNetworkUrl(topicCommentInfo.getContentPic())) {
                holder.commentContentPic.setImageURI(Uri.parse(topicCommentInfo.getContentPic()));
            } else {
                holder.commentContentPic.setImageSrc(topicCommentInfo.getContentPic());
            }
            holder.commentContentPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 交作业时，显示的是本地图片
                    if (!URLUtil.isNetworkUrl(topicCommentInfo.getContentPic())) {
                        mIcvTopicCommentImage.setImageURI(Uri.parse(topicCommentInfo.getContentPic()));
                    } else {
                        mIcvTopicCommentImage.setImageSrc(topicCommentInfo.getContentPic());
                    }
                    mDecorView.removeView(mZoomInImageLayout);
                    mDecorView.addView(mZoomInImageLayout);
                    int[] location = new int[2];
                    holder.commentContentPic.getLocationInWindow(location);
                    float locationX = location[0];
                    float locationY = location[1];
                    float imageWidth = holder.commentContentPic.getWidth();
                    float imageHeight = holder.commentContentPic.getHeight();

                    if (locationY > heightPixels / 2) {
                        locationX += imageHeight;
                        locationY += imageHeight;
                    }
                    float fromX = imageWidth / widthPixels;
                    float fromY = imageHeight / heightPixels;
                    float pivotXValue = locationX / widthPixels;
                    float pivotYValue = locationY / heightPixels;

                    //渐变尺寸缩放
                    //放大动画
                    homeworkZoomInScaleAnimation = null;
                    homeworkZoomOutScaleAnimation = null;

                    homeworkZoomInScaleAnimation = new ScaleAnimation(fromX, 1f, fromY, 1f, Animation.RELATIVE_TO_PARENT, pivotXValue, Animation.RELATIVE_TO_PARENT, pivotYValue);
                    //缩小动画
                    homeworkZoomOutScaleAnimation = new ScaleAnimation(1f, fromX, 1f, fromY, Animation.RELATIVE_TO_PARENT, pivotXValue, Animation.RELATIVE_TO_PARENT, pivotYValue);
                    //设置动画时间
                    homeworkZoomInScaleAnimation.setDuration(HOMEWORK_IMAGE_ZOOM_ANIMATION_TIME);
                    homeworkZoomOutScaleAnimation.setDuration(HOMEWORK_IMAGE_ZOOM_ANIMATION_TIME);
                    mIcvTopicCommentImage.startAnimation(homeworkZoomInScaleAnimation);
                }
            });
        } else {
            holder.commentContentPic.setImageSrc(Constants.EMPTY_STRING);
        }

        holder.commentReplayLayout.removeAllViews();

        // -----------------设置评论回复内容-----------------
        if (!CollectionUtil.isEmpty(topicCommentInfo.getReplies())) {
            int index = 0;
            for (TopicCommentReplyInfo topicCommentReplyInfo : topicCommentInfo.getReplies()) {
                TextView topicCommentReplyTv = buildCommentReplyTextView(topicCommentInfo, topicCommentReplyInfo);
                if (index == 0) {
                    // 设置padding
                    topicCommentReplyTv.setPadding(mCommentReplyPaddingLeft, mCommentReplyPaddingTop
                            , mCommentReplyPaddingRight, mCommentReplyPaddingBottom);
                } else {
                    topicCommentReplyTv.setPadding(mCommentReplyPaddingLeft, 0
                            , mCommentReplyPaddingRight, mCommentReplyPaddingBottom);
                }
                holder.commentReplayLayout.addView(topicCommentReplyTv);
                index++;
            }
        }


        convertView.setTag(R.id.topic_comment_info, topicCommentInfo);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TopicCommentInfo commentInfo = (TopicCommentInfo) v.getTag(R.id.topic_comment_info);
                addTopicCommentReply(commentInfo, null, Constants.TOPIC_COMMENT_TYPE_REPLY);
            }
        });

        return convertView;
    }

    public void addTopicCommentInfoList(ArrayList<TopicCommentInfo> topicCommentInfoList) {
        this.topicCommentInfoList.addAll(topicCommentInfoList);
    }

    public void addTopicCommentInfoList(ArrayList<TopicCommentInfo> topicCommentInfoList, int index) {
        this.topicCommentInfoList.addAll(index, topicCommentInfoList);
    }

    public void addTopicCommentReply(String topicCommentId, TopicCommentReplyInfo topicCommentReplyInfo) {
        // 从评论中找到回复评论的位置
        if (CollectionUtil.isEmpty(topicCommentInfoList)) {
            return;
        }
        TopicCommentInfo topicCommentInfo = null;
        for (TopicCommentInfo item : topicCommentInfoList) {
            if (topicCommentId.equals(item.getId())) {
                topicCommentInfo = item;
                break;
            }
        }
        if (null != topicCommentInfo) {
            if (null == topicCommentInfo.getReplies()) {
                topicCommentInfo.setReplies(new ArrayList<TopicCommentReplyInfo>());
            }
            topicCommentInfo.getReplies().add(topicCommentReplyInfo);
            notifyDataSetChanged();
        }
    }

    @NonNull
    private TextView buildCommentReplyTextView(TopicCommentInfo topicCommentInfo
            , TopicCommentReplyInfo topicCommentReplyInfo) {
        TextView topicCommentReplyTv = (TextView) mInflater.inflate(R.layout.topic_comment_replay_item, null);

        StringBuilder replyText = new StringBuilder()
                .append(buildTextFont(R.color.orange, topicCommentReplyInfo.getAuthor()));

        if (!topicCommentInfo.getUserId().equals(topicCommentReplyInfo.getAt().getUserId())) {
            replyText.append(buildTextFont(R.color.z3, mBaseActivity.getString(R.string.reply) + topicCommentReplyInfo.getAt().getNickName()));
        }

        replyText.append(": ");

        String content = StringUtil.buildTelNumberHtmlText(topicCommentReplyInfo.getContent());
        replyText.append(buildTextFont(R.color.z2, content));

        topicCommentReplyTv.setText(Html.fromHtml(replyText.toString()));
        topicCommentReplyTv.setMovementMethod(LinkMovementMethod.getInstance());

        // 设置行高
//        topicCommentReplyTv.setLineSpacing(mCommentReplyLineHeight, 1);

        topicCommentReplyTv.setTag(R.id.topic_comment_info, topicCommentInfo);
        topicCommentReplyTv.setTag(R.id.topic_comment_reply_info, topicCommentReplyInfo);
        topicCommentReplyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TopicCommentInfo commentInfo = (TopicCommentInfo) v.getTag(R.id.topic_comment_info);
                TopicCommentReplyInfo replyInfo = (TopicCommentReplyInfo) v.getTag(R.id.topic_comment_reply_info);
                addTopicCommentReply(commentInfo, replyInfo, Constants.TOPIC_COMMENT_TYPE_REPLY_AGAIN);
            }
        });
        return topicCommentReplyTv;
    }

    private String buildTextFont(int resColorId, String text) {
        int color = mBaseActivity.getResources().getColor(resColorId);
        StringBuilder stringBuilder = new StringBuilder().append(String.format("<font color=\"#%s\">", String.format("%X", color).substring(2))).append(text).append("</font>");
        return stringBuilder.toString();
    }

    private void addTopicCommentReply(TopicCommentInfo commentInfo, TopicCommentReplyInfo replyInfo, int type) {
        if (!LoginAPI.getInstance().isLogin()) {
            LoginAPI.getInstance().gotoLoginPage(mBaseActivity);
            return;
        }
        if (type == Constants.TOPIC_COMMENT_TYPE_REPLY_AGAIN) {
            showTopicCommentReplayAgainDialog(commentInfo, replyInfo, type);
            return;
        }
        Intent intent = new Intent(mBaseActivity, TopicCommentActivity.class);
        intent.putExtra(Constants.TOPIC_ID, mTopicId);
        if (null != commentInfo) {
            intent.putExtra(Constants.TOPIC_COMMENT_ID, commentInfo.getId());
            intent.putExtra(Constants.TOPIC_COMMENT_USER_ID, commentInfo.getUserId());
            intent.putExtra(Constants.TOPIC_COMMENT_AUTHOR, commentInfo.getAuthor());
        }
        intent.putExtra(Constants.TYPE, type);
        mBaseActivity.startActivityForResult(intent, Constants.TOPIC_COMMENT_REQUEST_CODE);
    }

    private void showTopicCommentReplayAgainDialog(final TopicCommentInfo commentInfo
            , final TopicCommentReplyInfo replyInfo, final int type) {
        final CharSequence[] items = {mBaseActivity.getString(R.string.reply), mBaseActivity.getString(R.string.cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(mBaseActivity);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (0 == item) {
                    Intent intent = new Intent(mBaseActivity, TopicCommentActivity.class);
                    intent.putExtra(Constants.TOPIC_ID, mTopicId);
                    if (null != commentInfo) {
                        intent.putExtra(Constants.TOPIC_COMMENT_ID, commentInfo.getId());
                        intent.putExtra(Constants.TOPIC_COMMENT_USER_ID, commentInfo.getUserId());
                        intent.putExtra(Constants.TOPIC_COMMENT_AUTHOR, commentInfo.getAuthor());
                    }
                    if (null != replyInfo && type == Constants.TOPIC_COMMENT_TYPE_REPLY_AGAIN) {
                        intent.putExtra(Constants.TOPIC_COMMENT_REPLY_ID, replyInfo.getId());
                        intent.putExtra(Constants.TOPIC_COMMENT_REPLY_USER_ID, replyInfo.getUserId());
                        intent.putExtra(Constants.TOPIC_COMMENT_REPLY_AUTHOR, replyInfo.getAuthor());
                    }
                    intent.putExtra(Constants.TYPE, type);
                    mBaseActivity.startActivityForResult(intent, Constants.TOPIC_COMMENT_REQUEST_CODE);
                } else if (1 == item) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(true);
        alert.show();
    }
}
