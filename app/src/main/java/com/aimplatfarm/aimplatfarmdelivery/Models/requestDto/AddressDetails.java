package com.aimplatfarm.aimplatfarmdelivery.Models.requestDto;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddressDetails implements Serializable
{

@SerializedName("default")
@Expose
private Boolean _default;
@SerializedName("_id")
@Expose
private String id;
@SerializedName("userID")
@Expose
private String userID;
@SerializedName("house")
@Expose
private String house;
@SerializedName("street")
@Expose
private String street;
@SerializedName("locality")
@Expose
private String locality;
@SerializedName("city")
@Expose
private String city;
@SerializedName("state")
@Expose
private String state;
@SerializedName("country")
@Expose
private String country;
@SerializedName("zip")
@Expose
private double zip;
@SerializedName("longitude")
@Expose
private String longitude;
@SerializedName("latitude")
@Expose
private String latitude;
@SerializedName("createdAt")
@Expose
private String createdAt;
@SerializedName("updatedAt")
@Expose
private String updatedAt;
@SerializedName("__v")
@Expose
private double v;
private final static long serialVersionUID = 7982667009867427684L;

public Boolean getDefault() {
return _default;
}

public void setDefault(Boolean _default) {
this._default = _default;
}

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public String getUserID() {
return userID;
}

public void setUserID(String userID) {
this.userID = userID;
}

public String getHouse() {
return house;
}

public void setHouse(String house) {
this.house = house;
}

public String getStreet() {
return street;
}

public void setStreet(String street) {
this.street = street;
}

public String getLocality() {
return locality;
}

public void setLocality(String locality) {
this.locality = locality;
}

public String getCity() {
return city;
}

public void setCity(String city) {
this.city = city;
}

public String getState() {
return state;
}

public void setState(String state) {
this.state = state;
}

public String getCountry() {
return country;
}

public void setCountry(String country) {
this.country = country;
}

public double getZip() {
return zip;
}

public void setZip(double zip) {
this.zip = zip;
}

public String getLongitude() {
return longitude;
}

public void setLongitude(String longitude) {
this.longitude = longitude;
}

public String getLatitude() {
return latitude;
}

public void setLatitude(String latitude) {
this.latitude = latitude;
}

public String getCreatedAt() {
return createdAt;
}

public void setCreatedAt(String createdAt) {
this.createdAt = createdAt;
}

public String getUpdatedAt() {
return updatedAt;
}

public void setUpdatedAt(String updatedAt) {
this.updatedAt = updatedAt;
}

public double getV() {
return v;
}

public void setV(double v) {
this.v = v;
}

}