package com.aimplatfarm.aimplatfarmdelivery.Models.requestDto;

import java.io.Serializable;

public class AccountDetails implements Serializable {
    private String bankName;
    private String ifscCode;
    private double accountNumber;

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

    public double getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(double accountNumber) {
        this.accountNumber = accountNumber;
    }
}
