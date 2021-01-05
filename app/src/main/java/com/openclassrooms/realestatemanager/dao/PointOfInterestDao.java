package com.openclassrooms.realestatemanager.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import com.openclassrooms.realestatemanager.models.PointOfInterest;

@Dao
public interface PointOfInterestDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertPointOfInterest(PointOfInterest pointOfInterest);
}
