package com.hydeudacityproject.footballscores.Service;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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
public class GetSeasons extends AsyncTask<Void, Void, GetSeasonsResponse> {

    private static final String TAG = GetSeasons.class.getSimpleName();

    private static final String BASE_URL = "http://api.football-data.org/v1/soccerseasons?";

    private String mUrl = BASE_URL;

    private Context mContext;
    private GetSeasonsListener mListener;
    private AbstractAppService.ServiceError mErrorCode;

    public GetSeasons(Context context, GetSeasonsListener listener, int year) {
        mContext = context;
        mListener = listener;

        mUrl += year;
    }


    @Override
    protected GetSeasonsResponse doInBackground(Void... params) {

        if(Utilities.isNetworkAvailable(mContext)) {
            StringBuilder content = new StringBuilder();

            URL url;
            BufferedReader buffered_reader = null;

            try {
                url = new URL(mUrl);

                Log.i(TAG, "Retrieving Seasons: " + url);

                URLConnection connection = url.openConnection();
                connection.addRequestProperty("X-Auth-Token", FixturesService.API_KEY);

                buffered_reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String read_line;

                while((read_line = buffered_reader.readLine()) != null) {
                    content.append(read_line);
                }

                mErrorCode = AbstractAppService.ServiceError.Success;

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

            return new GetSeasonsResponse.Builder().fromJSON(content.toString()).build();
        } else {
            mErrorCode = AbstractAppService.ServiceError.NetworkConnectionError;
        }

        return null;
    }

    @Override
    protected void onPostExecute(GetSeasonsResponse result) {
        super.onPostExecute(result);

        if(mListener != null) {
            if(mErrorCode == AbstractAppService.ServiceError.Success) {
                mListener.OnGetSeasonsFinished(result);
            } else {
                mListener.OnGetSeasonsError(mErrorCode);
            }
        }
    }


    public interface GetSeasonsListener {
        void OnGetSeasonsFinished(GetSeasonsResponse response);
        void OnGetSeasonsError(AbstractAppService.ServiceError error);
    }
}
