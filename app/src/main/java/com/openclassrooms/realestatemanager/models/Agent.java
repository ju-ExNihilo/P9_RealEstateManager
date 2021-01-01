package com.openclassrooms.realestatemanager.models;

import java.util.List;

public class Agent {

    private String agentId;
    private String agentName;
    private List<String> proximityPointOfInterestChoice;

    public Agent() {
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public List<String> getProximityPointOfInterestChoice() {
        return proximityPointOfInterestChoice;
    }

    public void setProximityPointOfInterestChoice(List<String> proximityPointOfInterestChoice) {
        this.proximityPointOfInterestChoice = proximityPointOfInterestChoice;
    }

    @Override
    public String toString() {
        return "Agent{" +
                "agentId='" + agentId + '\'' +
                ", agentName='" + agentName + '\'' +
                ", proximityPointOfInterestChoice=" + proximityPointOfInterestChoice +
                '}';
    }
}
