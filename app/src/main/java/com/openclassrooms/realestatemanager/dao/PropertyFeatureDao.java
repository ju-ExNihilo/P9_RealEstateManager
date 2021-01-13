package com.openclassrooms.realestatemanager.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.openclassrooms.realestatemanager.models.PropertyFeature;

import java.util.List;

@Dao
public interface PropertyFeatureDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertPropertyFeature(PropertyFeature propertyFeature);

    @Query("UPDATE PropertyFeature SET soldDate = :soldDate WHERE propertyId = :propertyId AND propertyFeatureId = :propertyFeatureId")
    void updateSoldDate(String soldDate, String propertyId, String propertyFeatureId);

    @Query("DELETE FROM PropertyFeature WHERE id = :propertyFeatureId")
    int deletePropertyFeatureForTest(long propertyFeatureId);

    @Query("SELECT * FROM PropertyFeature WHERE propertyId = :propertyId")
    LiveData<PropertyFeature> getAPropertyFeature(String propertyId);

    @Query("SELECT * FROM PropertyFeature")
    LiveData<List<PropertyFeature>> getAllPropertyFeatureForTest();
}
