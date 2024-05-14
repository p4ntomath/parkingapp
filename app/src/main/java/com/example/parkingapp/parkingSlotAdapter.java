package com.example.parkingapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class parkingSlotAdapter extends RecyclerView.Adapter<parkingViewHolder> {

    Context context;
    List<parkingSlotItem> parkingRows;
    LayoutInflater layoutInflater;
    private selectListner listner;

    public parkingSlotAdapter(Context context, List<parkingSlotItem> parkingRows,selectListner listner) {
        this.context = context;
        this.parkingRows = parkingRows;
        layoutInflater = LayoutInflater.from(context);
        this.listner=listner;
    }

    @NonNull
    @Override
    public parkingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new parkingViewHolder(layoutInflater.inflate(R.layout.parkinglotrecycleitem, parent, false));

    }


    @Override
    public void onBindViewHolder(@NonNull parkingViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //check if position is odd
        holder.slot1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.onItemClick(holder.slot1,1);
            }
        });
        holder.slot2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.onItemClick(holder.slot2,2);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (parkingRows != null) {
            return parkingRows.size();
        }
        return 0;
    }
}
