package com.yilos.widget.titlebar;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yilos.widget.R;

/**
 * Created by sisilai on 15/10/28.
 */
public class TitleBar extends RelativeLayout {
    private ImageView backButton;//返回按钮
    private ImageView searchButton;//左边搜索按钮
    private TextView rightTextButton;//右边确定按钮
    private ImageView rightImageButtonOne;//从右边算起的第一个图片按钮
    private ImageView rightImageButtonTwo;//从右边算起的第二个图片按钮
    private TextView titleView;//顶部居中标题
    private TextView leftTitleView;//顶部居左标题
    private ImageView imageTitleView;//顶部图片标题

    private RelativeLayout titleBarGroup;//背景

    private Context mContext;

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public void showWidget(View view) {
        view.setVisibility(VISIBLE);
    }

    public ImageView getBackButton(final Activity activity) {
        backButton.setImageResource(R.drawable.ic_head_back);
        showWidget(backButton);
        backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        return backButton;
    }

    public ImageView getBackButton() {
        showWidget(backButton);
        return backButton;
    }

    public ImageView getSearchButton() {
        showWidget(searchButton);
        return searchButton;
    }

    public TextView getRightTextButton() {
        showWidget(rightTextButton);
        return rightTextButton;
    }

    public TextView getTitleView() {
        showWidget(titleView);
        return titleView;
    }

    public TextView getTitleView(String title) {
        showWidget(titleView);
        titleView.setText(title);
        return titleView;
    }

    public TextView getLeftTitleView() {
        showWidget(leftTitleView);
        return leftTitleView;
    }

    public ImageView getImageTitleView() {
        showWidget(imageTitleView);
        return imageTitleView;
    }

    public ImageView getRightImageButtonOne() {
        showWidget(rightImageButtonOne);
        return rightImageButtonOne;
    }

    public ImageView getRightImageButtonTwo() {
        showWidget(rightImageButtonTwo);
        return rightImageButtonTwo;
    }

    public void setTitleBarBackgroundColor(int color) {
        titleBarGroup.setBackgroundColor(getResources().getColor(color));
    }

    private LayoutInflater layoutInflater;

    private void init() {
        layoutInflater = LayoutInflater.from(mContext);
        layoutInflater.inflate(R.layout.title_bar, this);

        titleBarGroup = (RelativeLayout) findViewById(R.id.title_bar_group);
        titleBarGroup.setBackgroundColor(getResources().getColor(R.color.orange));
        searchButton = (ImageView) findViewById(R.id.title_bar_left_search);
        imageTitleView = (ImageView) findViewById(R.id.title_bar_image_title);
        backButton = (ImageView) findViewById(R.id.title_bar_left_back);
        rightTextButton = (TextView) findViewById(R.id.title_bar_right_text_button);
        titleView = (TextView) findViewById(R.id.title_bar_title);
        leftTitleView = (TextView) findViewById(R.id.left_title_bar_title);
        rightImageButtonOne = (ImageView) findViewById(R.id.title_bar_right_image_button_one);
        rightImageButtonTwo = (ImageView) findViewById(R.id.title_bar_right_image_button_two);
        titleView.setMaxWidth(setTitleWidthPixels());
        leftTitleView.setMaxWidth(setTitleWidthPixels());
    }

    private int setTitleWidthPixels() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int backButtonWidth = getBackButtonWidth();
        int rightImageButtonTwoWidth = getRightImageButtonTwoWidth();
        int rightImageButtonOneWidth = getRightImageButtonOneWidth();
        w_screen = w_screen - 2 * backButtonWidth - 2 * rightImageButtonTwoWidth - rightImageButtonOneWidth - 100;
        return w_screen;
    }

    private int getBackButtonWidth() {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        backButton.measure(w, h);
        int width = backButton.getMeasuredWidth();
        return width;
    }

    private int getRightImageButtonTwoWidth() {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        rightImageButtonTwo.measure(w, h);
        int width = rightImageButtonTwo.getMeasuredWidth();
        return width;
    }

    private int getRightImageButtonOneWidth() {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        rightImageButtonOne.measure(w, h);
        int width = rightImageButtonOne.getMeasuredWidth();
        return width;
    }
}