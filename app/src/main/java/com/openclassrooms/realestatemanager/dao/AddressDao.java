package com.openclassrooms.realestatemanager.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.openclassrooms.realestatemanager.models.Address;
import com.openclassrooms.realestatemanager.models.Property;

import java.util.List;

@Dao
public interface AddressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertAddress(Address address);

    @Query("DELETE FROM Address WHERE id = :addressId")
    int deleteAddressForTest(long addressId);

    @Query("SELECT * FROM Address WHERE propertyId = :propertyId")
    LiveData<Address> getAAddress(String propertyId);

    @Query("SELECT * FROM Address")
    LiveData<List<Address>> getAllAddressForTest();

}
