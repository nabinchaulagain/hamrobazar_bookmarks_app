package com.example.tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.example.tracker.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class LoginActivity extends BaseActivity{
    AuthForm authForm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        authForm = findViewById(R.id.authForm);
    }
    public void goToRegisterScreen(View view){
        Intent intent = new Intent(this,SignupActivity.class);
        startActivity(intent);
    }
}
