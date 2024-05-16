package com.example.parkingapp;

import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class bookParkingParentAdapter extends RecyclerView.Adapter<bookParkingParentAdapter.ViewHolder> implements selectListner {
    Context context;
    List<horizontalParkingModel> item;
    bookParkingChildAdapter adapter;
    char block;
    List<List<Pair<Boolean, Boolean>>> markSelected = new ArrayList<List<Pair<Boolean, Boolean>>>();

    List<Pair<Boolean, Boolean>> selectedSlots = new ArrayList<>();
    public bookParkingParentAdapter(Context context, List<horizontalParkingModel> item,List<List<Pair<Boolean, Boolean>>> markSelected) {
        this.context = context;
        this.item = item;
        this.markSelected = markSelected;
        selectedSlots = new ArrayList<>();
        for(int i = 0;i < item.size(); i++){
            List<Pair<Boolean, Boolean>> temp = new ArrayList<>();
            for(int j = 0; j < 10; j++) {
                temp.add(new Pair<>(false, false));
            }
            markSelected.add(temp);
        }
    }




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
        selectedSlots  = markSelected.get(position);
        adapter = new bookParkingChildAdapter(context,item.get(position).getChildItem(),10,block,this,position,selectedSlots);
        holder.chilRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        holder.chilRecyclerView.setAdapter(adapter);
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
                setSlotSelected(position,slot,true);
                label.setText("");
            }else{
                button.setImageDrawable(null);
                String slotLabel = blockParam + String.valueOf(pattern-1);
                setSlotSelected(position,slot,false);
                label.setText(slotLabel);
            }
        }
        else{
            if (button.getDrawable() == null) {
                label.setText("");
                button.setImageResource(R.drawable.cartopviewright);
                setSlotSelected(position,slot,true);
            }else{
                String slotLabel = blockParam + String.valueOf(pattern);
                setSlotSelected(position,slot,false);
                label.setText(slotLabel);
                button.setImageDrawable(null);
            }

        }
    }
    public void setSlotSelected(int position,int slot,Boolean state){
        if(slot == 1){
            selectedSlots.set(position,new Pair<>(state,selectedSlots.get(position).second));
        }else{
            selectedSlots.set(position,new Pair<>(selectedSlots.get(position).first,state));
        }
        notifyDataSetChanged();
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
