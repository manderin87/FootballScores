package com.hydeudacityproject.footballscores.FixturesView.Tabs;

import com.hydeudacityproject.footballscores.AppData;
import com.hydeudacityproject.footballscores.FixturesView.FixturesView;
import com.hydeudacityproject.footballscores.Service.Framework.Fixture;
import com.lonewolfgames.framework.AbstractMainApplication;
import com.lonewolfgames.framework.AbstractViewController;
import com.lonewolfgames.framework.Tabs.AbstractPagerFragment;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jhyde on 1/11/2016.
 * Copyright (C) 2016 Jesse Hyde Lone Wolf Games
 */
public class FixturesViewTabViewController extends AbstractViewController<ArrayList<Fixture>, FixturesView,
        FixturesViewTabViewController.FixturesTabViewControllerListener> {


    public FixturesViewTabViewController(AbstractMainApplication app, FixturesView parentView, FixturesViewTab tab, ArrayList<Fixture> data) {
        super(app, parentView, tab, data);

    }

    @Override
    public void initialize(ArrayList<Fixture> data) {
        if(mListener != null) {
            mListener.OnDataLoaded(data);
        }

    }

    public interface FixturesTabViewControllerListener extends AbstractViewController.AbstractViewControllerListener {
        void OnDataLoaded(ArrayList<Fixture> data);
        void OnDataError();
    }

}
