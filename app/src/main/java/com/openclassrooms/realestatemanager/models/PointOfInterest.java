package com.openclassrooms.realestatemanager.models;

public class PointOfInterest {

    private String pointOfInterestId;
    private String propertyId;
    private String getPointOfInterestName;

    public PointOfInterest() {}

    public String getPointOfInterestId() {
        return pointOfInterestId;
    }

    public void setPointOfInterestId(String pointOfInterestId) {
        this.pointOfInterestId = pointOfInterestId;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getGetPointOfInterestName() {
        return getPointOfInterestName;
    }

    public void setGetPointOfInterestName(String getPointOfInterestName) {
        this.getPointOfInterestName = getPointOfInterestName;
    }

    @Override
    public String toString() {
        return "PointOfInterest{" +
                "pointOfInterestId='" + pointOfInterestId + '\'' +
                ", propertyId='" + propertyId + '\'' +
                ", getPointOfInterestName='" + getPointOfInterestName + '\'' +
                '}';
    }
}
