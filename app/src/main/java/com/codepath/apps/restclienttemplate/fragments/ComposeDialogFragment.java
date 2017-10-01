package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;


public class ComposeDialogFragment extends DialogFragment{

    Button btnTweet;
    Button btnCancel;
    ImageView ivProfileMe;
    TextView tvName;
    MultiAutoCompleteTextView tvContent;
    User currentUser;

    public static ComposeDialogFragment newInstance(User user) {

        ComposeDialogFragment fragment = new ComposeDialogFragment();
        fragment.currentUser = user;
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
        tvContent.requestFocus();
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
                Tweet newTweet = new Tweet();
                newTweet.body = tvContent.getText().toString();
            }
        });
    }

}
