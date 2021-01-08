package com.openclassrooms.realestatemanager.models;

import androidx.annotation.NonNull;
import androidx.room.*;


@Entity(foreignKeys = @ForeignKey(entity = Property.class, parentColumns = "propertyId", childColumns = "propertyId", onDelete = ForeignKey.CASCADE),
        indices = {@Index("propertyId")})
public class PropertyFeature {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id", index = true)
    private long id;
    private String propertyFeatureId;
    @ColumnInfo(name = "propertyId")
    private String propertyId;
    private int numberOfRooms, numberOfBathrooms, numberOfBedrooms;
    private String entranceDate, soldDate;
    private float propertySurface;
    private String propertyDescription;

    public PropertyFeature() {}

    @Ignore
    public PropertyFeature(long id, String propertyFeatureId, String propertyId, int numberOfRooms,
                           int numberOfBathrooms, int numberOfBedrooms, String entranceDate, String soldDate, float propertySurface, String propertyDescription) {
        this.id = id;
        this.propertyFeatureId = propertyFeatureId;
        this.propertyId = propertyId;
        this.numberOfRooms = numberOfRooms;
        this.numberOfBathrooms = numberOfBathrooms;
        this.numberOfBedrooms = numberOfBedrooms;
        this.entranceDate = entranceDate;
        this.soldDate = soldDate;
        this.propertySurface = propertySurface;
        this.propertyDescription = propertyDescription;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public String getSoldDate() {
        return soldDate;
    }

    public void setSoldDate(String soldDate) {
        this.soldDate = soldDate;
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
                ", saleDate=" + soldDate +
                ", propertySurface=" + propertySurface +
                ", propertyDescription='" + propertyDescription + '\'' +
                '}';
    }
}