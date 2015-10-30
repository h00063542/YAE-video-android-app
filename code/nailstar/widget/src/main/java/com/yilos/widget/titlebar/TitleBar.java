package com.yilos.widget.titlebar;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
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
    private TextView rightTextButton;//右边确定按钮
    private ImageView rightImageButtonOne;//从右边算起的第一个图片按钮
    private ImageView rightImageButtonTwo;//从右边算起的第一个图片按钮
    private TextView titleView;//顶部标题

    private Context mContext;

    public TitleBar(Context context,AttributeSet attrs) {
        super(context,attrs);
        mContext = context;
        init();
    }

    public void hideWidget(View view) {
        view.setVisibility(INVISIBLE);
    }

    public ImageView getBackButton() {
        return backButton;
    }

    public TextView getRightTextButton() {
        return rightTextButton;
    }

    public TextView getTitleView() {
        return titleView;
    }

    public ImageView getRightImageButtonOne() {
        return rightImageButtonOne;
    }

    public ImageView getRightImageButtonTwo() {
        return rightImageButtonTwo;
    }

    private void init() {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        layoutInflater.inflate(R.layout.title_bar, this);
        backButton = (ImageView)findViewById(R.id.title_bar_left_back);
        rightTextButton = (TextView)findViewById(R.id.title_bar_right_text_button);
        titleView= (TextView)findViewById(R.id.title_bar_title);
        rightImageButtonOne = (ImageView)findViewById(R.id.title_bar_right_image_button_one);
        rightImageButtonTwo = (ImageView)findViewById(R.id.title_bar_right_image_button_two);
    }
}