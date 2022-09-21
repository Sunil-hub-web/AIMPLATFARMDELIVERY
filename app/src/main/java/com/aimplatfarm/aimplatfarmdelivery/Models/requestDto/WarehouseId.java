package com.aimplatfarm.aimplatfarmdelivery.Models.requestDto;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WarehouseId implements Serializable
{

@SerializedName("location")
@Expose
private Location_Dto location;
@SerializedName("accountDetails")
@Expose
private AccountDetails accountDetails;
@SerializedName("mobileVerified")
@Expose
private Boolean mobileVerified;
@SerializedName("deliveryRange")
@Expose
private List<Object> deliveryRange = null;
@SerializedName("status")
@Expose
private String status;
@SerializedName("_id")
@Expose
private String id;
@SerializedName("name")
@Expose
private String name;
@SerializedName("mobile")
@Expose
private Long mobile;
@SerializedName("emailID")
@Expose
private String emailID;
@SerializedName("password")
@Expose
private String password;
@SerializedName("countryCode")
@Expose
private Integer countryCode;
@SerializedName("__v")
@Expose
private Integer v;
@SerializedName("gstImage")
@Expose
private String gstImage;
@SerializedName("gstNumber")
@Expose
private String gstNumber;
@SerializedName("registrationNumber")
@Expose
private String registrationNumber;
private final static long serialVersionUID = 1172937777218214485L;

public Location_Dto getLocation() {
return location;
}

public void setLocation(Location_Dto location) {
this.location = location;
}

public AccountDetails getAccountDetails() {
return accountDetails;
}

public void setAccountDetails(AccountDetails accountDetails) {
this.accountDetails = accountDetails;
}

public Boolean getMobileVerified() {
return mobileVerified;
}

public void setMobileVerified(Boolean mobileVerified) {
this.mobileVerified = mobileVerified;
}

public List<Object> getDeliveryRange() {
return deliveryRange;
}

public void setDeliveryRange(List<Object> deliveryRange) {
this.deliveryRange = deliveryRange;
}

public String getStatus() {
return status;
}

public void setStatus(String status) {
this.status = status;
}

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
}

public Long getMobile() {
return mobile;
}

public void setMobile(Long mobile) {
this.mobile = mobile;
}

public String getEmailID() {
return emailID;
}

public void setEmailID(String emailID) {
this.emailID = emailID;
}

public String getPassword() {
return password;
}

public void setPassword(String password) {
this.password = password;
}

public Integer getCountryCode() {
return countryCode;
}

public void setCountryCode(Integer countryCode) {
this.countryCode = countryCode;
}

public Integer getV() {
return v;
}

public void setV(Integer v) {
this.v = v;
}

public String getGstImage() {
return gstImage;
}

public void setGstImage(String gstImage) {
this.gstImage = gstImage;
}

public String getGstNumber() {
return gstNumber;
}

public void setGstNumber(String gstNumber) {
this.gstNumber = gstNumber;
}

public String getRegistrationNumber() {
return registrationNumber;
}

public void setRegistrationNumber(String registrationNumber) {
this.registrationNumber = registrationNumber;
}

}