package com.example.parkingapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class whereto_fragment extends Fragment implements onCardViewSelected {


SearchView searchView;
RecyclerView recyclerView;
TextView noParking;
List<parkingModel> parkings = new ArrayList<>();
Map<String, Pair<Double, Double>> placeToCoordinates = new HashMap<>();
List<String>namePlaces = new ArrayList<>();

    public whereto_fragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.whereto_fragment, container, false);
        parkings.add(new parkingModel("Students","Barnato","West Campus",20,0,0,R.drawable.parkinglot));

        searchView = view.findViewById(R.id.whereToSearch);
        noParking = view.findViewById(R.id.whereToNoParking);
        recyclerView = view.findViewById(R.id.whereToRecyclerView);
        onSearch();

        try {
            addToMap();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return view;
    }

    private void addToMap() throws JSONException {
        String json = readJSONFromRaw(R.raw.placesatmain);
        JSONArray jsonArray = new JSONArray(json);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject place = jsonArray.getJSONObject(i);
            String name = place.getString("name");
            double lat = place.getDouble("latitude");
            double lon = place.getDouble("longitude");
            placeToCoordinates.put(name,new Pair<>(lat,lon));
            namePlaces.add(name);
        }
    }

    public void onSearch(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                try {
                    compareWithQuery(query);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void compareWithQuery(String query) throws JSONException {
        nearByParkings near = new nearByParkings(getContext());
        query = query.toLowerCase().trim();
        HashSet<parkingModel> searched = new HashSet<>();
        for(String name : namePlaces){
            String name2 = name.toLowerCase().trim();
            if(name2.contains(query)){
                double lat = placeToCoordinates.get(name).first;
                double lon = placeToCoordinates.get(name).second;
                near.compareLocations(lat,lon);
                parkings = near.getParkings();
                searched.addAll(parkings);
            }
            parkings.clear();
            parkings.addAll(searched);
        }
        if(parkings.isEmpty()){
            noParking.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else{
            noParking.setVisibility(View.GONE);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(new findParkingAdapter(parkings,getContext(),this));
        }

    }


    private String readJSONFromRaw(int rawResourceId) {
        StringBuilder stringBuilder = new StringBuilder();
        try (InputStream inputStream = getResources().openRawResource(rawResourceId);
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

    @Override
    public void onCardViewSelected(String parkingName, String parkingSpace, String parkingType) {


    }
}