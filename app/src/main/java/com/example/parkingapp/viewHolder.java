package com.example.parkingapp;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class viewHolder extends RecyclerView.ViewHolder {

    ImageView parkingImage;
    CardView cardView;
    TextView parkingName,parkingSpace,parkingType;

    public viewHolder(@NonNull View itemView) {
        super(itemView);
        cardView = itemView.findViewById(R.id.parkingCard);
        parkingImage = itemView.findViewById(R.id.parkingImage);
        parkingName = itemView.findViewById(R.id.parkingName);
        parkingSpace = itemView.findViewById(R.id.parkingSpace);
        parkingType = itemView.findViewById(R.id.parkingType);
    }
}
