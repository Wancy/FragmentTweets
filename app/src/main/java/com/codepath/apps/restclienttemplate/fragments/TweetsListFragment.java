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
import com.codepath.apps.restclienttemplate.TweetAdapter;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.raizlabs.android.dbflow.config.FlowManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Wancy on 10/3/17.
 */

public class TweetsListFragment extends Fragment {
    TweetAdapter tweetAdapter;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweets;
    EndlessRecyclerViewScrollListener scrollListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragments_tweets_list, container, false);
        rvTweets = (RecyclerView) v.findViewById(R.id.rvTweet);
        tweets = new ArrayList<>();
        tweetAdapter = new TweetAdapter(tweets);
        rvTweets.setLayoutManager(new LinearLayoutManager(getContext()));

        rvTweets.setAdapter(tweetAdapter);
        //rvTweets.addOnScrollListener(scrollListener);

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

}
