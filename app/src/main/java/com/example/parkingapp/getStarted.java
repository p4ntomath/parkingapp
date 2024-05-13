package com.example.parkingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public class getStarted extends AppCompatActivity {
    TextView logInPage;
    Button signUpPage;

TextView signUpText;
    EditText logInUserId;
    EditText logInPassword;
    logInManager loginManager;
    Button logInButton;
    Button signUpBtn;
    EditText signUpuserId;
    EditText signUpEmail;
    EditText signUpPassword;
    RadioGroup userType;
    TextView errorMessage;
    TextView signInText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        userSessionManager userSessionManager = new userSessionManager(this);


        // Check if the user is already logged in
        if (userSessionManager.isLoggedIn()) {
            openNavigationDrawer();
        }

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_get_started);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        logInPage = findViewById(R.id.SignInPage);
        logInPage.setOnClickListener(v -> openSignInPage(v));
        signUpPage = findViewById(R.id.signUpPage);
        signUpPage.setOnClickListener(v -> openSignUpPage(v));




    }
    public void openSignInPage(View view) {
        BottomSheetDialog signInbottomSheetDialog = new BottomSheetDialog(getStarted.this);
        View newLogInView = LayoutInflater.from(this).inflate(R.layout.login_sheet, null);
        signInbottomSheetDialog.setContentView(newLogInView);
        signInbottomSheetDialog.show();
        logInUserId = newLogInView.findViewById(R.id.logInUserId);
        logInPassword = newLogInView.findViewById(R.id.logInPassword);
        logInButton = newLogInView.findViewById(R.id.logInBtn);
        signUpText = newLogInView.findViewById(R.id.signUpText);
        loginManager = new logInManager(this,logInUserId,logInPassword);


        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginManager.logIn();
            }
        });
        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInbottomSheetDialog.dismiss();
                openSignUpPage(v);
            }});

    }
    public  void openSignUpPage(View view){
        BottomSheetDialog signUpbottomSheetDialog = new BottomSheetDialog(getStarted.this);
        View newSignUpView = LayoutInflater.from(this).inflate(R.layout.signup_sheet, null);
        signUpbottomSheetDialog.setContentView(newSignUpView);
        signUpbottomSheetDialog.show();
        signUpBtn = newSignUpView.findViewById(R.id.signUpBtn);
        signUpuserId = newSignUpView.findViewById(R.id.signUpUserId);
        signUpEmail = newSignUpView.findViewById(R.id.signUpEmail);
        signUpPassword = newSignUpView.findViewById(R.id.signUpPassword);
        userType = newSignUpView.findViewById(R.id.userTypeSelection);
        errorMessage = newSignUpView.findViewById(R.id.errorMessage);
        signInText = newSignUpView.findViewById(R.id.signInText);
        signUpManager signUpManager = new signUpManager(this,signUpuserId,signUpEmail,signUpPassword,userType,errorMessage);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpManager.signUp();
            }});

        signInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpbottomSheetDialog.dismiss();
                openSignInPage(v);}});
    }

    public void openNavigationDrawer() {
        Intent intent = new Intent(this, navigationDrawer.class);
        startActivity(intent);
        finish();
    }





}