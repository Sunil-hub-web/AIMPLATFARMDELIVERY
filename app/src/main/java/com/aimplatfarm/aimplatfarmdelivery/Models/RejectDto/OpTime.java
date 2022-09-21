package com.aimplatfarm.aimplatfarmdelivery.Models.RejectDto;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OpTime implements Serializable
{

@SerializedName("ts")
@Expose
private String ts;
@SerializedName("t")
@Expose
private Integer t;
private final static long serialVersionUID = -3128075800085277431L;

public String getTs() {
return ts;
}

public void setTs(String ts) {
this.ts = ts;
}

public Integer getT() {
return t;
}

public void setT(Integer t) {
this.t = t;
}

}