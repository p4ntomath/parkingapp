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

    public void openNavigationDrawer() {
        Intent intent = new Intent(this, navigationDrawer.class);
        startActivity(intent);
    }
    public void storeToSharedPreferences(String userIdString,String email,String password,String uType){
        userSessionManager sessionManager = new userSessionManager(this);
        sessionManager.createSession(userIdString,uType,email,password);
    }
    public void toast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    public void SQLReq(String userIdString, String email, String password, String uType){

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
                            toast("Account created successfully");
                            storeToSharedPreferences(userIdString,email,password,uType);
                            openNavigationDrawer();
                        });
                    } else if(responseBody.equals("failed")){
                        runOnUiThread(()->{
                            toast("Failed to create an account");
                        });

                    }
                    else if(responseBody.equals("exists")){
                        runOnUiThread(()-> {
                            toast("Account already exists. Sign in");
                        });
                    }
                }else{
                    runOnUiThread(()->
                            toast("Failed to create an account"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    private void SignUp() {
        String userIdString = signUpuserId.getText().toString();
        String email = signUpEmail.getText().toString();
        String password = signUpPassword.getText().toString();
        int selectedId = userType.getCheckedRadioButtonId();
        String uType;

        if(selectedId == R.id.student){
            uType = "Student";}
        else{
            uType = "Staff";}

        if (validateForm(userIdString, email, password, selectedId)){

            //Insertion of data to Database
            SQLReq(userIdString,email,password,uType);

            errorMessage.setVisibility(View.GONE);
            userType.clearCheck();
            signUpuserId.setText("");
            signUpEmail.setText("");
            signUpPassword.setText("");
        }
    }
}

