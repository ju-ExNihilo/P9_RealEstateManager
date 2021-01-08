package com.openclassrooms.realestatemanager.models;

import androidx.annotation.NonNull;
import androidx.room.*;

@Entity(foreignKeys = @ForeignKey(entity = Property.class, parentColumns = "propertyId", childColumns = "propertyId", onDelete = ForeignKey.CASCADE),
        indices = {@Index("propertyId")})
public class PointOfInterest {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id", index = true)
    private long id;
    private String pointOfInterestId;
    @ColumnInfo(name = "propertyId")
    private String propertyId;
    private String pointOfInterestName;

    public PointOfInterest() {}

    @Ignore
    public PointOfInterest(long id, String pointOfInterestId, String propertyId, String pointOfInterestName) {
        this.id = id;
        this.pointOfInterestId = pointOfInterestId;
        this.propertyId = propertyId;
        this.pointOfInterestName = pointOfInterestName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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
