package com.example.parkingapp;

import android.content.Context;
import android.util.Log;

import java.util.Calendar;
import java.util.HashMap;

public class bookingManager {


    Quartet<Integer,Integer,Integer,Integer> bookedspot;
    String parkingName;
    Context context;
    String entryTime, exitTime;
    public bookingManager(Context context,Quartet<Integer, Integer, Integer, Integer> bookedspot, String parkingName, String entryTime, String exitTime) {
        this.bookedspot = bookedspot;
        this.parkingName = parkingName;
        this.context = context;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
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



        //if successful return true
        return true;
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
