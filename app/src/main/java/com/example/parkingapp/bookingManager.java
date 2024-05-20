package com.example.parkingapp;

public class bookingManager {

    Quartet<Integer,Integer,Integer,Integer> bookedspot;
    String parkingName;
    public bookingManager(Quartet<Integer, Integer, Integer, Integer> bookedspot, String parkingName) {
        this.bookedspot = bookedspot;
        this.parkingName = parkingName;
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

    public boolean insertToDatabase(){

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
