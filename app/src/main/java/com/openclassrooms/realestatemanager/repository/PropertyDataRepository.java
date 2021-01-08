package com.openclassrooms.realestatemanager.repository;

import android.net.Uri;
import android.util.Log;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

public class PropertyDataRepository {

    private final String COLLECTION_PROPERTY = "property";
    private final String COLLECTION_SEARCH = "featureForSearch";
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

    private CollectionReference getSearchCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_SEARCH);
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
        insertPropertyForSearch(property);
        return getPropertyCollection().document(property.getPropertyId()).set(property);
    }

    public Task<Void> insertAddressToProperty(String propertyId, Address address){
        executor.execute(() -> propertyDatabase.addressDao().insertAddress(address));
        return getSubCollection(propertyId, COLLECTION_ADDRESS).document(address.getAddressId()).set(address);
    }

    public Task<Void> insertFeatureToProperty(String propertyId, PropertyFeature propertyFeature){
        executor.execute(() -> propertyDatabase.propertyFeatureDao().insertPropertyFeature(propertyFeature));
        updateSurfacePropertyForSearch(propertyId, propertyFeature.getPropertySurface());
        updateDatePropertyForSearch(propertyId, propertyFeature.getEntranceDate());
        return getSubCollection(propertyId, COLLECTION_FEATURE).document(propertyFeature.getPropertyFeatureId()).set(propertyFeature);
    }

    public Task<Void> insertPointOfInterestToProperty(String propertyId, PointOfInterest pointOfInterest){
        executor.execute(() -> propertyDatabase.pointOfInterestDao().insertPointOfInterest(pointOfInterest));
        return getSubCollection(propertyId, COLLECTION_POINT_OF_INTEREST).document(pointOfInterest.getPointOfInterestId()).set(pointOfInterest);
    }

    public Task<Void> insertImageToProperty(String propertyId, PropertyImage propertyImage){
        executor.execute(() -> propertyDatabase.propertyImageDao().insertPropertyImage(propertyImage));
        getNumberOfPic(propertyId);
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
        List<String> pointInterestForSearch = new ArrayList<>();
        final int[] c = {0};
        agentRepository.getAgentFromFirestore(agentRepository.getCurrentUserId()).observe(owner, agent -> {
            int n = agent.getProximityPointOfInterestChoice().size();
            for (String pointOfInterest : agent.getProximityPointOfInterestChoice()){
                googlePlaceRepository.getPlace(location, 2000, pointOfInterest, "none").observe(owner, finalPlaces -> {
                    if (finalPlaces.size() > 0){
                        PointOfInterest pointOfInterest1 = new PointOfInterest();
                        pointOfInterest1.setPointOfInterestName(pointOfInterest);
                        pointOfInterest1.setPropertyId(propertyId);
                        pointOfInterest1.setPointOfInterestId(pointOfInterest +"Id");
                        insertPointOfInterestToProperty(propertyId, pointOfInterest1);
                        pointInterestForSearch.add(pointOfInterest);
                    }
                    c[0]++;
                    if (c[0] == n){
                        updatePointOfInterestPropertyForSearch(propertyId, pointInterestForSearch);
                    }
                });
            }
        });
    }

    /** ***************************** **/
    /** ******** GET Method  ******** **/
    /** ***************************** **/

    /** *********** Room  *********** **/

    public LiveData<List<Property>> getAllPropertyFromRoom(){return propertyDatabase.propertyDao().getAllProperty();}


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
        getSubCollection(propertyId, COLLECTION_ADDRESS).whereEqualTo(Utils.PROPERTY_ID, propertyId).get()
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
        getSubCollection(propertyId, COLLECTION_FEATURE).whereEqualTo(Utils.PROPERTY_ID, propertyId).get()
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
        getSubCollection(propertyId, COLLECTION_POINT_OF_INTEREST).whereEqualTo(Utils.PROPERTY_ID, propertyId).get()
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
        Query query = getSubCollection(propertyId, COLLECTION_IMAGE).whereEqualTo(Utils.PROPERTY_ID, propertyId);
        return new FirestoreRecyclerOptions.Builder<PropertyImage>()
                .setQuery(query, PropertyImage.class)
                .build();
    }

    public MutableLiveData<List<PropertyImage>> getAllImagesByPropertyIdForDetails(String propertyId){
        MutableLiveData<List<PropertyImage>> propertyImagesLiveData = new MutableLiveData<>();
        getSubCollection(propertyId, COLLECTION_IMAGE).whereEqualTo(Utils.PROPERTY_ID, propertyId).get()
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

    /** *********** Search  *********** **/

    private Task<Void> insertPropertyForSearch(Property property){
        FeatureForSearch featureForSearch = new FeatureForSearch();
        featureForSearch.setPropertyId(property.getPropertyId());
        featureForSearch.setLocation(property.getPropertyLocatedCity());
        featureForSearch.setPrice(property.getPropertyPrice());
        return getSearchCollection().document(property.getPropertyId()).set(featureForSearch);
    }

    private Task<Void> updateSurfacePropertyForSearch(String propertyId, float surface){
        return getSearchCollection().document(propertyId).update("surface", surface);
    }

    private Task<Void> updateDatePropertyForSearch(String propertyId, String entranceDate){
        Date finalDateStart = Utils.getFrenchTodayDate(entranceDate);
        return getSearchCollection().document(propertyId).update("entranceDate", finalDateStart);
    }

    private Task<Void> updatePointOfInterestPropertyForSearch(String propertyId, List<String> pointOfInterest){
        return getSearchCollection().document(propertyId).update("pointOfInterest", pointOfInterest);
    }

    private void getNumberOfPic(String propertyId){
        getAllImagesByPropertyIdForDetails(propertyId).observe(owner, propertyImages -> {
            updatePropertyNumberOfPicsForSearch(propertyId, propertyImages.size()+1);
        });
    }

    private Task<Void> updatePropertyNumberOfPicsForSearch(String propertyId, int numberOfPics){
        return getSearchCollection().document(propertyId).update("numberOfPics", numberOfPics);
    }

    public MutableLiveData<Integer> getMaxSurface(){
        MutableLiveData<Integer> maxSurface = new MutableLiveData<>();
        getAllProperty().observe(owner, properties -> {
            final int[] max = {0};
            final int[] c = {0};
            int n = properties.size();
            for (Property property : properties){
                getPropertyFeatureById(property.getPropertyId()).observe(owner, propertyFeature -> {
                    if (propertyFeature != null){
                        if (propertyFeature.getPropertySurface() > max[0])
                            max[0] = (int) propertyFeature.getPropertySurface();
                    }
                    c[0]++;
                    if (c[0] == n){
                        maxSurface.setValue(max[0]);
                    }
                });
            }
        });
        return maxSurface;
    }

    private MutableLiveData<List<FeatureForSearch>> getSearchList(){
        MutableLiveData<List<FeatureForSearch>> searchProperty = new MutableLiveData<>();
        getSearchCollection().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                searchProperty.setValue(task.getResult().toObjects(FeatureForSearch.class));
            }else {
                searchProperty.setValue(null);
            }
        });
        return searchProperty;
    }

    private MutableLiveData<List<FeatureForSearch>> searchMethod(String city, float minPrice, float maxPrice, float minSurface, float maxSurface, String dateStart,
                                                                 List<String> finalPointOfInterest, int finalNumberOfPics){
        MutableLiveData<List<FeatureForSearch>> searchProperty = new MutableLiveData<>();
        List<FeatureForSearch> propertyAdd = new ArrayList<>();

        getSearchList().observe(owner, featureForSearches -> {
            int n = featureForSearches.size();
            int c = 0;
            Date finalDateStart = Utils.getFrenchTodayDate(dateStart);
            for (FeatureForSearch featureForSearch : featureForSearches){
                if (featureForSearch.getEntranceDate().after(finalDateStart)){
                    if ((featureForSearch.getLocation().equals(city) || city.equals("null"))  && featureForSearch.getPrice() >= minPrice){
                        if (featureForSearch.getSurface() >= minSurface && featureForSearch.getSurface() <= maxSurface){
                            if (featureForSearch.getPrice() <= maxPrice || maxPrice == 0){
                                featureForSearch.getPointOfInterest().retainAll(finalPointOfInterest);
                                if (featureForSearch.getPointOfInterest().size() > 0 || finalPointOfInterest.equals(Arrays.asList("null"))){
                                    if (featureForSearch.getNumberOfPics() >= finalNumberOfPics || finalNumberOfPics == 0){
                                        propertyAdd.add(featureForSearch);
                                    }
                                }
                            }
                        }
                    }
                }
                c++;
                if (c == n){
                    searchProperty.setValue(propertyAdd);
                }
            }
        });

        return searchProperty;
    }

    public MutableLiveData<List<Property>> getDataFromSearch(String city, float minPrice, float maxPrice, float minSurface, float maxSurface, String dateStart,
                                                             List<String> finalPointOfInterest, int finalNumberOfPics){
        MutableLiveData<List<Property>> searchProperty = new MutableLiveData<>();
        List<Property> propertyAdd = new ArrayList<>();
        searchMethod(city, minPrice, maxPrice, minSurface, maxSurface, dateStart, finalPointOfInterest, finalNumberOfPics).observe(owner, featureForSearches -> {
            int n = featureForSearches.size();
            final int[] c = {0};
            for (FeatureForSearch featureForSearch : featureForSearches){
                getAPropertyById(featureForSearch.getPropertyId()).observe(owner, property -> {
                    propertyAdd.add(property);
                    c[0]++;
                    if (c[0] == n){
                        searchProperty.setValue(propertyAdd);
                    }
                });
            }
        });
        return searchProperty;
    }
}
