package com.openclassrooms.realestatemanager.viewmodel;

import android.net.Uri;
import androidx.lifecycle.LifecycleOwner;
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

    /** *********** Search  *********** **/
    public LiveData<List<Property>> searchMethod(String city, float minPrice, float maxPrice, float minSurface, float maxSurface, String dateStart,
                                                         List<String> finalPointOfInterest, int finalNumberOfPics, LifecycleOwner owner1){
        return propertyDataRepository.getDataFromSearch(city, minPrice, maxPrice, minSurface, maxSurface, dateStart, finalPointOfInterest, finalNumberOfPics, owner1);
    }

    public LiveData<List<Property>> searchMethodFromRoom(String city, float minPrice, float maxPrice, float minSurface, float maxSurface, String dateStart,
                                                 List<String> finalPointOfInterest, int finalNumberOfPics, LifecycleOwner owner1) {
        return propertyDataRepository.searchMethodFromRoom(city, minPrice, maxPrice, minSurface, maxSurface, dateStart, finalPointOfInterest, finalNumberOfPics, owner1);
    }
    /** **************************** **/

    public LiveData<List<Property>> getAllProperty(){return propertyDataRepository.getAllProperty();}

    public LiveData<Integer> getMaxSurface(){return propertyDataRepository.getMaxSurface();}



    public LiveData<Property> getAPropertyById(String propertyId){return propertyDataRepository.getAPropertyById(propertyId);}

    public LiveData<Address> getPropertyAddressById(String propertyId){return propertyDataRepository.getPropertyAddressById(propertyId);}

    public LiveData<PropertyFeature> getPropertyFeatureById(String propertyId){return propertyDataRepository.getPropertyFeatureById(propertyId);}

    public LiveData<List<PointOfInterest>> getPointOfInterestById(String propertyId){return propertyDataRepository.getPointOfInterestById(propertyId);}

    public LiveData<List<PropertyImage>> getAllImagesByPropertyIdForDetails(String propertyId){return propertyDataRepository.getAllImagesByPropertyIdForDetails(propertyId);}

    public FirestoreRecyclerOptions<PropertyImage> getAllImagesByPropertyId(String propertyId){return propertyDataRepository.getAllImagesByPropertyId(propertyId);}

    /** *********** Room  *********** **/
    public LiveData<List<Property>> getAllPropertyFromRoom(){return propertyDataRepository.getAllPropertyFromRoom();}
    /** **************************** **/

    /** INSERT **/

    public void createProperty(Property property){propertyDataRepository.createProperty(property);}

    public void insetAddressToProperty(String propertyId, Address address){propertyDataRepository.insertAddressToProperty(propertyId, address);}

    public void insertFeatureToProperty(String propertyId, PropertyFeature propertyFeature){propertyDataRepository.insertFeatureToProperty(propertyId, propertyFeature);}

    public void insertImageToProperty(String propertyId, PropertyImage propertyImage){propertyDataRepository.insertImageToProperty(propertyId, propertyImage);}

    /** **************************** **/

    /** GET ID **/

    public String getPropertyId(){return propertyDataRepository.getPropertyId();}

    public String getAddressId(String propertyId){return propertyDataRepository.getAddressId(propertyId);}

    public String getPropertyFeatureId(String propertyId){return propertyDataRepository.getPropertyFeatureId(propertyId);}

    public String getPropertyImageId(String propertyId){return propertyDataRepository.getPropertyImageId(propertyId);}

    /** DELETE **/

    public void deleteImage(String propertyId, String propertyImageId){propertyDataRepository.deleteImage(propertyId, propertyImageId);}

    public void deleteFromRoom(String propertyId){propertyDataRepository.deleteFromRoom(propertyId);}

    /** UPDATE **/

    public void uploadImageInFirebase(String propertyId, Uri uriImage){ propertyDataRepository.uploadImageInFirebase(propertyId, uriImage);}

    public void uploadImageInFirebase(String propertyId, PropertyImage propertyImage){ propertyDataRepository.uploadImageInFirebase(propertyId, propertyImage);}

    public void updateLatLng(String propertyId, String addressCompact){propertyDataRepository.updateLatLng(propertyId, addressCompact);}

    public void getProximityPointOfInterest(String location, String propertyId){propertyDataRepository.getProximityPointOfInterest(location, propertyId);}

    public void propertySale(String propertyId, String documentId, String soldDate){propertyDataRepository.propertySale(propertyId, documentId, soldDate);}

}
