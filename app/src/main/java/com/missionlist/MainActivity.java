package com.missionlist;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.Toast;

import com.missionlist.adapter.TabAdapter;
import com.missionlist.astuetz.PagerSlidingTabStrip;
import com.missionlist.fragment.TaskGroupFragment;
import com.missionlist.fragment.MeFragment;
import com.missionlist.fragment.TaskFragment;
import com.missionlist.model.Mission;
import com.missionlist.util.Util;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;


public class MainActivity extends FragmentActivity {

    private MeFragment meFragment;
    private TaskFragment taskFragment;
    private TaskGroupFragment taskGroupFragment;
    private PagerSlidingTabStrip tabs;

    private static final int ACTIVITY_CREATE = 0;

    /**
     * 获取当前屏幕的密度
     */
    private DisplayMetrics dm;
    private TabAdapter tabAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setOverflowShowingAlways();
        //getActionBar().setDisplayShowHomeEnabled(false);
        dm = getResources().getDisplayMetrics();
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabAdapter = new TabAdapter(getSupportFragmentManager());
        pager.setAdapter(tabAdapter);
        tabs.setViewPager(pager);
        setTabsValue();
    }

    /**
     * 对PagerSlidingTabStrip的各项属性进行赋值。
     */
    private void setTabsValue() {
        // 设置Tab是自动填充满屏幕的
        tabs.setShouldExpand(true);
        // 设置Tab的分割线是透明的
        tabs.setDividerColor(Color.TRANSPARENT);
        // 设置Tab底部线的高度
        tabs.setUnderlineHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 1, dm));
        // 设置Tab Indicator的高度
        tabs.setIndicatorHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 4, dm));
        // 设置Tab标题文字的大小
        tabs.setTextSize((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 16, dm));
        // 设置Tab Indicator的颜色
        tabs.setIndicatorColor(Color.parseColor("#45c01a"));
        // 设置选中Tab文字的颜色 (这是我自定义的一个方法)
        tabs.setSelectedTextColor(Color.parseColor("#45c01a"));
        // 取消点击Tab时的背景色
        tabs.setTabBackground(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_new:
                Intent intent = new Intent(this,ItemActivity.class);
                startActivityForResult(intent, ACTIVITY_CREATE);
                break;
            case R.id.action_refresh:
                sync();
                break;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == Activity.RESULT_OK) {
            TaskFragment taskFragment = (TaskFragment) tabAdapter.getItem(0);
            taskFragment.getList(1);

            TaskGroupFragment taskGroupFragment = (TaskGroupFragment)tabAdapter.getItem(1);
            taskGroupFragment.getList();
        }
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    private void setOverflowShowingAlways() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            menuKeyField.setAccessible(true);
            menuKeyField.setBoolean(config, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sync(){
        ParseQuery<Mission> query = Mission.getQuery();
        query.fromPin(MListApp.GROUP_NAME);
        query.findInBackground(new FindCallback<Mission>() {
            @Override
            public void done(List<Mission> missions, ParseException e) {
                if(e==null){
                    for(Mission mission:missions){
                        if(mission.getDelete() == true){
                            mission.deleteInBackground();
                            mission.unpinInBackground(MListApp.GROUP_NAME);
                        }else {
                            mission.setDraft(false);
                            mission.saveInBackground();
                            mission.pinInBackground(MListApp.GROUP_NAME);
                        }
                    }
                    Util.showMessage(getApplicationContext(), "Sync data success", Toast.LENGTH_SHORT);
                }else {
                    Util.showMessage(getApplicationContext(), "Sync data failed", Toast.LENGTH_SHORT);
                }
            }
        });

    }

}