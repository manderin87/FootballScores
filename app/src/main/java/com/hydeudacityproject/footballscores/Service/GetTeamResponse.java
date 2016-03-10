package com.hydeudacityproject.footballscores.Service;

import com.hydeudacityproject.footballscores.Service.Framework.Team;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jhyde on 1/8/2016.
 * Copyright (C) 2016 Jesse Hyde Lone Wolf Games
 */
public class GetTeamResponse {

    private Team mItem;

    public Team item() { return mItem; }

    private GetTeamResponse(Builder build) {
        mItem = build.mItem;
    }

    public static class Builder {
        private Team mItem;

        public Builder() { }

        public Builder fromJSON(long teamId, String content) {
            try {
                mItem = new Team.Builder().fromJSON(teamId, content).build();
            } catch(Exception e) {

            }

            return this;
        }

        public GetTeamResponse build() {
            return new GetTeamResponse(this);
        }
    }
}
