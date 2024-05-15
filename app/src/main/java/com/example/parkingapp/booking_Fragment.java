package com.example.parkingapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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


public class booking_Fragment extends Fragment implements selectListner {









    parkingSlotItem item = new parkingSlotItem(R.drawable.cartopviewleft,R.drawable.cartopviewright);
    parkingSlotAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        adapter = new parkingSlotAdapter(getContext(), item,10,this);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.booking_fragment, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.parkingSlotVertical);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);


        return view;
    }

    @Override
    public void onItemClick(ImageButton button, TextView label, int slot, int position) {

        int pattern = (position + 1)*2;

        if(slot==1){
            if (button.getDrawable() == null) {
                button.setImageResource(R.drawable.cartopviewleft);
                label.setText("");
                adapter.setClickedPosition(position,slot,true);
            }else{
                button.setImageDrawable(null);
                String slotLabel = "A" + String.valueOf(pattern-1);
                label.setText(slotLabel);
                adapter.setClickedPosition(position,slot,false);
            }
        }
        else{
            if (button.getDrawable() == null) {
                label.setText("");
                adapter.setClickedPosition(position,slot,true);
                button.setImageResource(R.drawable.cartopviewright);
            }else{
                String slotLabel = "A" + String.valueOf(pattern);
                label.setText(slotLabel);
                adapter.setClickedPosition(position,slot,false);
                button.setImageDrawable(null);
            }

        }
    }
}