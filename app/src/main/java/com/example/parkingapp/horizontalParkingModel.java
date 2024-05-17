package com.example.parkingapp;

import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class horizontalParkingModel {


    String parkingBlock;
    String availableSpots;
    parkingSlotItem childItem;
    public horizontalParkingModel(String parkingBlock, String availableSpots,parkingSlotItem childItem) {

        this.parkingBlock = parkingBlock;
        this.availableSpots = availableSpots;
        this.childItem = childItem;
    }



    public String getParkingBlock() {
        return parkingBlock;
    }

    public String getAvailableSpots() {
        return availableSpots;
    }
    public parkingSlotItem getChildItem() {
        return childItem;
    }
}
