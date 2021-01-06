package com.openclassrooms.realestatemanager.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.openclassrooms.realestatemanager.models.Address;
import com.openclassrooms.realestatemanager.models.PropertyImage;

import java.util.List;

@Dao
public interface PropertyImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertPropertyImage(PropertyImage propertyImage);

    @Query("UPDATE PropertyImage SET imageDescription = :description WHERE propertyId = :propertyId AND propertyImageId = :propertyImageId")
    void updateImageDescription(String description, String propertyId, String propertyImageId);

    @Query("DELETE FROM PropertyImage WHERE id = :propertyImageId")
    int deletePropertyImageForTest(long propertyImageId);

    @Query("SELECT * FROM PropertyImage WHERE propertyId = :propertyId")
    LiveData<PropertyImage> getAPropertyImage(String propertyId);

    @Query("SELECT * FROM PropertyImage")
    LiveData<List<PropertyImage>> getAllPropertyImageForTest();

}
