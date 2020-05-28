package com.example.hamrobookmark.custom_views;

import android.content.Context;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.example.hamrobookmark.adapters.NotificationAdapter;
import com.example.hamrobookmark.models.Notification;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Objects;

public class NotificationList extends RecyclerView {
    public NotificationList(@NonNull Context context) {
        super(context);
    }

    public NotificationList(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NotificationList(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initializeList(JSONArray notifications){
        ((SimpleItemAnimator) Objects.requireNonNull(this.getItemAnimator())).setSupportsChangeAnimations(true);
        ArrayList<Notification> notificationArrayList = new ArrayList<>();
        for(int i=0;i < notifications.length();i++){
            try {
                notificationArrayList.add(new Notification(notifications.getJSONObject(i)));
            }
            catch(JSONException ex) {
                ex.printStackTrace();
            }
        }
        NotificationAdapter adapter = new NotificationAdapter(getContext(),notificationArrayList);
        this.setLayoutManager(new LinearLayoutManager(getContext()));
        this.setAdapter(adapter);
    }
}
