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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.List;


public class booking_Fragment extends Fragment {



RecyclerView recyclerView;
List<horizontalParkingModel> parent = new ArrayList<>();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.booking_fragment, container, false);
        parkingSlotItem item = new parkingSlotItem(R.drawable.cartopviewleft,R.drawable.cartopviewright);
        RecyclerView recyclerView1 = view.findViewById(R.id.parentRecyclerView);
        recyclerView1.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        parent.add(new horizontalParkingModel("Hall29","Block A","10",item));
        parent.add(new horizontalParkingModel("Hall29","Block B","12",item));
        parent.add(new horizontalParkingModel("Hall29","Block C","22",item));
        parent.add(new horizontalParkingModel("Hall29","Block D","12",item));
        parent.add(new horizontalParkingModel("Hall29","Block E","10",item));
        bookParkingParentAdapter parentAdapter = new bookParkingParentAdapter(getContext(),parent);
        recyclerView1.setAdapter(parentAdapter);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView1);
        parentAdapter.notifyDataSetChanged();




        return view;
    }


}