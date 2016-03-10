package com.hydeudacityproject.footballscores.SplashView;

import com.hydeudacityproject.footballscores.AppData;
import com.lonewolfgames.framework.AbstractAppData;
import com.lonewolfgames.framework.AbstractAppService;
import com.lonewolfgames.framework.AbstractMainApplication;
import com.lonewolfgames.framework.AbstractViewController;

/**
 * Created by jhyde on 1/12/2016.
 * Copyright (C) 2016 Jesse Hyde Lone Wolf Games
 */
public class SplashViewController extends AbstractViewController<Void, SplashView, SplashViewController.SplashViewControllerListener>
        implements AbstractAppData.AppDataLoadedListener {

    public SplashViewController(AbstractMainApplication app, SplashView parentView) {
        super(app, parentView, parentView);

        AppData.instance().addListener(this);

        if(AppData.instance().isLoaded()) {
            AppData.instance().removeListener(this);
            mListener.OnDataLoaded();
        }
    }

    @Override
    public void initialize(Void data) {

    }

    @Override
    public void OnAppDataLoadingFinished() {
        AppData.instance().removeListener(this);
        mListener.OnDataLoaded();
    }

    public interface SplashViewControllerListener extends AbstractViewController.AbstractViewControllerListener {
        void OnDataLoaded();
        void OnDataError(AbstractAppService.ServiceError errorCode);
    }
}
