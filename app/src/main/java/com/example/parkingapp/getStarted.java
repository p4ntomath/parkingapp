package com.example.parkingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class getStarted extends AppCompatActivity {
    Button signUpPage;
    Button loginPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        userSessionManager userSessionManager = new userSessionManager(this);


        /*
        // Check if the user is already logged in
        if (userSessionManager.isLoggedIn()) {
            Intent intent = new Intent(this, navigationDrawer.class);
            startActivity(intent);
            finish();
        }
        */


        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_get_started);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        signUpPage = findViewById(R.id.signUpPage);
        signUpPage.setOnClickListener(v -> openSignUpPage(v));

    }

    public void openSignUpPage(View view) {
        Intent intent = new Intent(this, signup.class);
        startActivity(intent);
    }

    public void openSignInPage(View view){
        Intent intent = new Intent(this, login.class);
        startActivity(intent);
    }


}