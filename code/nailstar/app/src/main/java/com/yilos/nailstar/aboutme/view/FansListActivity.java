package com.yilos.nailstar.aboutme.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.entity.FansList;
import com.yilos.nailstar.aboutme.presenter.FansListPresenter;
import com.yilos.widget.titlebar.TitleBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sisilai on 15/11/5.
 */
public class FansListActivity extends Activity {
    private TitleBar titleBar;
    private TextView titleText;
    private ImageView backButton;

    private List<FansList> dataList = new ArrayList<FansList>();
    private List<FansList> nailArtTeacher = new ArrayList<FansList>();
    private List<FansList> other = new ArrayList<FansList>();

    public void initFansList(ArrayList<FansList> fansLists) {
        if (fansLists == null) {
            return;
        }
        dataList = fansLists;
        for (FansList fansList : dataList) {
            //        1美甲店主
            //        2美甲师
            //        3美甲从业者
            //        4美甲消费者
            //        5美甲老师
            //        6其他
            if (fansList.getType() == 5) {
                    nailArtTeacher.add(fansList);
            } else {
                other.add(fansList);
            }
        }

        if(nailArtTeacher.size() != 0) {
//            FansListAdapter fansListAdapter = new FansListAdapter(FansListActivity.this, R.layout.fans_list_item, nailArtTeacher);
//            ListView listView = (ListView) findViewById(R.id.inner_fans_list);
//            listView.setAdapter(fansListAdapter);
//            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//                @Override
//                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//                                        long arg3) {
//                    // TODO Auto-generated method stub
//                    FansList fansList = nailArtTeacher.get(arg2);
//                    Toast.makeText(FansListActivity.this, fansList.getNickname(), Toast.LENGTH_SHORT).show();
//                }
//            });
            mFansListCategoryAdapter.addCategory("老师 " + nailArtTeacher.size(), new FansListAdapter(FansListActivity.this, R.layout.fans_list_item, nailArtTeacher));
        }

        if(other.size() != 0) {
            mFansListCategoryAdapter.addCategory("普通用户 " + other.size(),new FansListAdapter(FansListActivity.this, R.layout.fans_list_item, other));
        }

        ListView categoryList = (ListView) findViewById(R.id.categoryList);

        categoryList.setAdapter(mFansListCategoryAdapter);
        categoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                FansList fansList = dataList.get(arg2);
                Toast.makeText(FansListActivity.this, fansList.getNickname(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fans_list);
        titleBar = (TitleBar)findViewById(R.id.fans_list_title_bar);
        titleText = titleBar.getTitleView();
        titleText.setText("我的粉丝");
        backButton = titleBar.getBackButton();
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        FansListPresenter fansListPresenter = FansListPresenter.getInstance(this);
        fansListPresenter.getFansList("a8affd60-efe6-11e4-a908-3132fc2abe39");
    }

    private FansListCategoryAdapter mFansListCategoryAdapter = new FansListCategoryAdapter() {
        @Override
        protected View getTitleView(String title, int index, View convertView, ViewGroup parent) {
            TextView titleView;

            if (convertView == null) {
                titleView = (TextView)getLayoutInflater().inflate(R.layout.fans_list_item_title, null);
            } else {
                titleView = (TextView)convertView;
            }

            titleView.setText(title);

            return titleView;
        }
    };

}