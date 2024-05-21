package com.example.parkingapp;

import static okhttp3.internal.Util.filterList;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class home_fragment extends Fragment implements OnMapReadyCallback,onCardViewSelected {

    private GoogleMap mMap;
    NavigationView navigationView;
    navigationDrawerAcess accessNavigationDrawer;
    public home_fragment(navigationDrawerAcess accessNavigationDrawer) {
        this.accessNavigationDrawer = accessNavigationDrawer;
    }
    public home_fragment() {
    }

    List<item> items = new ArrayList<>();
    SearchView searchView;
    TextView noParking;
    RecyclerView recyclerView;
    FrameLayout bottomSheet;
    BottomSheetBehavior<FrameLayout> bottomSheetBehavior;
    String parkingName,parkingSpace,parkingType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home_fragment, container, false);

        navigationView = accessNavigationDrawer.getNavigationDrawer();
        navigationView.setCheckedItem(R.id.nav_home);


        bottomSheetBehavior(view);
        addItems();//adding items to the list

        initRecyclerView(view);//initializing the recyclerview

        supportMapFragment(); //support map fragment
        return view;
    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Define the southwest and northeast corners of the boundary
        LatLng southwest = new LatLng(-26.192660, 28.02390);
        LatLng northeast = new LatLng(-26.1859, 28.032700);

        // Create a LatLngBounds object that contains the specified boundaries
        LatLngBounds bounds = new LatLngBounds(southwest, northeast);

        // Set max zoom level
        mMap.setMaxZoomPreference(17);

        // Set camera bounds to prevent camera from leaving the specified boundaries
        mMap.setLatLngBoundsForCameraTarget(bounds);

        // Add marker to the middle of Wits Uni
        LatLng johannesburg = new LatLng(-26.1887, 28.0267);
        mMap.addMarker(new MarkerOptions().position(johannesburg).title("Middle Of Wits Uni"));

        // Move camera to Johannesburg and zoom closer
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(johannesburg, 18));
    }




    public void supportMapFragment(){


        // Initialize the SupportMapFragment
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.map, mapFragment)
                    .commit();
        }

        mapFragment.getMapAsync(this);
    }
    public void initRecyclerView(View view){
        // Initialize the RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new recycleViewAdapter(getContext(),items,this));

        noParking = view.findViewById(R.id.noParking);
        searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                filterList(newText);
                return true;
            }
        });

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
    }
    public void bottomSheetBehavior(View view){

        // Initialize the BottomSheetBehavior
        bottomSheet = view.findViewById(R.id.bottomSheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setPeekHeight(600);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);


    }

    public void addItems() {
        // Add items to the list
        items.add(new item("Barnato Parking", "Space : 35","Students", R.drawable.applogo));
        items.add(new item("Wits Plus Parking Lot", "Space : 60","Staff" ,R.drawable.eye));
        items.add(new item("Zesti Lemonz Parking Lot", "Space : 79","Staff" ,R.drawable.findparkingicon));
        items.add(new item("Hall 29 Parking Lot", "Space : 100","Students", R.drawable.homeicon));
        items.add(new item("FNB Parking Lot", "Space : 90", "Students", R.drawable.bookingicon));
        items.add(new item("John Moffat Parking Lot", "Space : 35","Students", R.drawable.applogo));
        items.add(new item("Biology Building Parking Lot", "Space : 60","Staff" ,R.drawable.eye));
        items.add(new item("David Webster Parking Lot", "Space : 79","Students" ,R.drawable.findparkingicon));
        items.add(new item("Northwest Engineering Parking Lot", "Space : 100","Staff", R.drawable.homeicon));
        items.add(new item("Men's Res Roadside Parking Lot", "Space : 90", "Students", R.drawable.bookingicon));

    }
    private void filterList(String newText) {
        // Initialize the filteredList
        List<item> filteredList = new ArrayList<>();
        // Loop through the items list
        for (item item : items) {
            // Check if the item's name contains the newText
            if (item.getParkingName().toLowerCase().contains(newText.toLowerCase())) {
                // Add the item to the filteredList
                filteredList.add(item);
            }
        }

        // Set the filteredList to the adapter
        if (filteredList.isEmpty()) {
            // If the filteredList is empty, show a message
            recyclerView.setVisibility(View.GONE);
            noParking.setVisibility(View.VISIBLE);
        } else {
            // If the filteredList is not empty, update the adapter
            recycleViewAdapter adapter = (recycleViewAdapter) recyclerView.getAdapter();
            adapter.setFilteredList(filteredList);
            recyclerView.setVisibility(View.VISIBLE);
            noParking.setVisibility(View.GONE);
        }
    }



    @Override
    public void onCardViewSelected(String Name, String Space, String Type) {
        parkingName = Name;
        parkingSpace = Space.split(":")[1].trim();
        int space = Integer.parseInt(parkingSpace);
        parkingType = Type;
        navigationView.setCheckedItem(R.id.nav_booking);
        Fragment newFragment = new booking_Fragment(accessNavigationDrawer,parkingName,space,parkingType);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentLayout, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }
    public void checkFindParking(){
        navigationView.setCheckedItem(R.id.nav_parking);
    }public void checkBooking(){
        navigationView.setCheckedItem(R.id.nav_booking);
    }

}
