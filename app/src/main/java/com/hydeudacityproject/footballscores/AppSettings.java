package com.hydeudacityproject.footballscores;

import com.lonewolfgames.framework.AbstractAppSettings;
import com.lonewolfgames.framework.AbstractMainApplication;

/**
 * Created by jhyde on 1/8/2016.
 * Copyright (C) 2016 Jesse Hyde Lone Wolf Games
 */
public class AppSettings extends AbstractAppSettings {

    public AppSettings(AbstractMainApplication app, AppSettingsLoadedListener listener) {
        super(app, listener);

        if(mListener != null) {
            mListener.OnAppSettingsLoadingFinished();
        }
    }
}
