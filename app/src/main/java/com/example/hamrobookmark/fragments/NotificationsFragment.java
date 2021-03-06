package com.example.hamrobookmark.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.hamrobookmark.misc.Constants;
import com.example.hamrobookmark.custom_views.NotificationList;
import com.example.hamrobookmark.R;
import com.example.hamrobookmark.activities.BaseActivity;
import com.example.hamrobookmark.adapters.NotificationAdapter;
import com.example.hamrobookmark.misc.RequestFactory;
import com.example.hamrobookmark.models.Notification;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NotificationsFragment extends Fragment  implements Response.Listener<JSONArray>, AdapterView.OnItemSelectedListener {
    private List<Notification> notificationArrayList;
    private NotificationList notificationList;
    private Spinner dropDown;
    private TextView notificationEmptyMsg;
    private String[] dropDownOptions = new String[]{"any", "viewed", "not viewed"};
    private boolean isLoading = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notifications,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        notificationList = view.findViewById(R.id.notificationList);
        dropDown = view.findViewById(R.id.dropdown);
        notificationEmptyMsg = view.findViewById(R.id.notificationEmptyMsg);
        notificationArrayList = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                Objects.requireNonNull(getContext()),
                android.R.layout.simple_spinner_dropdown_item,
                dropDownOptions
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropDown.setAdapter(adapter);
        dropDown.setOnItemSelectedListener(this);
        JsonArrayRequest request = RequestFactory.makeJsonArrayRequest(
                getContext(),
                JsonArrayRequest.Method.GET,
                Constants.API_URL+"/notifications",
                null,
                this,
                null
        );
        ((BaseActivity) Objects.requireNonNull(getActivity())).requestQueue.add(request);
    }
    @Override
    public void onResponse(JSONArray response) {
        if(response.length() == 0){
            showEmptyMessage();
            return;
        }
        notificationList.initializeList(response);
        notificationArrayList = ((NotificationAdapter) Objects.requireNonNull(notificationList.getAdapter())).getNotificationList();
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
            if(criteria.equals(dropDownOptions[0])){
                newNotifications.add(notification);
            }
            else if(criteria.equals(dropDownOptions[1]) && notification.isNotified()){
                newNotifications.add(notification);
            }
            else if(criteria.equals(dropDownOptions[2]) && !notification.isNotified()){
                newNotifications.add(notification);
            }
        }
        if(newNotifications.size() == 0){
            showEmptyMessage();
        }
        else{
            notificationEmptyMsg.setVisibility(View.GONE);
        }
        NotificationAdapter adapter = (new NotificationAdapter(getContext(),newNotifications));
        notificationList.setAdapter(adapter);
    }

    @Override
    public void onNothingSelected(AdapterView parent) {

    }

    private void showEmptyMessage(){
        notificationEmptyMsg.setText("No notifications were found!!");
        notificationEmptyMsg.setVisibility(View.VISIBLE);
    }
}
