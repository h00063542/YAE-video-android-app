package com.yilos.nailstar.aboutme.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.entity.FollowList;
import com.yilos.nailstar.aboutme.presenter.FollowListPresenter;
import com.yilos.widget.titlebar.TitleBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sisilai on 15/11/4.
 */
public class FollowListActivity extends Activity implements View.OnClickListener {
    private TitleBar titleBar;
    private ImageView backButton;
    private TextView titleText;
    private List<FollowList> dataList = new ArrayList<FollowList>();

    public void initFollowList(ArrayList<FollowList> followLists) {
        if (followLists == null) {
            return;
        }
        dataList = followLists;
        FollowListAdapter adapter = new FollowListAdapter(FollowListActivity.this, R.layout.activity_follow_list_item, dataList);
        ListView listView = (ListView) findViewById(R.id.follow_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                FollowList followList = dataList.get(arg2);
                Toast.makeText(FollowListActivity.this, followList.getNickname(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_list);
        titleBar = (TitleBar) findViewById(R.id.follow_list_title_bar);
        backButton = titleBar.getBackButton();
        titleText = titleBar.getTitleView();
        titleText.setText(R.string.my_follow_list);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        FollowListPresenter followListPresenter = FollowListPresenter.getInstance(this);
        followListPresenter.getFollowList("a8affd60-efe6-11e4-a908-3132fc2abe39");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
}
