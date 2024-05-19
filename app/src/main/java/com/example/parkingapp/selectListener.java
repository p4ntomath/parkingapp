package com.example.parkingapp;

import android.widget.ImageButton;
import android.widget.TextView;

import kotlin.Triple;

public interface selectListener {

    void onVerticalItemClick(ImageButton button, TextView label , int slot,int parentPosition,int position);
    void setChoice(int parentPosition,int position,int slot,int image);
    Quartet<Integer,Integer,Integer,Integer> getChoice();
}
