package com.example.hamrobookmark.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.hamrobookmark.custom_views.AuthForm;
import com.example.hamrobookmark.R;

import java.util.Objects;

public class SignupActivity extends BaseActivity {
    AuthForm authForm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Objects.requireNonNull(getSupportActionBar()).hide();
        authForm = findViewById(R.id.authForm);
        authForm.setRequestQueue(requestQueue);
    }
    public void goToLoginScreen(View view){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }

}
