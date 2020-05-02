package com.example.tracker;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.tracker.adapters.NotificationAdapter;
import com.example.tracker.misc.RequestFactory;
import com.example.tracker.models.Notification;
import org.json.JSONArray;
import java.util.ArrayList;

public class NotificationsActivity extends BottomNavigationActivity implements Response.Listener<JSONArray>,AdapterView.OnItemSelectedListener {
    ArrayList<Notification> notificationArrayList;
    NotificationList notificationList;
    Spinner dropDown;
    static String dropDownOptions[] = new String[]{"any", "viewed", "not viewed"};
    static boolean isLoading = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        super.initialize();
        notificationList = findViewById(R.id.notificationList);
        dropDown = findViewById(R.id.dropdown);
        notificationArrayList = new ArrayList<>();
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,dropDownOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropDown.setAdapter(adapter);
        dropDown.setOnItemSelectedListener(this);
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest request = RequestFactory.makeJsonArrayRequest(
                this,
                JsonArrayRequest.Method.GET,
                Constants.API_URL+"/notifications",
                null,
                this,
                null
        );
        queue.add(request);
    }

    @Override
    public void onResponse(JSONArray response) {
        System.out.println(response.toString());
        notificationList.initializeList(response);
        notificationArrayList = ((NotificationAdapter)notificationList.getAdapter()).getNotificationList();
        isLoading = false;
    }

    @Override
    public void onItemSelected(AdapterView parent, View view, int position, long id) {
        if(isLoading){
            return;
        }
        String criteria = dropDown.getSelectedItem().toString();
        ArrayList<Notification> newNotifications = new ArrayList<>();
        for(int i = 0; i < notificationArrayList.size();i++){
            Notification notification = notificationArrayList.get(i);
            if(criteria == dropDownOptions[0]){
                newNotifications.add(notification);
            }
            else if(criteria == dropDownOptions[1] && notification.isNotified()){
                newNotifications.add(notification);
            }
            else if(criteria == dropDownOptions[2] && !notification.isNotified()){
                newNotifications.add(notification);
            }
            else{
                System.out.println("else when item selected");
            }
        }
        NotificationAdapter adapter = (new NotificationAdapter(this,newNotifications));
        notificationList.setAdapter(adapter);
    }

    @Override
    public void onNothingSelected(AdapterView parent) {

    }
}
