package com.example.parkingapp;

import static androidx.appcompat.content.res.AppCompatResources.getDrawable;

import android.app.Dialog;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;

import java.util.concurrent.CompletableFuture;


public class reserve_fragment extends Fragment {

    navigationDrawerAcess navigationDrawerAcess;
    public reserve_fragment(navigationDrawerAcess navigationDrawerAcess) {
        this.navigationDrawerAcess = navigationDrawerAcess;
    }
    public reserve_fragment() {

    }


    TextView parkingName,parkimgSpot,parkingType,parkingEntry,parkingExit;
    public NavigationView navigationView;
    CardView reserve;
    MaterialButton cancel;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        navigationView = navigationDrawerAcess.getNavigationDrawer();
        navigationView.setCheckedItem(R.id.nav_reserve);
        view = inflater.inflate(R.layout.myreservations_fragment, container, false);
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
        reserve = view.findViewById(R.id.reservationCard);
        cancel = view.findViewById(R.id.cancelBooking);
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
        cancel.setOnClickListener(v -> confirmDialog());
        reserve.setOnLongClickListener(v -> {confirmDialog();
            return true;
        });

    }
    public void confirmDialog(){
        Button dialogCancelBtn,DialogConfirmbtn;
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.confirmcancel);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.show();
        dialogCancelBtn = dialog.findViewById(R.id.btn_cancel);
        DialogConfirmbtn = dialog.findViewById(R.id.btn_confirm);
        dialogCancelBtn.setOnClickListener(v -> dialog.dismiss());
        DialogConfirmbtn.setOnClickListener(v -> {
            removeBooking().thenAccept(result -> {
                dialog.dismiss();
            });
        });
    }



    public void toFindParking(){

        navigationView.setCheckedItem(R.id.nav_home);
        Fragment newFragment = new home_fragment(navigationDrawerAcess);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentLayout, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }public CompletableFuture<Boolean> removeBooking() {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        bookingManager bookingManager = new bookingManager(getContext());
        bookingManager.deleteFromDatabase().thenAccept(deleteSuccess -> {
            if (deleteSuccess) {
                getActivity().runOnUiThread(() -> {
                    BookingSession bookingSession = new BookingSession(getContext());
                    bookingSession.deleteBooking();
                    Toast.makeText(getContext(), "Booking deleted successfully", Toast.LENGTH_SHORT).show();
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    transaction.remove(this); //
                    Fragment reservationFragmentNew = new reserve_fragment(navigationDrawerAcess); // Create a new instance of the fragment
                    transaction.replace(R.id.fragmentLayout, reservationFragmentNew); // Replace with the new instance
                    transaction.commit();

                });

                future.complete(true); // Complete the future when deletion is successful
            } else {
                Toast.makeText(getContext(), "Failed to delete booking", Toast.LENGTH_SHORT).show();
                future.complete(false); // Complete the future when deletion fails
            }
        }).exceptionally(ex -> {
            // Handle exceptions here
            ex.printStackTrace();
            future.complete(false); // Complete the future exceptionally
            return null;
        });

        return future;
    }




}