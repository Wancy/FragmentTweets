package com.codepath.apps.restclienttemplate.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.fragments.ComposeDialogFragment;
import com.codepath.apps.restclienttemplate.fragments.HomeTimelineFragment;
import com.codepath.apps.restclienttemplate.fragments.TweetsListFragment;
import com.codepath.apps.restclienttemplate.adapters.TweetsPagerAdapter;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.json.JSONException;

public class TimelineActivity extends AppCompatActivity implements TweetsListFragment.TweetSelectedListener, ComposeDialogFragment.ComposeDialogListener {
    private TweetsPagerAdapter tweetsPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // get the  view pager
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        // set the adapter for the pager
        tweetsPagerAdapter = new TweetsPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(tweetsPagerAdapter);
        // setup the Tablayout to use the view pager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    // Inflate the menu; this adds items to the action bar if it is present.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miProfile:
                onProfileView(item);
                return true;
            case R.id.miCompose:
                onCompose(item);
                return true;
        }
        return false;
    }

    public void onProfileView(MenuItem item) {
        // launch the profile view
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }

    @Override
    public void onTweetSelected(Tweet tweet) {

        //Toast.makeText(this, tweet.body, Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, ProfileActivity.class);
        try {
            i.putExtra("screen_name", tweet.user.getString("screen_name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        startActivity(i);
    }

    public void onCompose(MenuItem item) {
        ComposeDialogFragment composeDialogFragment = new ComposeDialogFragment();
        composeDialogFragment.show(getSupportFragmentManager(), "compose_dialog");
    }

    public void onFinishCompose(Tweet tweet) {
        HomeTimelineFragment fragment = (HomeTimelineFragment) tweetsPagerAdapter.getRegisteredFragment(0);
        fragment.addNewTweet(tweet);
    }
}