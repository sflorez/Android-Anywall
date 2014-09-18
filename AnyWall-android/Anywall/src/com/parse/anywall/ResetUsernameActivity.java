package com.parse.anywall;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;


public class ResetUsernameActivity extends Activity {
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_reset_username_activity);

    // Set up the submit button click handler
    Button actionButton = (Button) findViewById(R.id.action_button);
    actionButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
        resetUsername();
      }
    });
  }
  private void resetUsername() {
    Bundle extras = getIntent().getExtras();
    String newUsername;
    EditText newUsernameEditText = null;
    ParseQuery<ParseUser> forgetfulUsers;
    ParseUser forgetfulUser;

    newUsernameEditText = (EditText) findViewById(R.id.new_username);
    newUsername = newUsernameEditText.getText().toString().trim();

    Toast.makeText(this, newUsername, Toast.LENGTH_LONG);
    forgetfulUsers = ParseUser.getQuery().whereEqualTo("email", extras.getString("EXTRA_USER_EMAIL"));
    //Toast.makeText(this, extras.getString("EXTRA_USER_EMAIL"), Toast.LENGTH_LONG).show();

    // Exits out of try before requestedSuccessfully() is called
    try {
      forgetfulUser = forgetfulUsers.getFirst();
      Toast.makeText(this, forgetfulUser.getUsername(), Toast.LENGTH_SHORT).show();
      forgetfulUser.put("username", newUsername);
      forgetfulUser.saveInBackground();
    } catch (ParseException e) {
      Toast.makeText(ResetUsernameActivity.this, R.string.error_email_not_found, Toast.LENGTH_LONG).show();
      Log.i("HEY", e.toString());

    }
  }

}