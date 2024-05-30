package com.example.parkingapp;





import android.content.Context;
import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class nearByParkings {
    private List<parkingModel> parkings;
    private static final double EARTH_RADIUS = 6371000;
    Context context;
    List<Pair<Double, Double>> coord = new ArrayList<>();
    public nearByParkings(Context context) {
        parkings = new ArrayList<>();
        this.context = context;

        try {
            addAllCoordinates();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

    public void addAllCoordinates() throws JSONException {
        String json = readJSONFromRaw(R.raw.parkings);
        JSONObject jsonObject = new JSONObject(json);
        JSONArray parkingsArray = jsonObject.getJSONArray("Parkings");
        for(int i=0;i<parkingsArray.length();i++){
            JSONObject parking = parkingsArray.getJSONObject(i);
            double lat = parking.getDouble("Latitude");
            double lon = parking.getDouble("Longitude");
            coord.add(new Pair<>(lat,lon));
        }
    }

    public void addToParking(int pos) throws JSONException {

        String json = readJSONFromRaw(R.raw.parkings);
        JSONObject jsonObject = new JSONObject(json);
        JSONArray parkingsArray = jsonObject.getJSONArray("Parkings");
        JSONObject parking = parkingsArray.getJSONObject(pos);
        String name = parking.getString("Name");
        String parkingType = parking.getString("Type");
        int parkingCapacity = parking.getInt("Capacity");
        String Location = parking.getString("Location");
        String lotID = parking.getString("Lot_ID");

        parkings.add(new parkingModel(parkingType,name,Location,parkingCapacity,0,0,R.drawable.parkinglot));
    }


     public List<parkingModel> getParkings() {
        return parkings;
     }


    // Convert degrees to radians
    private static double toRadians(double degrees) {
        return Math.toRadians(degrees);
    }

    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = toRadians(lat2 - lat1);
        double dLon = toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(toRadians(lat1)) * Math.cos(toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

    public static boolean areLocationsNear(double lat1, double lon1, double lat2, double lon2, double thresholdDistance) {
        double distance = calculateDistance(lat1, lon1, lat2, lon2);
        return distance <= thresholdDistance;
    }

    public void compareLocations(double lat, double lon) throws JSONException {
        double thresholdDistance = 300;
        int i = 0;
        for(Pair<Double,Double> parking : coord){
            boolean isNear = areLocationsNear(lat, lon, parking.first, parking.second, thresholdDistance);
            if(isNear){
                addToParking(i);
            }
            i++;
        }


    }



    private String readJSONFromRaw(int rawResourceId) {
        StringBuilder stringBuilder = new StringBuilder();
        try (InputStream inputStream = context.getResources().openRawResource(rawResourceId);
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }


}
