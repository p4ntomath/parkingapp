package com.example.parkingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class recycleViewAdapter extends RecyclerView.Adapter<viewHolder> {

    Context context;
    List<item> items;
    onCardViewSelected listener;
    LayoutInflater layoutInflater; // Declare layoutInflater

    public recycleViewAdapter(Context context, List<item> list,onCardViewSelected listener) {
        this.context = context;
        this.items = list;
        this.listener = listener;
        layoutInflater = LayoutInflater.from(context); // Initialize layoutInflater
    }

    public void setFilteredList(List<item> filteredList) {
        this.items = filteredList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Use layoutInflater to inflate the layout
        return new viewHolder(layoutInflater.inflate(R.layout.recycle_item, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.parkingName.setText(items.get(position).getParkingName());
        holder.parkingSpace.setText(items.get(position).getParkingSpace());
        holder.parkingImage.setImageResource(items.get(position).getParkingImage());
        holder.parkingType.setText(items.get(position).getParkingType());
        holder.cardView.setOnClickListener(v -> {onCardViewSelected(items.get(position).getParkingName(),items.get(position).getParkingSpace(),items.get(position).getParkingType());});
    }
    public void onCardViewSelected(String parkingName, String parkingSpace,String parkingType){
        listener.onCardViewSelected(parkingName,parkingSpace,parkingType);
    };

    @Override
    public int getItemCount() {
        return items.size();
    }
}
