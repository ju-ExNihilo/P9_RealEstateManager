package com.openclassrooms.realestatemanager.models;

import android.content.ContentValues;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Property {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long id;
    private String propertyId;
    private String agentId;
    private String propertyType;
    private String propertyLocatedCity;
    private float propertyPrice;
    private String propertyPreviewImageUrl;
    private double latitude, longitude;
    private boolean isSale;

    public Property() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getPropertyLocatedCity() {
        return propertyLocatedCity;
    }

    public void setPropertyLocatedCity(String propertyLocatedCity) {
        this.propertyLocatedCity = propertyLocatedCity;
    }

    public float getPropertyPrice() {
        return propertyPrice;
    }

    public void setPropertyPrice(float propertyPrice) {
        this.propertyPrice = propertyPrice;
    }

    public String getPropertyPreviewImageUrl() {
        return propertyPreviewImageUrl;
    }

    public void setPropertyPreviewImageUrl(String propertyPreviewImageUrl) {
        this.propertyPreviewImageUrl = propertyPreviewImageUrl;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean isSale() {
        return isSale;
    }

    public void setSale(boolean sale) {
        this.isSale = sale;
    }

    public static Property fromContentValues(ContentValues contentValues){
        final Property property = new Property();
        if (contentValues.containsKey("id")) property.setId(contentValues.getAsLong("id"));
        if (contentValues.containsKey("propertyId")) property.setPropertyId(contentValues.getAsString("propertyId"));
        if (contentValues.containsKey("agentId")) property.setAgentId(contentValues.getAsString("agentId"));
        if (contentValues.containsKey("propertyType")) property.setPropertyType(contentValues.getAsString("propertyType"));
        if (contentValues.containsKey("propertyLocatedCity")) property.setPropertyLocatedCity(contentValues.getAsString("propertyLocatedCity"));
        if (contentValues.containsKey("propertyPrice")) property.setPropertyPrice(contentValues.getAsFloat("propertyPrice"));
        if (contentValues.containsKey("propertyPreviewImageUrl")) property.setPropertyPreviewImageUrl(contentValues.getAsString("propertyPreviewImageUrl"));
        if (contentValues.containsKey("latitude")) property.setLatitude(contentValues.getAsDouble("latitude"));
        if (contentValues.containsKey("longitude")) property.setLongitude(contentValues.getAsDouble("longitude"));
        if (contentValues.containsKey("isSale")) property.setSale(contentValues.getAsBoolean("isSale"));
        return property;
    }

    @Override
    public String toString() {
        return "Property{" +
                "propertyId='" + propertyId + '\'' +
                ", agentId='" + agentId + '\'' +
                ", propertyType='" + propertyType + '\'' +
                ", propertyLocatedCity='" + propertyLocatedCity + '\'' +
                ", propertyPrice=" + propertyPrice +
                ", propertyPreviewImageUrl='" + propertyPreviewImageUrl + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", isSale=" + isSale +
                '}';
    }
}
