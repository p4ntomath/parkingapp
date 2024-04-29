package com.example.parkingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

public class getStarted extends AppCompatActivity {
    Button signUpPage;
    TextView signInText = findViewById(R.id.SignInPage);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_get_started);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        signUpPage = findViewById(R.id.signUpPage);
        signUpPage.setOnClickListener(v -> openSignUpPage(v));
        signInText.setOnClickListener(v -> openLoginPage(v));
    }

    public void openSignUpPage(View view) {
        Intent intent = new Intent(this, signup.class);
        startActivity(intent);
    }
    public void openLoginPage(View view) {
        Intent intent = new Intent(this, login.class);
        startActivity(intent);
    }

}