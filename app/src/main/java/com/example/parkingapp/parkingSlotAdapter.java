package com.example.parkingapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class parkingSlotAdapter extends RecyclerView.Adapter<parkingViewHolder> {

    Context context;
    LayoutInflater layoutInflater;
    private selectListner listner;
    parkingSlotItem item;
    int itemCount;
     public List<Pair<Boolean, Boolean>> selectedPositions = new ArrayList<>();

    public parkingSlotAdapter(Context context,parkingSlotItem item,int itemCount,selectListner listner) {
        this.context = context;
        this.item = item;
        this.itemCount = itemCount;
        layoutInflater = LayoutInflater.from(context);
        this.listner=listner;
        for (int i = 0; i < itemCount; i++) {
            selectedPositions.add(new Pair<>(false, false));
        }

    }

    @NonNull
    @Override
    public parkingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new parkingViewHolder(layoutInflater.inflate(R.layout.parkinglotrecycleitem, parent, false));

    }


    @Override
    public void onBindViewHolder(@NonNull parkingViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.slot1.setImageResource(0);
        holder.slot2.setImageResource(0);

        if (selectedPositions.get(position).first) {
            holder.slot1.setImageResource(item.getSlotImage1());
        }
        if (selectedPositions.get(position).second) {
            holder.slot2.setImageResource(item.getSlotImage2());
        }

        //check if position is odd
        holder.slot1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.onItemClick(holder.slot1,1,position);
            }
        });
        holder.slot2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.onItemClick(holder.slot2,2,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemCount;
    }

    public void setClickedPosition(int position,int side,boolean state) {
    if (side==1){
        selectedPositions.set(position, new Pair<>(state, selectedPositions.get(position).second));
    }else{
        selectedPositions.set(position, new Pair<>(selectedPositions.get(position).first, state));
    }


    }
}
