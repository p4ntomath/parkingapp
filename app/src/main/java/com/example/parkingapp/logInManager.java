package com.example.parkingapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class logInManager {

    private Context context;
    private EditText logInUserId;
    private EditText logInPassword;

    public logInManager(Context context, EditText logInUserId, EditText logInPassword) {
        this.context = context;
        this.logInUserId = logInUserId;
        this.logInPassword = logInPassword;
    }

    public void logIn() {
        String userIdString = logInUserId.getText().toString();
        String password = logInPassword.getText().toString();

        if (validateForm(userIdString, password)) {
            SQLReq(userIdString, password);
            logInUserId.setText("");
            logInPassword.setText("");
        }
    }

    private boolean validateForm(String userIdString, String password) {
        if (userIdString.isEmpty()) {
            logInUserId.setError("Person Number is required");
            return false;
        } else if (!validateUserId(userIdString)) {
            logInUserId.setError("Please enter a valid Person Number");
            logInUserId.setText("");
            return false;
        } else if (password.isEmpty()) {
            logInPassword.setError("Password is required");
            return false;
        } else if (!validatePassword(password)) {
            logInPassword.setError("Password must be at least 8 characters");
            return false;
        }
        return true;
    }

    private boolean validateUserId(String userId) {
        return userId.length() == 7;
    }

    private boolean validatePassword(String password) {
        return password.length() >= 8;
    }

    private void showToast(String message) {
        Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void showToastOnUiThread(String message) {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToast(message);
            }
        });
    }

    private void storeToSharedPreferences(String userIdString,String uType) {
        userSessionManager sessionManager = new userSessionManager(context);
        sessionManager.createSession(userIdString, uType);
    }

    private void SQLReq(String userIdString, String password) {
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
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast("Failed to connect to server");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    showToast("Failed to connect to server");
                    return;
                }

                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    String outcome = jsonObject.getString("OUTCOME");

                    if (outcome.equals("exists")) {
                        showToastOnUiThread("Login Successful");
                        String uType = jsonObject.getString("USER_TYPE");
                        String userId = jsonObject.getString("USER_ID");

                        storeToSharedPreferences(userId, uType);
                        openNavigationDrawer();
                    } else {
                        showToastOnUiThread("User does not exist");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    public void openNavigationDrawer() {
        Intent intent = new Intent(context, navigationDrawer.class);
        context.startActivity(intent);
        ((Activity) context).finish();
    }
}
