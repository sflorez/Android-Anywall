package com.parse.anywall;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

public class ForgotPasswordActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Set up the submit button click handler
        Button actionButton = (Button) findViewById(R.id.action_button);
        actionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sendPasswordReset();
            }
        });
    }

    private void sendPasswordReset() {
        ParseUser.requestPasswordResetInBackground("me@ordonezalex.com",
        new RequestPasswordResetCallback() {
            public void done(ParseException e) {
                if (e == null) {
//                    requestedSuccessfully();
                } else {
//                    requestDidNotSucceed();
                }
            }
        });
    }
}