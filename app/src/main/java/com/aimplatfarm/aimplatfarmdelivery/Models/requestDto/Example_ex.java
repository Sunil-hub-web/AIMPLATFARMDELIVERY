package com.aimplatfarm.aimplatfarmdelivery.Models.requestDto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Example_ex {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("err")
    @Expose
    private Boolean err;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("deliveryProfile")
    @Expose
    private DeliveryProfile_ex deliveryProfile;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Boolean getErr() {
        return err;
    }

    public void setErr(Boolean err) {
        this.err = err;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DeliveryProfile_ex getDeliveryProfile() {
        return deliveryProfile;
    }

    public void setDeliveryProfile(DeliveryProfile_ex deliveryProfile) {
        this.deliveryProfile = deliveryProfile;
    }
}
