package com.codepath.apps.restclienttemplate.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.EndlessRecyclerViewScrollListener;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TweetAdapter;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.fragments.ComposeDialogFragment;
import com.codepath.apps.restclienttemplate.fragments.TweetsListFragment;
import com.codepath.apps.restclienttemplate.fragments.TweetsPagerAdapter;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.raizlabs.android.dbflow.config.FlowManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static android.R.attr.max;
import static com.codepath.apps.restclienttemplate.R.string.tweet;
import static com.raizlabs.android.dbflow.config.FlowManager.getModelAdapter;

public class TimelineActivity extends AppCompatActivity implements ComposeDialogFragment.ComposeDialogListener {

    private TweetsListFragment fragmentTweetsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // get the  view pager
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        // set the adapter for the pager
        viewPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager(), this));
        // setup the Tablayout to use the view pager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    // Inflate the menu; this adds items to the action bar if it is present.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfstatement
        if (id == R.id.compose) {
            getScreenName();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/






    @Override
    public void onFinishCompose(Tweet tweet) {
        /*tweets.add(0, tweet);
        tweetAdapter.notifyItemInserted(0);
        scrollListener.resetState();*/
    }

    public void onProfileView(MenuItem item) {
        // launch the profile view
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }
}