package com.example.parkingapp;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;

public class bookingManager {


    Quartet<Integer,Integer,Integer,Integer> bookedspot;
    String parkingName;
    Context context;
    public bookingManager(Context context,Quartet<Integer, Integer, Integer, Integer> bookedspot, String parkingName) {
        this.bookedspot = bookedspot;
        this.parkingName = parkingName;
        this.context = context;
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
    }


    public boolean insertToDatabase(){

        String LotID = getlotID(parkingName);
        String Spot = conversion(bookedspot);






        //insert to database
        return false;
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
