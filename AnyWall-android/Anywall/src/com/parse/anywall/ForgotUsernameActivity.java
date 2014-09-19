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


public class ForgotUsernameActivity extends Activity {
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_forgot_username);

    EditText emailEditText = (EditText) findViewById(R.id.forgetful_email_edit_text);
    emailEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == R.id.edittext_action_submit ||
            actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
          sendUsernameReset();
          return true;
        }
        return false;
      }
    });

    // Set up the submit button click handler
    Button actionButton = (Button) findViewById(R.id.action_button);

    actionButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
        sendUsernameReset();
      }
    });
  }

  private void sendUsernameReset() {
    String forgetfulEmail = null;
    String forgetfulUsername = null;
    EditText forgetfulEmailEditText = null;
    ParseQuery<ParseUser> forgetfulUser;

    forgetfulEmailEditText = (EditText) findViewById(R.id.forgetful_email_edit_text);
    forgetfulEmail = forgetfulEmailEditText.getText().toString().trim();

    // Get the Users which have the email entered by the user
    forgetfulUser = ParseUser.getQuery().whereEqualTo("email", forgetfulEmail);

    // TODO: check if email is empty

    // Exits out of try before requestedSuccessfully() is called
    try {
      forgetfulUsername = forgetfulUser.getFirst().getUsername();

      requestedSuccessfully(forgetfulUsername);
    } catch (ParseException e) {
      // We use error_email_not_found because forgetfulUser query depends on the forgetfulEmail value being found in the Users table.
      Toast.makeText(ForgotUsernameActivity.this, R.string.error_email_not_found, Toast.LENGTH_LONG).show();
      Log.i("HEY", e.toString());
      requestDidNotSucceed();
    }
  }

  private void requestedSuccessfully(String forgetfulUsername) {
    Intent intent = new Intent(ForgotUsernameActivity.this, SecurityQuestionActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.putExtra("EXTRA_USER_USERNAME", forgetfulUsername);
    startActivity(intent);
  }

  private void requestDidNotSucceed() {
    Toast.makeText(ForgotUsernameActivity.this, R.string.error_email_not_found, Toast.LENGTH_LONG).show();
  }
}
