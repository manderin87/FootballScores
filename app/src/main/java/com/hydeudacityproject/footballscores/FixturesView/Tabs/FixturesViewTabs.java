package com.hydeudacityproject.footballscores.FixturesView.Tabs;

import com.hydeudacityproject.footballscores.MainApplication;
import com.hydeudacityproject.footballscores.R;
import com.hydeudacityproject.footballscores.Service.Framework.Fixture;
import com.lonewolfgames.framework.Tabs.AbstractPagerFragment;
import com.lonewolfgames.framework.Tabs.AbstractPagerTab;
import com.lonewolfgames.framework.Tabs.AbstractPagerTabs;
import com.lonewolfgames.framework.Utilities;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jhyde on 1/11/2016.
 * Copyright (C) 2016 Jesse Hyde Lone Wolf Games
 */
public class FixturesViewTabs extends AbstractPagerTabs<FixturesViewTabs.FixturesViewPagerTab> {

    public FixturesViewTabs(MainApplication app) {
        super(app);

        mTabs = new ArrayList<>();

        Date day_before_yesterday = Utilities.modifyDay(new Date(), -2);
        Date yesterday = Utilities.modifyDay(new Date(), -1);
        Date today = new Date();
        Date tomorrow = Utilities.modifyDay(new Date(), 1);
        Date day_after_tomorrow = Utilities.modifyDay(new Date(), 2);

        mTabs.add((FixturesViewPagerTab) new FixturesViewPagerTab.Builder(app)
                .tab(eFixturesViewTab.TAB_BEFORE_YESTERDAY)
                .title(Utilities.dateToString(Fixture.DAY_NAME_FORMAT, day_before_yesterday))
                .fragment(FixturesViewTab.newInstance(day_before_yesterday))
                .build());

        mTabs.add((FixturesViewPagerTab) new FixturesViewPagerTab.Builder(app)
                .tab(eFixturesViewTab.TAB_YESTERDAY)
                .title(eFixturesViewTab.TAB_YESTERDAY.title())
                .fragment(FixturesViewTab.newInstance(yesterday))
                .build());

        mTabs.add((FixturesViewPagerTab) new FixturesViewPagerTab.Builder(app)
                .tab(eFixturesViewTab.TAB_TODAY)
                .title(eFixturesViewTab.TAB_TODAY.title())
                .fragment(FixturesViewTab.newInstance(today))
                .build());

        mTabs.add((FixturesViewPagerTab) new FixturesViewPagerTab.Builder(app)
                .tab(eFixturesViewTab.TAB_TOMORROW)
                .title(eFixturesViewTab.TAB_TOMORROW.title())
                .fragment(FixturesViewTab.newInstance(tomorrow))
                .build());

        mTabs.add((FixturesViewPagerTab) new FixturesViewPagerTab.Builder(app)
                .tab(eFixturesViewTab.TAB_AFTER_TOMORROW)
                .title(Utilities.dateToString(Fixture.DAY_NAME_FORMAT, day_after_tomorrow))
                .fragment(FixturesViewTab.newInstance(day_after_tomorrow))
                .build());
    }

    public static class FixturesViewPagerTab extends AbstractPagerTab<AbstractPagerFragment, eFixturesViewTab> {

        private FixturesViewPagerTab(Builder build) {
            super(build);
        }

        public static class Builder extends AbstractPagerTab.Builder<AbstractPagerFragment, eFixturesViewTab> {

            public Builder(MainApplication app) {
                super(app);
            }

            public FixturesViewPagerTab build() {
                return new FixturesViewPagerTab(this);
            }
        }
    }

    public enum eFixturesViewTab {
        TAB_BEFORE_YESTERDAY(""),
        TAB_YESTERDAY(MainApplication.mInstance.getResources().getString(R.string.day_title_yesterday)),
        TAB_TODAY(MainApplication.mInstance.getResources().getString(R.string.day_title_today)),
        TAB_TOMORROW(MainApplication.mInstance.getResources().getString(R.string.day_title_tomorrow)),
        TAB_AFTER_TOMORROW("");

        private String mTitle = "";

        eFixturesViewTab(String title) {
            mTitle = title;
        }

        public String title() { return mTitle; }
    }

}
