package com.hydeudacityproject.footballscores.Service.Framework;

import android.database.Cursor;

import com.hydeudacityproject.footballscores.MainApplication;
import com.hydeudacityproject.footballscores.R;
import com.hydeudacityproject.footballscores.Service.ContentProvider.FixtureContract;
import com.hydeudacityproject.footballscores.Service.GetFixtures;
import com.lonewolfgames.framework.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by jhyde on 1/8/2016.
 * Copyright (C) 2016 Jesse Hyde Lone Wolf Games
 */
public class Fixture {

    public static final SimpleDateFormat FIXTURE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    public static final SimpleDateFormat FIXTURE_DAY_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat FIXTURE_TIME_FORMAT = new SimpleDateFormat("HH:mm");
    public static final SimpleDateFormat DAY_NAME_FORMAT = new SimpleDateFormat("EEEE");

    private long mId = -1;
    private int mSeasonId = 0;
    private String mDate = "";
    private String mTime = "";
    private int mMatchDay = 0;
    private String mHomeTeamName = "";
    private long mHomeTeamId = -1;
    private String mHomeTeamCrestUrl = "";
    private String mAwayTeamName = "";
    private long mAwayTeamId = -1;
    private String mAwayTeamCrestUrl = "";
    private int mHomeTeamGoals = 0;
    private int mAwayTeamGoals = 0;

    public long id() { return mId; }
    public int seasonId() { return mSeasonId; }
    public String date() { return mDate; }
    public String time() { return mTime; }
    public int matchDay() { return mMatchDay; }
    public String homeTeamName() { return mHomeTeamName; }
    public long homeTeamId() { return mHomeTeamId; }
    public String homeTeamCrestUrl() { return mHomeTeamCrestUrl; }
    public String awayTeamName() { return mAwayTeamName; }
    public long awayTeamId() { return mAwayTeamId; }
    public String awayTeamCrestUrl() { return mAwayTeamCrestUrl; }
    public int homeTeamGoals() { return mHomeTeamGoals; }
    public int awayTeamGoals() { return mAwayTeamGoals; }

    public void setHomeTeamCrestUrl(String url) {
        mHomeTeamCrestUrl = parseCrestPNG(url);
    }

    public void setAwayTeamCrestUrl(String url) {
        mAwayTeamCrestUrl = parseCrestPNG(url);
    }

    public String matchDayChampionsLeague() {
        switch(mMatchDay) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                return MainApplication.mInstance.getResources().getString(R.string.champions_league_group) + String.valueOf(mMatchDay);
            case 7:
                return MainApplication.mInstance.getResources().getString(R.string.champions_league_161st);
            case 8:
                return MainApplication.mInstance.getResources().getString(R.string.champions_league_162nd);
            case 9:
                return MainApplication.mInstance.getResources().getString(R.string.champions_league_QF1st);
            case 10:
                return MainApplication.mInstance.getResources().getString(R.string.champions_league_QF2nd);
            case 11:
                return MainApplication.mInstance.getResources().getString(R.string.champions_league_SF1st);
            case 12:
                return MainApplication.mInstance.getResources().getString(R.string.champions_league_SF2nd);
            case 13:
                return MainApplication.mInstance.getResources().getString(R.string.champions_league_final);
            default:
                return MainApplication.mInstance.getResources().getString(R.string.matchday) + String.valueOf(mMatchDay);
        }
    }

    private String parseCrestPNG(String url) {
        if(url.contains(".svg")) {
            String[] str_array = url.split("/");

            StringBuilder sb = new StringBuilder();

            for(int index = 0; index < str_array.length; index++) {
                switch(index) {
                    case 0:
                        str_array[index] = str_array[index].replace("http:", "https:");
                        sb.append(str_array[index] + "//");
                        break;
                    case 1:
                        break;
                    case 2:
                    case 3:
                    case 5:
                    case 6:
                        sb.append(str_array[index] + "/");
                        break;
                    case 4:
                        sb.append(str_array[index] + "/thumb/");
                        break;
                    case 7:
                        sb.append(str_array[index] + "/256px-" + str_array[index] + ".png");
                }
            }

            return sb.toString();
        } else {
            url = url.replace("http:", "https:");
        }

        return url;
    }

    private Fixture(Builder build) {
        mId = build.mId;
        mSeasonId = build.mSeasonId;
        mDate = build.mDate;
        mTime = build.mTime;
        mMatchDay = build.mMatchDay;
        mHomeTeamName = build.mHomeTeamName;
        mHomeTeamId = build.mHomeTeamId;
        mHomeTeamCrestUrl = build.mHomeTeamCrestUrl;
        mAwayTeamName = build.mAwayTeamName;
        mAwayTeamId = build.mAwayTeamId;
        mAwayTeamCrestUrl = build.mAwayTeamCrestUrl;
        mHomeTeamGoals = build.mHomeTeamGoals;
        mAwayTeamGoals = build.mAwayTeamGoals;
    }


    public static class Builder {
        private long mId = -1;
        private int mSeasonId = 0;
        private String mDate = "";
        private String mTime = "";
        private int mMatchDay = 0;
        private String mHomeTeamName = "";
        private long mHomeTeamId = -1;
        private String mHomeTeamCrestUrl = "";
        private String mAwayTeamName = "";
        private long mAwayTeamId = -1;
        private String mAwayTeamCrestUrl = "";
        private int mHomeTeamGoals = -1;
        private int mAwayTeamGoals = -1;

        public Builder() {

        }

        public Builder fromJSON(String content) {

            try {
                JSONObject json_object = new JSONObject(content);

                if(json_object.has("_links")) {
                    JSONObject links_object = json_object.getJSONObject("_links");

                    if(links_object.has("self")) {
                        JSONObject self = links_object.getJSONObject("self");

                        String id_string = self.getString("href");

                        mId = Utilities.parseInt(id_string.replace(GetFixtures.GET_FIXTURES_URL, ""));
                    }

                    if(links_object.has("soccerseason")) {
                        JSONObject soccer_season = links_object.getJSONObject("soccerseason");

                        String id_string = soccer_season.getString("href");

                        mSeasonId = Utilities.parseInt(id_string.replace(GetFixtures.GET_SOCCER_SEASONS_BASE_URL, ""));
                    }

                    if(links_object.has("homeTeam")) {
                        JSONObject home_team = links_object.getJSONObject("homeTeam");

                        String id_string = home_team.getString("href");

                        mHomeTeamId = Utilities.parseLong(id_string.replace(GetFixtures.GET_TEAMS_BASE_URL, ""));
                    }

                    if(links_object.has("awayTeam")) {
                        JSONObject away_team = links_object.getJSONObject("awayTeam");

                        String id_string = away_team.getString("href");

                        mAwayTeamId = Utilities.parseLong(id_string.replace(GetFixtures.GET_TEAMS_BASE_URL, ""));
                    }
                }

                if(json_object.has("date")) {
                    Date date = Utilities.parseDate(FIXTURE_DATE_FORMAT, json_object.getString("date"), TimeZone.getTimeZone("UTC"));

                    // Set to local timezone
                    FIXTURE_DATE_FORMAT.setTimeZone(TimeZone.getDefault());
                    FIXTURE_TIME_FORMAT.setTimeZone(TimeZone.getDefault());

                    mDate = FIXTURE_DAY_FORMAT.format(date);
                    mTime = FIXTURE_TIME_FORMAT.format(date);
                }

                if(json_object.has("matchday")) {
                    mMatchDay = json_object.getInt("matchday");
                }

                if(json_object.has("homeTeamName")) {
                    mHomeTeamName = json_object.getString("homeTeamName");
                }

                if(json_object.has("awayTeamName")) {
                    mAwayTeamName = json_object.getString("awayTeamName");
                }


                if(json_object.has("result")) {
                    JSONObject result = json_object.getJSONObject("result");

                    if(result.has("goalsHomeTeam")) {
                        String goals = result.getString("goalsHomeTeam");

                        if(!goals.equalsIgnoreCase("null")) {
                            mHomeTeamGoals = Utilities.parseInt(goals);
                        }
                    }

                    if(result.has("goalsAwayTeam")) {
                        String goals = result.getString("goalsAwayTeam");

                        if(!goals.equalsIgnoreCase("null")) {
                            mAwayTeamGoals = Utilities.parseInt(goals);
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return this;
        }

        public Builder fromCursor(Cursor cursor) {
            mId = cursor.getLong(cursor.getColumnIndex(FixtureContract.FixtureEntry._ID));
            mSeasonId = cursor.getInt(cursor.getColumnIndex(FixtureContract.FixtureEntry.SEASON_ID));
            mDate = cursor.getString(cursor.getColumnIndex(FixtureContract.FixtureEntry.DATE));
            mTime = cursor.getString(cursor.getColumnIndex(FixtureContract.FixtureEntry.TIME));
            mMatchDay = cursor.getInt(cursor.getColumnIndex(FixtureContract.FixtureEntry.MATCH_DAY));
            mHomeTeamName = cursor.getString(cursor.getColumnIndex(FixtureContract.FixtureEntry.HOME_TEAM_NAME));
            mHomeTeamId = cursor.getLong(cursor.getColumnIndex(FixtureContract.FixtureEntry.HOME_TEAM_ID));
            mHomeTeamCrestUrl = cursor.getString(cursor.getColumnIndex(FixtureContract.FixtureEntry.HOME_TEAM_CREST_URL));
            mAwayTeamName = cursor.getString(cursor.getColumnIndex(FixtureContract.FixtureEntry.AWAY_TEAM_NAME));
            mAwayTeamId = cursor.getLong(cursor.getColumnIndex(FixtureContract.FixtureEntry.AWAY_TEAM_ID));
            mAwayTeamCrestUrl = cursor.getString(cursor.getColumnIndex(FixtureContract.FixtureEntry.AWAY_TEAM_CREST_URL));
            mHomeTeamGoals = cursor.getInt(cursor.getColumnIndex(FixtureContract.FixtureEntry.HOME_TEAM_GOALS));
            mAwayTeamGoals = cursor.getInt(cursor.getColumnIndex(FixtureContract.FixtureEntry.AWAY_TEAM_GOALS));

            return this;
        }


        public Fixture build() {
            return new Fixture(this);
        }
    }
}
