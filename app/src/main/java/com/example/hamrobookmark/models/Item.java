package com.example.hamrobookmark.models;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

public class Item {
    private String name,price,link;

    Item(JSONObject object) throws JSONException {
        this.name = object.getString("name");
        this.price = object.getString("price");
        this.link = object.getString("link");
    }

    public String getMinifiedName(){
        int minifiedLength = 30;
        if(this.name.length() < minifiedLength){
            return this.name;
        }
        return this.name.substring(0,minifiedLength) + "....";
    }
    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getLink() {
        return link;
    }

    @NonNull
    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}
