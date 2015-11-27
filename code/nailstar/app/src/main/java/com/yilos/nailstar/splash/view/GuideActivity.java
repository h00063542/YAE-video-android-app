package com.yilos.nailstar.splash.view;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.shizhefei.view.indicator.FixedIndicatorView;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.yilos.nailstar.R;
import com.yilos.nailstar.framework.application.NailStarApplication;
import com.yilos.nailstar.main.MainActivity;
import com.yilos.nailstar.util.SettingUtil;

/**
 * Created by sisilai on 15/11/24.
 */

public class GuideActivity extends AppCompatActivity {
    private LinearLayout guideItemLayout;
    private IndicatorViewPager indicatorViewPager;
    private LayoutInflater inflate;
    private int[] images = {R.mipmap.guide_p1, R.mipmap.guide_p2, R.mipmap.guide_p3};
    private FixedIndicatorView indicator;

    private int width;
    private int height;

    @Override
    protected void onCreate(Bundle arg0) {
        // 设置不显示状态栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 设置不允许旋转屏幕
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        width = NailStarApplication.getApplication().getScreenWidth(this);
        height = NailStarApplication.getApplication().getScreenHeight(this);
        if(width * 1.78 > height) {
            width = (int)(height / 1.78);
        } else {
            height = (int)(width * 1.78);
        }

        super.onCreate(arg0);
        setContentView(R.layout.activity_guide);
        ViewPager viewPager = (ViewPager) findViewById(R.id.guide_viewPager);
        indicator = (FixedIndicatorView) findViewById(R.id.guide_indicator);
        indicatorViewPager = new IndicatorViewPager(indicator, viewPager);
        inflate = LayoutInflater.from(getApplicationContext());
        indicatorViewPager.setAdapter(adapter);
        initApplicationSetting();
    }

    private void initApplicationSetting() {
        SettingUtil.setFirstFlag(false);
    }

    public void setStartActivity() {
        Intent intent = new Intent(GuideActivity.this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    private IndicatorViewPager.IndicatorPagerAdapter adapter = new IndicatorViewPager.IndicatorViewPagerAdapter() {
        @Override
        public View getViewForTab(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = inflate.inflate(R.layout.activity_guide, container, false);
            }
            return convertView;
        }

        @Override
        public View getViewForPage(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = inflate.inflate(R.layout.activity_guide_item, container, false);
                guideItemLayout = (LinearLayout) convertView.findViewById(R.id.guide_item);
                switch (position) {
                    case 0:
                        guideItemLayout.setBackgroundResource(R.color.guide_yellow);
                        break;
                    case 1:
                        guideItemLayout.setBackgroundResource(R.color.guide_pink);
                        break;
                    case 2:
                        guideItemLayout.setBackgroundResource(R.color.guide_blue);
                        break;
                    default:
                        break;
                }
                if (position == images.length - 1) {
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setStartActivity();
                        }
                    });
                    indicator.setVisibility(View.GONE);
                }
                ImageView imageView = ((ImageView)guideItemLayout.findViewById(R.id.guide_item_image));
                imageView.setImageResource(images[position]);
                ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
                if(null == layoutParams) {
                    layoutParams = new ViewGroup.LayoutParams(width, height);
                } else {
                    layoutParams.width = width;
                    layoutParams.height = height;
                }
                imageView.setLayoutParams(layoutParams);
            }
            return convertView;
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public int getCount() {
            return images.length;
        }
    };
}