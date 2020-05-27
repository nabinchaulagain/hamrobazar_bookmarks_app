package com.example.tracker.models;

import androidx.annotation.NonNull;

import com.example.tracker.misc.DateTimeParser;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    private String id;
    private String joinedAt;
    private String username;
    private String password;

    public User(JSONObject object) throws JSONException{
        this.joinedAt = DateTimeParser.getTimeFrom(object.getString("joinedAt"));
        this.username = object.getString("username");
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getJoinedAt() {
        return joinedAt;
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", this.username);
            jsonObject.put("password", this.password);
        }
        catch (JSONException ex){
            ex.printStackTrace();
        }
        return jsonObject;
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
