package com.hydeudacityproject.footballscores.Service;

import com.hydeudacityproject.footballscores.Service.Framework.Fixture;
import com.hydeudacityproject.footballscores.Service.Framework.Season;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jhyde on 1/8/2016.
 * Copyright (C) 2016 Jesse Hyde Lone Wolf Games
 */
public class GetSeasonsResponse {

    private ArrayList<Season> mItems;

    public ArrayList<Season> items() { return mItems; }

    private GetSeasonsResponse(Builder build) {
        mItems = build.mItems;
    }

    public static class Builder {
        private ArrayList<Season> mItems;

        public Builder() {
            mItems = new ArrayList<>();
        }

        public Builder fromJSON(String content) {
            try {
                JSONArray results_array = new JSONArray(content);

                for(int index = 0; index < results_array.length(); index++) {
                    mItems.add(new Season.Builder().fromJSON(results_array.getString(index)).build());
                }

            } catch(Exception e) {
                e.printStackTrace();
            }

            return this;
        }

        public GetSeasonsResponse build() {
            return new GetSeasonsResponse(this);
        }
    }
}
