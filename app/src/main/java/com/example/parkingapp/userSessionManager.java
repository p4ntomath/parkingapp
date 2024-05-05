package com.example.parkingapp;

import android.content.Context;
import android.content.SharedPreferences;
public class userSessionManager {

    public static final String PREF_NAME = "userPref";
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_USER_TYPE= "userType";
    public static final String KEY_USER_EMAIL = "userEmail";
    public static final String KEY_USER_PASSWORD = "userPassword";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";


    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public userSessionManager(Context context){
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    public void createSession(String userId, String userType, String userEmail, String userPassword){
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_USER_ID, userId);
        editor.putString(KEY_USER_TYPE, userType);
        editor.putString(KEY_USER_EMAIL, userEmail);
        editor.putString(KEY_USER_PASSWORD, userPassword);
    }
    public String getUserId(){
        return sharedPreferences.getString(KEY_USER_ID, null);
    }
    public String getUserType(){
        return sharedPreferences.getString(KEY_USER_TYPE, null);
    }
    public String getUserEmail(){
        return sharedPreferences.getString(KEY_USER_EMAIL, null);
        }
    public String getUserPassword(){
        return sharedPreferences.getString(KEY_USER_PASSWORD, null);
    }
    public boolean isLoggedIn(){
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }
    public void logout(){
        editor.clear();
        editor.commit();
    }


}
