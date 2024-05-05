package com.example.parkingapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;

import okhttp3.*;

public class signup extends AppCompatActivity {

    Button signUpBtn;
    EditText signUpuserId;
    EditText signUpEmail;
    EditText signUpPassword;
    RadioGroup userType;
    TextView errorMessage;

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
        signUpuserId = findViewById(R.id.signUpUserId);
        signUpEmail = findViewById(R.id.signUpEmail);
        signUpPassword = findViewById(R.id.signUpPassword);
        userType = findViewById(R.id.userTypeSelection);
        errorMessage = findViewById(R.id.errorMessage);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUp();
            }
        });

    }

    public void openSignInPage(View view) {
        Intent intent = new Intent(this, login.class);
        startActivity(intent);
    }
    public boolean validatePassword(String password) {
        return password.length() >= 8 && password.length() <= 16;}
    public boolean validateEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public boolean validateUserId(String userId) {
        return userId.length() == 7;
    }

    public boolean validateUserType(int userTypeId) {
        return userTypeId != -1;
    }

    public boolean validateForm(String userIdString, String email, String password, int userTypeId) {

        if (userIdString.isEmpty()) {
            signUpuserId.setError("Please enter your Person Number");
            return false;
        }
        else if (!validateUserId(userIdString)) {
            signUpuserId.setError("Please enter a valid Person Number");
            signUpuserId.setText("");
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
        }else if (!validateUserType(userTypeId)) {
            errorMessage.setVisibility(View.VISIBLE);
            errorMessage.setText("Please select a user type");
            return false;
        }
        return true;
    }


    private void SignUp() {
        String userIdString = signUpuserId.getText().toString();
        String email = signUpEmail.getText().toString();
        String password = signUpPassword.getText().toString();
        int selectedId = userType.getCheckedRadioButtonId();
        String uType;

        if(selectedId == R.id.student){
            uType = "Student";
        }
        else{
            uType = "Staff";
        }

        if (validateForm(userIdString, email, password, selectedId)){
            //Insertion of data to Database

            String url = "https://lamp.ms.wits.ac.za/home/s2691450/signup.php";

            AsyncTask.execute(() -> {
                OkHttpClient client = new OkHttpClient();

                RequestBody requestBody = new FormBody.Builder()
                        .add("userIdString", userIdString)
                        .add("password", password)
                        .add("userType", uType)
                        .build();

                try{
                    Request request = new Request.Builder()
                            .url(url)
                            .post(requestBody)
                            .build();

                    Response response = client.newCall(request).execute();

                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();
                        if(responseBody.equals("success")){
                            runOnUiThread(() -> {
                                Toast.makeText(getApplicationContext(), "Account created successfully", Toast.LENGTH_SHORT).show();
                                userSessionManager sessionManager = new userSessionManager(this);
                                sessionManager.createSession(userIdString,uType,email,password);
                                //intent to navigation drawer
                                Intent intent = new Intent(this, navigationDrawer.class);
                                startActivity(intent);
                            });
                        } else if(responseBody.equals("failed")){
                            runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Failed to create an account", Toast.LENGTH_SHORT).show());
                        }
                        else if(responseBody.equals("exists")){
                            runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Account already exists. Sign in", Toast.LENGTH_SHORT).show());
                        }
                    }else{
                        runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Failed to connect to the database", Toast.LENGTH_SHORT).show());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });



            errorMessage.setVisibility(View.GONE);
            userType.clearCheck();
            signUpuserId.setText("");
            signUpEmail.setText("");
            signUpPassword.setText("");
        }
    }
}

