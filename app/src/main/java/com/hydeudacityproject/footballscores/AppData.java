package com.hydeudacityproject.footballscores;

import android.content.Intent;
import android.util.Log;

import com.hydeudacityproject.footballscores.Service.ContentProvider.FixtureDBHelper;
import com.hydeudacityproject.footballscores.Service.FixturesService;
import com.hydeudacityproject.footballscores.Service.GetFixtures;
import com.hydeudacityproject.footballscores.Service.GetFixturesResponse;
import com.hydeudacityproject.footballscores.Service.GetSeasons;
import com.hydeudacityproject.footballscores.Service.GetSeasonsResponse;
import com.lonewolfgames.framework.AbstractAppData;
import com.lonewolfgames.framework.AbstractAppService;
import com.lonewolfgames.framework.AbstractAppSettings;
import com.lonewolfgames.framework.AbstractMainApplication;
import com.lonewolfgames.framework.Utilities;

import java.util.Date;

/**
 * Created by jhyde on 1/8/2016.
 * Copyright (C) 2016 Jesse Hyde Lone Wolf Games
 */
public class AppData extends AbstractAppData implements AbstractAppSettings.AppSettingsLoadedListener,
        GetFixtures.GetFixturesListener,
        GetSeasons.GetSeasonsListener {

    private FixtureDBHelper mHelper;

    public AppData(AbstractMainApplication app) {
        super(app);

        mHelper = new FixtureDBHelper(mApp);
    }

    public static AppData instance() {
        return (AppData) mInstance;
    }

    @Override
    public void OnAppSettingsLoadingFinished() {
        new GetSeasons(context(), this, Utilities.year(new Date())).execute();
        //Intent service_start = new Intent(mApp, FixturesService.class);
        //mApp.startService(service_start);
    }

    public FixtureDBHelper database() { return mHelper; }

    @Override
    public void OnGetSeasonsFinished(GetSeasonsResponse response) {

        if(response != null && response.items().size() > 0) {
            mHelper.addSeasons(response.items());
        }

        new GetFixtures(context(), this).execute();
    }

    @Override
    public void OnGetSeasonsError(AbstractAppService.ServiceError error) {

    }

    @Override
    public void OnGetFixturesFinished(GetFixturesResponse response) {

        if(response != null && response.items().size() > 0) {
            mHelper.addFixtures(response.items());
        }

        mLoaded = true;

        notifiyListeners();
    }

    @Override
    public void OnGetFixturesError(AbstractAppService.ServiceError error) {

    }


}
