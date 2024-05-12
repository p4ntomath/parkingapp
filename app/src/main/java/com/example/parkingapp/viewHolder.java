package com.example.parkingapp;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class viewHolder extends RecyclerView.ViewHolder {

    ImageView parkingImage;
    TextView parkingName,parkingSpace,parkingType;

    public viewHolder(@NonNull View itemView) {
        super(itemView);
        parkingImage = itemView.findViewById(R.id.parkingImage);
        parkingName = itemView.findViewById(R.id.parkingName);
        parkingSpace = itemView.findViewById(R.id.parkingSpace);
        parkingType = itemView.findViewById(R.id.parkingType);
    }
}
