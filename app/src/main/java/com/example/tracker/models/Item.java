package com.example.tracker.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Item {
    private String name,price,link;

    public Item(JSONObject object) throws JSONException {
        this.name = object.getString("name");
        this.price = object.getString("price");
        this.link = object.getString("link");
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

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}
