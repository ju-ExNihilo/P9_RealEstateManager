package com.openclassrooms.realestatemanager.models;

public class Property {

    private String propertyId;
    private String agentId;
    private String propertyType;
    private String propertyLocatedCity;
    private float propertyPrice;
    private String propertyPreviewImageUrl;

    public Property() {}

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getPropertyLocatedCity() {
        return propertyLocatedCity;
    }

    public void setPropertyLocatedCity(String propertyLocatedCity) {
        this.propertyLocatedCity = propertyLocatedCity;
    }

    public float getPropertyPrice() {
        return propertyPrice;
    }

    public void setPropertyPrice(float propertyPrice) {
        this.propertyPrice = propertyPrice;
    }

    public String getPropertyPreviewImageUrl() {
        return propertyPreviewImageUrl;
    }

    public void setPropertyPreviewImageUrl(String propertyPreviewImageUrl) {
        this.propertyPreviewImageUrl = propertyPreviewImageUrl;
    }

    @Override
    public String toString() {
        return "Property{" +
                "propertyId='" + propertyId + '\'' +
                ", agentId='" + agentId + '\'' +
                ", propertyType='" + propertyType + '\'' +
                ", propertyLocatedCity='" + propertyLocatedCity + '\'' +
                ", propertyPrice=" + propertyPrice +
                ", propertyPreviewImageUrl='" + propertyPreviewImageUrl + '\'' +
                '}';
    }
}
