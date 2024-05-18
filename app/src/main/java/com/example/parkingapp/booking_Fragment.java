package com.example.parkingapp;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

import kotlin.Triple;


public class booking_Fragment extends Fragment implements selectListener {

    private HorizontalAdapter horizontalAdapter;
    private RecyclerView horizontalRecyclerView;
    Quartet<Integer,Integer,Integer,Integer> selected = new Quartet<>(0,0,0,-1);
    TextView parkingName,parkingBlock, availableSpots;
    ImageButton leftArrow,rightArrow;
    int itemCount = 5;//how many blocks
    parkingSlotItem images;
    BottomSheetDialog bottomSheetDialog;
    Button bookNow;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.booking_fragment, container, false);
        setUpParentRecyclerView(view); //This function is responsible for setting up the recycler view
        variablesDeclaration(view);//This function is responsible for setting up the variables
        arrowOnClick();//This function is responsible for handling the arrows
        onScrollChangeRecycleView();//This function is responsible for handling the scroll change
        declaringBookingBottomSheet(view);
        bookNow = view.findViewById(R.id.bookNow);
        bookNow.setOnClickListener(v -> {bottomSheetDialog.show();});

        return view;
    }

    public void declaringBookingBottomSheet(View view){
        bottomSheetDialog = new BottomSheetDialog(getContext());
        View bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.booking_dialog,null);
        bottomSheetDialog.setContentView(bottomSheetView);
    }





















    public void variablesDeclaration(View view){
        parkingName = view.findViewById(R.id.parkingNameBooking);
        parkingBlock = view.findViewById(R.id.parkingBlock);
        availableSpots = view.findViewById(R.id.availableSpots);
        leftArrow = view.findViewById(R.id.leftButton);
        rightArrow = view.findViewById(R.id.rightButton);
        parkingName.setText("FNB Parking");
    }
    public void setUpParentRecyclerView(View view){
        horizontalRecyclerView = view.findViewById(R.id.horizontalRecyclerView);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        horizontalRecyclerView.setLayoutManager(horizontalLayoutManager);
        images = new parkingSlotItem(getContext());
        horizontalAdapter = new HorizontalAdapter(itemCount,this,images);
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
                char capital = 'A';
                int asciiValue = (int) capital;
                asciiValue += pos;
                char newChar = (char) asciiValue;
                parkingBlock.setText("Block " + newChar);
                if(pos == itemCount-1){
                    rightArrow.setVisibility(View.INVISIBLE);
                }else if(pos == 0){
                    leftArrow.setVisibility(View.INVISIBLE);
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
        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
        return lastVisibleItemPosition;
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
}



