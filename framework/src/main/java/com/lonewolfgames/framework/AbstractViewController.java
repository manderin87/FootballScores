package com.lonewolfgames.framework;

import android.app.Activity;
import android.content.Context;

/**
 * Created by jhyde on 8/24/2015.
 * Copyright (C) 2015 Jesse Hyde Lone Wolf Games
 */

public abstract class AbstractViewController<D, T extends Activity, L extends AbstractViewController.AbstractViewControllerListener> {

    protected AbstractMainApplication mApp;
    protected AbstractViewController mInstance = null;
    private D mData;
    protected T mParentView;
    protected L mListener;

    protected AbstractViewController(AbstractMainApplication app, T parentView) {
        mApp = app;

        mInstance = this;
        mParentView = parentView;

        initialize(mData);
    }

    protected AbstractViewController(AbstractMainApplication app, T parentView, L listener) {
        mApp = app;

        mInstance = this;
        mParentView = parentView;
        mListener = listener;

        initialize(mData);
    }

    protected AbstractViewController(AbstractMainApplication app, T parentView, L listener, D data) {
        mApp = app;

        mInstance = this;
        mParentView = parentView;
        mListener = listener;
        mData = data;

        initialize(mData);
    }

    public abstract void initialize(D data);

    public T parent() { return mParentView; }
    public Context context() { return mParentView; }
    //public D data() { return mData; }

    //public void setData(D data) {
    //    mData = data;
    //}
    //public void setListener(L listener) { mListener = listener; }

    public interface AbstractViewControllerListener {
    }

}
