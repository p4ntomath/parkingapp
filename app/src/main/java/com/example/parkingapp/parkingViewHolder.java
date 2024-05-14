package com.example.parkingapp;

import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class parkingViewHolder extends RecyclerView.ViewHolder{

    public ImageButton slot1;
    public ImageButton slot2;


    public parkingViewHolder(@NonNull View itemView) {
        super(itemView);
        slot1 = itemView.findViewById(R.id.slot1);
        slot2 = itemView.findViewById(R.id.slot2);

    }


}
