package com.yilos.nailstar.aboutme.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.entity.FansList;
import com.yilos.nailstar.aboutme.entity.FansListCategory;
import com.yilos.nailstar.aboutme.presenter.FansListPresenter;
import com.yilos.widget.titlebar.TitleBar;

import java.util.ArrayList;

/**
 * Created by sisilai on 15/11/6.
 */
public class FansListActivity extends Activity {
    private TitleBar titleBar;
    private TextView titleText;
    private ImageView backButton;

    private FansCategoryAdapter mCustomBaseAdapter;
    ArrayList<FansListCategory> listData = new ArrayList<>();
    private static int TeacherCount = 0;
    private static int otherCount = 0;

    public void initFansList(ArrayList<FansList> fansLists) {
        if (fansLists == null) {
            return;
        }

        ListView listView = (ListView) findViewById(R.id.fans_list);
        // 数据
        for (FansList fansList : fansLists) {
            if (fansList.getType() == 5) {
                TeacherCount = TeacherCount + 1;
            } else {
                otherCount = otherCount + 1;
            }
        }
        FansListCategory teacherCategory = new FansListCategory("老师" + " " + TeacherCount);
        FansListCategory otherCategory = new FansListCategory("咖友" + " " + otherCount);
        for (FansList fansList : fansLists) {
            if (fansList.getType() == 5) {
                teacherCategory.addItem(fansList);
            } else {
                otherCategory.addItem(fansList);
            }
        }

        listData.add(teacherCategory);
        listData.add(otherCategory);

        mCustomBaseAdapter = new FansCategoryAdapter(getBaseContext(), listData);

        // 适配器与ListView绑定
        listView.setAdapter(mCustomBaseAdapter);

        listView.setOnItemClickListener(new ItemClickListener());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fans_list);
        titleBar = (TitleBar)findViewById(R.id.fans_list_title_bar);
        titleText = titleBar.getTitleView();
        titleText.setText(R.string.my_fans_list);
        backButton = titleBar.getBackButton();
        backButton.setImageResource(R.drawable.ic_head_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        FansListPresenter fansListPresenter = FansListPresenter.getInstance(this);
        fansListPresenter.getFansList("a8affd60-efe6-11e4-a908-3132fc2abe39");

    }


    private class ItemClickListener implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            Toast.makeText(getBaseContext(),mCustomBaseAdapter.getItem(position).getNickname(),
                    Toast.LENGTH_SHORT).show();
        }

    }
}