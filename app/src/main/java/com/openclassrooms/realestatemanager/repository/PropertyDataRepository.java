package com.openclassrooms.realestatemanager.repository;

import android.net.Uri;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.*;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.openclassrooms.realestatemanager.database.PropertyDatabase;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.models.*;
import com.openclassrooms.realestatemanager.utils.Utils;
import fr.juju.googlemaplibrary.repository.GooglePlaceRepository;

import java.util.List;
import java.util.concurrent.Executor;

public class PropertyDataRepository {

    private final String COLLECTION_PROPERTY = "property";
    private final String COLLECTION_ADDRESS = "address";
    private final String COLLECTION_FEATURE = "propertyFeature";
    private final String COLLECTION_POINT_OF_INTEREST = "pointOfInterest";
    private final String COLLECTION_IMAGE = "propertyImage";
    private final GooglePlaceRepository googlePlaceRepository;
    private final LifecycleOwner owner;
    private final PropertyDatabase propertyDatabase;
    private Executor executor;

    public PropertyDataRepository(GooglePlaceRepository googlePlaceRepository, LifecycleOwner owner, PropertyDatabase propertyDatabase, Executor executor) {
        this.googlePlaceRepository = googlePlaceRepository;
        this.owner = owner;
        this.propertyDatabase = propertyDatabase;
        this.executor = executor;
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

    public String getPropertyImageId(String propertyId){
        DocumentReference reference = getSubCollection(propertyId, COLLECTION_IMAGE).document();
        return reference.getId();
    }

    /** ***************************** **/
    /** ******* Create Method  ****** **/
    /** ***************************** **/

    public Task<Void> createProperty(Property property){
        executor.execute(() -> propertyDatabase.propertyDao().insertProperty(property));
        return getPropertyCollection().document(property.getPropertyId()).set(property);
    }


    public Task<Void> insertAddressToProperty(String propertyId, Address address){
        executor.execute(() -> propertyDatabase.addressDao().insertAddress(address));
        return getSubCollection(propertyId, COLLECTION_ADDRESS).document(address.getAddressId()).set(address);
    }

    public Task<Void> insertFeatureToProperty(String propertyId, PropertyFeature propertyFeature){
        executor.execute(() -> propertyDatabase.propertyFeatureDao().insertPropertyFeature(propertyFeature));
        return getSubCollection(propertyId, COLLECTION_FEATURE).document(propertyFeature.getPropertyFeatureId()).set(propertyFeature);
    }

    public Task<Void> insertPointOfInterestToProperty(String propertyId, PointOfInterest pointOfInterest){
        executor.execute(() -> propertyDatabase.pointOfInterestDao().insertPointOfInterest(pointOfInterest));
        return getSubCollection(propertyId, COLLECTION_POINT_OF_INTEREST).document(pointOfInterest.getPointOfInterestId()).set(pointOfInterest);
    }

    public Task<Void> insertImageToProperty(String propertyId, PropertyImage propertyImage){
        executor.execute(() -> propertyDatabase.propertyImageDao().insertPropertyImage(propertyImage));
        return getSubCollection(propertyId, COLLECTION_IMAGE).document(propertyImage.getPropertyImageId()).set(propertyImage);
    }

    public void uploadImageInFirebase(String propertyId, Uri uriImage){
        String uuid = Utils.randomUUID();
        StorageReference mImageRef = FirebaseStorage.getInstance().getReference(uuid);
        UploadTask uploadTask = mImageRef.putFile(uriImage);
        uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return mImageRef.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri downloadUri = task.getResult();
                updateImageUrl(propertyId, downloadUri.toString());
                PropertyImage propertyImage = new PropertyImage();
                propertyImage.setImageUrl(downloadUri.toString());
                propertyImage.setImageDescription("Preview");
                propertyImage.setPropertyId(propertyId);
                propertyImage.setPropertyImageId(getPropertyImageId(propertyId));
                insertImageToProperty(propertyId, propertyImage);
            }
        });
    }

    public void uploadImageInFirebase(String propertyId, PropertyImage propertyImage){
        String uuid = Utils.randomUUID();
        StorageReference mImageRef = FirebaseStorage.getInstance().getReference(uuid);
        Uri uriImage = Uri.parse(propertyImage.getImageUrl());
        UploadTask uploadTask = mImageRef.putFile(uriImage);
        uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return mImageRef.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri downloadUri = task.getResult();
                propertyImage.setImageUrl(downloadUri.toString());
                insertImageToProperty(propertyId, propertyImage);
            }
        });
    }

    public void getProximityPointOfInterest(String location, String propertyId){
        AgentRepository agentRepository = Injection.provideAgentRepository();
        agentRepository.getAgentFromFirestore().observe(owner, agent -> {
            for (String pointOfInterest : agent.getProximityPointOfInterestChoice()){
                googlePlaceRepository.getPlace(location, 2000, pointOfInterest, "none").observe(owner, finalPlaces -> {
                    if (finalPlaces.size() > 0){
                        PointOfInterest pointOfInterest1 = new PointOfInterest();
                        pointOfInterest1.setPointOfInterestName(pointOfInterest);
                        pointOfInterest1.setPropertyId(propertyId);
                        pointOfInterest1.setPointOfInterestId(pointOfInterest +"Id");
                        insertPointOfInterestToProperty(propertyId, pointOfInterest1);
                    }
                });
            }
        });
    }

    /** ***************************** **/
    /** ******** GET Method  ******** **/
    /** ***************************** **/

    /** *********** Room  *********** **/

    public LiveData<List<Property>> getAllPropertyFromRoom(String agentId){return propertyDatabase.propertyDao().getAllProperty(agentId);}

    public LiveData<List<Property>> getAllPropertyFromRoomForTest(){return propertyDatabase.propertyDao().getAllPropertyProvider();}

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

    public MutableLiveData<List<PropertyImage>> getAllImagesByPropertyIdForDetails(String propertyId){
        MutableLiveData<List<PropertyImage>> propertyImagesLiveData = new MutableLiveData<>();
        getSubCollection(propertyId, COLLECTION_IMAGE).whereEqualTo("propertyId", propertyId).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()){
                        propertyImagesLiveData.setValue(queryDocumentSnapshots.toObjects(PropertyImage.class));
                    }else {
                        propertyImagesLiveData.setValue(null);
                    }})
                .addOnFailureListener(e -> propertyImagesLiveData.setValue(null));
        return propertyImagesLiveData;
    }

    /** ***************************** **/
    /** ****** DELETE Method  ******* **/
    /** ***************************** **/

    public Task<Void> deleteImage(String propertyId, String propertyImageId){
        return getSubCollection(propertyId, COLLECTION_IMAGE).document(propertyImageId).delete();
    }

    /** ******** Room  ****** **/
    public void deleteFromRoom(String propertyId){
        executor.execute(() -> propertyDatabase.propertyDao().deleteProperty(propertyId));
    }

    /** ***************************** **/
    /** ******* UPDATE Method  ****** **/
    /** ***************************** **/

    /** ******** Update LatLn  ****** **/
    private Task<Void> updateLongitude(String propertyId, double longitude) {
        executor.execute(() -> propertyDatabase.propertyDao().updateLongitude(longitude, propertyId));
        return getPropertyCollection().document(propertyId).update("longitude", longitude);
    }
    private Task<Void> updateLatitude(String propertyId, double latitude) {
        executor.execute(() -> propertyDatabase.propertyDao().updateLatitude(latitude, propertyId));
        return getPropertyCollection().document(propertyId).update("latitude", latitude);
    }

    public Task<Void> updateImageUrl(String propertyId, String imageUrl) {
        executor.execute(() -> propertyDatabase.propertyDao().updatePreviewImageUrl(imageUrl, propertyId));
        return getPropertyCollection().document(propertyId).update("propertyPreviewImageUrl", imageUrl);
    }

    public Task<Void> updateImageDescription(String propertyId, String propertyImageId, String description) {
        executor.execute(() -> propertyDatabase.propertyImageDao().updateImageDescription(description, propertyId, propertyImageId));
        return getSubCollection(propertyId, COLLECTION_IMAGE).document(propertyImageId).update("imageDescription", description);
    }

    private Task<Void> updateSoldProperty(String propertyId){
        executor.execute(() -> propertyDatabase.propertyDao().updateSold(true, propertyId));
        return getPropertyCollection().document(propertyId).update("sold", true);
    }

    private Task<Void> updateSoldDate(String propertyId, String documentId, String soldDate){
        executor.execute(() -> propertyDatabase.propertyFeatureDao().updateSoldDate(soldDate, propertyId, documentId));
        return getSubCollection(propertyId, COLLECTION_FEATURE).document(documentId).update("soldDate", soldDate);
    }

    public void updateLatLng(String propertyId, String addressCompact){
        googlePlaceRepository.getGeocodePlaceByAddress(addressCompact).observe(owner, geocodePlace -> {
            if (geocodePlace != null){
                this.updateLongitude(propertyId, geocodePlace.getLng());
                this.updateLatitude(propertyId, geocodePlace.getLat());
            }else {
                this.updateLongitude(propertyId, 0.0);
                this.updateLatitude(propertyId, 0.0);
            }

        });
    }

    public void propertySale(String propertyId, String documentId, String saleDate){
        this.updateSoldProperty(propertyId);
        this.updateSoldDate(propertyId, documentId, saleDate);
    }
}
