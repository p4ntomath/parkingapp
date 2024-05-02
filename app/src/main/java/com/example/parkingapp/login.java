package com.example.parkingapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class login extends AppCompatActivity {

    Button logInBtn ;
    EditText logInEmail;
    EditText logInPassword;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        logInBtn = findViewById(R.id.logInBtn);
        logInEmail = findViewById(R.id.logInEmail);
        logInPassword = findViewById(R.id.logInPassword);
        logInBtn.setOnClickListener(v -> logIn(v));


    }


    public void openSignUpPage(View view){
        Intent intent = new Intent(this, signup.class);
        startActivity(intent);
    }
    public void logIn(View view){
        logInEmail.setText("");
        logInPassword.setText("");

    }


}