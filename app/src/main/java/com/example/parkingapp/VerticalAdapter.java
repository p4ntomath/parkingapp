package com.example.parkingapp;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kotlin.Triple;

public class VerticalAdapter extends RecyclerView.Adapter<VerticalAdapter.ViewHolder> {

   int itemCount;
    private selectListener listner;
    parkingSlotItem images;
    int parentPosition;

    public VerticalAdapter(int itemCount, selectListener itemClickListener, parkingSlotItem images, int parentPosition) {
        this.itemCount = itemCount;
        this.listner = itemClickListener;
        this.images = images;
        this.parentPosition = parentPosition;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vertical_recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.slot1.setImageResource(0);
        holder.slot2.setImageResource(0);

        Quartet<Integer,Integer,Integer,Integer> selectedChoice =  listner.getChoice();

        char capital = 'A';
        int asciiValue = (int) capital;
        asciiValue += parentPosition;
        char newChar = (char) asciiValue;
        int pattern = (position + 1)*2;
        String slot1Label = newChar + String.valueOf(pattern-1);
        String slot2Label =  newChar + String.valueOf(pattern);
        holder.slot1Label.setText(slot1Label);
        holder.slot2Label.setText(slot2Label);

        if(selectedChoice.getFirst() == parentPosition && selectedChoice.getSecond() == position && selectedChoice.getThird() == 1){
            holder.slot1.setImageResource(selectedChoice.getFourth());
            holder.slot1Label.setText("");
        }
        if(selectedChoice.getFirst() == parentPosition && selectedChoice.getSecond() == position && selectedChoice.getThird() == 2){
            holder.slot2.setImageResource(selectedChoice.getFourth());
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
    public void rightSlotOnClick(int position, VerticalAdapter.ViewHolder holder){
        listner.onVerticalItemClick(holder.slot2,holder.slot2Label,2,parentPosition,position);
        notifyDataSetChanged();
    }
    public void leftSlotOnClick(int position, VerticalAdapter.ViewHolder holder){
        listner.onVerticalItemClick(holder.slot1,holder.slot1Label,1,parentPosition,position);
        notifyDataSetChanged();
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