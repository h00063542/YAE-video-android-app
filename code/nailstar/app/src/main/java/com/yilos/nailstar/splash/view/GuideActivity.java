package com.yilos.nailstar.splash.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shizhefei.view.indicator.FixedIndicatorView;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.yilos.nailstar.R;
import com.yilos.nailstar.main.MainActivity;

/**
 * Created by sisilai on 15/11/24.
 */

public class GuideActivity extends FragmentActivity {
    private IndicatorViewPager indicatorViewPager;
    private LayoutInflater inflate;
    private int[] images = { R.mipmap.guide_p1,R.mipmap.guide_p2,R.mipmap.guide_p3};
    private FixedIndicatorView indicator;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_guide);
        ViewPager viewPager = (ViewPager) findViewById(R.id.guide_viewPager);
        indicator = (FixedIndicatorView) findViewById(R.id.guide_indicator);
        indicatorViewPager = new IndicatorViewPager(indicator, viewPager);
        inflate = LayoutInflater.from(getApplicationContext());
        indicatorViewPager.setAdapter(adapter);
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
                convertView = new View(getApplicationContext());
                convertView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }
            if (position == images.length - 1) {
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setStartActivity();
                    }
                });
                indicator.setVisibility(View.GONE);
                convertView.setBackgroundResource(images[position]);
            } else {
                convertView.setBackgroundResource(images[position]);
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