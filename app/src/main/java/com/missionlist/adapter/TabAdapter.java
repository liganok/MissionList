package com.missionlist.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.missionlist.fragment.MeFragment;
import com.missionlist.fragment.TaskFragment;
import com.missionlist.fragment.TaskGroupFragment;

/**
 * Created by Administrator on 2014/10/23.
 */
public class TabAdapter extends FragmentPagerAdapter{
    private MeFragment meFragment;
    private TaskFragment taskFragment;
    private TaskGroupFragment taskGroupFragment;
    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    private final String[] titles = { "任务", "任务组", "我" };

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if (taskFragment == null) {
                    taskFragment = new TaskFragment();
                }
                return taskFragment;
            case 1:
                if (taskGroupFragment == null) {
                    taskGroupFragment = new TaskGroupFragment();
                }
                return taskGroupFragment;
            case 2:
                if (meFragment == null) {
                    meFragment = new MeFragment();
                }
                return meFragment;
            default:
                return null;
        }
    }

}
