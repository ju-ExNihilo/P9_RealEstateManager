package com.openclassrooms.realestatemanager.models;

public class PropertyImage {

    private String propertyImageId;
    private String propertyId;
    private String imageUrl;
    private String imageDescription;

    public PropertyImage() {}

    public String getPropertyImageId() {
        return propertyImageId;
    }

    public void setPropertyImageId(String propertyImageId) {
        this.propertyImageId = propertyImageId;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageDescription() {
        return imageDescription;
    }

    public void setImageDescription(String imageDescription) {
        this.imageDescription = imageDescription;
    }

    @Override
    public String toString() {
        return "PropertyImage{" +
                "propertyImageId='" + propertyImageId + '\'' +
                ", propertyId='" + propertyId + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", imageDescription='" + imageDescription + '\'' +
                '}';
    }
}
