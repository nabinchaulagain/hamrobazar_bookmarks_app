package com.example.hamrobookmark.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.hamrobookmark.misc.Constants;
import com.example.hamrobookmark.misc.LogoutListener;
import com.example.hamrobookmark.R;
import com.example.hamrobookmark.activities.BaseActivity;
import com.example.hamrobookmark.misc.RequestFactory;
import com.example.hamrobookmark.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class ProfileFragment extends Fragment implements Response.Listener<JSONObject> {
    private TextView username,bookmarksCount,notificationsCount,joinedDate;
    private LogoutListener logoutListener;

    public ProfileFragment(LogoutListener logoutListener) {
        this.logoutListener = logoutListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        username = view.findViewById(R.id.profileUsername);
        bookmarksCount = view.findViewById(R.id.bookmarksCount);
        notificationsCount = view.findViewById(R.id.notificationsCount);
        Button logoutBtn = view.findViewById(R.id.logoutBtn);
        joinedDate = view.findViewById(R.id.profileJoinedDate);
        JsonObjectRequest request = RequestFactory.makeJsonObjectRequest(
                this.getContext(),
                JsonObjectRequest.Method.GET,
                Constants.API_URL+ "/auth/user",
                null,
                this,
                null
        );
        ((BaseActivity) Objects.requireNonNull(this.getActivity())).
                requestQueue.add(request);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutListener.onLogout();
            }
        });
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            JSONObject userJson = response.getJSONObject("user");
            String notificationsNum = response.getString("notificationsCount");
            String bookmarksNum = response.getString("bookmarksCount");
            User user = new User(userJson);
            username.setText(user.getUsername());
            joinedDate.setText("joined "+user.getJoinedAt());
            bookmarksCount.setText(bookmarksNum);
            notificationsCount.setText(notificationsNum);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
