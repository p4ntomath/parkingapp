package com.example.parkingapp;

import static android.content.ContentValues.TAG;
import static okhttp3.internal.Util.filterList;

import android.location.Location;
import android.text.TextUtils;

import android.content.pm.PackageManager;
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
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import okhttp3.*;

public class home_fragment extends Fragment implements OnMapReadyCallback, onCardViewSelected {

    private GoogleMap mMap;
    NavigationView navigationView;
    navigationDrawerAcess accessNavigationDrawer;
    HashMap<String, String> nameToLotIDMap;

    public home_fragment(navigationDrawerAcess accessNavigationDrawer) {
        this.accessNavigationDrawer = accessNavigationDrawer;
    }

    public home_fragment() {
    }

    List<parkingModel> parkings = new ArrayList<>();
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    SearchView searchView;
    TextView noParking;
    RecyclerView recyclerView;
    FrameLayout bottomSheet;
    BottomSheetBehavior<FrameLayout> bottomSheetBehavior;
    String parkingName, parkingSpace, parkingType;
    private PlacesClient placesClient;
    FloatingActionButton button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home_fragment, container, false);

        navigationView = accessNavigationDrawer.getNavigationDrawer();
        navigationView.setCheckedItem(R.id.nav_home);
        // Define a variable to hold the Places API key.


        bottomSheetBehavior(view);
        try {
            addParkings();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
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

        button = view.findViewById(R.id.nearby);
        button.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Wait for a moment", Toast.LENGTH_SHORT).show();
        });




        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        button.setOnClickListener(v -> {
            nearbyParking();
        });
        // Define the southwest and northeast corners of the boundary
        LatLng southwest = new LatLng(-26.192660, 28.02390);
        LatLng northeast = new LatLng(-26.1859, 28.032700);


        LatLngBounds bounds = new LatLngBounds(southwest, northeast);
        mMap.setLatLngBoundsForCameraTarget(bounds);
        mMap.setMaxZoomPreference(18);
        mMap.setMinZoomPreference(18);
        LatLng centerOfWits = new LatLng(-26.190026236576962, 28.02761784931059);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centerOfWits, 18));

        String json = readJSONFromRaw(R.raw.parkings);
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray parkingsArray = jsonObject.getJSONArray("Parkings");
            for (int i = 0; i < parkingsArray.length(); i++) {
                JSONObject parking = parkingsArray.getJSONObject(i);
                String name = parking.getString("Name");
                double latitude = parking.getDouble("Latitude");
                double longitude = parking.getDouble("Longitude");
                LatLng location = new LatLng(latitude, longitude);

                Marker marker = mMap.addMarker(new MarkerOptions().position(location).title(name));

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        searchView.setQuery(marker.getTitle(), false);
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                        return false;
                    }
                });
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void nearbyParking(){

        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
           ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        // Create location request
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000); // Update interval in milliseconds

        // Create location callback
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    Log.d("LocationUpdate", "Lat: " + location.getLatitude() + ", Lon: " + location.getLongitude());
                    nearByParkings near = new nearByParkings(getContext());
                    try {
                        near.compareLocations(location.getLatitude(), location.getLongitude());
                    } catch (JSONException e) {
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                    List<parkingModel> filteredList = new ArrayList<>();
                    filteredList = near.getParkings();
                    if (filteredList.isEmpty()) {
                        recyclerView.setVisibility(View.GONE);
                        noParking.setVisibility(View.VISIBLE);
                    } else {
                        findParkingAdapter adapter = (findParkingAdapter) recyclerView.getAdapter();
                        adapter.setFilteredList(filteredList);
                        recyclerView.setVisibility(View.VISIBLE);
                        noParking.setVisibility(View.GONE);
                    }
                }
            }
        };
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
        bottomSheet = view.findViewById(R.id.bottomSheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setPeekHeight(600);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    public void addParkings() throws JSONException {

        String json = readJSONFromRaw(R.raw.parkings);
        JSONObject jsonObject = new JSONObject(json);
        JSONArray parkingsArray = jsonObject.getJSONArray("Parkings");
        nameToLotIDMap = new HashMap<>();

        for (int i = 0; i < parkingsArray.length(); i++) {
            JSONObject parking = parkingsArray.getJSONObject(i);
            String name = parking.getString("Name");
            String parkingType = parking.getString("Type");
            int parkingCapacity = parking.getInt("Capacity");
            String Location = parking.getString("Location");
            String lotID = parking.getString("Lot_ID");


            nameToLotIDMap.put(name, lotID); // Store the name and lotID in the map
            parkings.add(new parkingModel(parkingType,name,Location,parkingCapacity,0,0,R.drawable.parkinglot));
        }

        GlobalData globalData = GlobalData.getInstance();
        globalData.addToGlobalMap(nameToLotIDMap);

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
