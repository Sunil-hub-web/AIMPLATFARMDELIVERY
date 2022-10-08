package com.aimplatfarm.aimplatfarmdelivery.Models.requestDto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeliveryProfile_ex {

    @SerializedName("location")
    @Expose
    private Location_ex location;
    @SerializedName("accountDetails")
    @Expose
    private AccountDetails accountDetails;
    @SerializedName("drivingLisence")
    @Expose
    private String drivingLisence;
    @SerializedName("fcm_id")
    @Expose
    private String fcmId;
    @SerializedName("profilePhoto")
    @Expose
    private String profilePhoto;
    @SerializedName("DlPhoto")
    @Expose
    private String dlPhoto;
    @SerializedName("AddressProof")
    @Expose
    private String addressProof;
    @SerializedName("availabilityStatus")
    @Expose
    private String availabilityStatus;
    @SerializedName("registrationNumber")
    @Expose
    private String registrationNumber;
    @SerializedName("mobileVerified")
    @Expose
    private Boolean mobileVerified;
    @SerializedName("emailVerified")
    @Expose
    private Boolean emailVerified;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("countryCode")
    @Expose
    private Integer countryCode;
    @SerializedName("driver_id")
    @Expose
    private String driverId;
    @SerializedName("mobile")
    @Expose
    private Long mobile;
    @SerializedName("emailID")
    @Expose
    private String emailID;
    @SerializedName("__v")
    @Expose
    private Integer v;

    public Location_ex getLocation() {
        return location;
    }

    public void setLocation(Location_ex location) {
        this.location = location;
    }

    public AccountDetails getAccountDetails() {
        return accountDetails;
    }

    public void setAccountDetails(AccountDetails accountDetails) {
        this.accountDetails = accountDetails;
    }

    public String getDrivingLisence() {
        return drivingLisence;
    }

    public void setDrivingLisence(String drivingLisence) {
        this.drivingLisence = drivingLisence;
    }

    public String getFcmId() {
        return fcmId;
    }

    public void setFcmId(String fcmId) {
        this.fcmId = fcmId;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getDlPhoto() {
        return dlPhoto;
    }

    public void setDlPhoto(String dlPhoto) {
        this.dlPhoto = dlPhoto;
    }

    public String getAddressProof() {
        return addressProof;
    }

    public void setAddressProof(String addressProof) {
        this.addressProof = addressProof;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public Boolean getMobileVerified() {
        return mobileVerified;
    }

    public void setMobileVerified(Boolean mobileVerified) {
        this.mobileVerified = mobileVerified;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
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

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
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

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }
}
