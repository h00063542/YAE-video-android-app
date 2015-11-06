package com.yilos.nailstar.aboutme.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.entity.FansList;
import com.yilos.nailstar.aboutme.presenter.FansListPresenter;

import java.util.ArrayList;

/**
 * Created by sisilai on 15/11/6.
 */
public class MainActivity extends Activity {

    private CategoryAdapter mCustomBaseAdapter;
    private ArrayList<FansList> dataList = new ArrayList<FansList>();
    ArrayList<Category> listData = new ArrayList<Category>();

    public void initFansList(ArrayList<FansList> fansLists) {
        if (fansLists == null) {
            return;
        }
        dataList = fansLists;

        ListView listView = (ListView) findViewById(R.id.listView1);
        // 数据
        Category categoryOne = new Category("老师");
        Category categoryTwo = new Category("普通用户");
        for (FansList fansList : fansLists) {
            //        1美甲店主
            //        2美甲师
            //        3美甲从业者
            //        4美甲消费者
            //        5美甲老师
            //        6其他
            if (fansList.getType() == 5) {
                categoryOne.addItem(fansList);
            } else {
                categoryTwo.addItem(fansList);
            }
        }

        listData.add(categoryOne);
        listData.add(categoryTwo);

        mCustomBaseAdapter = new CategoryAdapter(getBaseContext(), listData);

        // 适配器与ListView绑定
        listView.setAdapter(mCustomBaseAdapter);

        listView.setOnItemClickListener(new ItemClickListener());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        FansListPresenter fansListPresenter = FansListPresenter.getInstance(this);
        fansListPresenter.getFansList("a8affd60-efe6-11e4-a908-3132fc2abe39");


    }


    private class ItemClickListener implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            Toast.makeText(getBaseContext(),  (String)mCustomBaseAdapter.getItem(position).getNickname(),
                    Toast.LENGTH_SHORT).show();
        }

    }

//
//    /**
//     * 创建测试数据
//     */
//    private ArrayList<Category> getData(ArrayList<FansList> fansLists) {
//
//        ArrayList<Category> listData = new ArrayList<Category>();
//        Category categoryOne = new Category("老师");
//        Category categoryTwo = new Category("普通用户");
//        for (FansList fansList : fansLists) {
//            //        1美甲店主
//            //        2美甲师
//            //        3美甲从业者
//            //        4美甲消费者
//            //        5美甲老师
//            //        6其他
//            if (fansList.getType() == 5) {
//                //nailArtTeacher.add(fansList);
//                categoryOne.addItem(fansList);
//            } else {
//                //other.add(fansList);
//                categoryTwo.addItem(fansList);
//            }
//        }
//
//        listData.add(categoryOne);
//        listData.add(categoryTwo);
//
//        return listData;
//    }
}