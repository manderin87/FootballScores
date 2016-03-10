package com.hydeudacityproject.footballscores.FixturesView;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.hydeudacityproject.footballscores.AboutView.AboutView;
import com.hydeudacityproject.footballscores.FixturesView.Tabs.FixturesViewTab;
import com.hydeudacityproject.footballscores.MainApplication;
import com.hydeudacityproject.footballscores.R;
import com.hydeudacityproject.footballscores.Service.FixturesService;
import com.lonewolfgames.framework.AbstractAppActivity;
import com.lonewolfgames.framework.Tabs.AbstractPagerFragment;

/**
 * Created by jhyde on 1/8/2016.
 * Copyright (C) 2016 Jesse Hyde Lone Wolf Games
 */
public class FixturesView extends AbstractAppActivity<MainApplication, FixturesViewController> {

    private ViewPager mViewPager;
    private ViewPagerAdapter mViewAdapter;
    private TabLayout mTabLayout;

    private int mCurrentTab = 2;

    public FixturesView() {
       super(FixturesView.class.getSimpleName());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fixtures_view);

        mApp = (MainApplication) getApplicationContext();

        if(savedInstanceState != null && savedInstanceState.containsKey("current_tab")) {
            mCurrentTab = savedInstanceState.getInt("current_tab");
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        initializeToolbar(getResources().getString(R.string.app_name), false);

        mViewPager = (ViewPager) findViewById(R.id.viewPager_fixtures);
        mTabLayout = (TabLayout) findViewById(R.id.slidingTabLayout_fixtures);

        mController = new FixturesViewController(mApp, this);

        mViewAdapter = new ViewPagerAdapter(getFragmentManager());
        mViewPager.setAdapter(mViewAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mCurrentTab = tab.getPosition();
                Log.i(TAG, "Current Tab: " + String.valueOf(mCurrentTab));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mViewPager.setCurrentItem(mCurrentTab);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putInt("current_tab", mCurrentTab);

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fixtures_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_about: {
                Intent start_about = new Intent(this, AboutView.class);
                startActivity(start_about);
                return true;
            }
            default: return super.onOptionsItemSelected(item);
        }
    }

    public void updateTabs() {
        for(int index = 0; index < mViewAdapter.getCount(); index++) {
            FixturesViewTab tab = (FixturesViewTab) mViewAdapter.getItem(index);
            tab.updateViewController();
        }
    }

    public class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            AbstractPagerFragment fragment = mController.tabs().tab(position);

            return fragment;
        }

        @Override
        public int getCount() {
            return mController.tabs().size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mController.tabs().title(position);
        }
    }

}
