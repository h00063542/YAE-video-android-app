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
import com.yilos.nailstar.aboutme.entity.MyData;
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
    private List<MyData> dataList = new ArrayList<MyData>();

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
        initData();
        MyDataAdapter adapter = new MyDataAdapter(FollowListActivity.this, R.layout.mydata_item, dataList);
        ListView listView = (ListView) findViewById(R.id.listView1);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                MyData data = dataList.get(arg2);
                Toast.makeText(FollowListActivity.this, data.getDataString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initData() {
        // TODO Auto-generated method stub
        MyData one = new MyData("one", R.mipmap.ic_default_photo);
        MyData two = new MyData("two", R.mipmap.ic_about_me_star_test);
        dataList.add(one);
        dataList.add(two);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
}
