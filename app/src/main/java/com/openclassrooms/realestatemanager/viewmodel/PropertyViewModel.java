package com.openclassrooms.realestatemanager.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.openclassrooms.realestatemanager.models.Address;
import com.openclassrooms.realestatemanager.models.PointOfInterest;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.models.PropertyFeature;
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

    public LiveData<PropertyFeature> getPropertyFeatureById(String propertyId){return propertyDataRepository.getPropertyFeatureById(propertyId);}

    public LiveData<List<PointOfInterest>> getPointOfInterestById(String propertyId){return propertyDataRepository.getPointOfInterestById(propertyId);}

    /** INSERT **/

    public void createProperty(Property property){propertyDataRepository.createProperty(property);}

    public void insetAddressToProperty(String propertyId, Address address){propertyDataRepository.insertAddressToProperty(propertyId, address);}

    public void insertFeatureToProperty(String propertyId, PropertyFeature propertyFeature){propertyDataRepository.insertFeatureToProperty(propertyId, propertyFeature);}

    public void insertPointOfInterestToProperty(String propertyId, PointOfInterest pointOfInterest){propertyDataRepository.insertPointOfInterestToProperty(propertyId, pointOfInterest);}

    /** GET ID **/

    public String getPropertyId(){return propertyDataRepository.getPropertyId();}

    public String getAddressId(String propertyId){return propertyDataRepository.getAddressId(propertyId);}

    public String getPropertyFeatureId(String propertyId){return propertyDataRepository.getPropertyFeatureId(propertyId);}

    /** DELETE **/

    public void resetPointOfInterest(String propertyId){propertyDataRepository.resetPointOfInterest(propertyId);}
}
