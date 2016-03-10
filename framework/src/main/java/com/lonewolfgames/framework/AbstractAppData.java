package com.lonewolfgames.framework;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by jhyde on 8/13/2015.
 * Copyright (C) 2015 Jesse Hyde Lone Wolf Games
 */

public abstract class AbstractAppData {

    protected AbstractMainApplication mApp;
    protected static AbstractAppData mInstance;

    protected boolean mLoaded = false;

    protected ArrayList<AppDataLoadedListener> mListeners;

    protected AbstractAppData(AbstractMainApplication app) {
        this(app, null);
    }

    protected AbstractAppData(AbstractMainApplication app, AppDataLoadedListener listener) {
        mApp = app;
        mInstance = this;

        addListener(listener);
    }

    public Context context() { return mApp; }

    public boolean isLoaded() { return mLoaded; }

    public void addListener(AppDataLoadedListener listener) {
        if(listener == null) {
            return;
        }

        if(mListeners == null) {
            mListeners = new ArrayList<>();
        }

        mListeners.add(listener);
    }

    public void removeListener(AppDataLoadedListener listener) {
        if(mListeners == null) {
            return;
        }

        mListeners.remove(listener);
    }

    public void notifiyListeners() {
        if(mListeners != null && mListeners.size() > 0) {
            for(AppDataLoadedListener listener : mListeners) {
                listener.OnAppDataLoadingFinished();
            }
        }
    }

    public interface AppDataLoadedListener {
        void OnAppDataLoadingFinished();
    }

}
