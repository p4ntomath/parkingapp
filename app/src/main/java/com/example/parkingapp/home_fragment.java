package com.example.parkingapp;

import static android.content.ContentValues.TAG;
import static okhttp3.internal.Util.filterList;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import java.time.LocalTime;
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
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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

    SearchView searchView;
    TextView noParking;
    RecyclerView recyclerView;
    FrameLayout bottomSheet;
    BottomSheetBehavior<FrameLayout> bottomSheetBehavior;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home_fragment, container, false);

        navigationView = accessNavigationDrawer.getNavigationDrawer();
        navigationView.setCheckedItem(R.id.nav_home);


        BookingSession bookingSession = new BookingSession(getContext());
        if(bookingSession.isBooked()){
            String exitTime = bookingSession.getLeavingTime();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            Calendar currentNow = Calendar.getInstance();
            int hour = currentNow.get(Calendar.HOUR_OF_DAY);
            int minute = currentNow.get(Calendar.MINUTE);
            String currentTime = String.format("%02d:%02d", hour, minute);
            LocalTime time = LocalTime.parse(exitTime, formatter);
            LocalTime currentTimeObj = LocalTime.parse(currentTime, formatter);

            if(time.isBefore(currentTimeObj)){
                bookingManager bookingManager = new bookingManager(getContext());
                bookingManager.deleteFromDatabase().thenAccept(success -> {
                    if (success) {
                        Toast.makeText(getContext(), "Booking Was Removed At Exit Time", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getContext(), "Failed To Remove Booking,Please Manually Remove", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }



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




        requestPermissions();

        return view;
    }
    private void requestPermissions() {

        // Check if the permissions are already granted
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.USE_EXACT_ALARM)
                != PackageManager.PERMISSION_GRANTED) {
            // Request the permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.USE_EXACT_ALARM}, 5);
            }

        }
        if ((ContextCompat.checkSelfPermission(getContext(), Manifest.permission.SCHEDULE_EXACT_ALARM))
                != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SCHEDULE_EXACT_ALARM}, 8);
            }

        }









    }



    private boolean isConnectedToInternet() {
        ConnectivityManager cm = (ConnectivityManager) requireContext().getSystemService(getContext().CONNECTIVITY_SERVICE);
        NetworkCapabilities networkCapabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
        return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
    }

    private void showDialogToSwitchOnInternet() {
        new AlertDialog.Builder(requireContext())
                .setMessage("Please switch on the internet to continue using the app.")
                .setPositiveButton("Switch on internet", (dialog, which) -> {
                    startActivityForResult(new Intent(Settings.ACTION_WIRELESS_SETTINGS), 5);
                })
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isConnectedToInternet()) {
            showDialogToSwitchOnInternet();
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;



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
        userSessionManager userSessionManager = new userSessionManager(getContext());
        String userType = userSessionManager.getUserType();
        for (int i = 0; i < parkingsArray.length(); i++) {
            JSONObject parking = parkingsArray.getJSONObject(i);
            String name = parking.getString("Name");
            String parkingType = parking.getString("Type");
            int parkingCapacity = parking.getInt("Capacity");
            String Location = parking.getString("Location");
            String lotID = parking.getString("Lot_ID");

            if(userType.equals(parkingType)){
                nameToLotIDMap.put(name, lotID); // Store the name and lotID in the map
                parkings.add(new parkingModel(parkingType,name,Location,parkingCapacity,0,0,R.drawable.parkinglot));
            }

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
