package com.example.hamrobookmark.activities;

import android.content.Intent;
import android.os.Bundle;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.hamrobookmark.misc.Constants;
import com.example.hamrobookmark.R;
import com.example.hamrobookmark.misc.AuthTokenHelper;
import com.example.hamrobookmark.misc.NotificationBackgroundJob;
import com.example.hamrobookmark.misc.RequestFactory;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends BaseActivity implements Response.ErrorListener,Response.Listener<JSONObject> {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Objects.requireNonNull(getSupportActionBar()).hide();
        String token = AuthTokenHelper.getToken(this);
        if(token == null){
           goToLoginScreen();
        }
        else{
            authenticate();
        }
    }
    public void authenticate(){
        requestQueue.add(RequestFactory.makeJsonObjectRequest(
                this,
                com.android.volley.Request.Method.GET,
                Constants.API_URL+"/auth/user",
                null,
                this,
                this
                )
        );
    }

    @Override
    public void onErrorResponse(VolleyError error) {
       error.printStackTrace();
    }

    @Override
    public void onResponse(JSONObject response) {
        try{
            boolean isLoggedIn = response.getBoolean("isLoggedIn");
            if(isLoggedIn){
                NotificationBackgroundJob.start(this);
                Intent intent = new Intent(this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }
        catch (JSONException ex){
            ex.printStackTrace();
        }
    }
    public void goToLoginScreen(){
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        };
        Timer timer = new Timer();
        timer.schedule(task,3000);
    }
}
