package com.parse.anywall;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity which displays a login screen to the user.
 */
public class SignUpActivity extends Activity {
  // UI references.
  private EditText usernameEditText;
  private EditText emailEditText;
  private EditText passwordEditText;
  private EditText passwordAgainEditText;
  private Spinner securityQuestion1;
  private Spinner securityQuestion2;
  private EditText securityAnswerEditText1;
  private EditText securityAnswerEditText2;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_signup);

    // Set up the signup form.
    usernameEditText = (EditText) findViewById(R.id.username_edit_text);
    emailEditText = (EditText) findViewById(R.id.email_edit_text);

    passwordEditText = (EditText) findViewById(R.id.password_edit_text);
    passwordAgainEditText = (EditText) findViewById(R.id.password_again_edit_text);

    securityQuestion1 = (Spinner) findViewById(R.id.security_question1_spinner);
    securityQuestion2 = (Spinner) findViewById(R.id.security_question2_spinner);

    securityAnswerEditText1 = (EditText) findViewById(R.id.security_answer1_edit_text);
    securityAnswerEditText2 = (EditText) findViewById(R.id.security_answer2_edit_text);

    passwordAgainEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == R.id.edittext_action_signup ||
            actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
          signup();
          return true;
        }
        return false;
      }
    });
    ParseQuery<ParseObject> query = ParseQuery.getQuery("SecurityQuestions");
    query.findInBackground(new FindCallback<ParseObject>() {
      public void done(List<ParseObject> questions, ParseException e) {
        if (e == null) {
          questionsWereRetrievedSuccessfully(questions);
        } else {
          questionsRetrievalFailed();
        }
      }
    });

    // Set up the submit button click handler
    Button mActionButton = (Button) findViewById(R.id.action_button);
    mActionButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
        signup();
      }
    });
  }

  private void signup() {
    String username = usernameEditText.getText().toString().trim();
    String email = emailEditText.getText().toString().trim();
    String password = passwordEditText.getText().toString().trim();
    String passwordAgain = passwordAgainEditText.getText().toString().trim();
    String securityAnswer1 = securityAnswerEditText1.getText().toString().trim();
    String securityAnswer2 = securityAnswerEditText2.getText().toString().trim();

    // Validate the sign up data
    boolean validationError = false;
    StringBuilder validationErrorMessage = new StringBuilder(getString(R.string.error_intro));
    if (username.length() == 0) {
      validationError = true;
      validationErrorMessage.append(getString(R.string.error_blank_username));
    }
    if (email.length() == 0) {
      if (validationError) {
        validationErrorMessage.append(getString(R.string.error_join));
      }
      validationError = true;
      validationErrorMessage.append(getString(R.string.error_blank_email));
    }
    if (password.length() == 0) {
      if (validationError) {
        validationErrorMessage.append(getString(R.string.error_join));
      }
      validationError = true;
      validationErrorMessage.append(getString(R.string.error_blank_password));
    }
    if (!password.equals(passwordAgain)) {
      if (validationError) {
        validationErrorMessage.append(getString(R.string.error_join));
      }
      validationError = true;
      validationErrorMessage.append(getString(R.string.error_mismatched_passwords));
    }
    if (securityAnswer1.length() == 0 || securityAnswer2.length() == 0) {
      if (validationError) {
        validationErrorMessage.append(getString(R.string.error_join));
      }
      validationError = true;
      validationErrorMessage.append(getString(R.string.error_blank_security_answer));
    }

    validationErrorMessage.append(getString(R.string.error_end));

    // If there is a validation error, display the error
    if (validationError) {
      Toast.makeText(SignUpActivity.this, validationErrorMessage.toString(), Toast.LENGTH_LONG)
          .show();
      return;
    }

    // Set up a progress dialog
    final ProgressDialog dialog = new ProgressDialog(SignUpActivity.this);
    dialog.setMessage(getString(R.string.progress_signup));
    dialog.show();

    // Set up a new Parse user
    ParseUser user = new ParseUser();
    user.setUsername(username);
    user.setEmail(email);
    user.setPassword(password);
    user.put("securityAnswer1", securityAnswer1);
    user.put("securityAnswer2", securityAnswer2);




    // Call the Parse signup method
    user.signUpInBackground(new SignUpCallback() {
      @Override
      public void done(ParseException e) {
        dialog.dismiss();
        if (e != null) {
          // Show the error message
          Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        } else {
          // Start an intent for the dispatch activity
          Intent intent = new Intent(SignUpActivity.this, DispatchActivity.class);
          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
          startActivity(intent);
        }
      }
    });
  }

  private void questionsWereRetrievedSuccessfully(List<ParseObject> questionObjects) {
    // Extract Strings from security questions
    List<String> questions = new ArrayList<String>();
    for (ParseObject q : questionObjects) {
      questions.add(q.get("question").toString());
    }

    // Populate spinners
    securityQuestion1 = (Spinner) findViewById(R.id.security_question1_spinner);
    securityQuestion2 = (Spinner) findViewById(R.id.security_question2_spinner);

    // Create an ArrayAdapter using the questions ArrayList and a default spinner layout
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(SignUpActivity.this,
        R.layout.multiline_spinner_item, questions);

    // Specify the layout to use when the list of choices appears
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    // Apply the adapter to the spinners
    securityQuestion1.setAdapter(adapter);
    securityQuestion2.setAdapter(adapter);
  }

  private void questionsRetrievalFailed() {
    Toast.makeText(SignUpActivity.this, R.string.error_security_questions_not_found, Toast.LENGTH_LONG).show();
  }
}

