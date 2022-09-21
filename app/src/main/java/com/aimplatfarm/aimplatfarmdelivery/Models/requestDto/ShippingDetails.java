package com.aimplatfarm.aimplatfarmdelivery.Models.requestDto;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShippingDetails implements Serializable
{

@SerializedName("name")
@Expose
private String name;
@SerializedName("addressID")
@Expose
private String addressID;
@SerializedName("contacts")
@Expose
private Long contacts;
@SerializedName("address_details")
@Expose
private AddressDetails addressDetails;
private final static long serialVersionUID = -917785084933570L;

public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
}

public String getAddressID() {
return addressID;
}

public void setAddressID(String addressID) {
this.addressID = addressID;
}

public Long getContacts() {
return contacts;
}

public void setContacts(Long contacts) {
this.contacts = contacts;
}

public AddressDetails getAddressDetails() {
return addressDetails;
}

public void setAddressDetails(AddressDetails addressDetails) {
this.addressDetails = addressDetails;
}

}