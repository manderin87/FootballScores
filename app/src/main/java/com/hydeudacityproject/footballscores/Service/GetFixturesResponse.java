package com.hydeudacityproject.footballscores.Service;

import com.hydeudacityproject.footballscores.Service.Framework.Fixture;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jhyde on 1/8/2016.
 * Copyright (C) 2016 Jesse Hyde Lone Wolf Games
 */
public class GetFixturesResponse {

    private ArrayList<Fixture> mItems;

    public ArrayList<Fixture> items() { return mItems; }

    private GetFixturesResponse(Builder build) {
        mItems = build.mItems;
    }

    public static class Builder {
        private ArrayList<Fixture> mItems;

        public Builder() {
            mItems = new ArrayList<>();
        }

        public Builder fromResponse(GetFixturesResponse response) {
            if(response != null && response.items().size() > 0) {
                mItems.addAll(response.items());
            }

            return this;
        }

        public Builder fromJSON(String content) {
            try {
                JSONObject json_object = new JSONObject(content);

                if(json_object.has("fixtures")) {
                    JSONArray results_array = json_object.getJSONArray("fixtures");

                    for(int index = 0; index < results_array.length(); index++) {
                        mItems.add(new Fixture.Builder().fromJSON(results_array.getString(index)).build());
                    }
                }

            } catch(Exception e) {

            }

            return this;
        }

        public GetFixturesResponse build() {
            return new GetFixturesResponse(this);
        }
    }
}
