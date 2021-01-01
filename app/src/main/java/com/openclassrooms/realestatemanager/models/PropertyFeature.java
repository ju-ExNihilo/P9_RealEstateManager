package com.openclassrooms.realestatemanager.models;

import java.util.Date;

public class PropertyFeature {

    private String propertyFeatureId;
    private String propertyId;
    private int numberOfRooms, numberOfBathrooms, numberOfBedrooms;
    private String entranceDate, saleDate;
    private float propertySurface;
    private String propertyDescription;

    public PropertyFeature() {}

    public String getPropertyFeatureId() {
        return propertyFeatureId;
    }

    public void setPropertyFeatureId(String propertyFeatureId) {
        this.propertyFeatureId = propertyFeatureId;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public int getNumberOfBathrooms() {
        return numberOfBathrooms;
    }

    public void setNumberOfBathrooms(int numberOfBathrooms) {
        this.numberOfBathrooms = numberOfBathrooms;
    }

    public int getNumberOfBedrooms() {
        return numberOfBedrooms;
    }

    public void setNumberOfBedrooms(int numberOfBedrooms) {
        this.numberOfBedrooms = numberOfBedrooms;
    }

    public String getEntranceDate() {
        return entranceDate;
    }

    public void setEntranceDate(String entranceDate) {
        this.entranceDate = entranceDate;
    }

    public String getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(String saleDate) {
        this.saleDate = saleDate;
    }

    public float getPropertySurface() {
        return propertySurface;
    }

    public void setPropertySurface(float propertySurface) {
        this.propertySurface = propertySurface;
    }


    public String getPropertyDescription() {
        return propertyDescription;
    }

    public void setPropertyDescription(String propertyDescription) {
        this.propertyDescription = propertyDescription;
    }

    @Override
    public String toString() {
        return "PropertyFeature{" +
                "propertyFeatureId='" + propertyFeatureId + '\'' +
                ", propertyId='" + propertyId + '\'' +
                ", numberOfRooms=" + numberOfRooms +
                ", numberOfBathrooms=" + numberOfBathrooms +
                ", numberOfBedrooms=" + numberOfBedrooms +
                ", entranceDate=" + entranceDate +
                ", saleDate=" + saleDate +
                ", propertySurface=" + propertySurface +
                ", propertyDescription='" + propertyDescription + '\'' +
                '}';
    }
}