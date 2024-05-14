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
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link booking_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class booking_Fragment extends Fragment implements selectListner {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public booking_Fragment() {
        // Required empty public constructor
    }


    public static booking_Fragment newInstance(String param1, String param2) {
        booking_Fragment fragment = new booking_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

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
    public void onItemClick(ImageButton button, int slot, int position) {
        if(slot==1){
            if (button.getDrawable() == null) {
                button.setImageResource(R.drawable.cartopviewleft);
                adapter.setClickedPosition(position,slot,true);
            }else{
                button.setImageDrawable(null);
                adapter.setClickedPosition(position,slot,false);
            }
        }
        else{
            if (button.getDrawable() == null) {
                adapter.setClickedPosition(position,slot,true);
                button.setImageResource(R.drawable.cartopviewright);
            }else{
                adapter.setClickedPosition(position,slot,false);
                button.setImageDrawable(null);
            }

        }
    }
}