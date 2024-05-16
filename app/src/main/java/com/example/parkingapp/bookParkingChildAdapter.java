package com.example.parkingapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class bookParkingChildAdapter extends RecyclerView.Adapter<bookParkingChildAdapter.ViewHolder> {

    Context context;
    private selectListner listner;
    parkingSlotItem item;
    int itemCount;
    char block;
    int parentPosition;
    public List<Pair<Boolean,Boolean>> selectedSlots = new ArrayList<>(); //slot1,slot2

    public bookParkingChildAdapter(Context context, parkingSlotItem item, int itemCount,char block,selectListner listner,int parentPosition,List<Pair<Boolean,Boolean>> selectedSlots) {
        this.context = context;
        this.item = item;
        this.itemCount = itemCount;
        this.listner = listner;
        this.parentPosition = parentPosition;
        this.block = block;
        this.selectedSlots = selectedSlots;


    }
    @NonNull
    @Override
    public bookParkingChildAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.parkinglotrecycleitem, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull bookParkingChildAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.slot1.setImageResource(0);
        holder.slot2.setImageResource(0);
        int pattern = (position + 1)*2;
        String slot1Label = block + String.valueOf(pattern-1);
        String slot2Label =  block + String.valueOf(pattern);
        holder.slot1Label.setText(slot1Label);
        holder.slot2Label.setText(slot2Label);

        if(selectedSlots.get(position).first){
            holder.slot1.setImageResource(item.getSlotImage1());
            holder.slot1Label.setText("");
        }
        if(selectedSlots.get(position).second){
            holder.slot2.setImageResource(item.getSlotImage2());
            holder.slot2Label.setText("");
        }


        holder.slot1.setOnClickListener(v -> leftSlotOnClick(position,holder));
        holder.slot2.setOnClickListener(v -> rightSlotOnClick(position,holder));
        holder.slot1Label.setOnClickListener(v -> leftSlotOnClick(position,holder));
        holder.slot2Label.setOnClickListener(v -> rightSlotOnClick(position,holder));
    }

    @Override
    public int getItemCount() {
        return itemCount;
    }




    public void rightSlotOnClick(int position, bookParkingChildAdapter.ViewHolder holder){
        listner.onItemClick(holder.slot2,holder.slot2Label,2,position,block);
    }
    public void leftSlotOnClick(int position, bookParkingChildAdapter.ViewHolder holder){
        listner.onItemClick(holder.slot1,holder.slot1Label,1,position,block);
    }













    public static class ViewHolder extends RecyclerView.ViewHolder {

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
