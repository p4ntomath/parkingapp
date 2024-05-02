package com.example.parkingapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class signup extends AppCompatActivity {

    Button signUpBtn;
    EditText signUpNames;
    EditText signUpEmail;
    EditText signUpPassword;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        signUpBtn = findViewById(R.id.signUpBtn);
        signUpNames = findViewById(R.id.signUpNames);
        signUpEmail = findViewById(R.id.signUpEmail);
        signUpPassword = findViewById(R.id.signUpPassword);
        signUpBtn.setOnClickListener(this::SignUp);

    }

    public void openSignInPage(View view) {
        Intent intent = new Intent(this, login.class);
        startActivity(intent);
    }
    public boolean validatePassword(String password) {
        return password.length() >= 8;}
    public boolean validateEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    public boolean validateForm(String names, String email, String password) {

        if (names.isEmpty()) {
            signUpNames.setError("Please enter your name");
            return false;
        }else if (email.isEmpty()) {
            signUpEmail.setError("Please enter your email");
            return false;
        }else if (!validateEmail(email)) {
            signUpEmail.setError("Please enter a valid email");
            signUpEmail.setText("");
            return false;
        }else if (!validatePassword(password)) {
            signUpPassword.setError("Password must be at least 8 characters");
            signUpPassword.setText("");
            return false;
        }
        return true;
    }


    public void SignUp(View view) {
        String names = signUpNames.getText().toString();
        String email = signUpEmail.getText().toString();
        String password = signUpPassword.getText().toString();

        if (validateForm(names, email, password)){
            //Insertion of data to Database

            signUpNames.setText("");
            signUpEmail.setText("");
            signUpPassword.setText("");
        }


    }
}

