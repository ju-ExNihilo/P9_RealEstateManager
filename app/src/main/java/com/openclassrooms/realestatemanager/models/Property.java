package com.openclassrooms.realestatemanager.models;

import java.util.Date;
import java.util.List;

public class Property {

    private String typeOfProperty;
    private String statusOfProperty;
    private float surfaceOfProperty;
    private int priceOfProperty;
    private String description;
    private int numberOfRooms;
    private String realEstateAgent;
    private Date dateEntranceMarket;
    private String dateSale;
    private List<String> urlsPictures;
    private int addressNumber;
    private String addressRoad;
    private int addressPostCode;
    private String addressTown;

    public Property() {}

    public String getTypeOfProperty() {
        return typeOfProperty;
    }

    public void setTypeOfProperty(String typeOfProperty) {
        this.typeOfProperty = typeOfProperty;
    }

    public String getStatusOfProperty() {
        return statusOfProperty;
    }

    public void setStatusOfProperty(String statusOfProperty) {
        this.statusOfProperty = statusOfProperty;
    }

    public float getSurfaceOfProperty() {
        return surfaceOfProperty;
    }

    public void setSurfaceOfProperty(float surfaceOfProperty) {
        this.surfaceOfProperty = surfaceOfProperty;
    }

    public int getPriceOfProperty() {
        return priceOfProperty;
    }

    public void setPriceOfProperty(int priceOfProperty) {
        this.priceOfProperty = priceOfProperty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public String getRealEstateAgent() {
        return realEstateAgent;
    }

    public void setRealEstateAgent(String realEstateAgent) {
        this.realEstateAgent = realEstateAgent;
    }

    public Date getDateEntranceMarket() {
        return dateEntranceMarket;
    }

    public void setDateEntranceMarket(Date dateEntranceMarket) {
        this.dateEntranceMarket = dateEntranceMarket;
    }

    public String getDateSale() {
        return dateSale;
    }

    public void setDateSale(String dateSale) {
        this.dateSale = dateSale;
    }

    public List<String> getUrlsPictures() {
        return urlsPictures;
    }

    public void setUrlsPictures(List<String> urlsPictures) {
        this.urlsPictures = urlsPictures;
    }

    public int getAddressNumber() {
        return addressNumber;
    }

    public void setAddressNumber(int addressNumber) {
        this.addressNumber = addressNumber;
    }

    public String getAddressRoad() {
        return addressRoad;
    }

    public void setAddressRoad(String addressRoad) {
        this.addressRoad = addressRoad;
    }

    public int getAddressPostCode() {
        return addressPostCode;
    }

    public void setAddressPostCode(int addressPostCode) {
        this.addressPostCode = addressPostCode;
    }

    public String getAddressTown() {
        return addressTown;
    }

    public void setAddressTown(String addressTown) {
        this.addressTown = addressTown;
    }

    @Override
    public String toString() {
        return "Property{" +
                "typeOfProperty='" + typeOfProperty + '\'' +
                ", statusOfProperty='" + statusOfProperty + '\'' +
                ", surfaceOfProperty=" + surfaceOfProperty +
                ", priceOfProperty=" + priceOfProperty +
                ", description='" + description + '\'' +
                ", numberOfRooms=" + numberOfRooms +
                ", realEstateAgent='" + realEstateAgent + '\'' +
                ", dateEntranceMarket=" + dateEntranceMarket +
                ", dateSale='" + dateSale + '\'' +
                ", urlsPictures=" + urlsPictures +
                ", addressNumber=" + addressNumber +
                ", addressRoad='" + addressRoad + '\'' +
                ", addressPostCode=" + addressPostCode +
                ", addressTown='" + addressTown + '\'' +
                '}';
    }
}
