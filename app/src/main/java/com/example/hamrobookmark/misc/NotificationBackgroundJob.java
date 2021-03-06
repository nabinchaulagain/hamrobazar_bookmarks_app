package com.example.hamrobookmark.misc;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;

import com.example.hamrobookmark.services.NotificationService;
import java.util.List;

public class NotificationBackgroundJob {
    public static void start(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(context);
        }
        JobScheduler scheduler =(JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        List<JobInfo> jobs = scheduler.getAllPendingJobs();
        if(jobs.size() == 0){
            ComponentName componentName = new ComponentName(context, NotificationService.class);
            JobInfo info = new JobInfo
                    .Builder(Constants.NOTIFICATION_JOB_ID,componentName)
                    .setPeriodic(Constants.JOB_TIME)
                    .setRequiresCharging(false)
                    .setPersisted(true)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .build();
            scheduler.schedule(info);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void createNotificationChannel(Context context){
         NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
         if(notificationManager.getNotificationChannel(Constants.NOTIFICATION_CHANNEL_ID) != null){
            return;
         }
         NotificationChannel channel1 = new NotificationChannel(
                 Constants.NOTIFICATION_CHANNEL_ID,
                 "Item Notifications",
                 NotificationManager.IMPORTANCE_DEFAULT
         );
         notificationManager.createNotificationChannel(channel1);
    }

    public static void stop(Context context){
        JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        scheduler.cancelAll();
    }
}
