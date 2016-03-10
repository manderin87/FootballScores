package com.hydeudacityproject.footballscores.Service.Framework;

import android.database.Cursor;

import com.hydeudacityproject.footballscores.Service.ContentProvider.FixtureContract;
import com.hydeudacityproject.footballscores.Service.GetFixtures;
import com.lonewolfgames.framework.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.TimeZone;

/**
 * Created by jhyde on 1/8/2016.
 * Copyright (C) 2016 Jesse Hyde Lone Wolf Games
 */
public class Season {
    private long mId = -1;
    private String mCaption = "";
    private String mLeague = "";
    private String mYear = "";
    private int mCurrentMatchday = -1;
    private int mNumberOfMatchdays = -1;
    private int mNumberOfTeams = -1;
    private int mNumberOfGames = -1;
    private String mLastUpdated = "";

    public long id() { return mId; }
    public String caption() { return mCaption; }
    public String league() { return mLeague; }
    public String year() { return mYear; }
    public int currentMatchday() { return mCurrentMatchday; }
    public int numberOfMatchdays() { return mNumberOfMatchdays; }
    public int numberOfTeams() { return mNumberOfTeams; }
    public int numberOfGames() { return mNumberOfGames; }
    public String lastUpdated() { return mLastUpdated; }

    private Season(Builder build) {
        mId = build.mId;
        mCaption = build.mCaption;
        mLeague = build.mLeague;
        mYear = build.mYear;
        mCurrentMatchday = build.mCurrentMatchday;
        mNumberOfMatchdays = build.mNumberOfMatchdays;
        mNumberOfTeams = build.mNumberOfTeams;
        mNumberOfGames = build.mNumberOfGames;
        mLastUpdated = build.mLastUpdated;
    }

    public static class Builder {
        private long mId = -1;
        private String mCaption = "";
        private String mLeague = "";
        private String mYear = "";
        private int mCurrentMatchday = -1;
        private int mNumberOfMatchdays = -1;
        private int mNumberOfTeams = -1;
        private int mNumberOfGames = -1;
        private String mLastUpdated = "";

        public Builder() {

        }

        public Builder fromJSON(String content) {

            try {
                JSONObject json_object = new JSONObject(content);

                if(json_object.has("id")) {
                    mId = Utilities.parseInt(json_object.getString("id"));
                }

                if(json_object.has("caption")) {
                    mCaption = json_object.getString("caption");
                }

                if(json_object.has("league")) {
                    mLeague = json_object.getString("league");
                }

                if(json_object.has("year")) {
                    mYear = json_object.getString("year");
                }

                if(json_object.has("currentMatchday")) {
                    mCurrentMatchday = json_object.getInt("currentMatchday");
                }

                if(json_object.has("numberOfMatchdays")) {
                    mNumberOfMatchdays = json_object.getInt("numberOfMatchdays");
                }

                if(json_object.has("numberOfTeams")) {
                    mNumberOfTeams = json_object.getInt("numberOfTeams");
                }

                if(json_object.has("numberOfGames")) {
                    mNumberOfGames = json_object.getInt("numberOfGames");
                }

                if(json_object.has("lastUpdated")) {
                    mLastUpdated = json_object.getString("lastUpdated");
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return this;
        }

        public Builder fromCursor(Cursor cursor) {

            mId = cursor.getLong(cursor.getColumnIndex(FixtureContract.SeasonEntry._ID));
            mCaption = cursor.getString(cursor.getColumnIndex(FixtureContract.SeasonEntry.CAPTION));
            mLeague = cursor.getString(cursor.getColumnIndex(FixtureContract.SeasonEntry.LEAGUE));
            mYear = cursor.getString(cursor.getColumnIndex(FixtureContract.SeasonEntry.YEAR));
            mCurrentMatchday = cursor.getInt(cursor.getColumnIndex(FixtureContract.SeasonEntry.CURRENT_MATCHDAY));
            mNumberOfMatchdays = cursor.getInt(cursor.getColumnIndex(FixtureContract.SeasonEntry.NUMBER_OF_MATCHDAYS));
            mNumberOfTeams = cursor.getInt(cursor.getColumnIndex(FixtureContract.SeasonEntry.NUMBER_OF_TEAMS));
            mNumberOfGames = cursor.getInt(cursor.getColumnIndex(FixtureContract.SeasonEntry.NUMBER_OF_GAMES));
            mLastUpdated = cursor.getString(cursor.getColumnIndex(FixtureContract.SeasonEntry.LAST_UPDATED));

            return this;
        }


        public Season build() {
            return new Season(this);
        }
    }
}
