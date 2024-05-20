package com.example.parkingapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;




public class findparking_fragment extends Fragment implements onCardViewSelected {

    public navigationDrawer accessNavigationDrawer;


    public findparking_fragment() {

    }
    public findparking_fragment(navigationDrawer accessNavigationDrawer){

    }



    List<parkingModel> parkings = new ArrayList<>();
    RecyclerView recyclerView;
    SearchView searchView;
    TextView noParkingFound;
    NavigationView navigationView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.findparking_fragment, container, false);


        addParkings();
        noParkingFound = view.findViewById(R.id.noParkingFound);
        searchView = view.findViewById(R.id.findParkingSearchView);
        recyclerView = view.findViewById(R.id.findParkingRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new findParkingAdapter(parkings,getContext(),this));
        navigationView = accessNavigationDrawer.getNavigationDrawer();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });




        return view;
    }


    public void addParkings(){

        parkings.add(new parkingModel("Student","Barnato","East Campus",35,3,0,R.drawable.parkinglot));
        parkings.add(new parkingModel("Staff","Men'Res","West Campus",30,23,0,R.drawable.parkinglot));
        parkings.add(new parkingModel("Staff","Wits Plus","East Campus",90,89,1,R.drawable.parkinglot));
        parkings.add(new parkingModel("Student","Biology Parking","West Campus",47,32,0,R.drawable.parkinglot));
        parkings.add(new parkingModel("Staff","FNB Parking","East Campus",89,56,0,R.drawable.parkinglot));
        parkings.add(new parkingModel("Staff","NorthWest ","West Campus",21,8,1,R.drawable.parkinglot));
        parkings.add(new parkingModel("Student","Hall 29 ","East Campus",98,87,0,R.drawable.parkinglot));
    }









    private void filterList(String newText) {
        // Initialize the filteredList
        List<parkingModel> filteredList = new ArrayList<>();
        // Loop through the items list
        for (parkingModel item : parkings) {
            // Check if the item's name contains the newText
            if (item.getParkingName().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(item);
            }
        }

        // Set the filteredList to the adapter]
        if (filteredList.isEmpty()) {
            // If the filteredList is empty, show a message
            recyclerView.setVisibility(View.GONE);
            noParkingFound.setVisibility(View.VISIBLE);
        } else {
            // If the filteredList is not empty, update the adapter
            findParkingAdapter adapter = (findParkingAdapter) recyclerView.getAdapter();
            adapter.setFilteredList(filteredList);
            recyclerView.setVisibility(View.VISIBLE);
            noParkingFound.setVisibility(View.GONE);
        }
    }


    @Override
    public void onCardViewSelected(String parkingName, String parkingSpace, String parkingType) {


        parkingSpace = parkingSpace.split(":")[1].trim();
        Log.d("Space",parkingSpace);
        int space = Integer.parseInt(parkingSpace);
        navigationView.setCheckedItem(R.id.nav_booking);
        Fragment newFragment = new booking_Fragment(accessNavigationDrawer,parkingName,space,parkingType);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentLayout, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }
}