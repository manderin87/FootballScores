package com.hydeudacityproject.footballscores.AboutView;

import com.hydeudacityproject.footballscores.MainApplication;
import com.lonewolfgames.framework.AbstractAppService;
import com.lonewolfgames.framework.AbstractViewController;

/**
 * Created by jhyde on 1/11/2016.
 * Copyright (C) 2016 Jesse Hyde Lone Wolf Games
 */
public class AboutViewController extends AbstractViewController<Void, AboutView, AboutViewController.AboutViewControllerListener> {

    public AboutViewController(MainApplication app, AboutView parentView) {
        super(app, parentView);
    }

    @Override
    public void initialize(Void data) {

    }


    public interface AboutViewControllerListener extends AbstractViewController.AbstractViewControllerListener {
        void OnDataLoaded();
        void OnDataError(AbstractAppService.ServiceError errorCode);
    }

}
