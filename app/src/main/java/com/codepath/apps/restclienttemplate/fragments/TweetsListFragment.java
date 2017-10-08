package com.codepath.apps.restclienttemplate.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.EndlessRecyclerViewScrollListener;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.adapters.TweetAdapter;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.raizlabs.android.dbflow.config.FlowManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;


public class TweetsListFragment extends Fragment implements TweetAdapter.TweetAdapterListener {
    TweetAdapter tweetAdapter;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweets;
    EndlessRecyclerViewScrollListener scrollListener;
    long maxId = 0;
    static int count = 10;

    public interface TweetSelectedListener {
        //handle the tweet selection
        public void onTweetSelected(Tweet tweet);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragments_tweets_list, container, false);
        rvTweets = (RecyclerView) v.findViewById(R.id.rvTweet);
        tweets = new ArrayList<>();
        tweetAdapter = new TweetAdapter(tweets, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvTweets.setLayoutManager(linearLayoutManager);

        rvTweets.setAdapter(tweetAdapter);
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromApi();
            }
        };
        rvTweets.addOnScrollListener(scrollListener);

        if (!isNetworkAvailable() || !isOnline()) {
            Toast.makeText(getContext(), "Please check your network...", Toast.LENGTH_SHORT);
            tweets.addAll(Tweet.recentTweets());
            tweetAdapter.notifyItemRangeInserted(0, tweets.size());
        } else {
            populateTimeline();

        }

        return v;
    }

    public void addItems(JSONArray response) {
        try {
            tweets.addAll(Tweet.fromJSONArray(response));
            tweetAdapter.notifyDataSetChanged();
            for (int i = 0; i < tweets.size(); i++) {
                FlowManager.getModelAdapter(Tweet.class).save(tweets.get(i));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void loadNextDataFromApi() {
        // if there is tweet in the tweet array, get the last one's uid as the maxId
        if (tweets.size() > 0) {
            maxId = tweets.get(tweets.size() - 1).uid - 1;
        }
        Toast.makeText(getContext(), "Loading more tweets...", Toast.LENGTH_SHORT).show();

        populateTimeline();

    }

    public void populateTimeline() {}

    @Override
    public void onItemSelected(View view, int position) {
        Tweet tweet = tweets.get(position);
        //Toast.makeText(getContext(), tweet.body, Toast.LENGTH_SHORT).show();
        ((TweetSelectedListener) getActivity()).onTweetSelected(tweet);
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean value = activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
        return value;
    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        return false;
    }
}
