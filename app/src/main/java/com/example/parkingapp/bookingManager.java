package com.example.parkingapp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class bookingManager {


    Quartet<Integer,Integer,Integer,Integer> bookedspot;
    String parkingName;
    Context context;
    String entryTime, exitTime;
    Boolean insert_success = false;

    public bookingManager(Context context,Quartet<Integer, Integer, Integer, Integer> bookedspot, String parkingName, String entryTime, String exitTime) {
        this.bookedspot = bookedspot;
        this.parkingName = parkingName;
        this.context = context;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
    }

    private void showToast(String message) {
        Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void showToastOnUiThread(String message) {
        ((Activity) context).runOnUiThread(new Runnable() {@Override
        public void run() {
            showToast(message);
        }
        });
    }

   public String conversion(Quartet<Integer,Integer,Integer,Integer> bookedspot){
       char capital = 'A';
       int asciiValue = (int) capital;
       asciiValue += bookedspot.getFirst();
       char block = (char) asciiValue;
       int pattern = (bookedspot.getSecond() + 1)*2;
       String spot = "";
       if(bookedspot.getThird() == 1){
           spot = block + String.valueOf(pattern-1);
       }else{
           spot = block + String.valueOf(pattern);
       }
       return spot;}


    public String getlotID(String parkingName){
        GlobalData globalData = GlobalData.getInstance();
        HashMap<String, String> map = globalData.getNameToLotIDMap();
        String lotID = map.get(parkingName);

       return lotID;
    }public void addToSharedPreferences(String parkingName, String lotID, String spot){
        BookingSession bookingSession = new BookingSession(context);
        bookingSession.bookParkingSpot(spot, parkingName, lotID,entryTime);
        if(!entryTime.equals("Unknown")){
            bookingSession.setLeavingTime(exitTime);
        }
    }


    public boolean insertToDatabase(){
        userSessionManager userSessionManager = new userSessionManager(context);

        String userId = userSessionManager.getUserId();
        String LotID = getlotID(parkingName);
        String Spot = conversion(bookedspot);
        String entryTime = this.entryTime;
        String exitTime = this.exitTime;
        if(exitTime.equals("Unknown")){
            //insert null for unknown exit time
        }

        //insert into database

        OkHttpClient client = new OkHttpClient();

        String parseUrl = "https://lamp.ms.wits.ac.za/home/s2691450/booking.php";
        HttpUrl.Builder urlBuilder = HttpUrl.parse(parseUrl).newBuilder();
        urlBuilder.addQueryParameter("userId", userId);
        urlBuilder.addQueryParameter("lotId", LotID);
        urlBuilder.addQueryParameter("spot", Spot);
        urlBuilder.addQueryParameter("entryTime", entryTime);
        urlBuilder.addQueryParameter("exitTime", exitTime);
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast("Failed to connect to server");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    showToast("Failed to connect to server");
                    return;
                }

                try {
                    String responseBody = response.body().string();

                    if (responseBody.equals("success")) {
                        insert_success = true;
                    } else {
                        insert_success = false;
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        });

        if(insert_success){
            return true;
        }
        else{
            return false;
        }
    }





    public String getBookedSpot(){
        return conversion(bookedspot);
    }


    public void cancelBooking(){
        //cancel booking
    }
    public void updateBooking(){
        //update booking
    }
    public void deleteFromDatabase(){
        //delete from database
    }




}
