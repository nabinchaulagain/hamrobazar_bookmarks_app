package com.example.tracker;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.tracker.misc.RequestFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class NotificationService extends JobService implements Response.Listener<JSONArray>{
    NotificationManager notificationManager;
    JobParameters params;
    @Override
    public boolean onStartJob(JobParameters params) {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        this.params = params;
        doBackgroundWork(params);
        return true;
    }


    public void doBackgroundWork(JobParameters params){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest request = RequestFactory.makeJsonArrayRequest(
                this,
                Request.Method.GET,
                Constants.API_URL+"/notifications/new",
                null,
                this,
                null
        );
        queue.add(request);
        jobFinished(params,false);
    }


    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    @Override
    public void onResponse(JSONArray response) {
        if(response.length() > 1){
            String group = new Date().toString();
            for(int i = 0; i < response.length();i++){
                try {
                    JSONObject notifJSON = response.getJSONObject(i);
                    showNotification(notifJSON,group);
                }
                catch(JSONException ex){
                    ex.printStackTrace();
                }
            }
            showNotificationSummary(group,response.length());
        }
        else if(response.length() == 1){
            try {
                JSONObject notifJSON = response.getJSONObject(0);
                showNotification(notifJSON,null);
            }
            catch(JSONException ex){
                ex.printStackTrace();
            }
        }
        jobFinished(params,false);
    }


    public void showNotification(JSONObject notifJSON, @Nullable String group) throws JSONException{
        String bookmarkName = notifJSON.getJSONObject("bookmark").getString("name");
        String link = notifJSON.getJSONObject("item").getString("link");
        Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(link));
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                0
        );
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,Constants.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("New item found for "+bookmarkName)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        if(group != null){
            builder.setGroup(group);
        }
        Notification notification = builder.build();
        notificationManager.notify((int)(Math.random() * 100000),notification);
    }

    public void showNotificationSummary(String group,int itemsCount){
        Notification notificationSummary = new NotificationCompat.Builder(this,Constants.NOTIFICATION_CHANNEL_ID)
                .setContentTitle(itemsCount + " new items")
                .setGroup(group)
                .setGroupSummary(true)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .build();
        notificationManager.notify((int) (Math.random() * 100000),notificationSummary);
    }
}
