package com.openclassrooms.realestatemanager.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.openclassrooms.realestatemanager.models.PropertyFeature;

@Dao
public interface PropertyFeatureDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertPropertyFeature(PropertyFeature propertyFeature);

    @Query("UPDATE PropertyFeature SET soldDate = :soldDate WHERE propertyId = :propertyId AND propertyFeatureId = :propertyFeatureId")
    void updateSoldDate(String soldDate, String propertyId, String propertyFeatureId);
}
