package com.parse.starter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class PatientLogin extends AppCompatActivity {
    public void signUp(View view){
        Intent intent = new Intent(this,PatientSignUp.class);
        startActivity(intent);
    }
    public void loginClick(View view){
        EditText usernameEditText = (EditText)findViewById(R.id.usernameEditText);
        EditText passwordEditText = (EditText)findViewById(R.id.passwordEditText);
        if(usernameEditText.getText().toString().matches("") || passwordEditText.getText().toString().matches("")){
            Toast.makeText(this,"Missing Field", Toast.LENGTH_LONG).show();
        }
        else{
            ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser parseUser, ParseException e) {
                    if(e==null) {
                        Toast.makeText(PatientLogin.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PatientLogin.this,Emergency.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(PatientLogin.this, "Login Unsuccessful !! Check Username and Password", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_login);
    }
}
