package com.openclassrooms.realestatemanager.repository;

import androidx.lifecycle.MutableLiveData;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;
import com.openclassrooms.realestatemanager.models.Address;
import com.openclassrooms.realestatemanager.models.Property;

import java.util.List;

public class PropertyDataRepository {

    private final String COLLECTION_PROPERTY = "property";
    private final String COLLECTION_ADDRESS = "address";



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

    /** ***************************** **/
    /** ******* Create Method  ****** **/
    /** ***************************** **/

    public static String getPropertyId(){
        DocumentReference reference = FirebaseFirestore.getInstance().collection("property").document();
        return reference.getId();
    }

    public static String getAddressId(String propertyId){
        DocumentReference reference = FirebaseFirestore.getInstance().collection("property")
                .document(propertyId).collection("address").document();
        return reference.getId();
    }

    public Task<Void> createProperty(Property property){
        return getPropertyCollection().document(property.getPropertyId()).set(property);
    }

    public Task<Void> insertAddressToProperty(String propertyId, Address address){
        return getAddressCollection(propertyId).document(address.getAddressId()).set(address);
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
}
