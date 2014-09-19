package com.parse.anywall;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

public class ForgotPasswordActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

      EditText usernameEditText = (EditText) findViewById(R.id.forgetful_username);
      usernameEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
          if (actionId == R.id.edittext_action_submit ||
              actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
            sendPasswordReset();
            return true;
          }
          return false;
        }
      });

        // Set up the submit button click handler
        Button actionButton = (Button) findViewById(R.id.action_button);
        actionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sendPasswordReset();
            }
        });
    }

    private void sendPasswordReset() {
      String forgetfulUsername = null, forgetfulEmail = null;
      EditText forgetfulUsernameEditText = null;
      ParseQuery<ParseUser> forgetfulUser;

      forgetfulUsernameEditText = (EditText) findViewById(R.id.forgetful_username);
      forgetfulUsername = forgetfulUsernameEditText.getText().toString().trim();

      Log.i("USER", forgetfulUsername);

      forgetfulUser = ParseUser.getQuery().whereEqualTo("username", forgetfulUsername);

      try {
        forgetfulEmail = forgetfulUser.getFirst().getEmail();
      } catch (ParseException e) {
        Toast.makeText(ForgotPasswordActivity.this, R.string.error_username_not_found, Toast.LENGTH_LONG).show();
        Log.i("HEY", e.toString());
      }

      ParseUser.requestPasswordResetInBackground(forgetfulEmail,
          new RequestPasswordResetCallback() {
          public void done(ParseException e) {
        if (e == null) {
                    requestedSuccessfully();
        } else {
                    requestDidNotSucceed();
        }
          }
      });
    }

    private void requestedSuccessfully()
    {
      Toast.makeText(ForgotPasswordActivity.this, R.string.success_password_reset, Toast.LENGTH_LONG).show();
      Intent intent = new Intent(ForgotPasswordActivity.this, DispatchActivity.class);
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
      startActivity(intent);
    }

    private void requestDidNotSucceed()
    {
      Toast.makeText(ForgotPasswordActivity.this, R.string.error_email_not_found, Toast.LENGTH_LONG).show();
    }
}