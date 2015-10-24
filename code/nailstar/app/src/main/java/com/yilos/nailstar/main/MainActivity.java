package com.yilos.nailstar.main;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.AboutMeFragment;
import com.yilos.nailstar.circle.CircleFragment;
import com.yilos.nailstar.requirelession.view.RequireLessionFragment;
import com.yilos.nailstar.index.view.IndexFragment;
import com.yilos.nailstar.player.VideoPlayerActivity;

public class MainActivity extends Activity {
    private FragmentManager fragmentManager;
    private TextView mainTabNewMessage;

    private IndexFragment indexFragment;
    private CircleFragment circleFragment;
    private RequireLessionFragment requireLessionFragment;
    private AboutMeFragment aboutMeFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                    case R.id.main_tab_circle:
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

//        Intent intent = new Intent(MainActivity.this, VideoPlayerActivity.class);
//        startActivity(intent);
//        MainActivity.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                if (circleFragment == null) {
                    // 如果indexFragment为空，则创建一个并添加到界面上
                    circleFragment = new CircleFragment();
                    transaction.add(R.id.tabcontent, circleFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(circleFragment);
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
        if (circleFragment != null) {
            transaction.hide(circleFragment);
        }
        if (aboutMeFragment != null) {
            transaction.hide(aboutMeFragment);
        }
    }
}
