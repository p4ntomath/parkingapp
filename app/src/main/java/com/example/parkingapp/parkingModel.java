package com.example.parkingapp;

import java.util.Objects;

public class parkingModel {

    private String parkingName;
    private String location;
    private int totalSpaces;
    private int bookedSpaces;
    private int favoriteSpaces;
    private int image;
    private String parkingType;
    // You can add more fields as needed

    public parkingModel(String parkingType,String parkingName, String location, int totalSpaces, int bookedSpaces, int favoriteSpaces, int image) {
        this.parkingName = parkingName;
        this.location = location;
        this.totalSpaces = totalSpaces;
        this.bookedSpaces = bookedSpaces;
        this.favoriteSpaces = favoriteSpaces;
        this.image = image;
        this.parkingType = parkingType;
    }

    public String getParkingType() {
        return parkingType;
    }
    public int getImage() {
        return image;
    }
    public String getParkingName() {
        return parkingName;
    }

    public void setParkingName(String parkingName) {
        this.parkingName = parkingName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getTotalSpaces() {
        return totalSpaces;
    }

    public void setTotalSpaces(int totalSpaces) {
        this.totalSpaces = totalSpaces;
    }

    public int getBookedSpaces() {
        return bookedSpaces;
    }

    public void setBookedSpaces(int bookedSpaces) {
        this.bookedSpaces = bookedSpaces;
    }

    public int getFavoriteSpaces() {
        return favoriteSpaces;
    }

    public void setFavoriteSpaces(int favoriteSpaces) {
        this.favoriteSpaces = favoriteSpaces;
    }
    public void setImages(int image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        parkingModel that = (parkingModel) o;
        return Objects.equals(parkingName, that.parkingName) &&
                Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parkingName, location);
    }
}
