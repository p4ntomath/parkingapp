package com.example.parkingapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;


public class reserve_fragment extends Fragment {

    navigationDrawerAcess navigationDrawerAcess;
    public reserve_fragment(navigationDrawerAcess navigationDrawerAcess) {
        this.navigationDrawerAcess = navigationDrawerAcess;
    }
    public reserve_fragment() {

    }


    TextView parkingName,parkimgSpot,parkingType,parkingEntry,parkingExit;
    public NavigationView navigationView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        navigationView = navigationDrawerAcess.getNavigationDrawer();
        navigationView.setCheckedItem(R.id.nav_reserve);
        View view = inflater.inflate(R.layout.myreservations_fragment, container, false);
        BookingSession bookingSession = new BookingSession(getContext());

        if(bookingSession.isBooked()){
            bookedUser(view,bookingSession);
            return view;
        }else{
            View view2 = inflater.inflate(R.layout.noreserve, container, false);
            MaterialButton findParking = view2.findViewById(R.id.toGetParking);
            findParking.setOnClickListener(v -> toFindParking());
            return view2;
        }

    }

    public void bookedUser(View view,BookingSession bookingSession){
        userSessionManager userSessionManager = new userSessionManager(getContext());
        parkingName = view.findViewById(R.id.bookedParkingName);
        parkimgSpot = view.findViewById(R.id.bookedSpotNumber);
        parkingType = view.findViewById(R.id.typeOfParking);
        parkingEntry = view.findViewById(R.id.bookedEntry);
        parkingExit = view.findViewById(R.id.bookedExit);
        parkingName.setText(bookingSession.getParkingName());
        parkimgSpot.setText(bookingSession.getBookedSpotNumber());
        parkingType.setText(userSessionManager.getUserType());
        parkingEntry.setText(bookingSession.getEntryTime());
        parkingExit.setText(bookingSession.getLeavingTime());
    }
    public void toFindParking(){

        navigationView.setCheckedItem(R.id.nav_home);
        Fragment newFragment = new home_fragment(navigationDrawerAcess);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentLayout, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

}