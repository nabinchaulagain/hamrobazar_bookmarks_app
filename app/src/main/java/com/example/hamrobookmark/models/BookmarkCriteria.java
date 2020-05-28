package com.example.hamrobookmark.models;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

public class BookmarkCriteria{
        private int minPrice;
        private int maxPrice;
        private String condition;
        private String searchWord;

        BookmarkCriteria(JSONObject object) throws JSONException {
                this.condition = object.getString("condition");
                this.minPrice = object.getInt("minPrice");
                this.maxPrice = object.getInt("maxPrice");
                this.searchWord = object.getString("searchWord");
        }

        public BookmarkCriteria(String searchWord) {
                this.searchWord = searchWord;
        }

        public void setMinPrice(int minPrice) {
                this.minPrice = minPrice;
        }

        public void setMaxPrice(int maxPrice) {
                this.maxPrice = maxPrice;
        }

        public void setCondition(String condition) {
                this.condition = condition;
        }

        public int getMinPrice() {
                return minPrice;
        }

        public int getMaxPrice() {
                return maxPrice;
        }

        public String getCondition() {
                return condition;
        }

        public String getSearchWord() {
                return searchWord;
        }

        JSONObject toJSON(){
                JSONObject obj = new JSONObject();
                try {
                        obj.put("condition", this.getCondition());
                        obj.put("maxPrice", this.getMaxPrice());
                        obj.put("minPrice", this.getMinPrice());
                        obj.put("searchWord",this.getSearchWord());
                }
                catch(JSONException ex){
                        ex.printStackTrace();
                }
                return obj;
        }

        @NonNull
        @Override
        public String toString() {
                return "BookmarkCriteria{" +
                        "minPrice=" + minPrice +
                        ", maxPrice=" + maxPrice +
                        ", condition='" + condition + '\'' +
                        ", searchWord='" + searchWord + '\'' +
                        '}';
        }
}