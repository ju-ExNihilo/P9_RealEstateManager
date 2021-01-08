package com.openclassrooms.realestatemanager.models;

import java.util.Date;
import java.util.List;

public class FeatureForSearch {

    private String propertyId;
    private String location;
    private float surface;
    private Date entranceDate;
    private List<String> pointOfInterest;
    private float price;
    private int numberOfPics;

    public FeatureForSearch() {}

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public float getSurface() {
        return surface;
    }

    public void setSurface(float surface) {
        this.surface = surface;
    }

    public Date getEntranceDate() {
        return entranceDate;
    }

    public void setEntranceDate(Date entranceDate) {
        this.entranceDate = entranceDate;
    }

    public List<String> getPointOfInterest() {
        return pointOfInterest;
    }

    public void setPointOfInterest(List<String> pointOfInterest) {
        this.pointOfInterest = pointOfInterest;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getNumberOfPics() {
        return numberOfPics;
    }

    public void setNumberOfPics(int numberOfPics) {
        this.numberOfPics = numberOfPics;
    }

    @Override
    public String toString() {
        return "FeatureForSearch{" +
                "propertyId='" + propertyId + '\'' +
                ", location='" + location + '\'' +
                ", surface=" + surface +
                ", entranceDate=" + entranceDate +
                ", pointOfInterest=" + pointOfInterest +
                ", price=" + price +
                ", numberOfPics=" + numberOfPics +
                '}';
    }
}
