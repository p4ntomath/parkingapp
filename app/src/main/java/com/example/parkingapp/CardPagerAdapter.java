package com.example.parkingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CardPagerAdapter extends RecyclerView.Adapter<CardPagerAdapter.ViewHolder> {
    private List<Integer> layouts;
    private Context context;

    public CardPagerAdapter(Context context, List<Integer> layouts) {
        this.context = context;
        this.layouts = layouts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(layouts.get(viewType), parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // No need to bind any data here since the layout is already inflated in onCreateViewHolder
    }

    @Override
    public int getItemCount() {
        return layouts.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
