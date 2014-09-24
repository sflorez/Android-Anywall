package com.parse.anywall;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.text.ParseException;


public class ProfileActivity extends Activity {

    private ParseQueryAdapter<AnywallPost> postsQueryAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ParseQueryAdapter.QueryFactory<AnywallPost> factory =
                new ParseQueryAdapter.QueryFactory<AnywallPost>() {
                    public ParseQuery<AnywallPost> create() {
                        ParseQuery<AnywallPost> query = AnywallPost.getQuery();
                        query.whereEqualTo("user" , ParseUser.getCurrentUser());
                        query.include("user");
                        query.orderByDescending("createdAt");
                        return query;
                    }
                };

        postsQueryAdapter = new ParseQueryAdapter<AnywallPost>(this, factory) {
            @Override
            public View getItemView(AnywallPost post, View view, ViewGroup parent) {
                if (view == null) {
                    view = View.inflate(getContext(), R.layout.anywall_post_item, null);
                }
                TextView contentView = (TextView) view.findViewById(R.id.content_view);
                TextView usernameView = (TextView) view.findViewById(R.id.username_view);
                contentView.setText(post.getText());
                usernameView.setText(post.getUser().getUsername());
                Log.i("QUERYText", post.getText());
                Log.i("QUERYUser", post.getUser().getUsername());

                return view;
            }
        };

        postsQueryAdapter.setPaginationEnabled(false);

        postsQueryAdapter.setAutoload(false);

        ListView postsListView = (ListView) findViewById(R.id.posts_profile_listview);
        postsListView.setAdapter(postsQueryAdapter);
    }


}
