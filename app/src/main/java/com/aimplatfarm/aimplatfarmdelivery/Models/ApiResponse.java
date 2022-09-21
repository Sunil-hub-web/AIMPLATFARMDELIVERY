package com.aimplatfarm.aimplatfarmdelivery.Models;

import java.util.List;

public class ApiResponse {

    private int code;
    private String msg;
    private boolean err;
    private Long mobile;
    private String token;
    private List<UserNotification> notification;
    private DriverDetails data;
    private OrderDetails orderDetails;
    private boolean mobileVerified;

    public boolean isMobileVerified() {
        return mobileVerified;
    }

    public void setMobileVerified(boolean mobileVerified) {
        this.mobileVerified = mobileVerified;
    }

    public OrderDetails getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(OrderDetails orderDetails) {
        this.orderDetails = orderDetails;
    }

    public boolean isErr() {
        return err;
    }

    public void setErr(boolean err) {
        this.err = err;
    }

    public DriverDetails getData() {
        return data;
    }

    public void setData(DriverDetails data) {
        this.data = data;
    }

    public List<UserNotification> getNotification() {
        return notification;
    }

    public void setNotification(List<UserNotification> notification) {
        this.notification = notification;
    }


    public Long getMobile() {
        return mobile;
    }

    public void setMobile(Long mobile) {
        this.mobile = mobile;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
