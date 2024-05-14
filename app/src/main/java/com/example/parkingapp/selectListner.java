package com.example.parkingapp;

import android.widget.ImageButton;
import android.widget.TextView;

public interface selectListner {
    void onItemClick(ImageButton button, TextView label , int slot, int position);
}
