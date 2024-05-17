package com.example.parkingapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.List;

import kotlin.Triple;


public class booking_Fragment extends Fragment implements selectListner {

    bookParkingParentAdapter parentAdapter;
    RecyclerView recyclerView;
List<horizontalParkingModel> parent = new ArrayList<>();
Triple<Integer,Integer,Integer> selected = new Triple<>(0,0,0);


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.booking_fragment, container, false);
        parkingSlotItem item = new parkingSlotItem(R.drawable.cartopviewleft,R.drawable.cartopviewright);
        RecyclerView recyclerView1 = view.findViewById(R.id.parentRecyclerView);
        recyclerView1.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        parent.add(new horizontalParkingModel("Block A","23",item));
        parent.add(new horizontalParkingModel("Block B","12",item));
        parent.add(new horizontalParkingModel("Block C","17",item));
        parent.add(new horizontalParkingModel("Block D","9",item));
        parent.add(new horizontalParkingModel("Block E","62",item));
        parentAdapter = new bookParkingParentAdapter(getContext(),parent,this,"Hall 29");
        recyclerView1.setAdapter(parentAdapter);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView1);
        recyclerView1.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                parentAdapter.notifyDataSetChanged();
            }
        });


        parentAdapter.notifyDataSetChanged();

        return view;
    }











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
    }

    @Override
    public void setChoice(int parentPosition, int position, int slot) {
        if(selected.equals(new Triple<>(parentPosition,position,slot))){
            selected = new Triple<>(0,0,0);
        }if(slot==0){
            selected = new Triple<>(0,0,0);
        }
        selected = new Triple<>(parentPosition,position,slot);
        parentAdapter.dataChangedChild();
    }

    @Override
    public Triple<Integer, Integer, Integer> getChoice() {
        return selected;
    }
}