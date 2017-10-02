package com.codepath.apps.restclienttemplate.models;


import com.codepath.apps.restclienttemplate.MyDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.codepath.apps.restclienttemplate.R.string.tweet;


@Table(database = MyDatabase.class)
public class Tweet extends BaseModel{
    @Column
    public String body;

    @PrimaryKey
    @Column
    public long uid;

    @Column(typeConverter = JSONConverter.class)
    public JSONObject user;

    @Column
    public String createdAt;

    // deserialize the JSON
    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException{
        Tweet tweet = new Tweet();

        //extract the values from JSON
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = jsonObject.getJSONObject("user");
        return tweet;

    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray response) throws JSONException{
        ArrayList<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < response.length(); i++) {
            Tweet tweet = null;
            try {
                tweet = Tweet.fromJSON(response.getJSONObject(i));
                tweets.add(tweet);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return tweets;
    }

    public static List<Tweet> recentTweets() {
        List<Tweet> list = new Select().from(Tweet.class).limit(300).queryList();
        Collections.sort(list, new Comparator<Tweet>() {
            @Override
            public int compare(Tweet o1, Tweet o2) {
                return (int) (o2.uid - o1.uid);
            }
        });
        return list;
    }
}
