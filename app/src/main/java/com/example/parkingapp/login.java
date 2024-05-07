package com.example.parkingapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.*;

public class login extends AppCompatActivity {

    Button logInBtn ;
    EditText logInUserId;
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
        logInUserId = findViewById(R.id.logInUserId);
        logInPassword = findViewById(R.id.logInPassword);
        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn();
            }
        });


    }


    public void openSignUpPage(View view){
        Intent intent = new Intent(this, signup.class);
        startActivity(intent);
    }

    public boolean validateUserId(String userId) {
        return userId.length() == 7;
    }

    public boolean validatePassword(String password){
        return password.length() >= 8;}

    public boolean validateForm(String userIdString, String password){
        if(userIdString.isEmpty()){
            logInUserId.setError("Person Number is required");
            return false;
        }
        else if (!validateUserId(userIdString)) {
            logInUserId.setError("Please enter a valid Person Number");
            logInUserId.setText("");
            return false;
        }
        else if(password.isEmpty()){
            logInPassword.setError("Password is required");
            return false;
        }
        else if(!validatePassword(password)){
            logInPassword.setError("Password must be at least 8 characters");
            return false;
        }
        return true;
    }

    public void openNavDrawer(){
        Intent intent = new Intent(this, navigationDrawer.class);
        startActivity(intent);
        finish();
    }
    public void toast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    public void storeToSharedPreferences(String userIdString,String email,String password,String uType){
        userSessionManager sessionManager = new userSessionManager(this);
        sessionManager.createSession(userIdString,uType,email,password);
    }
    public void SQLReq(String userIdString, String password) {
        OkHttpClient client = new OkHttpClient();

        String parseUrl = "https://lamp.ms.wits.ac.za/home/s2691450/login.php";
        HttpUrl.Builder urlBuilder = HttpUrl.parse(parseUrl).newBuilder();
        urlBuilder.addQueryParameter("userIdString", userIdString);
        urlBuilder.addQueryParameter("password", password);
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                // Handle failure here
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    String responseData = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        final String email = jsonObject.getString("EMAIL");
                        final String uType = jsonObject.getString("USER_TYPE");
                        final String userId = jsonObject.getString("USER_ID");
                        final String password = jsonObject.getString("PASSWORD");
                        final String outcome = jsonObject.getString("OUTCOME");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (outcome.equals("exists")) {
                                    toast("Login Successful");
                                    storeToSharedPreferences(userId,email,password,uType);
                                    openNavDrawer();
                                }else{
                                    toast("User" + userId + " does not exist");
                                }

                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                }
            }
        });
    }


    private void logIn(){

        String userIdString = logInUserId.getText().toString();
        String password = logInPassword.getText().toString();

        if(validateForm(userIdString, password)){
            //Retrieval of data from database
            SQLReq(userIdString, password);

            logInUserId.setText("");
            logInPassword.setText("");
        }
    }


}