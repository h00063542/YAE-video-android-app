package com.yilos.widget.titlebar;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yilos.widget.R;

/**
 * Created by sisilai on 15/10/28.
 */
public class TitleBar extends RelativeLayout {
    private Button backButton;//返回按钮
    private Button sureButton;//确定按钮
    private TextView titleView;//顶部标题
    private String titleText;

    private Context mContext;

    public TitleBar(Context context,AttributeSet attrs) {
        super(context,attrs);
        mContext = context;
        init();
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
        titleView.setText(this.titleText);
    }

    public String getTitleText() {
        return titleText;
    }

    public void backEvent(Activity activity) {
        activity.finish();
    }

    public void notShowBackButton() {
        backButton.setVisibility(INVISIBLE);
    }

    public void notShowSureButton() {
        sureButton.setVisibility(INVISIBLE);
    }

    public void notShowTitleView() {
        titleView.setVisibility(INVISIBLE);
    }

    public Button getBackButton() {
        return backButton;
    }

    public Button getSureButton() {
        return sureButton;
    }

    public TextView getTitleView() {
        return titleView;
    }

    private void init() {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        layoutInflater.inflate(R.layout.title_bar, this);
        backButton = (Button)findViewById(R.id.title_bar_left_back);
        sureButton = (Button)findViewById(R.id.title_bar_sure);
        titleView= (TextView)findViewById(R.id.title_bar_title);
    }
}
