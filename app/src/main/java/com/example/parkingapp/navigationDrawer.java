package com.example.parkingapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;

public class navigationDrawer extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageButton menuBtn;
    FrameLayout frameLayout;

    TextView userIdTextView;
    TextView userTypeTextView;
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
        drawerLayout = findViewById(R.id.drawer);
        menuBtn = findViewById(R.id.menuBtn);
        navigationView = findViewById(R.id.navigationView);
        frameLayout = findViewById(R.id.fragmentLayout);

        replaceFragment(new home_fragment());
        navigationView.setCheckedItem(R.id.nav_home);


        userIdTextView = findViewById(R.id.userIdDisplay);
        userTypeTextView = findViewById(R.id.userTypeDisplay);


        userSessionManager userSessionManager = new userSessionManager(this);
        if(userSessionManager.isLoggedIn()){
            displayProfile(userSessionManager.getUserId(),userSessionManager.getUserType());
        }


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
                    replaceFragment(new home_fragment());
                    return true;
                }else if (id == R.id.nav_booking) {
                    replaceFragment(new booking_Fragment());
                    return true;
                }else if (id == R.id.nav_reserve) {
                    replaceFragment(new reserve_fragment());
                    return true;
                }else if (id == R.id.nav_help) {
                    replaceFragment(new help_fragment());
                    return true;
                }else if (id == R.id.nav_about) {
                    replaceFragment(new about_fragment());
                    return true;
                }else if(id == R.id.nav_logout){
                    replaceFragment(new logout_fragment());
                    return true;
                }
                return false;
            }
        });
    }
    public void displayProfile(String userId,String userType){

        userIdTextView.setText(userId);
        userTypeTextView.setText(userType);

    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragmentLayout,fragment).commit();
    }
}