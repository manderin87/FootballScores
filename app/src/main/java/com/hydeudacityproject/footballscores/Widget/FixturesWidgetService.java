package com.hydeudacityproject.footballscores.Widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.hydeudacityproject.footballscores.AppData;
import com.hydeudacityproject.footballscores.R;
import com.hydeudacityproject.footballscores.Service.Framework.Fixture;
import com.lonewolfgames.framework.Cache.Images.ImageCache;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jhyde on 1/12/2016.
 * Copyright (C) 2016 Jesse Hyde Lone Wolf Games
 */
public class FixturesWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new FixturesViewFactory(getApplicationContext(), intent);
    }


    public class FixturesViewFactory implements RemoteViewsService.RemoteViewsFactory {

        private Context mContext;
        private ArrayList<Fixture> mItems;
        private int mAppWidgetId = -1;

        public FixturesViewFactory(Context context, Intent intent) {
            mContext = context;
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {
            mItems = AppData.instance().database().fixturesByDate(Fixture.FIXTURE_DAY_FORMAT.format(new Date()));
        }

        @Override
        public void onDataSetChanged() {
            mItems = AppData.instance().database().fixturesByDate(Fixture.FIXTURE_DAY_FORMAT.format(new Date()));
        }

        @Override
        public void onDestroy() {
            if(mItems != null) {
                mItems.clear();
            }
        }

        @Override
        public int getCount() {
            return mItems != null ? mItems.size() : 0;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            final RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.fixtures_widget_fixture_item);

            Fixture fixture = mItems.get(position);

            rv.setTextViewText(R.id.textView_home_team_name, fixture.homeTeamName());
            rv.setTextViewText(R.id.textView_away_team_name, fixture.awayTeamName());


            if(fixture.homeTeamGoals() < 0) {
                rv.setTextViewText(R.id.textView_home_team_goals, "X");
            } else {
                rv.setTextViewText(R.id.textView_home_team_goals, String.valueOf(fixture.homeTeamGoals()));
            }

            if(fixture.awayTeamGoals() < 0) {
                rv.setTextViewText(R.id.textView_away_team_goals, "X");
            } else {
                rv.setTextViewText(R.id.textView_away_team_goals, String.valueOf(fixture.awayTeamGoals()));
            }

            rv.setTextViewText(R.id.textView_fixture_time, fixture.time());

            Bitmap home_team_crest = ImageCache.instance().loadBitmapFromFile(String.valueOf(fixture.homeTeamId()) + ".png");
            Bitmap away_team_crest = ImageCache.instance().loadBitmapFromFile(String.valueOf(fixture.awayTeamId()) + ".png");

            if(home_team_crest != null) {
                rv.setImageViewBitmap(R.id.imageView_home_team_crest, home_team_crest);
            } else {
                rv.setImageViewResource(R.id.imageView_home_team_crest, R.drawable.ic_missing_crest);
            }

            if(away_team_crest != null) {
                rv.setImageViewBitmap(R.id.imageView_away_team_crest, away_team_crest);
            } else {
                rv.setImageViewResource(R.id.imageView_away_team_crest, R.drawable.ic_missing_crest);
            }

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                rv.setContentDescription(R.id.imageView_home_team_crest, fixture.homeTeamName());
                rv.setContentDescription(R.id.imageView_away_team_crest, fixture.awayTeamName());
            }

            // Set a fill-intent, which will be used to fill in the pending intent template
            final Intent fill_in_intent = new Intent();
            final Bundle extras = new Bundle();
            extras.putInt(FixturesWidgetProvider.EXTRA_ITEM, position);
            fill_in_intent.putExtras(extras);
            // On-click action of a given item
            rv.setOnClickFillInIntent(R.id.linearLayout_item, fill_in_intent);

            return rv;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
