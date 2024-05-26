package com.example.parkingapp;

import java.util.HashMap;

public class GlobalData {
    private static GlobalData instance;
    private HashMap<String, String> nameToLotIDMap;

    private GlobalData() {
        // Initialize the HashMap
        nameToLotIDMap = new HashMap<>();
    }

    public static synchronized GlobalData getInstance() {
        if (instance == null) {
            instance = new GlobalData();
        }
        return instance;
    }

    public HashMap<String, String> getNameToLotIDMap() {
        return nameToLotIDMap;
    }

    // Method to add to the global map
    public void addToGlobalMap(HashMap<String, String> map) {
        nameToLotIDMap.putAll(map);
    }
}

