package com.openclassrooms.realestatemanager.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.openclassrooms.realestatemanager.models.*;
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

    public FirestoreRecyclerOptions<PropertyImage> getAllImagesByPropertyId(String propertyId){return propertyDataRepository.getAllImagesByPropertyId(propertyId);}

    /** INSERT **/

    public void createProperty(Property property){propertyDataRepository.createProperty(property);}

    public void insetAddressToProperty(String propertyId, Address address){propertyDataRepository.insertAddressToProperty(propertyId, address);}

    public void insertFeatureToProperty(String propertyId, PropertyFeature propertyFeature){propertyDataRepository.insertFeatureToProperty(propertyId, propertyFeature);}

    public void insertPointOfInterestToProperty(String propertyId, PointOfInterest pointOfInterest){propertyDataRepository.insertPointOfInterestToProperty(propertyId, pointOfInterest);}

    public void insertImageToProperty(String propertyId, PropertyImage propertyImage){propertyDataRepository.insertImageToProperty(propertyId, propertyImage);}

    /** GET ID **/

    public String getPropertyId(){return propertyDataRepository.getPropertyId();}

    public String getAddressId(String propertyId){return propertyDataRepository.getAddressId(propertyId);}

    public String getPropertyFeatureId(String propertyId){return propertyDataRepository.getPropertyFeatureId(propertyId);}

    /** DELETE **/

    public void resetPointOfInterest(String propertyId){propertyDataRepository.resetPointOfInterest(propertyId);}

    public void deleteImage(String propertyId, String propertyImageId){propertyDataRepository.deleteImage(propertyId, propertyImageId);}

    /** UPDATE **/

    public void updateLongitude(String propertyId, double longitude){ propertyDataRepository.updateLongitude(propertyId, longitude);}

    public void updateLatitude(String propertyId, double latitude){ propertyDataRepository.updateLatitude(propertyId, latitude);}

    public void updateLatLng(String propertyId, String addressCompact){propertyDataRepository.updateLatLng(propertyId, addressCompact);}

}
