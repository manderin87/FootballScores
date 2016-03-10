package com.hydeudacityproject.footballscores.Service;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.hydeudacityproject.footballscores.AppData;
import com.hydeudacityproject.footballscores.Service.Framework.Fixture;
import com.hydeudacityproject.footballscores.Service.Framework.Team;
import com.lonewolfgames.framework.AbstractAppService;
import com.lonewolfgames.framework.Utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by jhyde on 1/8/2016.
 * Copyright (C) 2016 Jesse Hyde Lone Wolf Games
 */
public class GetFixtures extends AsyncTask<Void, Void, GetFixturesResponse> {

    private static final String TAG = GetFixtures.class.getSimpleName();

    public static final String GET_FIXTURES_BASE_URL = "http://api.football-data.org/v1/fixtures?";
    public static final String GET_FIXTURES_URL = "http://api.football-data.org/v1/fixtures/";

    private static final String QUERY_PARAM = "timeFrame=";

    public static final String GET_TEAMS_BASE_URL = "http://api.football-data.org/v1/teams/";
    public static final String GET_SOCCER_SEASONS_BASE_URL = "http://api.football-data.org/v1/soccerseasons/";

    private String mUrl = GET_FIXTURES_BASE_URL + QUERY_PARAM;

    private Context mContext;
    private GetFixturesListener mListener;
    private AbstractAppService.ServiceError mErrorCode;

    public GetFixtures(Context context, GetFixturesListener listener) {
        mContext = context;
        mListener = listener;
    }

    @Override
    protected GetFixturesResponse doInBackground(Void... params) {

        if(Utilities.isNetworkAvailable(mContext)) {

            String next_content = getNextFixtures();
            String previous_content = getPreviousFixtures();

            mErrorCode = AbstractAppService.ServiceError.Success;

            GetFixturesResponse next_response = processContent(next_content);
            GetFixturesResponse previous_response = processContent(previous_content);

            GetFixturesResponse all_responses = new GetFixturesResponse.Builder()
                    .fromResponse(next_response)
                    .fromResponse(previous_response)
                    .build();

            return all_responses;

        } else {
            mErrorCode = AbstractAppService.ServiceError.NetworkConnectionError;
        }

        return null;
    }

    private String getNextFixtures() {
        StringBuilder content = new StringBuilder();

        URL url;
        BufferedReader buffered_reader = null;

        try {
            url = new URL(mUrl + GetFixtureDay.NEXT_THREE_DAYS.day());

            Log.i(TAG, "Retrieving Fixtures: " + url);

            URLConnection connection = url.openConnection();
            connection.addRequestProperty("X-Auth-Token", FixturesService.API_KEY);

            buffered_reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String read_line;

            while((read_line = buffered_reader.readLine()) != null) {
                content.append(read_line);
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(buffered_reader != null) {
                try {
                    buffered_reader.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return content.toString();
    }

    private String getPreviousFixtures() {
        StringBuilder content = new StringBuilder();

        URL url;
        BufferedReader buffered_reader = null;

        try {
            url = new URL(mUrl + GetFixtureDay.PREVIOUS_TW0_DAYS.day());

            Log.i(TAG, "Retrieving Fixtures: " + url);

            URLConnection connection = url.openConnection();
            connection.addRequestProperty("X-Auth-Token", "69a773e0ee0c49e4af870099cb95eb0e");

            buffered_reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String read_line;

            while((read_line = buffered_reader.readLine()) != null) {
                content.append(read_line);
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(buffered_reader != null) {
                try {
                    buffered_reader.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return content.toString();
    }

    private GetFixturesResponse processContent(String content) {
        GetFixturesResponse response = new GetFixturesResponse.Builder().fromJSON(content).build();

        // Get the team crest
        for(Fixture fixture : response.items()) {
            long home_team_id = fixture.homeTeamId();
            long away_team_id = fixture.awayTeamId();

            Team home_team = getTeam(home_team_id);
            Team away_team = getTeam(away_team_id);

            if(home_team != null) {
                fixture.setHomeTeamCrestUrl(home_team.crestUrl());
            }

            if(away_team != null) {
                fixture.setAwayTeamCrestUrl(away_team.crestUrl());
            }
        }

        return response;
    }

    private Team getTeam(long teamId) {
        // See if we already have the team data
        Team team = AppData.instance().database().getTeam(teamId);

        if(team == null) {
            // Sleep 1.5 seconds due to API 50 requests per minute
            // constraint
            try {
                Thread.sleep(1500);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }

            team = getTeamDataFromAPI(teamId);

            // Save the team data
            if(team != null) {
                AppData.instance().database().addTeam(team);
            }
        }

        return team;
    }

    private Team getTeamDataFromAPI(long teamId) {
        StringBuilder content = new StringBuilder();

        String team_url = GET_TEAMS_BASE_URL + teamId;

        URL url;
        BufferedReader buffered_reader = null;

        try {
            url = new URL(team_url);

            Log.i(TAG, "Retrieving Team: " + url);

            URLConnection connection = url.openConnection();
            connection.addRequestProperty("X-Auth-Token", "69a773e0ee0c49e4af870099cb95eb0e");

            buffered_reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String read_line;

            while((read_line = buffered_reader.readLine()) != null) {
                content.append(read_line);
            }

        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(buffered_reader != null) {
                try {
                    buffered_reader.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }

        GetTeamResponse response = new GetTeamResponse.Builder().fromJSON(teamId, content.toString()).build();

        if(response != null && response.item() != null) {
            return response.item();
        }

        return null;
    }

    @Override
    protected void onPostExecute(GetFixturesResponse result) {
        super.onPostExecute(result);

        if(mListener != null) {
            if(mErrorCode == AbstractAppService.ServiceError.Success) {
                mListener.OnGetFixturesFinished(result);
            } else {
                mListener.OnGetFixturesError(mErrorCode);
            }
        }
    }


    public interface GetFixturesListener {
        void OnGetFixturesFinished(GetFixturesResponse response);
        void OnGetFixturesError(AbstractAppService.ServiceError error);
    }

    public enum GetFixtureDay {
        PREVIOUS_TW0_DAYS("p2"),
        NEXT_THREE_DAYS("n3");

        private String mDay = "";

        private GetFixtureDay(String day) {
            mDay = day;
        }

        public String day() { return mDay; }
    }
}
