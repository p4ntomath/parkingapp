package com.example.parkingapp;

import static androidx.appcompat.content.res.AppCompatResources.getDrawable;

import static java.util.Calendar.getInstance;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;
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
    MaterialButton cancel,remind;
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
            if(approachingTime(bookingSession) || bookingSession.getIsReminded()){
                changeOnClick(remind,bookingSession);
            }
            return view;
        }else{
            View view2 = inflater.inflate(R.layout.noreserve, container, false);
            MaterialButton findParking = view2.findViewById(R.id.toGetParking);
            findParking.setOnClickListener(v -> toFindParking());
            return view2;
        }

    }

    public void setExitTimeDialog(BookingSession bookingSession){
        Dialog exitDialog = new Dialog(getContext());
        exitDialog.setContentView(R.layout.setexitdialog);
        exitDialog.getWindow().setBackgroundDrawable(requireContext().getDrawable(R.drawable.dialog_bg));
        exitDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        exitDialog.setCancelable(true);
        CardView cardView = exitDialog.findViewById(R.id.exitTimeCard);
        TextView displayExit = exitDialog.findViewById(R.id.exitTimeDisplay);
        Button setExit = exitDialog.findViewById(R.id.comfirmExitTime);
        displayExit.setText(bookingSession.getLeavingTime());
        exitDialog.show();
        cardView.setOnClickListener(v -> {
            setExitTime(displayExit);
        });
        setExit.setOnClickListener(v -> {
            bookingManager bookingManager = new bookingManager(getContext());
            String exitTime = displayExit.getText().toString();
            bookingManager.updateExitTime(exitTime).thenAccept(result -> {
                requireActivity().runOnUiThread(() -> {
                    if (!result) {
                        int hour = Integer.parseInt(exitTime.split(":")[0]);
                        int minute = Integer.parseInt(exitTime.split(":")[1]);
                        int hourNow = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                        int minuteNow = Calendar.getInstance().get(Calendar.MINUTE);
                        if(hourNow > hour || (hourNow == hour && minuteNow > minute)){
                            exitDialog.dismiss();
                            Toast.makeText(getContext(), "Exit time cannot be set in the past", Toast.LENGTH_SHORT).show();
                            return;

                        }
                        Toast.makeText(getContext(), "Exit time updated successfully", Toast.LENGTH_SHORT).show();
                        bookingSession.setLeavingTime(exitTime);
                        bookingSession.setIsReminded(false);
                        parkingExit.setText(exitTime);
                        exitDialog.dismiss();
                        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                        transaction.remove(this); //
                        Fragment reservationFragmentNew = new reserve_fragment(navigationDrawerAcess); // Create a new instance of the fragment
                        transaction.replace(R.id.fragmentLayout, reservationFragmentNew); // Replace with the new instance
                        transaction.commit();
                    } else {
                        exitDialog.dismiss();
                        Toast.makeText(getContext(), "Failed to update exit time", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });

    }

    private void changeOnClick(MaterialButton remind,BookingSession bookingSession) {
        remind.setText("Extend Exit");
        remind.setOnClickListener(v->{
            setExitTimeDialog(bookingSession);
        });
    }

    public void bookedUser(View view,BookingSession bookingSession) {
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
        reserve.setOnLongClickListener(v -> {
            confirmDialog();
            return true;
        });
        remind = view.findViewById(R.id.remainder);
        remind.setOnClickListener(v -> {
            if(setReminder(bookingSession)){
                Toast.makeText(getContext(), "Reminder set successfully", Toast.LENGTH_SHORT).show();
                changeOnClick(remind,bookingSession);
                }else{
                Toast.makeText(getContext(), "Failed to set reminder", Toast.LENGTH_SHORT).show();
            };
        });

    }

    private boolean approachingTime(BookingSession bookingSession) {
        int hour = Integer.parseInt(bookingSession.getLeavingTime().split(":")[0]);
        int minute = Integer.parseInt(bookingSession.getLeavingTime().split(":")[1]);
        int hourNow = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int minuteNow = Calendar.getInstance().get(Calendar.MINUTE);
        if(hourNow > hour || (hourNow == hour && minuteNow > minute)){
            return true;
        }else if(hourNow == hour && minuteNow > (minute-10)){
            return true;
        }
        return false;
    }


    private Boolean setReminder(BookingSession bookingSession) {

        try {
            Calendar notificationTime = Calendar.getInstance();
            String leavingTime = bookingSession.getLeavingTime();
            String[] timeParts = leavingTime.split(":");
            int hour = Integer.parseInt(timeParts[0]);
            int minute = Integer.parseInt(timeParts[1]);
            int newMin = minute - 10;
            if (newMin < 0) {
                hour = hour - 1;
                newMin = 60 + newMin;
            }
            notificationTime.set(Calendar.HOUR_OF_DAY, hour);
            notificationTime.set(Calendar.MINUTE, newMin);
            notificationTime.set(Calendar.SECOND, 0);
            notificationTime.set(Calendar.MILLISECOND, 0);
            long delayMillis = notificationTime.getTimeInMillis() - System.currentTimeMillis();
            if (delayMillis <= 0) {
                delayMillis += 86400000;
            }
            NotificationScheduler.scheduleNotification(getContext(), delayMillis);
            bookingSession.setIsReminded(true);
        }
        catch (Exception e){
            Log.e("Error",e.getMessage());
            return false;
        }
        return true;
    }

    public void confirmDialog(){
        Button dialogCancelBtn,DialogConfirmbtn;
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.confirmcancel);
        dialog.getWindow().setBackgroundDrawable(requireContext().getDrawable(R.drawable.dialog_bg));
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

    public void setExitTime(TextView displayExitTime){

        int hourNow = getInstance().get(Calendar.HOUR_OF_DAY);
        int minuteNow = getInstance().get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                //Avoiding the time conflict,users booking time behind the current time
                int hourToSet = Math.max(hourOfDay, hourNow);
                int minToSet;
                if(hourNow == hourOfDay){
                    minToSet = Math.max(minute,minuteNow);
                }else if(hourNow > hourOfDay){
                    minToSet = minuteNow;
                }
                else{
                    minToSet = minute;
                }
                String sHourToSet = String.valueOf(hourToSet);
                if (sHourToSet.length() == 1) {sHourToSet = "0" + sHourToSet;} //add 0 if the hour is less than 10
                String sMinToSet = String.valueOf(minToSet);
                if (sMinToSet.length() == 1) {sMinToSet = "0" + sMinToSet;}//add 0 if the minute is less than 10
                displayExitTime.setText(sHourToSet + ":" + sMinToSet);//display the exit time

            }
        }, Calendar.HOUR_OF_DAY, Calendar.MINUTE, true);
        timePickerDialog.show();

    }


}