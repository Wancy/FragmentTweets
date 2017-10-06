package com.codepath.apps.restclienttemplate.fragments;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.EndlessRecyclerViewScrollListener;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cz.msebera.android.httpclient.Header;

public class HomeTimelineFragment extends TweetsListFragment {
    TwitterClient client;
    private static int count = 10;
    private long maxId;
    private User currentUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApp.getRestClient();
        currentUser = new User();
        /*scrollListener = new EndlessRecyclerViewScrollListener(new LinearLayoutManager(getContext())) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromApi();
            }
        };*/
        if (!isNetworkAvailable() || !isOnline()) {
            Toast.makeText(getContext(), "Please check your network...", Toast.LENGTH_SHORT);
            tweets.addAll(Tweet.recentTweets());
            tweetAdapter.notifyItemRangeInserted(0, tweets.size());
        } else {
            populateTimeline();

        }
    }

    private void populateTimeline() {
        client.getHomeTimeline(count, maxId, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                addItems(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }

        });
    }


    public void loadNextDataFromApi() {
        // if there is tweet in the tweet array, get the last one's uid as the maxId
        if (tweets.size() > 0) {
            maxId = tweets.get(tweets.size() - 1).uid - 1;
        }
        Toast.makeText(getContext(), "Loading more tweets...", Toast.LENGTH_SHORT).show();

        populateTimeline();

    }

    public void showComposeDialog() {
        ComposeDialogFragment composeDialogFragment = ComposeDialogFragment.newInstance(currentUser, client);
        composeDialogFragment.show(getFragmentManager(), "compose_dialog");
    }

    /*@Override
    public void onFinishCompose(Tweet tweet) {
        tweets.add(0, tweet);
        tweetAdapter.notifyItemInserted(0);
        scrollListener.resetState();
    }*/

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
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        return false;
    }
}
