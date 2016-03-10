package com.hydeudacityproject.footballscores.Service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.hydeudacityproject.footballscores.AppData;
import com.hydeudacityproject.footballscores.Widget.FixturesWidgetProvider;
import com.lonewolfgames.framework.AbstractAppService;

/**
 * Created by jhyde on 1/14/2016.
 * Copyright (C) 2016 Jesse Hyde Lone Wolf Games
 */
public class FixturesService extends IntentService implements GetFixtures.GetFixturesListener {

    private static final String TAG = FixturesService.class.getSimpleName();

    public static final String API_KEY = "requires_key";

    public FixturesService() {
        super(FixturesService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Log.i(TAG, "onHandleIntent");
                    new GetFixtures(FixturesService.this, FixturesService.this).execute();
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        };

        thread.start();
    }

    @Override
    public void OnGetFixturesFinished(GetFixturesResponse response) {
        if(response != null && response.items().size() > 0) {
            AppData.instance().database().addFixtures(response.items());
            sendBroadcast(FixturesWidgetProvider.getRefreshBroadcastIntent(this));
        }
    }

    @Override
    public void OnGetFixturesError(AbstractAppService.ServiceError error) {

    }
}
