package com.openclassrooms.realestatemanager.dao;

import android.database.Cursor;
import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.openclassrooms.realestatemanager.models.Property;

import java.util.List;

@Dao
public interface PropertyDao {

    @Query("SELECT * FROM Property WHERE agentId = :agentId")
    LiveData<List<Property>> getAllProperty(String agentId);

    @Query("SELECT * FROM Property")
    LiveData<List<Property>> getAllPropertyProvider();

    @Query("SELECT * FROM Property")
    Cursor getPropertyWithCursor();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertProperty(Property property);

    @Query("UPDATE Property SET propertyPreviewImageUrl = :imageUrl WHERE propertyId = :propertyId")
    void updateProperty(String imageUrl, String propertyId);

    @Update
    int updatePropertyProvider(Property property);

    @Query("DELETE FROM Property WHERE propertyId = :propertyId")
    int deleteProperty(String propertyId);

    @Query("DELETE FROM Property WHERE id = :propertyId")
    int deletePropertyProvider(long propertyId);
}
