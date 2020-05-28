package com.example.hamrobookmark.misc;

import android.content.Context;

import androidx.annotation.Nullable;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RequestFactory {
    public static JsonObjectRequest makeJsonObjectRequest(final Context context, int method, String url, @Nullable JSONObject jsonRequest, Response.Listener<JSONObject> listener, @Nullable Response.ErrorListener errorListener) {
        JsonObjectRequest request = new JsonObjectRequest(method,url,jsonRequest,listener,errorListener){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                String token = AuthTokenHelper.getToken(context);
                if(token != null){
                    params.put("Authorization", "Bearer "+token);
                }
                return params;
            }
        };
        request.setRetryPolicy(getRetryPolicy());
        request.setShouldRetryServerErrors(false);
        return request;
    }
    public static JsonArrayRequest makeJsonArrayRequest(final Context context, int method, String url, @Nullable JSONArray jsonRequest, Response.Listener<JSONArray> listener, @Nullable Response.ErrorListener errorListener) {
        JsonArrayRequest request = new JsonArrayRequest(method,url,jsonRequest,listener,errorListener){
            @Override
            public Map<String, String> getHeaders(){
                Map<String, String> params = new HashMap<>();
                String token = AuthTokenHelper.getToken(context);
                if(token != null){
                    params.put("Authorization", "Bearer "+token);
                }
                return params;
            }
        };
        request.setRetryPolicy(getRetryPolicy());
        request.setShouldRetryServerErrors(false);
        return request;
    }
    private static DefaultRetryPolicy getRetryPolicy(){
        return new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    }
}