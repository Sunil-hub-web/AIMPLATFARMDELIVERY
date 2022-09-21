package com.aimplatfarm.aimplatfarmdelivery.Models.requestDto;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentDetails implements Serializable
{

@SerializedName("paymentMethod")
@Expose
private String paymentMethod;
@SerializedName("paymentStatus")
@Expose
private String paymentStatus;
private final static long serialVersionUID = 868059762029554452L;

public String getPaymentMethod() {
return paymentMethod;
}

public void setPaymentMethod(String paymentMethod) {
this.paymentMethod = paymentMethod;
}

public String getPaymentStatus() {
return paymentStatus;
}

public void setPaymentStatus(String paymentStatus) {
this.paymentStatus = paymentStatus;
}

}