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
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;


public class SecurityQuestionActivity extends Activity {
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
  }

  private void objectsWereRetrievedSuccessfully(List<ParseObject> objects) {

    Spinner spinner1 = (Spinner) findViewById(R.id.security_question1_spinner);
    Spinner spinner2 = (Spinner) findViewById(R.id.security_question2_spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
        R.array.questions_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
    spinner1.setAdapter(adapter);
    spinner2.setAdapter(adapter);
  }

  private void checkSecurityAnswers() {

  }

  private void requestedSuccessfully() {
    Intent intent = new Intent(SecurityQuestionActivity.this, DispatchActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intent);
  }

  private void requestDidNotSucceed() {
    Toast.makeText(SecurityQuestionActivity.this, R.string.error_email_not_found, Toast.LENGTH_LONG).show();
  }
}
