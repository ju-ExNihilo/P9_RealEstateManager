package com.openclassrooms.realestatemanager.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.openclassrooms.realestatemanager.models.PointOfInterest;

import java.util.List;

@Dao
public interface PointOfInterestDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertPointOfInterest(PointOfInterest pointOfInterest);

    @Query("DELETE FROM PointOfInterest WHERE id = :pointOfInterestId")
    int deletePointOfInterestForTest(long pointOfInterestId);

    @Query("SELECT * FROM PointOfInterest WHERE propertyId = :propertyId")
    LiveData<PointOfInterest> getAPointOfInterest(String propertyId);

    @Query("SELECT * FROM PointOfInterest")
    LiveData<List<PointOfInterest>> getAllPointOfInterestForTest();
}
