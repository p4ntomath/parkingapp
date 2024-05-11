package com.example.parkingapp;

public class item {
    String parkingName;
    String parkingSpace;
    int image;

    public item(String parkingName, String parkingSpace, int image) {
        this.parkingName = parkingName;
        this.parkingSpace = parkingSpace;
        this.image = image;
    }

    public String getParkingName() {
        return parkingName;
    }

    public String getParkingSpace() {
        return parkingSpace;
    }

    public int getParkingImage() {
        return image;
    }
    public void setParkingName(String parkingName) {
        this.parkingName = parkingName;
    }
    public void setParkingSpace(String parkingSpace) {
        this.parkingSpace = parkingSpace;
    }
    public void setImage(int image) {
        this.image = image;
    }



}
