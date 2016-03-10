package com.lonewolfgames.framework;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.lonewolfgames.framework.UI.ProgressView;

/**
 * Created by jhyde on 1/11/2016.
 * Copyright (C) 2016 Jesse Hyde Lone Wolf Games
 */
public abstract class AbstractAppActivity<A extends Application, T extends AbstractViewController> extends AppCompatActivity {

    protected String TAG = getClass().getSimpleName();

    protected A mApp;
    protected Toolbar mToolbar;
//    protected DrawerLayout mDrawerLayout;
//    protected NavigationView mNavigationView;
//    protected ImageButton mNavigationButton;
    protected T mController;
    protected ProgressView mProgressView;

    public String getTag() { return TAG; }

    protected AbstractAppActivity(String tag) {
        TAG = tag;
    }

    protected void initializeToolbar() {
        if(mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle("");
        }
    }

    protected void initializeToolbar(String title, boolean showBack) {
        if(mToolbar != null) {
            setSupportActionBar(mToolbar);

            if(showBack) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeButtonEnabled(true);
            }

            getSupportActionBar().setTitle(title);
        }
    }

//    protected void initializeToolbar(int backButtonResourceId) {
//        if(mToolbar != null) {
//            setSupportActionBar(mToolbar);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setHomeButtonEnabled(true);
//            getSupportActionBar().setTitle("");
//
//            if(backButtonResourceId > 0) {
//                mToolbar.setNavigationIcon(backButtonResourceId);
//                mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        AbstractAppActivity.this.onBackPressed();
//                    }
//                });
//            }
//        }
//    }

//    protected void initializeNavigationDrawer(int navigationButtonResourceId, Listeners.AbstractNavigationItemSelectedListener listener) {
//        if(mToolbar != null) {
//            mToolbar.setNavigationIcon(navigationButtonResourceId);
//            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mDrawerLayout.openDrawer(GravityCompat.START);
//                }
//            });
//
//            mNavigationView.setNavigationItemSelectedListener(listener);
//
//            for(int i = 0; i < mToolbar.getChildCount(); i++) {
//                View view = mToolbar.getChildAt(i);
//                if(view instanceof ImageButton){
//                    if(view.getId() < 0) {
//                        mNavigationButton = (ImageButton) view;
//                        break;
//                    }
//                }
//            }
//        }
//    }

//    protected void initializeAnalytics(Class activityClass) {
//        //mApp = app;
//
//        if(mApp != null && mApp.isTracking()) {
//            Tracker tracker = mApp.getTracker(AbstractMainApplication.TrackerName.APP_TRACKER);
//            mApp.sendScreenHit(tracker, activityClass);
//        }
//    }

    @Override
    protected void onStart() {
        super.onStart();

//        if(mApp != null && mApp.isTracking()) {
//            GoogleAnalytics.getInstance(this).reportActivityStart(this);
//        }
    }

    @Override
    protected void onStop() {
        super.onStop();

//        if(mApp != null && mApp.isTracking()) {
//            GoogleAnalytics.getInstance(this).reportActivityStop(this);
//        }
    }

//    public void showProgress(){
//        if(mProgressView != null){
//            mProgressView.setVisibility(View.VISIBLE);
//        }
//    }
//
//    public void hideProgress(){
//        if(mProgressView != null){
//            mProgressView.setVisibility(View.GONE);
//        }
//    }

//    public View navigationButton() {
//        for(int i = 0; i < mToolbar.getChildCount(); i++) {
//            View view = mToolbar.getChildAt(i);
//            if(view instanceof ImageButton){
//                if(view.getId() < 0){
//                    return view;
//                }
//            }
//        }
//
//        return null;
//    }

}
