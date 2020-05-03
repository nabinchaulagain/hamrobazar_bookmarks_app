package com.example.tracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends BaseActivity{
    AuthForm authForm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        authForm = findViewById(R.id.authForm);
        authForm.setRequestQueue(requestQueue);
    }
    public void goToRegisterScreen(View view){
        Intent intent = new Intent(this,SignupActivity.class);
        startActivity(intent);
    }
}