package com.example.parkingapp;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class forgotPasswordManager {

    private Context context;
    private EditText email;
    private EditText otp;
    private EditText password, confirmPassword;
    private EditText setPasswordEmail;
    private getStarted getStartedActivity;
    private Boolean account_exists = false;

    private String currentOTP;

    public forgotPasswordManager(Context context, EditText email, EditText otp) {
        this.context = context;
        this.email = email;
        this.otp = otp;
    }

    public forgotPasswordManager(EditText password, EditText confirmPassword, EditText setPasswordEmail, Context context, getStarted getStartedActivity) {
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.context = context;
        this.getStartedActivity = getStartedActivity;
        this.setPasswordEmail = setPasswordEmail;
    }

    public CompletableFuture<Boolean> validateEmail() {
        String emailInput = email.getText().toString();
        if (emailInput.isEmpty()) {
            email.setError("Email is required");
            return CompletableFuture.completedFuture(false);
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            email.setError("Invalid Email");
            return CompletableFuture.completedFuture(false);
        }

        return checkEmailExistsAndSend(emailInput)
                .thenApply(accountExists -> {
                    if (accountExists) {
                        // Account exists
                    } else {
                        // Account does not exist
                    }
                    return accountExists;
                })
                .exceptionally(ex -> {
                    // Handle exceptions here
                    ex.printStackTrace();
                    return false;
                });
    }


    public boolean validateOtp() {
        String otpInput = otp.getText().toString();
        if (otpInput.isEmpty()) {
            otp.setError("Otp is required");
            return false;
        } else if (otpInput.length() != 5) {
            otp.setError("Invalid Otp");
            return false;
        }

        if (otpInput.equals(currentOTP)) {
            return true;
        } else {
            otp.setError("Incorrect Otp");
            return false;
        }
    }

    public boolean validatePassword() {
        String newPass = password.getText().toString();
        String confirmPass = confirmPassword.getText().toString();

        if (newPass.isEmpty()) {
            password.setError("Password is required");
            return false;
        } else if (newPass.length() < 8) {
            password.setError("Password must be at least 8 characters");
            return false;
        }
        if (confirmPass.isEmpty()) {
            confirmPassword.setError("Confirm Password is required");
            return false;
        } else if (!newPass.equals(confirmPass)) {
            confirmPassword.setError("Passwords do not match");
            return false;
        }
        return true;
    }

    public void resetPassword() {
        if(validatePassword()){
            updateDatabase();
        }
    }

    private void updateDatabase() {
        String updatePasswordEmail = setPasswordEmail.getText().toString();
        String newPassword = password.getText().toString();

        String url = "https://lamp.ms.wits.ac.za/home/s2691450/resetPassword.php";

        AsyncTask.execute(() -> {
            OkHttpClient client = new OkHttpClient();

            RequestBody requestBody = new FormBody.Builder()
                    .add("password",  newPassword)
                    .add("email", updatePasswordEmail)
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
                        showToastOnUiThread("Password Reset Successfully");
                        ((Activity) context).runOnUiThread(() -> {
                            getStartedActivity.openSignInPage();
                        });
                    } else if (responseBody.equals("failed")) {
                        showToastOnUiThread("Failed to update Password");
                    }
                } else {
                    showToastOnUiThread("Failed to update Password");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    private CompletableFuture<Boolean> checkEmailExistsAndSend(String email) {
        OkHttpClient client = new OkHttpClient();
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        String parseUrl = "https://lamp.ms.wits.ac.za/home/s2691450/generateOTP.php";
        HttpUrl.Builder urlBuilder = HttpUrl.parse(parseUrl).newBuilder();
        urlBuilder.addQueryParameter("email", email);
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                future.completeExceptionally(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    future.complete(false);
                    return;
                }

                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    String outcome = jsonObject.getString("OUTCOME");

                    if (outcome.equals("success")) {
                        currentOTP = jsonObject.getString("OTP");
                        sendEmail sendEmail = new sendEmail();
                        sendEmail.send(email, currentOTP);
                        showToastOnUiThread("OTP sent");
                        future.complete(true);
                    } else {
                        showToastOnUiThread("Account does not exist");
                        future.complete(false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    future.completeExceptionally(e);
                }
            }
        });

        return future;
    }


    private void showToast(String message) {
        Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void showToastOnUiThread(final String message) {
        ((Activity) context).runOnUiThread(() -> showToast(message));
    }
}
