package com.example.parkingapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import kotlin.Triple;


public class booking_Fragment extends Fragment implements selectListner {

    bookParkingParentAdapter parentAdapter;
    RecyclerView recyclerView1;
   TextView parkingName,parkingBlock, availableSpots;
   ImageButton leftArrow,rightArrow;
   int itemCount = 5;
Triple<Integer,Integer,Integer> selected = new Triple<>(0,0,0);


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.booking_fragment, container, false);

        setUpParentRecyclerView(view); //This function is responsible for setting up the recycler view
        variablesDeclaration(view);//This function is responsible for setting up the variables
        arrowOnClick();//This function is responsible for handling the arrows
        onScrollChangeRecycleView();//This function is responsible for handling the scroll change

        parentAdapter.notifyDataSetChanged();
        return view;
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
        parkingSlotItem item = new parkingSlotItem(R.drawable.cartopviewleft,R.drawable.cartopviewright);
        recyclerView1 = view.findViewById(R.id.parentRecyclerView);
        recyclerView1.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        parentAdapter = new bookParkingParentAdapter(getContext(),this,itemCount,item);
        recyclerView1.setAdapter(parentAdapter);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView1);
    }


    public void arrowOnClick(){
        leftArrow.setOnClickListener(v ->{
            int pos = findScrollPosition();
            if(pos>0){
                arrows(pos-1);
            }

        } );
        rightArrow.setOnClickListener(v ->{
            int pos = findScrollPosition();
            if(pos<itemCount-1) {
                arrows(pos + 1);
            }});
    }
    //This function is responsible for updating the UI based on the scroll position
    public void onScrollChangeRecycleView(){
        recyclerView1.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                parentAdapter.notifyDataSetChanged();
            }
        });
        recyclerView1.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView1.getLayoutManager();
        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
        return lastVisibleItemPosition;
    }





    //Dont touch Any Code Below.


    /*
    This function are the brain of the parking lot they are responsible for handling bookings,set
    choices and get choices,the onItemClick is responsible for click on the parking slot,each spot is categorised
    by 3 coordinates <parentPosition,position,slot> Parent is basically the Blocks,position determines the row and slot
    determines the column. they are 10 rows and 2 columns making 20 parking slots per block.
     */
    @Override
    public void onItemClick(ImageButton button, TextView label, int slot, int parentPosition, int position, char block) {
        int pattern = (position + 1)*2;

        if(slot==1){
            if (button.getDrawable() == null) {
                button.setImageResource(R.drawable.cartopviewleft);
                label.setText("");
                setChoice(parentPosition,position,slot);
            }else{
                button.setImageDrawable(null);
                setChoice(0,0,0);
                String slotLabel = block + String.valueOf(pattern-1);
                label.setText(slotLabel);
            }
        }
        else{
            if (button.getDrawable() == null) {
                button.setImageResource(R.drawable.cartopviewright);
                label.setText("");
                setChoice(parentPosition,position,slot);
            }else{
                setChoice(0,0,0);
                String slotLabel = block + String.valueOf(pattern);
                label.setText(slotLabel);
                button.setImageDrawable(null);
            }

        }
        parentAdapter.notifyChildDataChanged();
    }

    public void arrows(int pos){
        recyclerView1.smoothScrollToPosition(pos);
    }

    @Override
    public void setChoice(int parentPosition, int position, int slot) {
        if(selected.equals(new Triple<>(parentPosition,position,slot))){
            selected = new Triple<>(0,0,0);
        }if(slot==0){
            selected = new Triple<>(0,0,0);
        }
        selected = new Triple<>(parentPosition,position,slot);

    }

    @Override
    public Triple<Integer, Integer, Integer> getChoice() {
        return selected;
    }



}