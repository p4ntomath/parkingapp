package com.example.parkingapp;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class bookParkingChildAdapter extends RecyclerView.Adapter<bookParkingChildAdapter.ViewHolder> {

    Context context;
    parkingSlotItem item;
    int itemCount;

    public bookParkingChildAdapter(Context context, parkingSlotItem item, int itemCount) {
        this.context = context;
        this.item = item;
        this.itemCount = itemCount;
    }
    @NonNull
    @Override
    public bookParkingChildAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.parkinglotrecycleitem, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull bookParkingChildAdapter.ViewHolder holder, int position) {
        int pattern = (position + 1)*2;
        String slot1Label = "A" + String.valueOf(pattern-1);
        String slot2Label =  "A" + String.valueOf(pattern);
        holder.slot1Label.setText(slot1Label);
        holder.slot2Label.setText(slot2Label);
    }

    @Override
    public int getItemCount() {
        return itemCount;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageButton slot1;
        public ImageButton slot2;
        public TextView slot1Label;
        public TextView slot2Label;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            slot1 = itemView.findViewById(R.id.slot1);
            slot2 = itemView.findViewById(R.id.slot2);
            slot1Label = itemView.findViewById(R.id.slot1Label);
            slot2Label = itemView.findViewById(R.id.slot2Label);
        }
    }
}
