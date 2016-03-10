package com.hydeudacityproject.footballscores.SplashView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.hydeudacityproject.footballscores.FixturesView.FixturesView;
import com.hydeudacityproject.footballscores.MainApplication;
import com.hydeudacityproject.footballscores.R;
import com.lonewolfgames.framework.AbstractAppActivity;
import com.lonewolfgames.framework.AbstractAppService;

/**
 * Created by jhyde on 1/12/2016.
 * Copyright (C) 2016 Jesse Hyde Lone Wolf Games
 */
public class SplashView  extends AbstractAppActivity<MainApplication, SplashViewController> implements
        SplashViewController.SplashViewControllerListener {

    public SplashView() {
        super(SplashView.class.getSimpleName());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_view);

        mApp = (MainApplication) getApplicationContext();

        mController = new SplashViewController(mApp, this);
    }

    public void goToFixturesView() {
        Intent intent = new Intent(this, FixturesView.class);

        startActivity(intent);
        finish();
    }

    @Override
    public void OnDataLoaded() {
        goToFixturesView();
    }

    @Override
    public void OnDataError(AbstractAppService.ServiceError errorCode) {

    }
}
