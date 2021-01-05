package com.openclassrooms.realestatemanager.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.openclassrooms.realestatemanager.models.Address;

@Dao
public interface AddressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertAddress(Address address);

}
