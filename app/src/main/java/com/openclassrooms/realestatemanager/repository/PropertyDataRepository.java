package com.openclassrooms.realestatemanager.repository;

import androidx.lifecycle.MutableLiveData;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;
import com.openclassrooms.realestatemanager.models.Address;
import com.openclassrooms.realestatemanager.models.PointOfInterest;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.models.PropertyFeature;

import java.util.List;

public class PropertyDataRepository {

    private final String COLLECTION_PROPERTY = "property";
    private final String COLLECTION_ADDRESS = "address";
    private final String COLLECTION_FEATURE = "propertyFeature";
    private final String COLLECTION_POINT_OF_INTEREST = "pointOfInterest";



    public PropertyDataRepository() {}

    /** ***************************** **/
    /** ** Get Collection Method  *** **/
    /** ***************************** **/

    private CollectionReference getPropertyCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_PROPERTY);
    }

    private CollectionReference getAddressCollection(String propertyId){
        return FirebaseFirestore.getInstance().collection(COLLECTION_PROPERTY).document(propertyId).collection(COLLECTION_ADDRESS);
    }

    private CollectionReference getFeatureCollection(String propertyId){
        return FirebaseFirestore.getInstance().collection(COLLECTION_PROPERTY).document(propertyId).collection(COLLECTION_FEATURE);
    }

    private CollectionReference getPointOfInterestCollection(String propertyId){
        return FirebaseFirestore.getInstance().collection(COLLECTION_PROPERTY).document(propertyId).collection(COLLECTION_POINT_OF_INTEREST);
    }

    /** ***************************** **/
    /** ******* Get Id Method  ****** **/
    /** ***************************** **/

    public String getPropertyId(){
        DocumentReference reference = getPropertyCollection().document();
        return reference.getId();
    }

    public String getAddressId(String propertyId){
        DocumentReference reference = getAddressCollection(propertyId).document();
        return reference.getId();
    }

    public String getPropertyFeatureId(String propertyId){
        DocumentReference reference = getFeatureCollection(propertyId).document();
        return reference.getId();
    }

    /** ***************************** **/
    /** ******* Create Method  ****** **/
    /** ***************************** **/

    public Task<Void> createProperty(Property property){
        return getPropertyCollection().document(property.getPropertyId()).set(property);
    }

    public Task<Void> insertAddressToProperty(String propertyId, Address address){
        return getAddressCollection(propertyId).document(address.getAddressId()).set(address);
    }

    public Task<Void> insertFeatureToProperty(String propertyId, PropertyFeature propertyFeature){
        return getFeatureCollection(propertyId).document(propertyFeature.getPropertyFeatureId()).set(propertyFeature);
    }

    public Task<Void> insertPointOfInterestToProperty(String propertyId, PointOfInterest pointOfInterest){
        return getPointOfInterestCollection(propertyId).document(pointOfInterest.getPointOfInterestId()).set(pointOfInterest);
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
        getAddressCollection(propertyId).whereEqualTo("propertyId", propertyId).get()
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
        getFeatureCollection(propertyId).whereEqualTo("propertyId", propertyId).get()
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
        getPointOfInterestCollection(propertyId).whereEqualTo("propertyId", propertyId).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()){
                        pointOfInterestLiveData.setValue(queryDocumentSnapshots.toObjects(PointOfInterest.class));
                    }else {
                        pointOfInterestLiveData.setValue(null);
                    }})
                .addOnFailureListener(e -> pointOfInterestLiveData.setValue(null));
        return pointOfInterestLiveData;
    }

    /** ***************************** **/
    /** ****** DELETE Method  ******* **/
    /** ***************************** **/

    private Task<Void> deletePointOfInterest(String propertyId, String pointOfInterestId){
        return getPointOfInterestCollection(propertyId).document(pointOfInterestId).delete();
    }

    public void resetPointOfInterest(String propertyId){
        getPointOfInterestCollection(propertyId).whereEqualTo("propertyId", propertyId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        for (PointOfInterest pointOfInterest : task.getResult().toObjects(PointOfInterest.class)){
                            deletePointOfInterest(propertyId, pointOfInterest.getPointOfInterestId());
                        }
                    }
                });
    }
}
