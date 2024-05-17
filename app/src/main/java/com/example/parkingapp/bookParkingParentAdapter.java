package com.example.parkingapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

 RecyclerView recyclerView;
    bookParkingChildAdapter adapter;

    selectListner listner;

    parkingSlotItem item;
    int itemCount;

    public bookParkingParentAdapter(Context context,selectListner listner,int itemCount,parkingSlotItem item) {
        this.context = context;
        this.listner = listner;
        this.itemCount = itemCount;
        this.item = item;
    }




    @NonNull
    @Override
    public bookParkingParentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.parent_booking_recycleitem, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull bookParkingParentAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        char capital = 'A';
        int asciiValue = (int) capital;
        asciiValue += position;
        char newChar = (char) asciiValue;
        adapter = new bookParkingChildAdapter(context,item,10,listner,position,newChar);
        recyclerView = holder.childRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        holder.childRecyclerView.setAdapter(adapter);
    }


    @Override
    public int getItemCount() {
        return itemCount;
    }





    public void notifyChildDataChanged() {
        adapter.notifyDataSetChanged();
    }







    public class ViewHolder extends RecyclerView.ViewHolder {

        RecyclerView childRecyclerView;



        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            childRecyclerView = itemView.findViewById(R.id.parkingSlotVertical);


        }
    }
}
