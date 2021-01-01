package com.openclassrooms.realestatemanager.models;

public class PointOfInterest {

    private String pointOfInterestId;
    private String propertyId;
    private String pointOfInterestName;

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

    public String getPointOfInterestName() {
        return pointOfInterestName;
    }

    public void setPointOfInterestName(String pointOfInterestName) {
        this.pointOfInterestName = pointOfInterestName;
    }

    @Override
    public String toString() {
        return "PointOfInterest{" +
                "pointOfInterestId='" + pointOfInterestId + '\'' +
                ", propertyId='" + propertyId + '\'' +
                ", getPointOfInterestName='" + pointOfInterestName + '\'' +
                '}';
    }
}
