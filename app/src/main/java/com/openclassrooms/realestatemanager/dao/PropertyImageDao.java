package com.openclassrooms.realestatemanager.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.openclassrooms.realestatemanager.models.PropertyImage;

@Dao
public interface PropertyImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertPropertyImage(PropertyImage propertyImage);

    @Query("UPDATE PropertyImage SET imageDescription = :description WHERE propertyId = :propertyId AND propertyImageId = :propertyImageId")
    void updateImageDescription(String description, String propertyId, String propertyImageId);

}
