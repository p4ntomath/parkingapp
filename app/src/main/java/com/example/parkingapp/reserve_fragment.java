package com.example.parkingapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class reserve_fragment extends Fragment {

    navigationDrawerAcess navigationDrawerAcess;
    public reserve_fragment(navigationDrawerAcess navigationDrawerAcess) {
        this.navigationDrawerAcess = navigationDrawerAcess;
    }
    public reserve_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.myreservations_fragment, container, false);
    }
}