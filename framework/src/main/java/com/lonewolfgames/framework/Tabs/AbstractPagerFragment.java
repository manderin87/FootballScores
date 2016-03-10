package com.lonewolfgames.framework.Tabs;


import android.app.Fragment;

import com.lonewolfgames.framework.AbstractViewController;

/**
 * Created by jhyde on 7/7/2015.
 * Copyright (C) 2015 Jesse Hyde Lone Wolf Games
 */

public abstract class AbstractPagerFragment<P, D extends AbstractViewController> extends Fragment {

    // Static Variables ----------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------

    // View Member Variables -----------------------------------------------------------------------
    private D mViewController;
    private P mParentView;
    // ---------------------------------------------------------------------------------------------

    // Data Member Variables -----------------------------------------------------------------------
    protected boolean mIsVisible = false;
    // ---------------------------------------------------------------------------------------------

    // Layout Member Variables ---------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------

    public void setViewController(D viewController) {
        mViewController = viewController;
    }

    public D viewController() { return mViewController; }

    public void setParent(P parentView) {
        mParentView = parentView;
    }

    public P parent() { return mParentView; }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mIsVisible = isVisibleToUser;
    }
}
