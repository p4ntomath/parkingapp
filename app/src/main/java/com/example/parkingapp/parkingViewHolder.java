package com.example.parkingapp;

import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class parkingViewHolder extends RecyclerView.ViewHolder{

    public ImageButton slot1;
    public ImageButton slot2;
    public LinearLayout linearLayout;

    public parkingViewHolder(@NonNull View itemView) {
        super(itemView);
        slot1 = itemView.findViewById(R.id.slot1);
        slot2 = itemView.findViewById(R.id.slot2);
        linearLayout = itemView.findViewById(R.id.mainLayout);

    }

    public void bindSlot1(parkingSlotItem item) {
       slot1.setImageResource(item.getSlotImage1());
    }
    public void bindSlot2(parkingSlotItem item) {
        slot2.setImageResource(item.getSlotImage2());
    }
}
