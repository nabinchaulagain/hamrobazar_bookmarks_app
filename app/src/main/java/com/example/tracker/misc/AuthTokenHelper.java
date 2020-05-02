package com.example.tracker.misc;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.tracker.Constants;

public class AuthTokenHelper {
    public static void saveToken(Context context,String token){
        SharedPreferences preferences = context.getSharedPreferences(Constants.SHARED_PREF_AUTH,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.AUTH_TOKEN,token);
        editor.apply();
    }
    public static void deleteToken(Context context){
        SharedPreferences preferences = context.getSharedPreferences(Constants.SHARED_PREF_AUTH,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(Constants.AUTH_TOKEN);
        editor.apply();
    }
    public static String getToken(Context context){
        SharedPreferences preferences = context.getSharedPreferences(Constants.SHARED_PREF_AUTH,Context.MODE_PRIVATE);
        return preferences.getString(Constants.AUTH_TOKEN,null);
    }
}
