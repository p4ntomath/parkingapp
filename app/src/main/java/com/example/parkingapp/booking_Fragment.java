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

import java.util.Calendar;


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
    Button bookNow,bookSubmit,scheduleBtn;
    String parkingNameSelected;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        parkingNameSelected =  parkingName;
        navigationView =  navigationDrawerAcess.getNavigationDrawer();
        navigationView.setCheckedItem(R.id.nav_booking);
        if ((parkingSpace-1) % 20 == 0){
            parkingSpace = parkingSpace-1;
        }
        itemCount = (int) Math.ceil(parkingSpace / 20.0);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.booking_fragment, container, false);
        View view2 = inflater.inflate(R.layout.unablebook,container,false);
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
            else{bookNowHandler();}});
        scheduleBtn.setOnClickListener(v -> {
            if(selected.getFourth() == -1){Toast.makeText(getContext(), "Please select parking slot", Toast.LENGTH_SHORT).show();}
            else{scheduleHandler();}});



        return view;
    }















    public void declaringBookingBottomSheet(View view){
        bottomSheetDialog = new BottomSheetDialog(getContext());
        View bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.booking_dialog,null);
        bottomSheetDialog.setContentView(bottomSheetView);
    }













public void toFindParking(){


        navigationView.setCheckedItem(R.id.nav_parking);
    Fragment newFragment = new findparking_fragment(navigationDrawerAcess);
    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
    transaction.replace(R.id.fragmentLayout, newFragment);
    transaction.addToBackStack(null);
    transaction.commit();

}








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
    public void setUpParentRecyclerView(View view){
        horizontalRecyclerView = view.findViewById(R.id.horizontalRecyclerView);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        horizontalRecyclerView.setLayoutManager(horizontalLayoutManager);
        images = new parkingSlotItem(getContext());
        horizontalAdapter = new HorizontalAdapter(getContext(),itemCount,this,images, parkingSpace);
        horizontalRecyclerView.setAdapter(horizontalAdapter);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(horizontalRecyclerView);
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
                Log.d("pos",String.valueOf(pos));
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

    //return the last visible item position,or Block/Parent
    private int findScrollPosition() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) horizontalRecyclerView.getLayoutManager();
        return layoutManager.findLastVisibleItemPosition();
    }









    private void bookNowHandler() {
        bottomSheetDialog.show();
        bookSubmit = bottomSheetDialog.findViewById(R.id.bookSubmit);
        bookSubmit.setText("Book Now");
        spotNumberDisplay = bottomSheetDialog.findViewById(R.id.bookingNowParkingSpot);
        parkingNameDisplay = bottomSheetDialog.findViewById(R.id.bookingNowParkingName);
        bookingManager bookingManager = new bookingManager(selected,parkingNameSelected);//This class is responsible for handling the bookin
        spotNumberDisplay.setText(bookingManager.getBookedSpot());
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
    private void scheduleHandler() {
        bottomSheetDialog.show();
        bookSubmit = bottomSheetDialog.findViewById(R.id.bookSubmit);
        bookSubmit.setText("Schedule");
        spotNumberDisplay = bottomSheetDialog.findViewById(R.id.bookingNowParkingSpot);
        parkingNameDisplay = bottomSheetDialog.findViewById(R.id.bookingNowParkingName);
        bookingManager bookingManager = new bookingManager(selected,parkingNameSelected);//This class is responsible for handling the bookin
        spotNumberDisplay.setText(bookingManager.getBookedSpot());
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
                button.setImageDrawable(null);
                setChoice(0,0,0,-1);
                String slotLabel = block + String.valueOf(pattern-1);
                label.setText(slotLabel);
            }
        }
        else{
            if (button.getDrawable() == null) {
                button.setImageResource(images.getImage2());
                label.setText("");
                setChoice(parentPosition,position,slot,images.getImage2());
            }else{
                setChoice(0,0,0,-1);
                String slotLabel = block + String.valueOf(pattern);
                label.setText(slotLabel);
                button.setImageDrawable(null);
            }
        }
    }

    @Override
    public void setChoice(int parentPosition, int position, int slot,int image) {
        Log.d("old selected",selected.toString());
        selected = new Quartet<>(parentPosition,position,slot,image);
        horizontalAdapter.childNoifityOnChange();
        Log.d("selected",selected.toString());
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






}



