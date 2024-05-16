package com.example.parkingapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class bookParkingParentAdapter extends RecyclerView.Adapter<bookParkingParentAdapter.ViewHolder> implements selectListner {

    public bookParkingParentAdapter(Context context, List<horizontalParkingModel> item) {
        this.context = context;
        this.item = item;
    }

    Context context;
    List<horizontalParkingModel> item;
    bookParkingChildAdapter adapter;
    char block;


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
        String Block = item.get(position).getParkingBlock();
        block = Block.charAt(Block.length() - 1);
        adapter = new bookParkingChildAdapter(context,item.get(position).getChildItem(),10,block,this);
        holder.chilRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        holder.chilRecyclerView.setAdapter(adapter);

        holder.leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("onClick", "Left arrow clicked");
                Toast.makeText(context, "left", Toast.LENGTH_SHORT).show();

            }
        });
        holder.rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("onClick", "Right arrow clicked");
                Toast.makeText(context,"right",Toast.LENGTH_SHORT).show();
            }
        });




        adapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    @Override
    public void onItemClick(ImageButton button, TextView label, int slot, int position,char blockParam) {
        int pattern = (position + 1)*2;

        if(slot==1){
            if (button.getDrawable() == null) {
                button.setImageResource(R.drawable.cartopviewleft);
                label.setText("");
                adapter.setClickedPosition(position,slot,true);
            }else{
                button.setImageDrawable(null);
                String slotLabel = blockParam + String.valueOf(pattern-1);
                label.setText(slotLabel);
                adapter.setClickedPosition(position,slot,false);
            }
        }
        else{
            if (button.getDrawable() == null) {
                label.setText("");
                adapter.setClickedPosition(position,slot,true);
                button.setImageResource(R.drawable.cartopviewright);
            }else{
                String slotLabel = blockParam + String.valueOf(pattern);
                label.setText(slotLabel);
                adapter.setClickedPosition(position,slot,false);
                button.setImageDrawable(null);
            }

        }
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
