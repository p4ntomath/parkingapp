package com.example.parkingapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class home_fragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    public home_fragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        // Initialize the BottomSheetBehavior
        FrameLayout bottomSheet = view.findViewById(R.id.bottomSheet);
        BottomSheetBehavior<FrameLayout> bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setPeekHeight(600);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        ImageButton sheetBtn = view.findViewById(R.id.sheetBar);
        sheetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED ){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }else{
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });


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




        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng johannesburg = new LatLng(-26.1887, 28.0267);
        mMap.addMarker(new MarkerOptions().position(johannesburg).title("Middle Of Wits Uni"));

        // Move camera to Johannesburg and zoom closer
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(johannesburg, 15));


        mMap.setMaxZoomPreference(15); // Set max zoom level

    }

}
