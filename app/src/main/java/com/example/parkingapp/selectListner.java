package com.example.parkingapp;

import android.widget.ImageButton;
import android.widget.TextView;

import kotlin.Triple;

public interface selectListner {
    void onItemClick(ImageButton button, TextView label , int slot,int parentPosition,int position,char block);
    void setChoice(int parentPosition,int position,int slot);
    Triple<Integer,Integer,Integer> getChoice();
}
