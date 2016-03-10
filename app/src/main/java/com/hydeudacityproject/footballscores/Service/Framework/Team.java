package com.hydeudacityproject.footballscores.Service.Framework;

import android.database.Cursor;
import android.util.Log;

import com.hydeudacityproject.footballscores.Service.ContentProvider.FixtureContract;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jhyde on 1/8/2016.
 * Copyright (C) 2016 Jesse Hyde Lone Wolf Games
 */
public class Team {
    private long mId = -1;
    private String mName = "";
    private String mShortName = "";
    private String mSquadMarketValue = "";
    private String mCrestURL = "";

    public long id() { return mId; }
    public String name() { return mName; }
    public String shortName() { return mShortName; }
    public String squadMarketValue() { return mSquadMarketValue; }
    public String crestUrl() { return mCrestURL; }

    private Team(Builder build) {
        mId = build.mId;
        mName = build.mName;
        mShortName = build.mShortName;
        mSquadMarketValue = build.mSquadMarketValue;
        mCrestURL = build.mCrestURL;
    }

    public static class Builder {
        private long mId = -1;
        private String mName = "";
        private String mShortName = "";
        private String mSquadMarketValue = "";
        private String mCrestURL = "";

        public Builder() {
        }

        public Builder fromJSON(long teamId, String content) {
            Log.i("Team " + teamId, " Content: " + content);

            try {
                JSONObject json_object = new JSONObject(content);

                mId = teamId;

                if(json_object.has("name")) {
                    mName = json_object.getString("name");
                }

                if(json_object.has("shortName")) {
                    mShortName = json_object.getString("shortName");
                }

                if(json_object.has("squadMarketValue")) {
                    mSquadMarketValue = json_object.getString("squadMarketValue");
                }

                if(json_object.has("crestUrl")) {
                    mCrestURL = json_object.getString("crestUrl");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return this;
        }

        public Builder fromCursor(Cursor cursor) {
            mId = cursor.getLong(cursor.getColumnIndex(FixtureContract.TeamEntry._ID));
            mName = cursor.getString(cursor.getColumnIndex(FixtureContract.TeamEntry.NAME));
            mShortName = cursor.getString(cursor.getColumnIndex(FixtureContract.TeamEntry.SHORT_NAME));
            mSquadMarketValue = cursor.getString(cursor.getColumnIndex(FixtureContract.TeamEntry.SQUAD_MARKET_VALUE));
            mCrestURL = cursor.getString(cursor.getColumnIndex(FixtureContract.TeamEntry.CREST_URL));

            return this;
        }


        public Team build() {
            return new Team(this);
        }
    }
}
