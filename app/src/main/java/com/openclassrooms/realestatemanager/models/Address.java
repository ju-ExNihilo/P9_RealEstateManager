package com.openclassrooms.realestatemanager.models;

public class Address {

    private String addressId;
    private String propertyId;
    private int numberOfWay;
    private String way;
    private int postCode;
    private String additionalAddressField;

    public Address() {}

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public int getNumberOfWay() {
        return numberOfWay;
    }

    public void setNumberOfWay(int numberOfWay) {
        this.numberOfWay = numberOfWay;
    }

    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }

    public int getPostCode() {
        return postCode;
    }

    public void setPostCode(int postCode) {
        this.postCode = postCode;
    }

    public String getAdditionalAddressField() {
        return additionalAddressField;
    }

    public void setAdditionalAddressField(String additionalAddressField) {
        this.additionalAddressField = additionalAddressField;
    }

    @Override
    public String toString() {
        return "Address{" +
                "addressId='" + addressId + '\'' +
                ", propertyId='" + propertyId + '\'' +
                ", numberOfWay=" + numberOfWay +
                ", way='" + way + '\'' +
                ", postCode=" + postCode +
                ", additionalAddressField='" + additionalAddressField + '\'' +
                '}';
    }
}
