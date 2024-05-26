package com.example.parkingapp;

import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import kotlin.Pair;
import kotlin.Triple;

public class VerticalAdapter extends RecyclerView.Adapter<VerticalAdapter.ViewHolder> {

   int itemCount;
    private selectListener listner;
    parkingSlotItem images;
    int parentPosition;
    Context context;
   List<android.util.Pair<Integer, Integer>> bookedSpots;

    public VerticalAdapter(Context context,int itemCount, selectListener itemClickListener, parkingSlotItem images, int parentPosition,List<android.util.Pair<Integer, Integer>> bookedSpots) {
        this.itemCount = itemCount;
        this.listner = itemClickListener;
        this.images = images;
        this.parentPosition = parentPosition;
        this.context = context;
        this.bookedSpots = bookedSpots;
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
        Pair<Character,Integer> label = label(position);

        String slot1Label = label.getFirst() + String.valueOf(label.getSecond()-1);
        String slot2Label =  label.getFirst() + String.valueOf(label.getSecond());
        holder.slot1Label.setText(slot1Label);
        holder.slot2Label.setText(slot2Label);

        int initialColor = ContextCompat.getColor(context, R.color.tertiary); // Using the "green" color resource
        holder.slot1.setBackgroundTintList(ColorStateList.valueOf(initialColor));
        holder.slot2.setBackgroundTintList(ColorStateList.valueOf(initialColor));


        if(selectedChoice.getFirst() == parentPosition && selectedChoice.getSecond() == position && selectedChoice.getThird() == 1){
            holder.slot1.setImageResource(selectedChoice.getFourth());
            holder.slot1Label.setText("");
        }
        if(selectedChoice.getFirst() == parentPosition && selectedChoice.getSecond() == position && selectedChoice.getThird() == 2){
            holder.slot2.setImageResource(selectedChoice.getFourth());
            holder.slot2Label.setText("");
        }

        if(selectedChoice.getFirst() == parentPosition && selectedChoice.getSecond() == position && selectedChoice.getThird() == 1 && selectedChoice.getIsBooked()){
            int greenColor = ContextCompat.getColor(context, R.color.Green);
            holder.slot1.setBackgroundTintList(ColorStateList.valueOf(greenColor));
        }if(selectedChoice.getFirst() == parentPosition && selectedChoice.getSecond() == position && selectedChoice.getThird() == 2 && selectedChoice.getIsBooked()){
            int greenColor = ContextCompat.getColor(context, R.color.Green);
            holder.slot2.setBackgroundTintList(ColorStateList.valueOf(greenColor));
        }

        if(bookedSpots != null){
            handleBookedSpots(holder,position);//fills cars on booked spots
        }

            holder.slot1.setOnClickListener(v -> leftSlotOnClick(position,holder));
            holder.slot2.setOnClickListener(v -> rightSlotOnClick(position,holder));
            holder.slot1Label.setOnClickListener(v -> leftSlotOnClick(position,holder));
            holder.slot2Label.setOnClickListener(v -> rightSlotOnClick(position,holder));



    }
    public Pair<Character,Integer> label(int position){
        char capital = 'A';
        int asciiValue = (int) capital;
        asciiValue += parentPosition;
        char newChar = (char) asciiValue;
        int pattern = (position + 1)*2;
        return new Pair<>(newChar,pattern);
    }

    public void handleBookedSpots(VerticalAdapter.ViewHolder holder, int position){
        for(android.util.Pair<Integer, Integer> bookedSpot : bookedSpots){
            if(bookedSpot.first %2 ==0){
                int x = (bookedSpot.first/2)-1;
                if(x == position){
                    holder.slot2.setImageResource(bookedSpot.second);
                    holder.slot2Label.setText("");
                }
            }else{
                int x = (bookedSpot.first-1)/2;
                if(x == position){
                    holder.slot1.setImageResource(bookedSpot.second);
                    holder.slot1Label.setText("");
                }
            }
        }
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