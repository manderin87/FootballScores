package com.hydeudacityproject.footballscores.FixturesView;

import com.hydeudacityproject.footballscores.AppData;
import com.hydeudacityproject.footballscores.FixturesView.Tabs.FixturesViewTabs;
import com.hydeudacityproject.footballscores.MainApplication;
import com.lonewolfgames.framework.AbstractAppData;
import com.lonewolfgames.framework.AbstractAppService;
import com.lonewolfgames.framework.AbstractMainApplication;
import com.lonewolfgames.framework.AbstractViewController;

/**
 * Created by jhyde on 1/11/2016.
 * Copyright (C) 2016 Jesse Hyde Lone Wolf Games
 */
public class FixturesViewController extends AbstractViewController<Void, FixturesView, FixturesViewController.FixturesViewControllerListener> {

    private FixturesViewTabs mTabs;

    public FixturesViewController(MainApplication app, FixturesView parentView) {
        super(app, parentView);

        mTabs = new FixturesViewTabs((MainApplication) mApp);
    }

    @Override
    public void initialize(Void data) {

    }

    public FixturesViewTabs tabs() { return mTabs; }

    public interface FixturesViewControllerListener extends AbstractViewController.AbstractViewControllerListener {
        void OnDataLoaded();
        void OnDataError(AbstractAppService.ServiceError errorCode);
    }

}
