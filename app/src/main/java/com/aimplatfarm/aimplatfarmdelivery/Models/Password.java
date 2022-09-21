package com.aimplatfarm.aimplatfarmdelivery.Models;

public class Password {
    private String password;
    private String password2;
    private Long mobile;
    private String otp;
    private String countryCode;

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getCountry_code() {
        return countryCode;
    }

    public void setCountry_code(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public Long getMobile() {
        return mobile;
    }

    public void setMobile(Long mobile) {
        this.mobile = mobile;
    }
}
