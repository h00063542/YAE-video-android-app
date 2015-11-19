package com.yilos.nailstar.category.view;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.yilos.nailstar.R;
import com.yilos.nailstar.framework.view.BaseActivity;
import com.yilos.nailstar.index.entity.Topic;
import com.yilos.nailstar.topic.view.TopicVideoPlayerActivity;
import com.yilos.nailstar.util.Constants;
import com.yilos.widget.circleimageview.CircleImageView;
import com.yilos.widget.view.ImageCacheView;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by yangdan on 15/11/18.
 */
public class CategoryListItemViewHolder extends BaseViewHolder<Topic> {
    private ImageCacheView videoImage;
    private TextView titleView;
    private CircleImageView teacherPhoto;
    private TextView teacherNameView;
    private TextView createDateView;
    private TextView playTimesView;

    private ViewGroup parent;

    public CategoryListItemViewHolder(ViewGroup parent) {
        super(parent, R.layout.category_list_item_view);
        this.parent = parent;

        videoImage = $(R.id.videoImage);
        titleView = $(R.id.titleView);
        teacherPhoto = $(R.id.teacherPhoto);
        teacherNameView = $(R.id.teacherName);
        createDateView = $(R.id.createDateView);
        playTimesView = $(R.id.playTimesView);
    }

    @Override
    public void setData(final Topic data) {
        videoImage.setImageSrc(data.getThumbUrl());
        videoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(parent.getContext() instanceof BaseActivity) {
                    ((BaseActivity) parent.getContext()).showLoading(null);
                }
                Intent intent = new Intent(parent.getContext(), TopicVideoPlayerActivity.class);
                intent.putExtra(Constants.TOPIC_ID, data.getTopicId());
                if(parent.getContext() instanceof Activity) {
                    ((Activity) parent.getContext()).startActivityForResult(intent, 1);
                } else {
                    parent.getContext().startActivity(intent);
                }
            }
        });
        titleView.setText(data.getTitle());

        teacherPhoto.setImageSrc(data.getAuthorPhoto());
        teacherNameView.setText(data.getAuthor());

        playTimesView.setText(data.getPlayTimes() + "次播放");

        int year = getYearSpace(data.getCreateDate(), new Date());
        if(year > 0) {
            createDateView.setText(year + "年前");
            return;
        }

        int month = getMonthSpace(data.getCreateDate(), new Date());
        if(month > 0) {
            createDateView.setText(month + "月前");
            return;
        }

        int day = getDaySpace(data.getCreateDate(), new Date());
        if(day == 1) {
            createDateView.setText("昨天");
            return;
        } else if(day == 2) {
            createDateView.setText("前天");
            return;
        } else if(day > 2) {
            createDateView.setText(day + "天前");
            return;
        }

        int hour = getHourSpace(data.getCreateDate(), new Date());
        if(hour > 0) {
            createDateView.setText(hour + "小时前");
            return;
        }

        int minute = getMinuteSpace(data.getCreateDate(), new Date());
        if(minute > 0) {
            createDateView.setText(minute + "分钟前");
            return;
        } else {
            createDateView.setText("刚刚");
            return;
        }
    }

    private int getYearSpace(Date fromDate, Date toDate) {
        Calendar from = Calendar.getInstance();
        from.setTime(fromDate);

        Calendar to = Calendar.getInstance();
        to.setTime(toDate);

        return to.get(Calendar.YEAR) - from.get(Calendar.YEAR);
    }

    private int getMonthSpace(Date fromDate, Date toDate) {
        Calendar from = Calendar.getInstance();
        from.setTime(fromDate);

        Calendar to = Calendar.getInstance();
        to.setTime(toDate);

        return to.get(Calendar.MONTH) - from.get(Calendar.MONTH);
    }

    private int getDaySpace(Date fromDate, Date toDate) {
        Calendar from = Calendar.getInstance();
        from.setTime(fromDate);

        Calendar to = Calendar.getInstance();
        to.setTime(toDate);

        return to.get(Calendar.DATE) - from.get(Calendar.DATE);
    }

    private int getHourSpace(Date fromDate, Date toDate) {
        Calendar from = Calendar.getInstance();
        from.setTime(fromDate);

        Calendar to = Calendar.getInstance();
        to.setTime(toDate);

        return to.get(Calendar.HOUR) - from.get(Calendar.HOUR);
    }

    private int getMinuteSpace(Date fromDate, Date toDate) {
        Calendar from = Calendar.getInstance();
        from.setTime(fromDate);

        Calendar to = Calendar.getInstance();
        to.setTime(toDate);

        return to.get(Calendar.MINUTE) - from.get(Calendar.MINUTE);
    }
}
