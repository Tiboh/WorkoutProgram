package com.erikbuto.workoutprogram.Home;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * Created by Utilisateur on 22/07/2015.
 */
public class TabsAdapter extends FragmentPagerAdapter {
    private final Context mContext;
    private final ViewPager mViewPager;
    private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

    static final class TabInfo {
        private final Class<?> clss;
        private final Bundle args;
        private final String title;

        TabInfo(Class<?> _class, Bundle _args, String _title) {
            clss = _class;
            args = _args;
            title = _title;
        }
    }

    public TabsAdapter(AppCompatActivity activity, ViewPager pager) {
        super(activity.getSupportFragmentManager());
        mContext = activity;
        mViewPager = pager;
        mViewPager.setAdapter(this);
    }

    public void addTab(TabLayout.Tab tab, Class<?> clss, Bundle args, String title) {
        TabInfo info = new TabInfo(clss, args, title);
        tab.setTag(info);
        mTabs.add(info);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mTabs.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabs.get(position).title;
    }

    @Override
    public Fragment getItem(int position) {
        TabInfo info = mTabs.get(position);
        return Fragment.instantiate(mContext, info.clss.getName(), info.args);
    }
}