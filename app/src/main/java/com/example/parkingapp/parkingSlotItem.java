package com.example.parkingapp;

import android.content.Context;

import java.util.ArrayList;

public class parkingSlotItem {

    ImageHelper imageHelper;
    Context context;
    public parkingSlotItem(Context context) {
        this.context = context;
        this.imageHelper = new ImageHelper(context);
    }

    public int getImage1(){
      return   imageHelper.getRandomLeftImage();
    }
    public int getImage2(){
       return imageHelper.getRandomRightImage();
    }


}

