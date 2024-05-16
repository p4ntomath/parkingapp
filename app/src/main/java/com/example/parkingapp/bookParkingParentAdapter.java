package com.example.parkingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class bookParkingParentAdapter extends RecyclerView.Adapter<bookParkingParentAdapter.ViewHolder> {

    public bookParkingParentAdapter(Context context, List<horizontalParkingModel> item) {
        this.context = context;
        this.item = item;
    }

    Context context;
    List<horizontalParkingModel> item;
    List<parkingSlotItem> childItem;


    @NonNull
    @Override
    public bookParkingParentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.parent_booking_recycleitem, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull bookParkingParentAdapter.ViewHolder holder, int position) {
        holder.parkingName.setText(item.get(position).getParkingName());
        holder.parkingBlock.setText(item.get(position).getParkingBlock());
        holder.availableSpots.setText(item.get(position).getAvailableSpots());
        bookParkingChildAdapter adapter = new bookParkingChildAdapter(context,item.get(position).getChildItem(),10);
        holder.chilRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        holder.chilRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RecyclerView chilRecyclerView;
        TextView parkingName;
        TextView parkingBlock;
        TextView availableSpots;
        ImageButton rightArrow, leftArrow;


        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            chilRecyclerView = itemView.findViewById(R.id.parkingSlotVertical);
            parkingName = itemView.findViewById(R.id.parkingNameBooking);
            parkingBlock = itemView.findViewById(R.id.parkingBlock);
            availableSpots = itemView.findViewById(R.id.availableSpots);
            rightArrow = itemView.findViewById(R.id.rightButton);
            leftArrow = itemView.findViewById(R.id.leftButton);

        }
    }
}
