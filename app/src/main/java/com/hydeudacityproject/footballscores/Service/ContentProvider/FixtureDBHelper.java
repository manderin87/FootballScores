package com.hydeudacityproject.footballscores.Service.ContentProvider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.ParcelFormatException;

import com.hydeudacityproject.footballscores.Service.Framework.Fixture;
import com.hydeudacityproject.footballscores.Service.Framework.Season;
import com.hydeudacityproject.footballscores.Service.Framework.Team;

import java.util.ArrayList;

/**
 * Created by jhyde on 1/8/2016.
 * Copyright (C) 2016 Jesse Hyde Lone Wolf Games
 */
public class FixtureDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "footballscores.db";
    private static final int DATABASE_VERSION = 1;

    private Context mContext;
    private ContentResolver mContentResolver;

    public FixtureDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        mContext = context;
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FIXTURE_TABLE = "CREATE TABLE " + FixtureContract.FixtureEntry.TABLE_NAME + " (" +
                FixtureContract.FixtureEntry._ID + " INTEGER PRIMARY KEY," +
                FixtureContract.FixtureEntry.SEASON_ID + " INTEGER NOT NULL," +
                FixtureContract.FixtureEntry.DATE + " TEXT NOT NULL," +
                FixtureContract.FixtureEntry.TIME + " TEXT NOT NULL," +
                FixtureContract.FixtureEntry.MATCH_DAY + " INTEGER NOT NULL," +
                FixtureContract.FixtureEntry.HOME_TEAM_NAME + " TEXT NOT NULL," +
                FixtureContract.FixtureEntry.HOME_TEAM_ID + " INTEGER NOT NULL," +
                FixtureContract.FixtureEntry.HOME_TEAM_CREST_URL + " TEXT NOT NULL," +
                FixtureContract.FixtureEntry.AWAY_TEAM_NAME + " TEXT NOT NULL," +
                FixtureContract.FixtureEntry.AWAY_TEAM_ID + " INTEGER NOT NULL," +
                FixtureContract.FixtureEntry.AWAY_TEAM_CREST_URL + " TEXT NOT NULL," +
                FixtureContract.FixtureEntry.HOME_TEAM_GOALS + " INTEGER NOT NULL," +
                FixtureContract.FixtureEntry.AWAY_TEAM_GOALS + " INTEGER NOT NULL,"
                + " UNIQUE (" + FixtureContract.FixtureEntry._ID + ") ON CONFLICT REPLACE"
                + " );";

        final String SQL_CREATE_TEAM_TABLE = "CREATE TABLE " + FixtureContract.TeamEntry.TABLE_NAME + " (" +
                FixtureContract.TeamEntry._ID + " INTEGER PRIMARY KEY," +
                FixtureContract.TeamEntry.NAME + " TEXT NOT NULL," +
                FixtureContract.TeamEntry.SHORT_NAME + " TEXT NOT NULL," +
                FixtureContract.TeamEntry.SQUAD_MARKET_VALUE + " TEXT NOT NULL," +
                FixtureContract.TeamEntry.CREST_URL + " TEXT NOT NULL,"
                + " UNIQUE (" + FixtureContract.TeamEntry._ID + ") ON CONFLICT REPLACE"
                + " );";

        final String SQL_CREATE_SEASON_TABLE = "CREATE TABLE " + FixtureContract.SeasonEntry.TABLE_NAME + " (" +
                FixtureContract.SeasonEntry._ID + " INTEGER PRIMARY KEY," +
                FixtureContract.SeasonEntry.CAPTION + " TEXT NOT NULL," +
                FixtureContract.SeasonEntry.LEAGUE + " TEXT NOT NULL," +
                FixtureContract.SeasonEntry.YEAR + " TEXT NOT NULL," +
                FixtureContract.SeasonEntry.CURRENT_MATCHDAY + " INTEGER NOT NULL," +
                FixtureContract.SeasonEntry.NUMBER_OF_MATCHDAYS + " INTERGER NOT NULL," +
                FixtureContract.SeasonEntry.NUMBER_OF_TEAMS + " INTEGER NOT NULL," +
                FixtureContract.SeasonEntry.NUMBER_OF_GAMES + " INTEGER NOT NULL," +
                FixtureContract.SeasonEntry.LAST_UPDATED + " TEXT NOT NULL,"
                + " UNIQUE (" + FixtureContract.SeasonEntry._ID + ") ON CONFLICT REPLACE"
                + " );";

        db.execSQL(SQL_CREATE_FIXTURE_TABLE);
        db.execSQL(SQL_CREATE_TEAM_TABLE);
        db.execSQL(SQL_CREATE_SEASON_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addFixtures(ArrayList<Fixture> fixtures) {
        ArrayList<ContentValues> values = new ArrayList<>();

        for(Fixture fixture : fixtures) {
            ContentValues fixture_values = new ContentValues();
            fixture_values.put(FixtureContract.FixtureEntry._ID, fixture.id());
            fixture_values.put(FixtureContract.FixtureEntry.SEASON_ID, fixture.seasonId());
            fixture_values.put(FixtureContract.FixtureEntry.DATE, fixture.date());
            fixture_values.put(FixtureContract.FixtureEntry.TIME, fixture.time());
            fixture_values.put(FixtureContract.FixtureEntry.MATCH_DAY, fixture.matchDay());
            fixture_values.put(FixtureContract.FixtureEntry.HOME_TEAM_NAME, fixture.homeTeamName());
            fixture_values.put(FixtureContract.FixtureEntry.HOME_TEAM_ID, fixture.homeTeamId());
            fixture_values.put(FixtureContract.FixtureEntry.HOME_TEAM_CREST_URL, fixture.homeTeamCrestUrl());
            fixture_values.put(FixtureContract.FixtureEntry.AWAY_TEAM_NAME, fixture.awayTeamName());
            fixture_values.put(FixtureContract.FixtureEntry.AWAY_TEAM_ID, fixture.awayTeamId());
            fixture_values.put(FixtureContract.FixtureEntry.AWAY_TEAM_CREST_URL, fixture.awayTeamCrestUrl());
            fixture_values.put(FixtureContract.FixtureEntry.HOME_TEAM_GOALS, fixture.homeTeamGoals());
            fixture_values.put(FixtureContract.FixtureEntry.AWAY_TEAM_GOALS, fixture.awayTeamGoals());
            values.add(fixture_values);
        }

        ContentValues[] insert_data_values = new ContentValues[values.size()];
        values.toArray(insert_data_values);

        mContentResolver.bulkInsert(FixtureContract.FixtureEntry.CONTENT_URI, insert_data_values);
    }

    public ArrayList<Fixture> fixturesByDate(String date) {
        ArrayList<Fixture> fixtures = new ArrayList<>();

        Cursor cursor = mContentResolver.query(
                FixtureContract.FixtureEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                FixtureContract.FixtureEntry.DATE + "=?", // cols for "where" clause
                new String[] { date }, // values for "where" clause
                FixtureContract.FixtureEntry.TIME  // sort order
        );

        if(cursor != null && cursor.moveToFirst()) {
            do {
                fixtures.add(new Fixture.Builder().fromCursor(cursor).build());
            } while(cursor.moveToNext());

            if(!cursor.isClosed()) {
                cursor.close();
            }
        }

        return fixtures;
    }

    public void addTeam(Team team) {
        if(team == null) {
            return;
        }

        ContentValues values = new ContentValues();
            values.put(FixtureContract.TeamEntry._ID, team.id());
            values.put(FixtureContract.TeamEntry.NAME, team.name());
            values.put(FixtureContract.TeamEntry.SHORT_NAME, team.shortName());
            values.put(FixtureContract.TeamEntry.SQUAD_MARKET_VALUE, team.squadMarketValue());
            values.put(FixtureContract.TeamEntry.CREST_URL, team.crestUrl());

        mContentResolver.insert(FixtureContract.TeamEntry.CONTENT_URI, values);
    }

    public boolean hasTeam(int teamId) {
        Cursor cursor = null;

        try {
            cursor = mContentResolver.query(
                    FixtureContract.TeamEntry.buildTeamUri(teamId),
                    null, // leaving "columns" null just returns all the columns.
                    null, // cols for "where" clause
                    null, // values for "where" clause
                    null  // sort order
            );
        } catch(ParcelFormatException ex) {
            return false;
        }

        if(cursor == null) {
            return false;
        }

        if(cursor.getCount() > 0) {
            cursor.close();
            return true;
        }

        if(cursor != null) {
            cursor.close();
        }
        return false;
    }

    public Team getTeam(long teamId) {
        Team team = null;
        Cursor cursor = null;

        try {
            cursor = mContentResolver.query(
                    FixtureContract.TeamEntry.buildTeamUri(teamId),
                    null, // leaving "columns" null just returns all the columns.
                    null, // cols for "where" clause
                    null, // values for "where" clause
                    null  // sort order
            );
        } catch(ParcelFormatException ex) {
            return null;
        }

        if(cursor != null && cursor.moveToFirst()) {
            do {
                team = new Team.Builder().fromCursor(cursor).build();
            } while(cursor.moveToNext());

            if(!cursor.isClosed()) {
                cursor.close();
            }
        }

        return team;
    }

    public void addSeasons(ArrayList<Season> seasons) {
        ArrayList<ContentValues> values = new ArrayList<>();

        for(Season season : seasons) {
            ContentValues fixture_values = new ContentValues();
            fixture_values.put(FixtureContract.SeasonEntry._ID, season.id());
            fixture_values.put(FixtureContract.SeasonEntry.CAPTION, season.caption());
            fixture_values.put(FixtureContract.SeasonEntry.LEAGUE, season.league());
            fixture_values.put(FixtureContract.SeasonEntry.YEAR, season.year());
            fixture_values.put(FixtureContract.SeasonEntry.CURRENT_MATCHDAY, season.currentMatchday());
            fixture_values.put(FixtureContract.SeasonEntry.NUMBER_OF_MATCHDAYS, season.numberOfMatchdays());
            fixture_values.put(FixtureContract.SeasonEntry.NUMBER_OF_TEAMS, season.numberOfTeams());
            fixture_values.put(FixtureContract.SeasonEntry.NUMBER_OF_GAMES, season.numberOfGames());
            fixture_values.put(FixtureContract.SeasonEntry.LAST_UPDATED, season.lastUpdated());
            values.add(fixture_values);
        }

        ContentValues[] insert_data_values = new ContentValues[values.size()];
        values.toArray(insert_data_values);

        mContentResolver.bulkInsert(FixtureContract.SeasonEntry.CONTENT_URI, insert_data_values);
    }

    public boolean hasSeason(long seasonId) {
        Season season = null;
        Cursor cursor = null;

        try {
            cursor = mContentResolver.query(
                    FixtureContract.SeasonEntry.buildSeasonUri(seasonId),
                    null, // leaving "columns" null just returns all the columns.
                    null, // cols for "where" clause
                    null, // values for "where" clause
                    null  // sort order
            );
        } catch(ParcelFormatException ex) {
            return false;
        }

        if(cursor != null && cursor.moveToFirst()) {
            do {
                season = new Season.Builder().fromCursor(cursor).build();
            } while(cursor.moveToNext());

            if(!cursor.isClosed()) {
                cursor.close();
            }
        }

        if(season != null) {
            return true;
        }

        return false;
    }

    public Season getSeason(long seasonId) {
        Season season = null;
        Cursor cursor = null;

        try {
            cursor = mContentResolver.query(
                    FixtureContract.SeasonEntry.buildSeasonUri(seasonId),
                    null, // leaving "columns" null just returns all the columns.
                    null, // cols for "where" clause
                    null, // values for "where" clause
                    null  // sort order
            );
        } catch(ParcelFormatException ex) {
            return null;
        }

        if(cursor != null && cursor.moveToFirst()) {
            do {
                season = new Season.Builder().fromCursor(cursor).build();
            } while(cursor.moveToNext());

            if(!cursor.isClosed()) {
                cursor.close();
            }
        }

        return season;
    }
}
