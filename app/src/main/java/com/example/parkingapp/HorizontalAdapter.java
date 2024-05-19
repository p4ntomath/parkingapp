package com.example.parkingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.ViewHolder> {

    parkingSlotItem images;
    int itemCount;

    selectListener itemClickListener;
    VerticalAdapter verticalAdapter;
    Context context;
    int spot;
    int totalSpace;

    public HorizontalAdapter(Context context,int itemCount, selectListener itemClickListener, parkingSlotItem images,int totalSpace) {
        this.itemCount = itemCount;
        this.itemClickListener = itemClickListener;
        this.images = images;
        this.context = context;
        this.totalSpace = totalSpace;
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
            if(position == itemCount-1){
                int remainingSpace = totalSpace%20;
                spot = remainingSpace %2 == 0 ? remainingSpace/2 : (remainingSpace-1)/2;;
            }
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.VERTICAL, false);
        holder.verticalRecyclerView.setLayoutManager(layoutManager);
        verticalAdapter = new VerticalAdapter(context,spot,itemClickListener,images,position);
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