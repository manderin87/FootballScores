package com.hydeudacityproject.footballscores.Service.ContentProvider;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by jhyde on 1/8/2016.
 * Copyright (C) 2016 Jesse Hyde Lone Wolf Games
 */
public class FixtureContract {

    //URI data
    public static final String CONTENT_AUTHORITY = "com.hydeudacityproject.footballscores";

    public static Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_FIXTURES = "fixtures";
    public static final String PATH_SEASONS = "seasons";
    public static final String PATH_TEAMS = "teams";

    public static final class FixtureEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FIXTURES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FIXTURES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FIXTURES;

        public static final String TABLE_NAME = "fixtures";

        //Table data
        public static final String SEASON_ID = "season_id";
        public static final String DATE = "date";
        public static final String TIME = "time";
        public static final String MATCH_DAY = "match_day";
        public static final String HOME_TEAM_NAME = "home_team_name";
        public static final String HOME_TEAM_ID = "home_team_id";
        public static final String HOME_TEAM_CREST_URL = "home_team_crest_url";
        public static final String AWAY_TEAM_NAME = "away_team_name";
        public static final String AWAY_TEAM_ID = "away_team_id";
        public static final String AWAY_TEAM_CREST_URL = "away_team_crest_url";
        public static final String HOME_TEAM_GOALS = "home_team_goals";
        public static final String AWAY_TEAM_GOALS = "away_team_goals";


        public static Uri buildFixtureUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        public static Uri buildFixtureWithId()
        {
            return BASE_CONTENT_URI.buildUpon().appendPath("id").build();
        }
        public static Uri buildFixtureWithDate()
        {
            return BASE_CONTENT_URI.buildUpon().appendPath("date").build();
        }
    }

    public static final class TeamEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TEAMS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TEAMS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TEAMS;

        public static final String TABLE_NAME = "teams";

        //Table data
        public static final String NAME = "name";
        public static final String SHORT_NAME = "short_name";
        public static final String SQUAD_MARKET_VALUE = "squad_market_value";
        public static final String CREST_URL = "crest_url";

        public static Uri buildTeamUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class SeasonEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_SEASONS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SEASONS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SEASONS;

        public static final String TABLE_NAME = "seasons";
        //Table data
        public static final String CAPTION = "caption";
        public static final String LEAGUE = "league";
        public static final String YEAR = "year";
        public static final String CURRENT_MATCHDAY = "current_matchday";
        public static final String NUMBER_OF_MATCHDAYS = "number_of_matchdays";
        public static final String NUMBER_OF_TEAMS = "number_of_teams";
        public static final String NUMBER_OF_GAMES = "number_of_games";
        public static final String LAST_UPDATED = "last_updated";

        public static Uri buildSeasonUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

}
