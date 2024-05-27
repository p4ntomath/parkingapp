package com.example.parkingapp;

import static java.util.Calendar.getInstance;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.os.Parcelable;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class booking_Fragment extends Fragment implements selectListener {

    public static final String ARG_PARKING_NAME = "parking_name";
    public static final String ARG_PARKING_SPACE = "parking_space";
    public static final String ARG_PARKING_TYPE = "parking_type";
    public static final String ARG_NAV_ACCESS = "nav_access";
    int totalSpace;
    private String parkingName;
    private int parkingSpace; // Changed to int
    private String parkingType;

    navigationDrawerAcess navigationDrawerAcess;

    public booking_Fragment() {

    }
    public booking_Fragment(navigationDrawerAcess navigationDrawerAcess) {
        this.navigationDrawerAcess = navigationDrawerAcess;
    }
    public booking_Fragment(navigationDrawerAcess navigationDrawerAcess,String parkingName, int parkingSpace, String parkingType) {
        this.parkingName = parkingName;
        this.parkingSpace = parkingSpace;
        this.parkingType = parkingType;
        this.navigationDrawerAcess = navigationDrawerAcess;
    }



    private HorizontalAdapter horizontalAdapter;
    private RecyclerView horizontalRecyclerView;
    Quartet<Integer,Integer,Integer,Integer> selected = new Quartet<>(0,0,0,-1);
    TextView parkingNameTextView,parkingBlock, availableSpots,spotNumberDisplay,parkingNameDisplay;
    TextView displayEntryTime,displayExitTime,availableSpotsDisplay;
    ImageButton leftArrow,rightArrow;
    public NavigationView navigationView;

    int itemCount;




    CardView bookNowEntryTime,bookNowExitTime;
    parkingSlotItem images;
    BottomSheetDialog bottomSheetDialog;
    Button bookNow,bookSubmit,scheduleBtn,toReservation;
    String parkingNameSelected;
    List<String> bookedSpots;
    Map<Integer, List<Pair<Integer, Integer>>> blockToSpots;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.booking_fragment, container, false);//Inflating the layout

        bookedSpots = null;
        BookingSession bookingSession = new BookingSession(getContext());
        if(bookingSession.isBooked()){
            toReservation = view.findViewById(R.id.toReservations);
            scheduleBtn = view.findViewById(R.id.scheduleBtn);
            bookNow = view.findViewById(R.id.bookNow);
            bookNow.setVisibility(View.GONE);
            scheduleBtn.setVisibility(View.GONE);
            toReservation.setVisibility(View.VISIBLE);
            toReservation.setOnClickListener(v -> {
                toReservation();
            });
        }


        images = new parkingSlotItem(getContext());
        blockToSpots = convertToMap(bookedSpots);
        parkingNameSelected =  parkingName;
        navigationView =  navigationDrawerAcess.getNavigationDrawer();
        navigationView.setCheckedItem(R.id.nav_booking);
        if ((parkingSpace-1) % 20 == 0){
            parkingSpace = parkingSpace-1;
        }
        itemCount = (int) Math.ceil(parkingSpace / 20.0);

        View view2 = inflater.inflate(R.layout.unablebook,container,false); //Inflating the layout
        View view3 = inflater.inflate(R.layout.alreadybooked,container,false); //Inflating the layout
        if(parkingSpace == 0 && bookingSession.isBooked()){
            MaterialButton toFindParking = view3.findViewById(R.id.toReserve);
            toFindParking.setOnClickListener(v -> {toReservation();});
            return view3;
        }
        if(parkingSpace == 0){
            MaterialButton toFindParking = view2.findViewById(R.id.toFindParking);
            toFindParking.setOnClickListener(v -> {toFindParking();});
            return view2;
        }



        setUpParentRecyclerView(view); //This function is responsible for setting up the recycler view
        variablesDeclaration(view);//This function is responsible for setting up the variables
        arrowOnClick();//This function is responsible for handling the arrows
        onScrollChangeRecycleView();//This function is responsible for handling the scroll change
        declaringBookingBottomSheet(view);

        parkingNameTextView.setText(parkingName);
        availableSpots.setText(String.valueOf(parkingSpace));

        bookNow.setOnClickListener(v -> {
            if(selected.getFourth() == -1){Toast.makeText(getContext(), "Please select parking slot", Toast.LENGTH_SHORT).show();}
            else{
                bookNowBottomSheet();
                bookNowHandler();}});
        scheduleBtn.setOnClickListener(v -> {
            if(selected.getFourth() == -1){Toast.makeText(getContext(), "Please select parking slot", Toast.LENGTH_SHORT).show();}
            else{
                scheduleBottomSheet();
                scheduleHandler();}});



        return view;
    }

    private void toReservation() {
        navigationView.setCheckedItem(R.id.nav_reserve);
        Fragment reservationFragment = new reserve_fragment(navigationDrawerAcess);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentLayout, reservationFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public  Map<Integer, List<Pair<Integer, Integer>>> convertToMap(List<String> combinations) {
        Map<Integer, List<Pair<Integer, Integer>>> map = new HashMap<>();

        if (combinations == null || combinations.isEmpty()) {
            return null;
        }
        // Iterate through each combination
        for (String combo : combinations) {
            // Extract letter and number
            char letter = combo.charAt(0);
            int letterValue = letter - 'A'; // Convert letter to its numeric value (A=0, B=1, C=2, ...)
            int number = Integer.parseInt(combo.substring(1));

            // Check if letter value exists in the map
            if (map.containsKey(letterValue)) {
                if(number %2 == 0){
                    map.get(letterValue).add(new Pair<>(number,images.getImage2()));
                }
                else{
                    map.get(letterValue).add(new Pair<>(number,images.getImage1()));
                }
            } else {
                List<Pair<Integer, Integer>> pairs = new ArrayList<>();
                if(number %2 == 0){
                    pairs.add(new Pair<>(number,images.getImage2()));
                }
                else{
                    pairs.add(new Pair<>(number, images.getImage1()));
                }
                map.put(letterValue, pairs);
            }
        }

        return map;
    }














    public void declaringBookingBottomSheet(View view){
        bottomSheetDialog = new BottomSheetDialog(getContext());
        View bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.booking_dialog,null);
        bottomSheetDialog.setContentView(bottomSheetView);
    }





//This function is responsible for setting up the variables

    public void variablesDeclaration(View view){
        bookNow = view.findViewById(R.id.bookNow);
        scheduleBtn = view.findViewById(R.id.scheduleBtn);
        parkingNameTextView = view.findViewById(R.id.parkingNameBooking);
        parkingBlock = view.findViewById(R.id.parkingBlock);
        availableSpots = view.findViewById(R.id.availableSpots);
        leftArrow = view.findViewById(R.id.leftButton);
        rightArrow = view.findViewById(R.id.rightButton);
        availableSpotsDisplay = view.findViewById(R.id.availableSpots);
    }
    //This function is responsible for setting up the recycler view
    public void setUpParentRecyclerView(View view){

        horizontalRecyclerView = view.findViewById(R.id.horizontalRecyclerView);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        horizontalRecyclerView.setLayoutManager(horizontalLayoutManager);
        horizontalAdapter = new HorizontalAdapter(getContext(),itemCount,this,images, parkingSpace,blockToSpots,parkingName);
        horizontalRecyclerView.setAdapter(horizontalAdapter);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(horizontalRecyclerView);
    }




    //return the last visible item position,or Block/Parent







    private void bookNowHandler() {



        bookSubmit.setOnClickListener(v -> {

            String entryTime = displayEntryTime.getText().toString();
            String exitTime = displayExitTime.getText().toString();
            if(exitTime.equals("Unknown")){
                Toast.makeText(getContext(), "Please Select Exit Time", Toast.LENGTH_SHORT).show();
                return;
            }
            bookingManager bookingManager = new bookingManager(getContext(),selected,parkingNameSelected,entryTime,exitTime);//This class is responsible for handling the booking
            bookingManager.insertToDatabase().thenAccept(insertSuccess -> {
                getActivity().runOnUiThread(() -> {
                    if (insertSuccess) {
                        bookingManager.addToSharedPreferences();
                        Toast.makeText(getContext(), "Booking Successful", Toast.LENGTH_SHORT).show();
                        horizontalAdapter.notifyDataSetChanged();
                        BookingSession bookingSession = new BookingSession(getContext());
                        Boolean isBook = bookingSession.isBooked();
                        Log.d("Booking",isBook.toString());

                    } else {
                        Toast.makeText(getContext(), "Booking Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }).exceptionally(ex -> {
                // Handle exceptions here
                ex.printStackTrace();
                return null;
            });

            bottomSheetDialog.dismiss();
            });

    }





    private void scheduleHandler() {

          bookSubmit.setOnClickListener(v -> {
              String entryTime = displayEntryTime.getText().toString();
              String exitTime = displayExitTime.getText().toString();
              if(exitTime.equals("Unknown")){
                  Toast.makeText(getContext(), "Please Select Exit Time", Toast.LENGTH_SHORT).show();
                  return;
              }
              bookingManager bookingManager = new bookingManager(getContext(),selected,parkingNameSelected,entryTime,exitTime);//This class is responsible for handling the booking
              bookingManager.insertToDatabase().thenAccept(insertSuccess -> {
                  getActivity().runOnUiThread(() -> {
                      if (insertSuccess) {
                          Toast.makeText(getContext(), "Schedule Successful", Toast.LENGTH_SHORT).show();
                          horizontalAdapter.notifyDataSetChanged();
                      } else {
                          Toast.makeText(getContext(), "Schedule Failed", Toast.LENGTH_SHORT).show();
                      }
                  });
              }).exceptionally(ex -> {
                  // Handle exceptions here
                  ex.printStackTrace();
                  return null;
              });

              ;
            bottomSheetDialog.dismiss();
        });

    }







     /*
    This function are the brain of the parking lot they are responsible for handling bookings,set
    choices and get choices,the onItemClick is responsible for click on the parking slot,each spot is categorised
    by 3 coordinates <parentPosition,position,slot> Parent is basically the Blocks,position determines the row and slot
    determines the column. they are 10 rows and 2 columns making 20 parking slots per block.
     */


    @Override
    public void onVerticalItemClick(ImageButton button, TextView label, int slot, int parentPosition, int position) {
        char capital = 'A';
        int asciiValue = (int) capital;
        asciiValue += parentPosition;
        char block = (char) asciiValue;
        int pattern = (position + 1)*2;


        if(slot==1){
            if (button.getDrawable() == null) {
                button.setImageResource(images.getImage1());
                label.setText("");
                setChoice(parentPosition,position,slot,images.getImage1());
            }else{
                String slotLabel = block + String.valueOf(pattern-1);
                if(bookedSpots != null){
                    if(bookedSpots.contains(slotLabel)){
                        Toast.makeText(getContext(), "Spot Is Booked", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                button.setImageDrawable(null);
                setChoice(0,0,0,-1);
                label.setText(slotLabel);
            }
        }
        else{
            if (button.getDrawable() == null) {
                button.setImageResource(images.getImage2());
                label.setText("");
                setChoice(parentPosition,position,slot,images.getImage2());
            }else{
                String slotLabel = block + String.valueOf(pattern);
                if(bookedSpots != null){
                    if(bookedSpots.contains(slotLabel)){
                        Toast.makeText(getContext(), "Spot Is Booked", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                setChoice(0,0,0,-1);
                label.setText(slotLabel);
                button.setImageDrawable(null);
            }
        }
    }

    @Override
    public void setChoice(int parentPosition, int position, int slot,int image) {
        BookingSession bookingSession = new BookingSession(getContext());
        if(bookingSession.isBooked()){
            Toast.makeText(getContext(), "Please Unbook First", Toast.LENGTH_SHORT).show();
            return;
        }
        selected = new Quartet<>(parentPosition,position,slot,image);
        horizontalAdapter.childNoifityOnChange();
    }

    @Override
    public Quartet<Integer,Integer,Integer,Integer> getChoice() {
        return selected;
    }






    ///Time Manages ///

//Returns the current time in HH:MM format
    public String getCurrentTime(){
        int hour = getInstance().get(Calendar.HOUR_OF_DAY);
        int minute = getInstance().get(Calendar.MINUTE);
        String sHour = String.valueOf(hour);
        String sMin = String.valueOf(minute);
        if (sHour.length() == 1) {sHour = "0" + sHour;}
        if (sMin.length() == 1) {sMin = "0" + sMin;}
        return sHour + ":" + sMin;
    }

    //Sets the exit time of the booking
    public void setExitTime(){

        int hourNow = getInstance().get(Calendar.HOUR_OF_DAY);
        int minuteNow = getInstance().get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                //Avoiding the time conflict,users booking time behind the current time
                int hourToSet = Math.max(hourOfDay, hourNow);
                int minToSet = hourToSet == hourNow ? minuteNow : minute;
                if(hourToSet == hourOfDay){
                    minToSet = Math.max(minute, minuteNow);
                }
                String sHourToSet = String.valueOf(hourToSet);
                if (sHourToSet.length() == 1) {sHourToSet = "0" + sHourToSet;} //add 0 if the hour is less than 10
                String sMinToSet = String.valueOf(minToSet);
                if (sMinToSet.length() == 1) {sMinToSet = "0" + sMinToSet;}//add 0 if the minute is less than 10
                displayExitTime.setText(sHourToSet + ":" + sMinToSet);//display the exit time

            }
        }, Calendar.HOUR_OF_DAY, Calendar.MINUTE, true);

        timePickerDialog.show();

    }
    //Sets the entry time of the booking
    public void setEntryTime(){

        int hourNow = getInstance().get(Calendar.HOUR_OF_DAY);
        int minuteNow = getInstance().get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                //Avoiing the time conflict,users booking time behind the current time
                int hourToSet = Math.max(hourOfDay, hourNow);
                int minToSet = hourToSet == hourNow ? minuteNow : minute;
                String sHourToSet = String.valueOf(hourToSet);
                if (sHourToSet.length() == 1) {sHourToSet = "0" + sHourToSet;}//add 0 if the hour is less than 10
                String sMinToSet = String.valueOf(minToSet);
                if (sMinToSet.length() == 1) {sMinToSet = "0" + sMinToSet;}//add 0 if the minute is less than 10
                displayEntryTime.setText(sHourToSet + ":" + sMinToSet);//display the entry time
            }
        }, Calendar.HOUR_OF_DAY, Calendar.MINUTE, true);

        timePickerDialog.show();

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

    public void bookNowBottomSheet(){
        bottomSheetDialog.show();
        bookSubmit = bottomSheetDialog.findViewById(R.id.bookSubmit);
        bookSubmit.setText("Book Now");
        spotNumberDisplay = bottomSheetDialog.findViewById(R.id.bookingNowParkingSpot);
        parkingNameDisplay = bottomSheetDialog.findViewById(R.id.bookingNowParkingName);
        spotNumberDisplay.setText(conversion(selected));
        String firstWord = parkingNameSelected.split(" ")[0];
        parkingNameDisplay.setText(firstWord);
        bookNowEntryTime = bottomSheetDialog.findViewById(R.id.bookingNowEntryTime);
        bookNowExitTime = bottomSheetDialog.findViewById(R.id.bookingNowExitTime);
        displayEntryTime = bottomSheetDialog.findViewById(R.id.bookNowDisplayEntryTime);
        displayExitTime = bottomSheetDialog.findViewById(R.id.bookNowDisplayExitTime);
        displayEntryTime.setText(getCurrentTime());
        displayExitTime.setText("Unknown");
        bookNowExitTime.setOnClickListener(v -> {
            setExitTime();});
    }

    public void scheduleBottomSheet(){
        bottomSheetDialog.show();
        bookSubmit = bottomSheetDialog.findViewById(R.id.bookSubmit);
        bookSubmit.setText("Schedule");
        spotNumberDisplay = bottomSheetDialog.findViewById(R.id.bookingNowParkingSpot);
        parkingNameDisplay = bottomSheetDialog.findViewById(R.id.bookingNowParkingName);
        spotNumberDisplay.setText(conversion(selected));
        String firstWord = parkingNameSelected.split(" ")[0];
        parkingNameDisplay.setText(firstWord);
        bookNowEntryTime = bottomSheetDialog.findViewById(R.id.bookingNowEntryTime);
        bookNowExitTime = bottomSheetDialog.findViewById(R.id.bookingNowExitTime);
        displayEntryTime = bottomSheetDialog.findViewById(R.id.bookNowDisplayEntryTime);
        displayExitTime = bottomSheetDialog.findViewById(R.id.bookNowDisplayExitTime);
        displayEntryTime.setText(getCurrentTime());
        bookNowEntryTime.setOnClickListener(v -> {
            setEntryTime();});
        bookNowExitTime.setOnClickListener(v -> {
            setExitTime();});
    }

    public void toFindParking(){


        navigationView.setCheckedItem(R.id.nav_home);
        Fragment newFragment = new home_fragment(navigationDrawerAcess);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentLayout, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }
    private int findScrollPosition() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) horizontalRecyclerView.getLayoutManager();
        return layoutManager.findLastVisibleItemPosition();
    }

    public void arrowOnClick(){
        leftArrow.setOnClickListener(v ->{
            int pos = findScrollPosition();
            if(pos>0){
                horizontalRecyclerView.smoothScrollToPosition(pos-1);
            }

        } );
        rightArrow.setOnClickListener(v ->{
            int pos = findScrollPosition();
            if(pos<itemCount-1) {
                horizontalRecyclerView.smoothScrollToPosition(pos+1);
            }});
    }
    //This function is responsible for updating the UI based on the scroll position
    public void onScrollChangeRecycleView(){
        horizontalRecyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                horizontalAdapter.notifyDataSetChanged();
            }
        });
        horizontalRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int pos = findScrollPosition();
                char capital = 'A';
                int asciiValue = (int) capital;
                asciiValue += pos;
                char newChar = (char) asciiValue;
                parkingBlock.setText("Block " + newChar);
                if(parkingSpace == 20){
                    rightArrow.setVisibility(View.INVISIBLE);
                    leftArrow.setVisibility(View.INVISIBLE);
                }
                else if(pos == itemCount-1){
                    rightArrow.setVisibility(View.INVISIBLE);
                    leftArrow.setVisibility(View.VISIBLE);

                }else if(pos == 0 && itemCount>1){
                    leftArrow.setVisibility(View.INVISIBLE);
                    rightArrow.setVisibility(View.VISIBLE);
                }else{
                    leftArrow.setVisibility(View.VISIBLE);
                    rightArrow.setVisibility(View.VISIBLE);
                }
            }
        });
    }

}



