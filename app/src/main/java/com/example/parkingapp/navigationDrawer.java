package com.example.parkingapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;

public class navigationDrawer extends AppCompatActivity implements navigationDrawerAcess {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageButton menuBtn;
    FrameLayout frameLayout;
    Dialog dialog;
    Button dialogCancelBtn,DialoglogOutbtn;
    Button logOutBtn;


    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_navigation_drawer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        dialog = new Dialog(this);
        dialog.setContentView(R.layout.logout_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_bg));
        dialog.setCancelable(false);
        dialogCancelBtn = dialog.findViewById(R.id.button_cancel);
        DialoglogOutbtn = dialog.findViewById(R.id.button_logout);
        logOutBtn = findViewById(R.id.logoutButton);

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }});

        dialogCancelBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        DialoglogOutbtn.setOnClickListener(v -> logout());



        drawerLayout = findViewById(R.id.drawer);
        menuBtn = findViewById(R.id.menuBtn);
        navigationView = findViewById(R.id.navigationView);
        frameLayout = findViewById(R.id.fragmentLayout);

        replaceFragment(new home_fragment(navigationDrawer.this));
        navigationView.setCheckedItem(R.id.nav_home);


        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.open();
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                menuItem.setChecked(true);
                drawerLayout.close();
                if (id == R.id.nav_home) {
                    replaceFragment(new home_fragment(navigationDrawer.this));
                    return true;
                }
                else if (id == R.id.nav_booking) {
                    replaceFragment(new booking_Fragment(navigationDrawer.this));
                    return true;
                }else if (id == R.id.nav_reserve) {
                    replaceFragment(new reserve_fragment(navigationDrawer.this));
                    return true;
                }else if (id == R.id.nav_help) {
                    replaceFragment(new help_fragment(navigationDrawer.this));
                    return true;
                }else if (id == R.id.nav_about) {
                    replaceFragment(new about_fragment(navigationDrawer.this));
                    return true;
                }else if (id == R.id.nav_where) {
                    replaceFragment(new whereto_fragment(navigationDrawer.this));
                    return true;
                }
                return false;
            }
        });

        userSessionManager userSessionManager = new userSessionManager(this);

        if(userSessionManager.isLoggedIn()){
            String userId = userSessionManager.getUserId();
            String userType = userSessionManager.getUserType();
            View headerView = navigationView.getHeaderView(0);
            TextView userIdTextView = headerView.findViewById(R.id.userIdDisplay);
            TextView userTypeTextView = headerView.findViewById(R.id.userTypeDisplay);
            userIdTextView.setText(userId);
            userTypeTextView.setText(userType);
        }




    }



    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.close();
        } else {
            super.onBackPressed();
        }
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragmentLayout,fragment).commit();
    }

    public void logout() {
        userSessionManager userSessionManager = new userSessionManager(this);
        userSessionManager.logout();
        BookingSession bookingSession = new BookingSession(getApplicationContext());
        bookingSession.deleteBooking();
        Intent intent = new Intent(this, getStarted.class);
        startActivity(intent);
        finish();
        dialog.dismiss();
    }


    @Override
    public NavigationView getNavigationDrawer() {
        return navigationView;
    }
}