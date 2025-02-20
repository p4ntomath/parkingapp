package com.example.parkingapp;

import static android.app.PendingIntent.getActivity;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class bookingManager {


    Quartet<Integer, Integer, Integer, Integer> bookedspot;
    String parkingName;
    Context context;
    String entryTime, exitTime;

    public bookingManager(Context context, Quartet<Integer, Integer, Integer, Integer> bookedspot, String parkingName, String entryTime, String exitTime) {
        this.bookedspot = bookedspot;
        this.parkingName = parkingName;
        this.context = context;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
    }

    public bookingManager(Context context) {
        this.context = context;
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

    public String conversion(Quartet<Integer, Integer, Integer, Integer> bookedspot) {
        char capital = 'A';
        int asciiValue = (int) capital;
        asciiValue += bookedspot.getFirst();
        char block = (char) asciiValue;
        int pattern = (bookedspot.getSecond() + 1) * 2;
        String spot = "";
        if (bookedspot.getThird() == 1) {
            spot = block + String.valueOf(pattern - 1);
        } else {
            spot = block + String.valueOf(pattern);
        }
        return spot;
    }


    public String getlotID(String parkingName) {
        GlobalData globalData = GlobalData.getInstance();
        HashMap<String, String> map = globalData.getNameToLotIDMap();
        String lotID = map.get(parkingName);

        return lotID;
    }

    public void addToSharedPreferences() {
        BookingSession bookingSession = new BookingSession(context);
        String spot = conversion(bookedspot);
        String lotID = getlotID(parkingName);
        int image = bookedspot.getFourth();
        bookingSession.bookParkingSpot(spot, parkingName, lotID, entryTime, image);
        bookingSession.setLeavingTime(exitTime);
    }


    public CompletableFuture<Boolean> insertToDatabase() {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        userSessionManager userSessionManager = new userSessionManager(context);

        String userId = userSessionManager.getUserId();
        String LotID = getlotID(parkingName);
        String Spot = conversion(bookedspot);
        String entryTime = this.entryTime;
        String exitTime = this.exitTime;

        OkHttpClient client = new OkHttpClient();

        String parseUrl = "https://lamp.ms.wits.ac.za/home/s2691450/booking.php";
        HttpUrl.Builder urlBuilder = HttpUrl.parse(parseUrl).newBuilder();
        urlBuilder.addQueryParameter("userId", userId);
        urlBuilder.addQueryParameter("lotId", LotID);
        urlBuilder.addQueryParameter("spot", Spot);
        urlBuilder.addQueryParameter("entryTime", entryTime);
        urlBuilder.addQueryParameter("exitTime", exitTime);
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                ((Activity) context).runOnUiThread(() -> {
                    showToast("Failed to connect to server");
                    future.complete(false);
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    ((Activity) context).runOnUiThread(() -> {
                        showToast("Failed to connect to server");
                        future.complete(false);
                    });
                    return;
                }

                try {
                    String responseBody = response.body().string();

                    if (responseBody.equals("success")) {
                        future.complete(true);
                    } else {
                        future.complete(false);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        return future;
    }


    public CompletableFuture<Boolean> deleteFromDatabase() {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        userSessionManager userSessionManager = new userSessionManager(context);
        String userId = userSessionManager.getUserId();

        OkHttpClient client = new OkHttpClient();

        String parseUrl = "https://lamp.ms.wits.ac.za/home/s2691450/deleteBooking.php";
        HttpUrl.Builder urlBuilder = HttpUrl.parse(parseUrl).newBuilder();
        urlBuilder.addQueryParameter("userId", userId);
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                ((Activity) context).runOnUiThread(() -> {
                    showToast("Failed to connect to server");
                    future.complete(false);
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    ((Activity) context).runOnUiThread(() -> {
                        showToast("Failed to connect");
                        future.complete(false);
                    });
                    return;
                }

                try {
                    String responseBody = response.body().string();

                    if (responseBody.equals("success")) {
                        BookingSession bookingSession = new BookingSession(context);
                        bookingSession.deleteBooking();
                        future.complete(true);
                    } else if (responseBody.equals("no booking")) {
                        BookingSession bookingSession = new BookingSession(context);
                        bookingSession.deleteBooking();
                        future.complete(true);
                    } else {
                        future.complete(false);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        return future;
    }


    public List<String> getBookedSpots(String lotId) {
        List<String> bookedSpots = new ArrayList<>();

        CompletableFuture<String[]> future = fetchDataFromDatabase(lotId);

        try {
            String[] spotNumbers = future.get();
            for (String spotNumber : spotNumbers) {
                bookedSpots.add(spotNumber);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return bookedSpots;
    }

    public CompletableFuture<String[]> fetchDataFromDatabase(String lotId) {
        CompletableFuture<String[]> future = new CompletableFuture<>();

        OkHttpClient client = new OkHttpClient();

        String parseUrl = "https://lamp.ms.wits.ac.za/home/s2691450/spotsOfLot.php";
        HttpUrl.Builder urlBuilder = HttpUrl.parse(parseUrl).newBuilder();
        urlBuilder.addQueryParameter("lotId", lotId);
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                ((Activity) context).runOnUiThread(() -> {
                    showToast("Failed to connect to server");
                    future.completeExceptionally(e);
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (!response.isSuccessful()) {
                        ((Activity) context).runOnUiThread(() -> {
                            showToast("Failed to connect");
                        });
                    }
                    String responseData = response.body().string();
                    JSONArray jsonArray = new JSONArray(responseData);
                    String[] resultArray = new String[jsonArray.length()];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        resultArray[i] = jsonArray.getString(i);
                    }
                    future.complete(resultArray);
                } catch (JSONException e) {
                    future.completeExceptionally(e);
                }
            }
        });

        return future;
    }

    public CompletableFuture<Boolean> updateExitTime(String exitTime) {
        userSessionManager userSessionManager = new userSessionManager(context);
        String userId = userSessionManager.getUserId();

        CompletableFuture<Boolean> future = new CompletableFuture<>();
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://lamp.ms.wits.ac.za/home/s2691450/updateExitTime.php").newBuilder();
        urlBuilder.addQueryParameter("userId", userId);
        urlBuilder.addQueryParameter("exitTime", exitTime);
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    boolean isSuccess = responseBody.equals("success");
                    future.complete(isSuccess);
                } else {
                    ((Activity) context).runOnUiThread(() -> {
                        showToast("Failed: " + response.code() + " " + response.message());
                    });
                    future.complete(false);
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                ((Activity) context).runOnUiThread(() -> {
                    showToast("Failed to connect: " + e.getMessage());
                });
                future.complete(false);
            }
        });

        return future;
    }

}





