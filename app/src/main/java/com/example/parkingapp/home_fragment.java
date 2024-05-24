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
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
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

    List<parkingModel> parkings = new ArrayList<>();
    SearchView searchView;
    TextView noParking;
    RecyclerView recyclerView;
    FrameLayout bottomSheet;
    BottomSheetBehavior<FrameLayout> bottomSheetBehavior;
    String parkingName, parkingSpace, parkingType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home_fragment, container, false);

        navigationView = accessNavigationDrawer.getNavigationDrawer();
        navigationView.setCheckedItem(R.id.nav_home);


        bottomSheetBehavior(view);
        addParkings();
        initRecyclerView(view);//initializing the recyclerview
        supportMapFragment(); //support map fragment

        // Override onBackPressed
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else {
                    requireActivity().onBackPressed();
                }
            }
        });


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
        mMap.setLatLngBoundsForCameraTarget(bounds);
        mMap.setMaxZoomPreference(18);


        LatLng centerOfWits = new LatLng(-26.190026236576962, 28.02761784931059);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centerOfWits, 18));

        String json = readJSONFromRaw(R.raw.parking_coordinates);

        try {
            JSONObject jsonObject = new JSONObject(json);

            // Define bounds for camera target
            LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();

            Iterator<String> keys = jsonObject.keys();

            // Add markers for each place
            while (keys.hasNext()) {
                String place = keys.next();
                JSONObject coordinates = jsonObject.getJSONObject(place);
                double latitude = coordinates.getDouble("latitude");
                double longitude = coordinates.getDouble("longitude");
                LatLng location = new LatLng(latitude, longitude);

                // Add marker
                Marker marker = mMap.addMarker(new MarkerOptions().position(location).title(place));
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        // Handle marker click events here
                        searchView.setQuery(marker.getTitle(), false);
                        return false;
                    }
                });

                // Extend bounds
                boundsBuilder.include(location);
            }

            // Set camera bounds to prevent camera from leaving the specified boundaries
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
        searchView = view.findViewById(R.id.searchView);
        recyclerView = view.findViewById(R.id.findParkingRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new findParkingAdapter(parkings,getContext(),this));
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
            noParking.setVisibility(View.VISIBLE);
        } else {
            // If the filteredList is not empty, update the adapter
            findParkingAdapter adapter = (findParkingAdapter) recyclerView.getAdapter();
            adapter.setFilteredList(filteredList);
            recyclerView.setVisibility(View.VISIBLE);
            noParking.setVisibility(View.GONE);
        }
    }


    @Override
    public void onCardViewSelected(String parkingName, String parkingSpace, String parkingType) {

        int space = Integer.parseInt(parkingSpace);
        navigationView.setCheckedItem(R.id.nav_booking);
        Fragment newFragment = new booking_Fragment(accessNavigationDrawer,parkingName,space,parkingType);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentLayout, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }
    // Read JSON file from raw directory
    private String readJSONFromRaw(int rawResourceId) {
        StringBuilder stringBuilder = new StringBuilder();
        try (InputStream inputStream = getResources().openRawResource(rawResourceId);
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }


}
