package com.example.tracker.models;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    private String id;
    private String joinedAt;
    private String username;
    private String password;

    public User(JSONObject object) throws JSONException{
        this.id = object.getString("_id");
        this.joinedAt = object.getString("joinedAt");
        this.username = object.getString("username");
        this.password = object.getString("password");
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

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(String joinedAt) {
        this.joinedAt = joinedAt;
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

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
