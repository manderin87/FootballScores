package com.hydeudacityproject.footballscores.AboutView;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;

import com.hydeudacityproject.footballscores.MainApplication;
import com.hydeudacityproject.footballscores.R;
import com.lonewolfgames.framework.AbstractAppActivity;

/**
 * Created by jhyde on 1/14/2016.
 * Copyright (C) 2016 Jesse Hyde Lone Wolf Games
 */
public class AboutView extends AbstractAppActivity<MainApplication, AboutViewController> {

    public AboutView() {
        super(AboutView.class.getSimpleName());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.about_view);

        mApp = (MainApplication) getApplicationContext();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        initializeToolbar(getResources().getString(R.string.about_view_title), true);

        TextView content_text_view = (TextView) findViewById(R.id.textView_content);
        content_text_view.setText(Html.fromHtml(getResources().getString(R.string.about_content)));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
