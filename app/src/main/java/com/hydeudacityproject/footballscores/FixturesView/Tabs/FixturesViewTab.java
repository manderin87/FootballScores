package com.hydeudacityproject.footballscores.FixturesView.Tabs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hydeudacityproject.footballscores.AppData;
import com.hydeudacityproject.footballscores.FixturesView.FixturesView;
import com.hydeudacityproject.footballscores.MainApplication;
import com.hydeudacityproject.footballscores.R;
import com.hydeudacityproject.footballscores.Service.Framework.Fixture;
import com.hydeudacityproject.footballscores.Service.Framework.Season;
import com.hydeudacityproject.footballscores.Service.GetFixtures;
import com.hydeudacityproject.footballscores.Service.GetFixturesResponse;
import com.lonewolfgames.framework.AbstractAppService;
import com.lonewolfgames.framework.AbstractViewAdapter;
import com.lonewolfgames.framework.AbstractViewData;
import com.lonewolfgames.framework.AbstractViewHolder;
import com.lonewolfgames.framework.Cache.Images.ImageLoader;
import com.lonewolfgames.framework.Tabs.AbstractPagerFragment;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jhyde on 1/11/2016.
 * Copyright (C) 2016 Jesse Hyde Lone Wolf Games
 */
public class FixturesViewTab  extends AbstractPagerFragment<FixturesView, FixturesViewTabViewController>
        implements FixturesViewTabViewController.FixturesTabViewControllerListener, GetFixtures.GetFixturesListener {

    private static final String KEY_DATE = "date";
    private static final String FOOTBALL_SCORES_HASHTAG = "#Football_Scores";

    private MainApplication mApp;

    private SwipeRefreshLayout mSwipeLayout;
    private RecyclerView mListView;
    private LinearLayoutManager mListLayoutManager;
    private CardView mEmptyView;

    private String mDate;


    public static FixturesViewTab newInstance(Date date) {
        FixturesViewTab fragment = new FixturesViewTab();

        fragment.setDate(date);

        return fragment;
    }

    private void setDate(Date date) {
        mDate = Fixture.FIXTURE_DAY_FORMAT.format(date);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mApp = (MainApplication) getActivity().getApplicationContext();
        setParent((FixturesView) getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fixtures_view_tab, container, false);

        if(savedInstanceState != null && savedInstanceState.containsKey(KEY_DATE)) {
            mDate = savedInstanceState.getString(KEY_DATE);
        }

        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetFixtures(getActivity(), FixturesViewTab.this).execute();
            }
        });

        mEmptyView = (CardView) view.findViewById(R.id.cardView_empty);

        mListView = (RecyclerView) view.findViewById(R.id.recyclerView_fixtures);
        mListLayoutManager = new LinearLayoutManager(getActivity());
        mListView.setLayoutManager(mListLayoutManager);

        updateViewController();

        return view;
    }

    public void updateViewController() {
        setViewController(new FixturesViewTabViewController(mApp, parent(), this, AppData.instance().database().fixturesByDate(mDate)));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(KEY_DATE, mDate);
    }

    @Override
    public void OnGetFixturesFinished(GetFixturesResponse response) {
        if(response != null && response.items().size() > 0) {
            hideEmpty();
            AppData.instance().database().addFixtures(response.items());
            parent().updateTabs();
            mSwipeLayout.setRefreshing(false);
        } else {
            showEmpty();
        }
    }

    @Override
    public void OnGetFixturesError(AbstractAppService.ServiceError error) {

    }

    @Override
    public void OnDataLoaded(ArrayList<Fixture> data) {
        if(data.size() > 0) {
            hideEmpty();
            if(mListView != null) {
                mListView.setAdapter(new ViewAdapter(data));
            }
        } else {
            showEmpty();
        }
    }

    @Override
    public void OnDataError() {

    }

    public void showEmpty() {
        if(mEmptyView != null) {
            mEmptyView.setVisibility(View.VISIBLE);
        }

        if(mSwipeLayout != null) {
            mSwipeLayout.setVisibility(View.GONE);
        }
    }

    public void hideEmpty() {
        if(mEmptyView != null) {
            mEmptyView.setVisibility(View.GONE);
        }

        if(mSwipeLayout != null) {
            mSwipeLayout.setVisibility(View.VISIBLE);
        }
    }

    public static class ViewAdapter extends AbstractViewAdapter<ViewAdapter.Item, Fixture, AbstractViewHolder> {

        public ViewAdapter(ArrayList<Fixture> items) {
            super();

            addAll(items);
        }

        @Override
        public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ItemViewHolder.Builder(parent, R.layout.fixtures_view_tab_fixture_item).build();
        }

        @Override
        public void addAll(ArrayList<Fixture> items) {
            // Add the content items
            for(Fixture item : items) {
                addItem(new Item(item, 0));
            }
        }

        public static class Item extends AbstractViewData<Fixture> {
            public Item(Fixture item, int type) {
                super(item, type);
            }
        }

        public static class ItemViewHolder extends AbstractViewHolder<Item, ViewAdapter> {

            private ImageView mHomeTeamCrestImageView;
            private ImageView mAwayTeamCrestImageView;
            private ImageLoader mHomeTeamCrestImageLoader;
            private ImageLoader mAwayTeamCrestImageLoader;
            private TextView mHomeTeamNameTextView;
            private TextView mAwayTeamNameTextView;
            private TextView mHomeTeamGoalsTextView;
            private TextView mAwayTeamGoalsTextView;
            private TextView mFixtureTimeTextView;

            private LinearLayout mDetailsLayout;
            private TextView mMatchDayTextView;
            private TextView mLeagueTextView;
            private Button mShareButton;

            public ItemViewHolder(final View itemView) {
                super(itemView);

                mHomeTeamCrestImageView = (ImageView) itemView.findViewById(R.id.imageView_home_team_crest);
                mAwayTeamCrestImageView = (ImageView) itemView.findViewById(R.id.imageView_away_team_crest);
                mHomeTeamNameTextView = (TextView) itemView.findViewById(R.id.textView_home_team_name);
                mAwayTeamNameTextView = (TextView) itemView.findViewById(R.id.textView_away_team_name);
                mHomeTeamGoalsTextView = (TextView) itemView.findViewById(R.id.textView_home_team_goals);
                mAwayTeamGoalsTextView = (TextView) itemView.findViewById(R.id.textView_away_team_goals);
                mFixtureTimeTextView = (TextView) itemView.findViewById(R.id.textView_fixture_time);
                mDetailsLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout_details);
                mMatchDayTextView = (TextView) itemView.findViewById(R.id.textView_matchDay);
                mLeagueTextView = (TextView) itemView.findViewById(R.id.textView_league);
                mShareButton = (Button) itemView.findViewById(R.id.button_share);

                if(mHomeTeamCrestImageLoader != null) {
                    if(mHomeTeamCrestImageLoader.getStatus() == AsyncTask.Status.RUNNING) {
                        mHomeTeamCrestImageLoader.cancel(true);
                        mHomeTeamCrestImageLoader = null;
                    }
                }

                if(mAwayTeamCrestImageLoader != null) {
                    if(mAwayTeamCrestImageLoader.getStatus() == AsyncTask.Status.RUNNING) {
                        mAwayTeamCrestImageLoader.cancel(true);
                        mAwayTeamCrestImageLoader = null;
                    }
                }
            }

            @Override
            public void initialize(final Item data, final int position) {
                final Fixture fixture = data.item();
                final Season season = AppData.instance().database().getSeason(fixture.seasonId());

                mHomeTeamCrestImageView.setImageResource(R.drawable.ic_missing_crest);
                mAwayTeamCrestImageView.setImageResource(R.drawable.ic_missing_crest);

                mHomeTeamNameTextView.setText(fixture.homeTeamName());
                mAwayTeamNameTextView.setText(fixture.awayTeamName());


                if(fixture.homeTeamGoals() < 0) {
                    mHomeTeamGoalsTextView.setText("X");
                } else {
                    mHomeTeamGoalsTextView.setText(String.valueOf(fixture.homeTeamGoals()));
                }

                if(fixture.awayTeamGoals() < 0) {
                    mAwayTeamGoalsTextView.setText("X");
                } else {
                    mAwayTeamGoalsTextView.setText(String.valueOf(fixture.awayTeamGoals()));
                }

                mFixtureTimeTextView.setText(fixture.time());

                mHomeTeamCrestImageLoader = new ImageLoader.Builder(itemView.getContext())
                        .imageView(mHomeTeamCrestImageView)
                        .url(fixture.homeTeamCrestUrl())
                        .filename(String.valueOf(fixture.homeTeamId()))
                        .defaultResourceId(R.drawable.ic_missing_crest)
                        .listener(new ImageLoader.ImageLoaderListener() {
                            @Override
                            public void OnImageLoaded(Bitmap bitmap) {

                            }

                            @Override
                            public void OnImageFailed(AbstractAppService.ServiceError error) {
                            }
                        })
                        .build();
                mHomeTeamCrestImageLoader.execute();

                mAwayTeamCrestImageLoader = new ImageLoader.Builder(itemView.getContext())
                        .imageView(mAwayTeamCrestImageView)
                        .url(fixture.awayTeamCrestUrl())
                        .filename(String.valueOf(fixture.awayTeamId()))
                        .defaultResourceId(R.drawable.ic_missing_crest)
                        .listener(new ImageLoader.ImageLoaderListener() {
                            @Override
                            public void OnImageLoaded(Bitmap bitmap) {

                            }

                            @Override
                            public void OnImageFailed(AbstractAppService.ServiceError error) {
                            }
                        })
                        .build();
                mAwayTeamCrestImageLoader.execute();


                itemView.setFocusable(true);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mDetailsLayout.getVisibility() == View.GONE) {
                            mDetailsLayout.setVisibility(View.VISIBLE);
                        } else {
                            mDetailsLayout.setVisibility(View.GONE);
                        }
                    }
                });


                if(season != null && season.caption().contains("Champions League")) {
                    mMatchDayTextView.setText(fixture.matchDayChampionsLeague());
                } else {
                    mMatchDayTextView.setText(
                            itemView.getResources().getString(R.string.matchday) +
                                    " " +
                                    String.valueOf(fixture.matchDay()));
                }


                if(season != null) {
                    mLeagueTextView.setText(season.caption());
                }

                mShareButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemView.getContext().startActivity(createShareFixtureIntent(
                                fixture.homeTeamName() +
                                        " " +
                                        String.valueOf(fixture.homeTeamGoals()) +
                                        " - " +
                                        String.valueOf(fixture.awayTeamGoals()) +
                                        " " +
                                        fixture.awayTeamName()));
                    }
                });

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                    mHomeTeamCrestImageView.setContentDescription(fixture.homeTeamName());
                    mAwayTeamCrestImageView.setContentDescription(fixture.awayTeamName());
                    mShareButton.setContentDescription(itemView.getResources().getString(R.string.share_text));
                }
            }

            private Intent createShareFixtureIntent(String shareText) {
                Intent share_intent = new Intent(Intent.ACTION_SEND);
                share_intent.setType("text/plain");
                share_intent.putExtra(Intent.EXTRA_TEXT, shareText + FOOTBALL_SCORES_HASHTAG);
                return share_intent;
            }

            public static class Builder extends AbstractViewHolder.Builder {

                public Builder(ViewGroup parent, int layoutResourceId) {
                    super(parent, layoutResourceId);
                }

                @Override
                public ItemViewHolder build() {
                    View view = LayoutInflater.from(mParent.getContext()).inflate(mLayoutResourceId, mParent, false);

                    return new ItemViewHolder(view);
                }
            }
        }
    }

}
