package com.yilos.nailstar.main;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.umeng.update.UmengUpdateAgent;
import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.view.AboutMeFragment;
import com.yilos.nailstar.framework.view.BaseActivity;
import com.yilos.nailstar.index.view.IndexFragment;
import com.yilos.nailstar.mall.view.MallIndexFragment;
import com.yilos.nailstar.requirelession.view.RequireLessionFragment;

public class MainActivity extends BaseActivity {
    private FragmentManager fragmentManager;
    private TextView mainTabNewMessage;

    private IndexFragment indexFragment;
    private MallIndexFragment mallFragment;
    private RequireLessionFragment requireLessionFragment;
    private AboutMeFragment aboutMeFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UmengUpdateAgent.update(this);

        initViews();
        setTabSelection(0);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.main_tab_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.main_tab_index:
                        setTabSelection(0);
                        break;
                    case R.id.main_tab_requirelession:
                        setTabSelection(1);
                        break;
                    case R.id.main_tab_mall:
                        setTabSelection(2);
                        break;
                    case R.id.main_tab_aboutme:
                        setTabSelection(3);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void initViews() {
        //indexFragment = findViewById(R.id.fragment_index);
        mainTabNewMessage = (TextView) findViewById(R.id.main_tab_new_message);

        fragmentManager = getFragmentManager();
    }

    /**
     * 根据传入的index参数来设置选中的tab页。
     *
     * @param index 每个tab页对应的下标。0表示消息，1表示联系人，2表示动态，3表示设置。
     */
    private void setTabSelection(int index) {
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (index) {
            case 0:
                if (indexFragment == null) {
                    // 如果indexFragment为空，则创建一个并添加到界面上
                    indexFragment = new IndexFragment();
                    transaction.add(R.id.tabcontent, indexFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(indexFragment);
                }
                break;
            case 1:
                if (requireLessionFragment == null) {
                    // 如果indexFragment为空，则创建一个并添加到界面上
                    requireLessionFragment = new RequireLessionFragment();
                    transaction.add(R.id.tabcontent, requireLessionFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(requireLessionFragment);
                }
                break;
            case 2:
                if (mallFragment == null) {
                    // 如果indexFragment为空，则创建一个并添加到界面上
                    mallFragment = new MallIndexFragment();
                    transaction.add(R.id.tabcontent, mallFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(mallFragment);
                }
                break;
            case 3:
                if (aboutMeFragment == null) {
                    // 如果indexFragment为空，则创建一个并添加到界面上
                    aboutMeFragment = new AboutMeFragment();
                    transaction.add(R.id.tabcontent, aboutMeFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(aboutMeFragment);
                }
                break;
            default:
                break;
        }
        transaction.commit();
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction 用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (indexFragment != null) {
            transaction.hide(indexFragment);
        }
        if (requireLessionFragment != null) {
            transaction.hide(requireLessionFragment);
        }
        if (mallFragment != null) {
            transaction.hide(mallFragment);
        }
        if (aboutMeFragment != null) {
            transaction.hide(aboutMeFragment);
        }
    }
}
