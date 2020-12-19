package com.openclassrooms.realestatemanager.repository;

import android.util.Log;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.*;
import com.openclassrooms.realestatemanager.models.*;
import fr.juju.googlemaplibrary.repository.GooglePlaceRepository;

import java.util.List;

public class PropertyDataRepository {

    private final String COLLECTION_PROPERTY = "property";
    private final String COLLECTION_ADDRESS = "address";
    private final String COLLECTION_FEATURE = "propertyFeature";
    private final String COLLECTION_POINT_OF_INTEREST = "pointOfInterest";
    private final String COLLECTION_IMAGE = "propertyImage";
    private final GooglePlaceRepository googlePlaceRepository;
    private final LifecycleOwner owner;




    public PropertyDataRepository(GooglePlaceRepository googlePlaceRepository, LifecycleOwner owner) {
        this.googlePlaceRepository = googlePlaceRepository;
        this.owner = owner;
    }

    /** ***************************** **/
    /** ** Get Collection Method  *** **/
    /** ***************************** **/

    private CollectionReference getPropertyCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_PROPERTY);
    }

    private CollectionReference getSubCollection(String propertyId, String collectionName){
        return FirebaseFirestore.getInstance().collection(COLLECTION_PROPERTY).document(propertyId).collection(collectionName);
    }

    /** ***************************** **/
    /** ******* Get Id Method  ****** **/
    /** ***************************** **/

    public String getPropertyId(){
        DocumentReference reference = getPropertyCollection().document();
        return reference.getId();
    }

    public String getAddressId(String propertyId){
        DocumentReference reference = getSubCollection(propertyId, COLLECTION_ADDRESS).document();
        return reference.getId();
    }

    public String getPropertyFeatureId(String propertyId){
        DocumentReference reference = getSubCollection(propertyId, COLLECTION_FEATURE).document();
        return reference.getId();
    }

    /** ***************************** **/
    /** ******* Create Method  ****** **/
    /** ***************************** **/

    public Task<Void> createProperty(Property property){
        return getPropertyCollection().document(property.getPropertyId()).set(property);
    }

    public Task<Void> insertAddressToProperty(String propertyId, Address address){
        return getSubCollection(propertyId, COLLECTION_ADDRESS).document(address.getAddressId()).set(address);
    }

    public Task<Void> insertFeatureToProperty(String propertyId, PropertyFeature propertyFeature){
        return getSubCollection(propertyId, COLLECTION_FEATURE).document(propertyFeature.getPropertyFeatureId()).set(propertyFeature);
    }

    public Task<Void> insertPointOfInterestToProperty(String propertyId, PointOfInterest pointOfInterest){
        return getSubCollection(propertyId, COLLECTION_POINT_OF_INTEREST).document(pointOfInterest.getPointOfInterestId()).set(pointOfInterest);
    }

    public Task<Void> insertImageToProperty(String propertyId, PropertyImage propertyImage){
        DocumentReference reference = getPropertyCollection().document();
        String id = reference.getId();
        propertyImage.setPropertyImageId(id);
        return getSubCollection(propertyId, COLLECTION_IMAGE).document(id).set(propertyImage);
    }

    /** ***************************** **/
    /** ******** GET Method  ******** **/
    /** ***************************** **/

    public MutableLiveData<List<Property>> getAllProperty(){
        MutableLiveData<List<Property>> propertyList = new MutableLiveData<>();
        getPropertyCollection().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){

                propertyList.setValue(task.getResult().toObjects(Property.class));
            }else {
                propertyList.setValue(null);
            }
        });
        return propertyList;
    }

    public MutableLiveData<Property> getAPropertyById(String propertyId){
        MutableLiveData<Property> propertyLiveData = new MutableLiveData<>();
        getPropertyCollection().document(propertyId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()){
                        propertyLiveData.setValue(documentSnapshot.toObject(Property.class));
                    }else {
                        propertyLiveData.setValue(null);
                    }})
                .addOnFailureListener(e -> propertyLiveData.setValue(null));
        return propertyLiveData;
    }

    public MutableLiveData<Address> getPropertyAddressById(String propertyId){
        MutableLiveData<Address> addressLiveData = new MutableLiveData<>();
        getSubCollection(propertyId, COLLECTION_ADDRESS).whereEqualTo("propertyId", propertyId).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()){
                        addressLiveData.setValue(queryDocumentSnapshots.getDocuments().get(0).toObject(Address.class));
                    }else {
                        addressLiveData.setValue(null);
                    }})
                .addOnFailureListener(e -> addressLiveData.setValue(null));
        return addressLiveData;
    }

    public MutableLiveData<PropertyFeature> getPropertyFeatureById(String propertyId){
        MutableLiveData<PropertyFeature> propertyLiveData = new MutableLiveData<>();
        getSubCollection(propertyId, COLLECTION_FEATURE).whereEqualTo("propertyId", propertyId).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()){
                        propertyLiveData.setValue(queryDocumentSnapshots.getDocuments().get(0).toObject(PropertyFeature.class));
                    }else {
                        propertyLiveData.setValue(null);
                    }})
                .addOnFailureListener(e -> propertyLiveData.setValue(null));
        return propertyLiveData;
    }

    public MutableLiveData<List<PointOfInterest>> getPointOfInterestById(String propertyId){
        MutableLiveData<List<PointOfInterest>> pointOfInterestLiveData = new MutableLiveData<>();
        getSubCollection(propertyId, COLLECTION_POINT_OF_INTEREST).whereEqualTo("propertyId", propertyId).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()){
                        pointOfInterestLiveData.setValue(queryDocumentSnapshots.toObjects(PointOfInterest.class));
                    }else {
                        pointOfInterestLiveData.setValue(null);
                    }})
                .addOnFailureListener(e -> pointOfInterestLiveData.setValue(null));
        return pointOfInterestLiveData;
    }

    public FirestoreRecyclerOptions<PropertyImage> getAllImagesByPropertyId(String propertyId){
        Query query = getSubCollection(propertyId, COLLECTION_IMAGE).whereEqualTo("propertyId", propertyId);
        return new FirestoreRecyclerOptions.Builder<PropertyImage>()
                .setQuery(query, PropertyImage.class)
                .build();
    }

    /** ***************************** **/
    /** ****** DELETE Method  ******* **/
    /** ***************************** **/

    private Task<Void> deletePointOfInterest(String propertyId, String pointOfInterestId){
        return getSubCollection(propertyId, COLLECTION_POINT_OF_INTEREST).document(pointOfInterestId).delete();
    }

    public Task<Void> deleteImage(String propertyId, String propertyImageId){
        return getSubCollection(propertyId, COLLECTION_IMAGE).document(propertyImageId).delete();
    }

    public void resetPointOfInterest(String propertyId){
        getSubCollection(propertyId, COLLECTION_POINT_OF_INTEREST).whereEqualTo("propertyId", propertyId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        for (PointOfInterest pointOfInterest : task.getResult().toObjects(PointOfInterest.class)){
                            deletePointOfInterest(propertyId, pointOfInterest.getPointOfInterestId());
                        }
                    }
                });
    }

    /** ***************************** **/
    /** ******* Update Method  ****** **/
    /** ***************************** **/

    /** ******** Update LatLn  ****** **/
    public Task<Void> updateLongitude(String propertyId, double longitude) {
        return getPropertyCollection().document(propertyId).update("longitude", longitude);
    }
    public Task<Void> updateLatitude(String propertyId, double latitude) {
        return getPropertyCollection().document(propertyId).update("latitude", latitude);
    }

    public void updateLatLng(String propertyId, String addressCompact){
        Log.i("DEBUGGG", addressCompact);
        googlePlaceRepository.getGeocodePlaceByAddress(addressCompact).observe(owner, geocodePlace -> {
            Log.i("DEBUGGG", "inside");
            Log.i("DEBUGGG", String.valueOf(geocodePlace.getLng() +','+ geocodePlace.getLat()));
            this.updateLongitude(propertyId, geocodePlace.getLng());
            this.updateLatitude(propertyId, geocodePlace.getLat());
            Log.i("DEBUGGG", "finish");
        });

    }
}
