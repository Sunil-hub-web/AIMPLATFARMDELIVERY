package com.aimplatfarm.aimplatfarmdelivery.Models.requestDto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccountDetails_ex {

    @SerializedName("accountNumber")
    @Expose
    private String accountNumber;
    @SerializedName("bankName")
    @Expose
    private String bankName;
    @SerializedName("ifscCode")
    @Expose
    private String ifscCode;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

}
