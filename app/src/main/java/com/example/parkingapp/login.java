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
    }
    public void toast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    public void storeToSharedPreferences(String userIdString,String email,String password,String uType){
        userSessionManager sessionManager = new userSessionManager(this);
        sessionManager.createSession(userIdString,uType,email,password);
    }
    public void SQLReq(String userIdString, String password){

        String url = "https://lamp.ms.wits.ac.za/home/s2691450/login.php";

        AsyncTask.execute(()->{
            OkHttpClient client = new OkHttpClient();

            RequestBody requestBody = new FormBody.Builder()
                    .add("userIdString", userIdString)
                    .add("password", password)
                    .build();

            try{
                Request request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();

                Response response = client.newCall(request).execute();



                if(response.isSuccessful()){

                    JSONObject jsonObject = new JSONObject(response.toString());

                    String returnedUserID = jsonObject.getString("USER_ID");
                    String returnedPassword = jsonObject.getString("PASSWORD");
                    String returnedUserType = jsonObject.getString("USER_TYPE");
                    String returnedEmail = jsonObject.getString("EMAIL");
                    String returnedOutcome = jsonObject.getString("OUTCOME");


                    if(returnedOutcome.equals("exists")){
                        runOnUiThread(() -> {
                            toast("Logged in Successful");
                            openNavDrawer();
                        });
                    }else if(returnedOutcome.equals("does not exist")){
                        runOnUiThread(()-> toast("User does not exist") );
                    }
                }else{
                    runOnUiThread(()-> toast("Failed to connect to database") );
                }
            }catch(IOException | JSONException e){
                e.printStackTrace();
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