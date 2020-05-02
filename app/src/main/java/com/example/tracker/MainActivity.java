package com.example.tracker;


import android.content.Intent;
import android.os.Bundle;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.tracker.misc.AuthTokenHelper;
import com.example.tracker.misc.NotificationBackgroundJob;
import com.example.tracker.misc.RequestFactory;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseActivity implements Response.ErrorListener,Response.Listener<JSONObject> {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        String token = AuthTokenHelper.getToken(this);
        if(token == null){
           goToLoginScreen();
        }
        else{
            authenticate();
        }
    }
    public void authenticate(){
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(RequestFactory.makeJsonObjectRequest(
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
                Intent intent = new Intent(this,BookmarksActivity.class);
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
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                finishAffinity();
                startActivity(intent);
            }
        };
        Timer timer = new Timer();
        timer.schedule(task,3000);
    }
}
