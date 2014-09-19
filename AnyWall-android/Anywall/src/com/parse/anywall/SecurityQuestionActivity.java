package com.parse.anywall;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;


public class SecurityQuestionActivity extends Activity {
  int securityQuestionChoice1, securityQuestionChoice2;
  private String securityQuestion1, securityQuestion2, securityAnswer1, securityAnswer2;
  private EditText securityAnswer1EditText, securityAnswer2EditText;

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_security_question);

    // Set up the submit button click handler
    Button actionButton = (Button) findViewById(R.id.action_button);
    actionButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
        checkSecurityAnswers();
      }
    });

    //set up the edit texts with the appropriate fields
    securityAnswer1EditText = (EditText) findViewById(R.id.security_answer1_edit_text);
    securityAnswer2EditText = (EditText) findViewById(R.id.security_answer2_edit_text);

    Bundle extras = getIntent().getExtras();
    ParseQuery<ParseUser> forgetfulUsers;
    ParseUser forgetfulUser = null;

    forgetfulUsers = ParseUser.getQuery().whereEqualTo("username", extras.getString("EXTRA_USER_USERNAME"));
    try {
      forgetfulUser = forgetfulUsers.getFirst();
    } catch (ParseException e) {
      Log.i("PU", e.toString()); // Bugs stink, pe yu,
    }

    securityQuestionChoice1 = forgetfulUser.getInt("securityQuestion1");
    securityQuestionChoice2 = forgetfulUser.getInt("securityQuestion2");
    securityAnswer1 = forgetfulUser.getString("securityAnswer1");
    securityAnswer2 = forgetfulUser.getString("securityAnswer2");

    // Get security question choices to match them to actual security questions
    ParseQuery<ParseObject> query = ParseQuery.getQuery("SecurityQuestions");
    query.findInBackground(new FindCallback<ParseObject>() {
      public void done(List<ParseObject> questions, ParseException e) {
        if (e == null) {
          // Match security question choices to actual choices. Store them
          questionsWereRetrievedSuccessfully(questions);
        } else {
          // Somethind d3rps
          questionsRetrievalFailed();
        }
      }
    });
  }

  private void questionsRetrievalFailed() {
    Toast.makeText(SecurityQuestionActivity.this, R.string.error_security_questions_not_found, Toast.LENGTH_LONG).show();
  }

  private void questionsWereRetrievedSuccessfully(List<ParseObject> questions) {
    TextView securityQuestion1TextView, securityQuestion2TextView;

    securityQuestion1 = questions.get(securityQuestionChoice1).get("question").toString();
    securityQuestion2 = questions.get(securityQuestionChoice2).get("question").toString();

    securityQuestion1TextView = (TextView) findViewById(R.id.security_question1_text_view);
    securityQuestion2TextView = (TextView) findViewById(R.id.security_question2_text_view);

    securityQuestion1TextView.setText(securityQuestion1);
    securityQuestion2TextView.setText(securityQuestion2);
  }

  private void checkSecurityAnswers() {
    // todo do not call too early. Only call at end. Actually don't even call this, this is linked to a clickListener.
    String answer1, answer2;

    answer1 = securityAnswer1EditText.getText().toString().trim();
    answer2 = securityAnswer2EditText.getText().toString().trim();

    if (answer1.equals(securityAnswer1) && answer2.equals(securityAnswer2)) {
      Log.i("HEY YOU", "It worked");
      Bundle extras = getIntent().getExtras();
      Intent intent = new Intent(SecurityQuestionActivity.this, LoginActivity.class);
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
      intent.putExtra("EXTRA_USER_USERNAME", extras.getString("EXTRA_USER_USERNAME"));
      startActivity(intent);
    }
  }
}
