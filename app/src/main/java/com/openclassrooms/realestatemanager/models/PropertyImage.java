package com.openclassrooms.realestatemanager.models;

import androidx.annotation.NonNull;
import androidx.room.*;

@Entity(foreignKeys = @ForeignKey(entity = Property.class, parentColumns = "propertyId", childColumns = "propertyId", onDelete = ForeignKey.CASCADE),
        indices = {@Index("propertyId")})
public class PropertyImage {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id", index = true)
    private long id;
    private String propertyImageId;
    @ColumnInfo(name = "propertyId")
    private String propertyId;
    private String imageUrl;
    private String imageDescription;

    public PropertyImage() {}

    public PropertyImage(long id, String propertyImageId, String propertyId, String imageUrl, String imageDescription) {
        this.id = id;
        this.propertyImageId = propertyImageId;
        this.propertyId = propertyId;
        this.imageUrl = imageUrl;
        this.imageDescription = imageDescription;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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
