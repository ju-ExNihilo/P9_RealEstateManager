package com.openclassrooms.realestatemanager.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.openclassrooms.realestatemanager.models.Address;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.repository.PropertyDataRepository;
import java.util.List;

public class PropertyViewModel extends ViewModel {

    private final PropertyDataRepository propertyDataRepository;

    public PropertyViewModel(PropertyDataRepository propertyDataRepository) {
        this.propertyDataRepository = propertyDataRepository;
    }

    /** GET **/

    public LiveData<List<Property>> getAllProperty(){return propertyDataRepository.getAllProperty();}

    public LiveData<Property> getAPropertyById(String propertyId){return propertyDataRepository.getAPropertyById(propertyId);}

    public LiveData<Address> getPropertyAddressById(String propertyId){return propertyDataRepository.getPropertyAddressById(propertyId);}

    /** INSERT **/

    public void createProperty(Property property){propertyDataRepository.createProperty(property);}

    public void insetAddressToProperty(String propertyId, Address address){propertyDataRepository.insertAddressToProperty(propertyId, address);}
}
