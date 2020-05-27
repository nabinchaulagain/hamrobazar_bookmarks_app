package com.example.tracker.models;


import androidx.annotation.NonNull;

import com.example.tracker.misc.DateTimeParser;

import org.json.JSONException;
import org.json.JSONObject;

public class Bookmark {
    private String id;
    private String name;
    private String date;
    private BookmarkCriteria criteria;

    public Bookmark(String name,BookmarkCriteria criteria) {
        this.name = name;
        this.criteria = criteria;
    }

    public Bookmark(JSONObject jsonObject) throws JSONException{
        this.id = jsonObject.getString("_id");
        this.name = jsonObject.getString("name");
        this.date = DateTimeParser.getTimeFrom(jsonObject.getString("bookmarkedAt"));
        this.criteria = new BookmarkCriteria(jsonObject.getJSONObject("criteria"));
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public BookmarkCriteria getCriteria(){
        return this.criteria;
    }

    public JSONObject toJSON(){
        JSONObject object = new JSONObject();
        try {
            object.put("name", this.name);
            object.put("date",this.date);
            if(this.criteria != null){
                object.put("criteria", this.criteria.toJSON());
            }
        }
        catch(JSONException ex){
            ex.printStackTrace();
        }
        return object;
    }


    @Override
    @NonNull
    public String toString() {
        return "Bookmark{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", criteria=" + criteria +
                '}';
    }
}
