package com.aimplatfarm.aimplatfarmdelivery.Models;

public class DriverDetails {

    private String _id="";
    private String name="";
    private String drivingLisence="";
    private String ifsc="";
    private String bankName="";
    private String panNum="";
    //private Striung mobile=0;
    private String mobile="";
    private long accountNum=0;
    private String password="";
    private int otp;
    private String countryCode="";
    private String locality="";
    private String state="";
    private String city="";
    private int zip;
    private String emailID="";
    private Location location;
    private AccountDetails accountDetails;
    private String availabilityStatus="";
    private String status="";
    private Boolean mobileVerified;
    private Boolean emailVerified;
    private String fieldname;
    private String originalname;
    private String encoding;
    private String mimetype;
    private String destination;
    private String filename;
    private String path;
    private String fcm_id;
    private String size;
    private Integer delivered;

    private Integer shipped;

    private Integer declined;

    private Integer earrning;

    public Integer getDelivered() {
        return delivered;
    }

    public void setDelivered(Integer delivered) {
        this.delivered = delivered;
    }

    public Integer getShipped() {
        return shipped;
    }

    public String getFcm_id() {
        return fcm_id;
    }

    public void setFcm_id(String fcm_id) {
        this.fcm_id = fcm_id;
    }

    public void setShipped(Integer shipped) {
        this.shipped = shipped;
    }

    public String getFieldname() {
        return fieldname;
    }

    public void setFieldname(String fieldname) {
        this.fieldname = fieldname;
    }

    public String getOriginalname() {
        return originalname;
    }

    public void setOriginalname(String originalname) {
        this.originalname = originalname;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getDeclined() {
        return declined;
    }

    public void setDeclined(Integer declined) {
        this.declined = declined;
    }

    public Integer getEarrning() {
        return earrning;
    }

    public void setEarrning(Integer earrning) {
        this.earrning = earrning;
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

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public AccountDetails getAccountDetails() {
        return accountDetails;
    }

    public void setAccountDetails(AccountDetails accountDetails) {
        this.accountDetails = accountDetails;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDrivingLisence() {
        return drivingLisence;
    }

    public void setDrivingLisence(String drivingLisence) {
        this.drivingLisence = drivingLisence;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getPanNum() {
        return panNum;
    }

    public void setPanNum(String panNum) {
        this.panNum = panNum;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /*public String getMobile1() {
        return mobile1;
    }

    public void setMobile1(String mobile1) {
        this.mobile1 = mobile1;
    }*/

    public long getAccountNum() {
        return accountNum;
    }

    public void setAccountNum(long accountNum) {
        this.accountNum = accountNum;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getOtp() {
        return otp;
    }

    public void setOtp(int otp) {
        this.otp = otp;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    @Override
    public String toString() {
        return "DriverDetails{" +
                "name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", password='" + password + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", zip=" + zip +
                ", emailID='" + emailID + '\'' +
                '}';
    }
}
