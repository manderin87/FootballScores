package com.hydeudacityproject.footballscores.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.hydeudacityproject.footballscores.FixturesView.FixturesView;
import com.hydeudacityproject.footballscores.R;
import com.hydeudacityproject.footballscores.SplashView.SplashView;

/**
 * Created by jhyde on 1/12/2016.
 * Copyright (C) 2016 Jesse Hyde Lone Wolf Games
 */
public class FixturesWidgetProvider extends AppWidgetProvider {

    private static final String TAG = FixturesWidgetProvider.class.getSimpleName();

    public static final String START_ACTION = "com.hydeudacityproject.footballscores.appwidget.START_ACTION";
    public static final String REFRESH_ACTION = "com.hydeudacityproject.footballscores.appwidget.action.REFRESH_ACTION";
    public static final String EXTRA_ITEM = "com.hydeudacityproject.footballscores.appwidget.EXTRA_ITEM";

    public static Intent getRefreshBroadcastIntent(Context context) {
        return new Intent(REFRESH_ACTION).setComponent(new ComponentName(context, FixturesWidgetProvider.class));
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i(TAG, "Action: " + intent.getAction());

        if(intent.getAction().equals(START_ACTION)) {
            Intent newIntent = new Intent(context, SplashView.class);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(newIntent);
        }

        if(intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE) ||
                intent.getAction().equals(REFRESH_ACTION)) {
            Log.i(TAG, "Refreshing widget");
            AppWidgetManager app_widget_manager = AppWidgetManager.getInstance(context);
            // Notify the widget that the list view needs to be updated
            app_widget_manager.notifyAppWidgetViewDataChanged(
                    app_widget_manager.getAppWidgetIds(new ComponentName(context, FixturesWidgetProvider.class)),
                    R.id.listView_fixtures);
        }

        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for(int i = 0; i < appWidgetIds.length; i++) {

            // Widget layout
            RemoteViews remote_views = updateWidgetListView(context, appWidgetIds[i]);

            // Create an Intent to launch the App
            final Intent intent = new Intent(context, FixturesWidgetProvider.class);
            intent.setAction(START_ACTION);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            final PendingIntent pending_intent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remote_views.setPendingIntentTemplate(R.id.listView_fixtures, pending_intent);

            // Update widget
            appWidgetManager.updateAppWidget(appWidgetIds[i], remote_views);
        }
    }


    private RemoteViews updateWidgetListView(Context context, int appWidgetId) {

        // Layout
        RemoteViews remote_views = new RemoteViews(context.getPackageName(),R.layout.fixtures_widget);

        // RemoteViews Service
        final Intent service_intent = new Intent(context, FixturesWidgetService.class);

        // Pass app widget id to RemoteViews Service
        service_intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        service_intent.setData(Uri.parse(service_intent.toUri(Intent.URI_INTENT_SCHEME)));

        // Set adapter to listview of the widget
        remote_views.setRemoteAdapter(R.id.listView_fixtures, service_intent);
        // Set an empty view
        remote_views.setEmptyView(R.id.listView_fixtures, R.id.empty_view);


        return remote_views;
    }
}
