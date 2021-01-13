package com.openclassrooms.realestatemanager.dao;

import android.database.Cursor;
import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.models.PropertyRelation;

import java.util.List;

@Dao
public interface PropertyDao {

    @Query("SELECT * FROM Property")
    LiveData<List<Property>> getAllProperty();

    @Transaction
    @Query("SELECT * FROM Property")
    LiveData<List<PropertyRelation>> getAllPropertyForSearch();

    @Query("SELECT * FROM Property WHERE propertyId = :propertyId")
    LiveData<Property> getAProperty(String propertyId);

    @Query("SELECT * FROM Property " +
            "LEFT JOIN Address ON Address.propertyId = Property.propertyId " +
            "LEFT JOIN PropertyFeature ON PropertyFeature.propertyId = Property.propertyId " +
            "LEFT JOIN PointOfInterest ON PointOfInterest.propertyId = Property.propertyId " +
            "LEFT JOIN PropertyImage ON PropertyImage.propertyId = Property.propertyId")
    Cursor getPropertyWithCursor();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertProperty(Property property);

    @Query("UPDATE Property SET propertyPreviewImageUrl = :imageUrl WHERE propertyId = :propertyId")
    void updatePreviewImageUrl(String imageUrl, String propertyId);

    @Query("UPDATE Property SET latitude = :latitude WHERE propertyId = :propertyId")
    void updateLatitude(double latitude, String propertyId);

    @Query("UPDATE Property SET longitude = :longitude WHERE propertyId = :propertyId")
    void updateLongitude(double longitude, String propertyId);

    @Query("UPDATE Property SET isSold = :isSold WHERE propertyId = :propertyId")
    void updateSold(boolean isSold, String propertyId);

    @Query("DELETE FROM Property WHERE propertyId = :propertyId")
    int deleteProperty(String propertyId);

    @Query("DELETE FROM Property WHERE id = :propertyId")
    int deletePropertyForTest(long propertyId);
}
