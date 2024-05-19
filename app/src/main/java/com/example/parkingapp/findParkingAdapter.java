package com.example.parkingapp;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class findParkingAdapter extends RecyclerView.Adapter<findParkingAdapter.ViewHolder> {

    private List<parkingModel> dataList;
    private Context context;

    public findParkingAdapter(List<parkingModel> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }
    public void setFilteredList(List<parkingModel> filteredList) {
        this.dataList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.findparking_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        parkingModel parkingModel = dataList.get(position);
        holder.roundedImageView.setImageResource(parkingModel.getImage());
        holder.typeParking.setText(parkingModel.getParkingType());
        holder.findParkingName.setText(parkingModel.getParkingName());
        holder.location.setText(parkingModel.getLocation());
        holder.parkingSpaceTotal.setText(String.valueOf(parkingModel.getTotalSpaces()));
        holder.bookedSpots.setText(String.valueOf(parkingModel.getBookedSpaces()));
        holder.favouriteSpots.setText(String.valueOf(parkingModel.getFavoriteSpaces()));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView roundedImageView;
        public TextView typeParking;
        public TextView findParkingName;
        public TextView location;
        public TextView parkingSpaceTotal;
        public TextView bookedSpots;
        public TextView favouriteSpots;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            roundedImageView = itemView.findViewById(R.id.roundedImageView);
            typeParking = itemView.findViewById(R.id.typeParking);
            findParkingName = itemView.findViewById(R.id.findParkingName);
            location = itemView.findViewById(R.id.location);
            parkingSpaceTotal = itemView.findViewById(R.id.parkingSpaceTotal);
            bookedSpots = itemView.findViewById(R.id.bookedSpots);
            favouriteSpots = itemView.findViewById(R.id.favouriteSpots);
        }
    }
}
