package com.example.parkingapp;

import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.ViewHolder> {

    parkingSlotItem images;
    int itemCount;

    selectListener itemClickListener;
    VerticalAdapter verticalAdapter;
    Map<Integer, List<Pair<Integer, Integer>>> blockToSpots;
    List<android.util.Pair<Integer, Integer>> bookedSpots;
    Context context;
    int spot;
    int totalSpace;

    public HorizontalAdapter(Context context,int itemCount, selectListener itemClickListener, parkingSlotItem images,int totalSpace,Map<Integer, List<Pair<Integer, Integer>>> blockToSpots) {
        this.itemCount = itemCount;
        this.itemClickListener = itemClickListener;
        this.images = images;
        this.context = context;
        this.totalSpace = totalSpace;
        this.blockToSpots = blockToSpots;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        spot = 10;
        if(totalSpace %20 != 0){
            if(position == itemCount-1) {
                int remainingSpace = totalSpace % 20;
                spot = remainingSpace % 2 == 0 ? remainingSpace / 2 : (remainingSpace - 1) / 2;
                ;
            }
        }
        if(blockToSpots != null) {
            bookedSpots = blockToSpots.get(position);;
        }
        else{
             bookedSpots = null;
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.VERTICAL, false);
        holder.verticalRecyclerView.setLayoutManager(layoutManager);
        verticalAdapter = new VerticalAdapter(context,spot,itemClickListener,images,position,bookedSpots);
        holder.verticalRecyclerView.setAdapter(verticalAdapter);
    }

    @Override
    public int getItemCount() {
        return itemCount;
    }

    public void childNoifityOnChange(){
        verticalAdapter.notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView verticalRecyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            verticalRecyclerView = itemView.findViewById(R.id.verticalRecyclerView);
        }
    }
}