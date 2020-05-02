package com.example.tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tracker.models.User;

public class SignupActivity extends BaseActivity {
    AuthForm authForm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();
        authForm = findViewById(R.id.authForm);
        authForm.setRequestQueue(requestQueue);
    }
    public void goToLoginScreen(View view){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }

}
