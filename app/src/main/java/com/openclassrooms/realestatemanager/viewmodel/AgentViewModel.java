package com.openclassrooms.realestatemanager.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.auth.FirebaseUser;
import com.openclassrooms.realestatemanager.models.Agent;
import com.openclassrooms.realestatemanager.repository.AgentRepository;

import java.util.List;

public class AgentViewModel extends ViewModel {

    private AgentRepository agentRepository;

    public AgentViewModel(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }

    /** GET **/
    public FirebaseUser getCurrentUser(){ return agentRepository.getCurrentUser();}

    public LiveData<Agent> getCurrentUserData(String agentId){return agentRepository.getAgentFromFirestore(agentId);}

    /** INSERT **/
    public void insertAgent(Agent agent) { agentRepository.insertAgent(agent);}

    /** UPDATE **/
    public void updatePointOfInterest(String agentId, List<String> pointOfInterests){agentRepository.updatePointOfInterest(agentId, pointOfInterests);}
}
