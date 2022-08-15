package com.gmail.wjdrhkddud2.rememberu.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gmail.wjdrhkddud2.rememberu.R;

public class AuthActivity extends AppCompatActivity {

    private EditText emailField, passwordField;
    private Button emailButton, googleButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        emailField = findViewById(R.id.et_email_login);
        passwordField = findViewById(R.id.et_password_login);
        emailButton = findViewById(R.id.btn_email_login);
        googleButton = findViewById(R.id.btn_google_login);

        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();

                Log.e(getClass().getSimpleName(), "email : " + email + ", pw : " + password);


            }
        });

        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}