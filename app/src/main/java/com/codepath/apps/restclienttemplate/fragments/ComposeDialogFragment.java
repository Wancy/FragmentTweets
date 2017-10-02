package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class ComposeDialogFragment extends DialogFragment{

    Button btnTweet;
    Button btnCancel;
    ImageView ivProfileMe;
    TextView tvName;
    MultiAutoCompleteTextView tvContent;
    User currentUser;
    TwitterClient client;
    TextView tvCount;

    public interface ComposeDialogListener{
        void onFinishCompose(Tweet tweet);
    }

    public static ComposeDialogFragment newInstance(User user, TwitterClient client) {

        ComposeDialogFragment fragment = new ComposeDialogFragment();
        fragment.currentUser = user;
        fragment.client = client;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.compose_dialog, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnTweet = (Button) view.findViewById(R.id.btnTweet);
        tvName = (TextView) view.findViewById(R.id.tvBody);
        tvContent = (MultiAutoCompleteTextView) view.findViewById(R.id.tvContent);
        ivProfileMe = (ImageView) view.findViewById(R.id.ivProfileMe);
        tvCount = (TextView) view.findViewById(R.id.tvCount);
        tvContent.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        tvContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // this will show characters remaining
                tvCount.setText(140 - s.toString().length() + "/140");
            }
        });
        Glide.with(view.getContext()).load(currentUser.profileImageUrl).error(R.drawable.ic_launcher).into(ivProfileMe);
        tvName.setText(currentUser.name);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = tvContent.getText().toString();
                if (str == null || str.length() == 0) return;
                String body = tvContent.getText().toString();
                tweet(body);
            }
        });
    }

    private void tweet(String body) {
        client.postTweet(body, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Tweet newTweet = Tweet.fromJSON(response);
                    ComposeDialogListener listener = (ComposeDialogListener) getActivity();
                    listener.onFinishCompose(newTweet);
                    dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }


        });
    }

}
