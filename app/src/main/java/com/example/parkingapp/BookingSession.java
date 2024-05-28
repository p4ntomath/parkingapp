package com.example.parkingapp;

import android.content.Context;
import android.content.SharedPreferences;

public class BookingSession {

    private static final String PREF_NAME = "BookingPreferences";
    private static final String KEY_PARKING_NAME = "parking_name";
    private static final String KEY_LOT_ID = "lot_id";
    private static final String KEY_SPOT_NUMBER = "spot_number";
    private static final String KEY_ENTRY_TIME = "entry_time";
    private static final String KEY_LEAVING_TIME = "leaving_time";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_IS_BOOKED = "is_booked";

    private SharedPreferences sharedPreferences;

    public BookingSession(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void bookParkingSpot(String spotNumber,String parkingName ,String lotId,String entryTime,int image) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_LOT_ID, lotId);
        editor.putString(KEY_SPOT_NUMBER, spotNumber);
        editor.putString(KEY_PARKING_NAME, parkingName);
        editor.putString(KEY_ENTRY_TIME, entryTime);
        editor.putInt(KEY_IMAGE,image);
        editor.putBoolean(KEY_IS_BOOKED, true);
        editor.apply();
    }




    public void setLeavingTime(String leavingTime) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_LEAVING_TIME, leavingTime);
        editor.apply();
    }

    public String getBookedSpotNumber() {
        return sharedPreferences.getString(KEY_SPOT_NUMBER, "-1"); // -1 indicates no spot booked
    }

    public String getEntryTime() {
        return sharedPreferences.getString(KEY_ENTRY_TIME, "-1"); // -1 indicates no entry time recorded
    }

    public String getLeavingTime() {
        return sharedPreferences.getString(KEY_LEAVING_TIME, "-1"); // -1 indicates no leaving time recorded
    }public int getImage() {
        return sharedPreferences.getInt(KEY_IMAGE, -1); // -1 indicates no image
    }
    public String getParkingName() {
        return sharedPreferences.getString(KEY_PARKING_NAME, "-1"); // -1 indicates no parking name
    }

    public boolean isBooked() {
        return sharedPreferences.getBoolean(KEY_IS_BOOKED, false);
    }
    public void deleteBooking(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
