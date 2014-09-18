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


public class ForgotUsernameActivity extends Activity {
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_forgot_username);

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
    EditText forgetfulEmailEditText = null;
    ParseQuery<ParseUser> forgetfulUser;

    forgetfulEmailEditText = (EditText) findViewById(R.id.forgetful_email);
    forgetfulEmail = forgetfulEmailEditText.getText().toString().trim();

    Log.i("Email", forgetfulEmail);

    forgetfulUser = ParseUser.getQuery().whereEqualTo("email", forgetfulEmail);

    // Exits out of try before requestedSuccessfully() is called
    try {
      forgetfulEmail = forgetfulUser.getFirst().getEmail();
      requestedSuccessfully(forgetfulEmail);
    } catch (ParseException e) {
      Toast.makeText(ForgotUsernameActivity.this, R.string.error_email_not_found, Toast.LENGTH_LONG).show();
      Log.i("HEY", e.toString());
      requestDidNotSucceed();
    }
  }
  private void requestedSuccessfully(String forgetfulEmail)
  {
    Intent intent = new Intent(ForgotUsernameActivity.this, SecurityQuestionActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.putExtra("EXTRA_USER_EMAIL", forgetfulEmail);
    startActivity(intent);
  }

  private void requestDidNotSucceed()
  {
    Toast.makeText(ForgotUsernameActivity.this, R.string.error_email_not_found, Toast.LENGTH_LONG).show();
  }
}
