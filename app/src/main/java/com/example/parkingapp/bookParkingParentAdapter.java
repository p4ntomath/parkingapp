package com.example.parkingapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kotlin.Triple;

public class bookParkingParentAdapter extends RecyclerView.Adapter<bookParkingParentAdapter.ViewHolder> {
    Context context;
    List<horizontalParkingModel> item;
    bookParkingChildAdapter adapter;
    char block;
    selectListner listner;
    int parentPosition;

    String parkingName;
  Triple <Integer,Integer,Integer> selectedSlot;
    public bookParkingParentAdapter(Context context, List<horizontalParkingModel> item,selectListner listner,String parkingName) {
        this.context = context;
        this.item = item;
        this.listner = listner;
        this.parkingName = parkingName;
    }




    @NonNull
    @Override
    public bookParkingParentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.parent_booking_recycleitem, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull bookParkingParentAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.parkingName.setText(parkingName);
        holder.parkingBlock.setText(item.get(position).getParkingBlock());
        holder.availableSpots.setText(item.get(position).getAvailableSpots());
        String Block = item.get(position).getParkingBlock();
        block = Block.charAt(Block.length() - 1);
        adapter = new bookParkingChildAdapter(context,item.get(position).getChildItem(),10,block,listner,position);
        holder.childRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        holder.childRecyclerView.setAdapter(adapter);

    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public void dataChangedChild(){
        adapter.notifyDataSetChanged();
    }








    public class ViewHolder extends RecyclerView.ViewHolder {

        RecyclerView childRecyclerView;
        TextView parkingName;
        TextView parkingBlock;
        TextView availableSpots;
        ImageButton rightArrow, leftArrow;


        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            childRecyclerView = itemView.findViewById(R.id.parkingSlotVertical);
            parkingName = itemView.findViewById(R.id.parkingNameBooking);
            parkingBlock = itemView.findViewById(R.id.parkingBlock);
            availableSpots = itemView.findViewById(R.id.availableSpots);
            rightArrow = itemView.findViewById(R.id.rightButton);
            leftArrow = itemView.findViewById(R.id.leftButton);

        }
    }
}
