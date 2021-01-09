package com.openclassrooms.realestatemanager.repository;

import androidx.lifecycle.MutableLiveData;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.openclassrooms.realestatemanager.models.Agent;

import java.util.List;

public class AgentRepository {

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public static final String COLLECTION_AGENT = "agent";

    public AgentRepository(){}

    private CollectionReference getAgentCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_AGENT);
    }

    /** ***************************** **/
    /** ******* Create Method  ****** **/
    /** ***************************** **/

    /** ******** Insert user in firebase  ****** **/
    public Task<Void> insertAgent(Agent agentToCreate) {
        return getAgentCollection().document(agentToCreate.getAgentId()).set(agentToCreate);
    }

    /** ***************************** **/
    /** ******** GET Method  ******** **/
    /** ***************************** **/

    /** ******** Get current user from firebaseAuth  ****** **/
    public FirebaseUser getCurrentUser(){return firebaseAuth.getCurrentUser();}

    public String getCurrentUserId(){return  getCurrentUser().getUid();}

    public MutableLiveData<Agent> getAgentFromFirestore(String agentId){
        MutableLiveData<Agent> agent = new MutableLiveData<>();
        getAgentCollection().document(agentId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()){
                agent.setValue(documentSnapshot.toObject(Agent.class));
            }else {
                agent.setValue(null);
            }
        });
        return agent;
    }

    /** ***************************** **/
    /** ******* UPDATE Method  ****** **/
    /** ***************************** **/

    /** ******** Update pointOfInterests  ****** **/
    public Task<Void> updatePointOfInterest(String agentId, List<String> pointOfInterests) {
        return getAgentCollection().document(agentId).update("proximityPointOfInterestChoice", pointOfInterests);
    }
}
