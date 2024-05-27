package com.example.parkingapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class signUpManager {

    private Context context;
    private EditText signUpuserId;
    private EditText signUpEmail;
    private EditText signUpPassword;
    private RadioGroup userType;
    private TextView errorMessage;

    public signUpManager(Context context, EditText signUpuserId, EditText signUpEmail,
                         EditText signUpPassword, RadioGroup userType, TextView errorMessage) {
        this.context = context;
        this.signUpuserId = signUpuserId;
        this.signUpEmail = signUpEmail;
        this.signUpPassword = signUpPassword;
        this.userType = userType;
        this.errorMessage = errorMessage;
    }

    public void signUp() {
        String userIdString = signUpuserId.getText().toString();
        String email = signUpEmail.getText().toString();
        String password = signUpPassword.getText().toString();
        int selectedId = userType.getCheckedRadioButtonId();
        String uType = (selectedId == R.id.student) ? "Student" : "Staff";

        if (validateForm(userIdString, email, password, selectedId)) {
            SQLReq(userIdString, email, password, uType);
        }
    }

    private boolean validateUserId(String userId) {
        return userId.length() == 7;
    }

    private boolean validatePassword(String password) {
        return password.length() >= 8;
    }

    private boolean validateEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean validateUserType(int userTypeId) {
        return userTypeId != -1;
    }

    private boolean validateForm(String userIdString, String email, String password, int userTypeId) {
        if (userIdString.isEmpty()) {
            signUpuserId.setError("Please enter your Person Number");
            return false;
        } else if (!validateUserId(userIdString)) {
            signUpuserId.setError("Please enter a valid Person Number");
            signUpuserId.setText("");
            return false;
        } else if (email.isEmpty()) {
            signUpEmail.setError("Please enter your email");
            return false;
        } else if (!validateEmail(email)) {
            signUpEmail.setError("Please enter a valid email");
            signUpEmail.setText("");
            return false;
        } else if (!validatePassword(password)) {
            signUpPassword.setError("Password must be at least 8 characters");
            signUpPassword.setText("");
            return false;
        } else if (!validateUserType(userTypeId)) {
            errorMessage.setVisibility(View.VISIBLE);
            errorMessage.setText("Please select a user type");
            return false;
        }
        return true;
    }

    private void showToast(String message) {
        Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void showToastOnUiThread(String message) {
        ((Activity) context).runOnUiThread(new Runnable() {@Override
        public void run() {
            showToast(message);
        }
        });
    }

    private void storeToSharedPreferences(String userIdString, String uType) {
        userSessionManager sessionManager = new userSessionManager(context);
        sessionManager.createSession(userIdString, uType);
    }

    private void openNavigationDrawer() {
        Intent intent = new Intent(context, navigationDrawer.class);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    private void SQLReq(String userIdString, String email, String password, String uType) {
        String url = "https://lamp.ms.wits.ac.za/home/s2691450/signup.php";

        AsyncTask.execute(() -> {
            OkHttpClient client = new OkHttpClient();

            RequestBody requestBody = new FormBody.Builder()
                    .add("userIdString", userIdString)
                    .add("password", password)
                    .add("userType", uType)
                    .add("email", email)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();

            try {
                Response response = client.newCall(request).execute();

                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    if (responseBody.equals("success")) {
                        showToastOnUiThread("Account created successfully");
                        storeToSharedPreferences(userIdString, uType);
                        openNavigationDrawer();
                    }else if(responseBody.equals("could not connect")){
                        showToastOnUiThread("Failed to connect to server");
                    }
                    else if (responseBody.equals("failed")) {
                        showToastOnUiThread("Failed to create an account");
                    } else if (responseBody.equals("exists")) {
                        showToastOnUiThread("Account already exists. Sign in");
                    }
                    else if(responseBody.equals("userid exists")) {
                        showToastOnUiThread("Account with this UserID already exists. Sign in");
                    }
                    else if(responseBody.equals("email exists")) {
                        showToastOnUiThread("Account with this email already exists. Sign in");
                    }
                } else {
                    showToastOnUiThread("Server couldn't respond");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
