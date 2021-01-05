package com.openclassrooms.realestatemanager.models;

import androidx.annotation.NonNull;
import androidx.room.*;

@Entity(foreignKeys = @ForeignKey(entity = Property.class, parentColumns = "propertyId", childColumns = "propertyId", onDelete = ForeignKey.CASCADE),
        indices = {@Index("propertyId")})
public class Address {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id", index = true)
    private long id;
    private String addressId;
    @ColumnInfo(name = "propertyId")
    private String propertyId;
    private int numberOfWay;
    private String way;
    private int postCode;
    private String additionalAddressField;

    public Address() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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
