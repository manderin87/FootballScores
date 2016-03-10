package com.hydeudacityproject.footballscores.Service.ContentProvider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

/**
 * Created by jhyde on 1/8/2016.
 * Copyright (C) 2016 Jesse Hyde Lone Wolf Games
 */
public class FixtureContentProvider extends ContentProvider {

    private static final String TAG = FixtureContentProvider.class.getSimpleName();

    private static final int FIXTURES = 100;
    private static final int FIXTURES_BY_DATE = 102;

    private static final int TEAMS = 200;
    private static final int TEAM_ID = 201;

    private static final int SEASONS = 300;
    private static final int SEASON_ID = 301;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private FixtureDBHelper mHelper;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FixtureContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, FixtureContract.PATH_FIXTURES, FIXTURES);
        matcher.addURI(authority, FixtureContract.PATH_FIXTURES + "/#/date", FIXTURES_BY_DATE);
        matcher.addURI(authority, FixtureContract.PATH_TEAMS + "/#", TEAM_ID);
        matcher.addURI(authority, FixtureContract.PATH_TEAMS, TEAMS);
        matcher.addURI(authority, FixtureContract.PATH_SEASONS + "/#", SEASON_ID);
        matcher.addURI(authority, FixtureContract.PATH_SEASONS, SEASONS);

        return matcher;
    }


    @Override
    public boolean onCreate() {
        mHelper = new FixtureDBHelper(getContext());

        return (mHelper == null) ? false : true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final Cursor cursor;
        final SQLiteDatabase db = mHelper.getReadableDatabase();

        switch (sUriMatcher.match(uri)) {
            case FIXTURES:
                cursor = db.query(
                        FixtureContract.FixtureEntry.TABLE_NAME,
                        projection,
                        selection,
                        selection == null ? null : selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case FIXTURES_BY_DATE:
                cursor = db.query(
                        FixtureContract.FixtureEntry.TABLE_NAME,
                        projection,
                        selection,
                        selection == null ? null : selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case TEAMS:
                cursor = db.query(
                        FixtureContract.TeamEntry.TABLE_NAME,
                        projection,
                        selection,
                        selection == null ? null : selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case TEAM_ID:
                cursor = db.query(
                        FixtureContract.TeamEntry.TABLE_NAME,
                        projection,
                        FixtureContract.TeamEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        selection == null ? null : selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case SEASONS:
                cursor = db.query(
                        FixtureContract.SeasonEntry.TABLE_NAME,
                        projection,
                        selection,
                        selection == null ? null : selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case SEASON_ID:
                cursor = db.query(
                        FixtureContract.SeasonEntry.TABLE_NAME,
                        projection,
                        FixtureContract.SeasonEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        selection == null ? null : selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            default: throw new UnsupportedOperationException("Unknown Uri" + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch(match) {
            case FIXTURES:
                return FixtureContract.FixtureEntry.CONTENT_TYPE;
            case FIXTURES_BY_DATE:
                return FixtureContract.FixtureEntry.CONTENT_TYPE;
            case TEAMS:
                return FixtureContract.TeamEntry.CONTENT_TYPE;
            case TEAM_ID:
                return FixtureContract.TeamEntry.CONTENT_ITEM_TYPE;
            case SEASONS:
                return FixtureContract.SeasonEntry.CONTENT_TYPE;
            case SEASON_ID:
                return FixtureContract.SeasonEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mHelper.getWritableDatabase();

        Uri return_uri;

        switch(sUriMatcher.match(uri)) {
            case TEAMS: {
                long _id = db.insert(FixtureContract.TeamEntry.TABLE_NAME, null, values);
                if( _id > 0 ) {
                    return_uri = FixtureContract.TeamEntry.buildTeamUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }

                getContext().getContentResolver().notifyChange(FixtureContract.TeamEntry.buildTeamUri(_id), null);
                break;
            }
            case SEASONS: {
                long _id = db.insert(FixtureContract.SeasonEntry.TABLE_NAME, null, values);
                if( _id > 0 ) {
                    return_uri = FixtureContract.SeasonEntry.buildSeasonUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }

                getContext().getContentResolver().notifyChange(FixtureContract.TeamEntry.buildTeamUri(_id), null);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return return_uri;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mHelper.getWritableDatabase();

        switch(sUriMatcher.match(uri)) {
            case FIXTURES: {
                db.beginTransaction();

                int return_count = 0;

                try {
                    for(ContentValues value : values) {
                        long _id = db.insertWithOnConflict(FixtureContract.FixtureEntry.TABLE_NAME,
                                null,
                                value,
                                SQLiteDatabase.CONFLICT_REPLACE);

                        Log.i(TAG, "Fixture Added: " + _id);

                        if(_id != -1) {
                            return_count++;
                        }
                    }

                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                getContext().getContentResolver().notifyChange(uri, null);

                return return_count;
            }
            case TEAMS: {
                db.beginTransaction();

                int return_count = 0;

                try {
                    for(ContentValues value : values) {
                        long _id = db.insertWithOnConflict(FixtureContract.TeamEntry.TABLE_NAME,
                                null,
                                value,
                                SQLiteDatabase.CONFLICT_REPLACE);

                        if(_id != -1) {
                            return_count++;
                        }
                    }

                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                getContext().getContentResolver().notifyChange(uri, null);

                return return_count;
            }
            case SEASONS: {
                db.beginTransaction();

                int return_count = 0;

                try {
                    for(ContentValues value : values) {
                        long _id = db.insertWithOnConflict(FixtureContract.SeasonEntry.TABLE_NAME,
                                null,
                                value,
                                SQLiteDatabase.CONFLICT_REPLACE);

                        Log.i(TAG, "Season Added: " + _id);

                        if(_id != -1) {
                            return_count++;
                        }
                    }

                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                getContext().getContentResolver().notifyChange(uri, null);

                return return_count;
            }
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) { return 0; }
}
