package com.example.tracker.models;

import androidx.annotation.NonNull;

import com.example.tracker.misc.DateTimeParser;
import org.json.JSONException;
import org.json.JSONObject;

public class Notification {
    private String id,foundAt;
    private boolean isNotified;
    private Item item;

    public Notification(JSONObject object) throws JSONException {
        this.id = object.getString("_id");
        this.foundAt = DateTimeParser.getTimeFrom(object.getString("foundAt"));
        this.isNotified = object.getBoolean("isNotified");
        this.item = new Item(object.getJSONObject("item"));
    }

    public String getId() {
        return id;
    }

    public String getFoundAt() {
        return foundAt;
    }

    public boolean isNotified() {
        return isNotified;
    }

    public Item getItem() {
        return item;
    }

    public void setNotified(boolean notified) {
        isNotified = notified;
    }

    @NonNull
    @Override
    public String toString() {
        return "Notification{" +
                "id='" + id + '\'' +
                ", foundAt='" + foundAt + '\'' +
                ", isNotified=" + isNotified +
                ", item=" + item +
                '}';
    }
}
