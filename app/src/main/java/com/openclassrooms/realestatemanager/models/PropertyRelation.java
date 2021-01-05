package com.openclassrooms.realestatemanager.models;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class PropertyRelation {

    @Embedded
    private Property property;
    @Relation(parentColumn = "propertyId", entityColumn = "propertyId", entity = Address.class)
    private List<Address> address;
    @Relation(parentColumn = "propertyId", entityColumn = "propertyId", entity = PropertyFeature.class)
    private List<PropertyFeature> propertyFeatures;
    @Relation(parentColumn = "propertyId", entityColumn = "propertyId", entity = PointOfInterest.class)
    private List<PointOfInterest> pointOfInterests;
    @Relation(parentColumn = "propertyId", entityColumn = "propertyId", entity = PropertyImage.class)
    private List<PropertyImage> propertyImages;

    public PropertyRelation() {}

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public List<Address> getAddress() {
        return address;
    }

    public void setAddress(List<Address> address) {
        this.address = address;
    }

    public List<PropertyFeature> getPropertyFeatures() {
        return propertyFeatures;
    }

    public void setPropertyFeatures(List<PropertyFeature> propertyFeatures) {
        this.propertyFeatures = propertyFeatures;
    }

    public List<PointOfInterest> getPointOfInterests() {
        return pointOfInterests;
    }

    public void setPointOfInterests(List<PointOfInterest> pointOfInterests) {
        this.pointOfInterests = pointOfInterests;
    }

    public List<PropertyImage> getPropertyImages() {
        return propertyImages;
    }

    public void setPropertyImages(List<PropertyImage> propertyImages) {
        this.propertyImages = propertyImages;
    }

    @Override
    public String toString() {
        return "PropertyRelation{" +
                "property=" + property +
                ", address=" + address +
                ", propertyFeatures=" + propertyFeatures +
                ", pointOfInterests=" + pointOfInterests +
                ", propertyImages=" + propertyImages +
                '}';
    }
}
